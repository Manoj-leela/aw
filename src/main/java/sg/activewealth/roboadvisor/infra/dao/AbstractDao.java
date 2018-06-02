package sg.activewealth.roboadvisor.infra.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.aspect.AutoPopulateAuditFieldsAspect;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.infra.service.UserSessionService;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;

@Repository
public class AbstractDao<T extends AbstractModel> {

    protected Logger logger = Logger.getLogger(AbstractDao.class);

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected PropertiesHelper propertiesHelper;

    @Autowired
    protected HibernateTemplate hibernateTemplate;

    // @Autowired
    // protected HibernateTemplate slaveHibernateTemplate;

    public T save(T model) {
        hibernateTemplate.evict(model);
        try {
            hibernateTemplate.saveOrUpdate(model);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            hibernateTemplate.clear();
            hibernateTemplate.saveOrUpdate(model);
        }
        return model;
    }

    public List<T> saveAll(List<T> models) {
        hibernateTemplate.evict(models);
        try {
            // TODO: Need to rewrite this for hibernate 5
            // hibernateTemplate.saveOrUpdateAll(models);
        } catch (Exception e) {
            hibernateTemplate.clear();
            // TODO: Need to rewrite this for hibernate 5
            // hibernateTemplate.saveOrUpdateAll(models);
        }
        return models;
    }

    public void delete(T model) {
        hibernateTemplate.evict(model);
        hibernateTemplate.delete(model);
    }

    public Integer retrieveVersion(Class<T> clazz, String id) {
        DetachedCriteria c = DetachedCriteria.forClass(clazz)
                .setProjection(Projections.property("version")).add(Restrictions.eq("id", id));
        return (Integer) hibernateTemplate.findByCriteria(c).get(0);
    }

    public T retrieve(Class<T> clazz, String id) {
        return retrieve(clazz, id, true);
    }

    public T retrieve(Class<T> clazz, String id, boolean fullInit) {
        T entity = null;

        if (id != null) {
            List<T> res = retrieve(DetachedCriteria.forClass(clazz).add(Restrictions.eq("id", id)),
                    null, fullInit).getResults();
            if (res.size() > 0) {
                entity = res.get(0);
            }
        }

        return entity;
    }

    public PagingDto<T> retrieve(Class<T> clazz, String sortBy, PagingDto<T> pagingDto) {
        return retrieve(clazz, sortBy, pagingDto, false);
    }

    public PagingDto<T> retrieve(Class<T> clazz, String sortBy, PagingDto<T> pagingDto,
            boolean fullInit) {
        PagingDto<T> ret = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if ((sortBy != null) && (sortBy.length() > 0)) {
            String col = null, dir = "asc";
            if (sortBy.contains(" ")) {
                col = sortBy.substring(0, sortBy.indexOf(" "));
                dir = sortBy.substring(sortBy.indexOf(" ") + 1, sortBy.length());
            } else {
                col = sortBy;
            }

            if (dir.equalsIgnoreCase("desc")) {
                criteria.addOrder(Order.desc(col));
            } else {
                criteria.addOrder(Order.asc(col));
            }
        } else {
            criteria.addOrder(Order.asc("createdOn"));
        }

        /*
         * if (!ValidationUtils.getInstance().isEmptyString(pagingDto.getSearch())) {
         * criteria = addSearchCriteria(criteria, pagingDto.getSearch());
         * }
         */

        if (pagingDto != null && pagingDto.getFilters() != null) {
            criteria = addSearchCriteria(criteria, pagingDto.getFilters());
        }

        ret = retrieve(criteria, pagingDto, fullInit);

        return ret;
    }

    protected DetachedCriteria addSearchCriteria(DetachedCriteria criteria,
                                                 List<FilterDto> filters) {
        for (FilterDto filter : filters) {
            switch (filter.getOperator()) {
                case NE:
                    criteria.add(Restrictions.ne(filter.getField(), filter.getValue()[0]));
                    break;
                case LT:
                    criteria.add(Restrictions.lt(filter.getField(), filter.getValue()[0]));
                    break;
                case LE:
                    criteria.add(Restrictions.le(filter.getField(), filter.getValue()[0]));
                    break;
                case GT:
                    criteria.add(Restrictions.gt(filter.getField(), filter.getValue()[0]));
                    break;
                case GE:
                    criteria.add(Restrictions.ge(filter.getField(), filter.getValue()[0]));
                    break;
                case LIKE:
                    criteria.add(Restrictions.like(filter.getField(), filter.getValue()[0].toString(),
                            MatchMode.ANYWHERE));
                    break;
                case ILIKE:
                    criteria.add(Restrictions.ilike(filter.getField(), filter.getValue()[0].toString(),
                            MatchMode.ANYWHERE));
                    break;
                case IN:
                    criteria.add(Restrictions.in(filter.getField(), Arrays.asList(filter.getValue())));
                    break;
                case BETWEEN:
                    criteria.add(Restrictions.between(filter.getField(), filter.getValue()[0],
                            filter.getValue()[1]));
                    break;
                default:
                    criteria.add(Restrictions.eq(filter.getField(), filter.getValue()[0]));
                    break;
            }
        }
        return criteria;
    }

    public PagingDto<T> retrieveForReport(Class<T> clazz, Date startDate, Date endDate,
            PagingDto<T> pagingDto, boolean fullInit) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(clazz);

        if (startDate != null) {
            detachedCriteria.add(Restrictions.ge("createdOn", startDate));
        }

        if (endDate != null) {
            detachedCriteria.add(Restrictions.le("createdOn", endDate));
        }

        return findByCriteria(detachedCriteria, pagingDto, fullInit);
    }

    @SuppressWarnings("rawtypes")
    protected List findByCriteria(DetachedCriteria c) {
        return hibernateTemplate.findByCriteria(c);
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(DetachedCriteria c, boolean fullInit) {
        List<T> res = (List<T>) hibernateTemplate.findByCriteria(c);
        for (T t : res) {
            initProxies(t, fullInit);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(DetachedCriteria c, boolean fullInit, Integer maxResults) {
        List<T> res = (List<T>) hibernateTemplate.findByCriteria(c, 0, maxResults);
        for (T t : res) {
            initProxies(t, fullInit);
        }
        return res;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected PagingDto findByCriteria(DetachedCriteria c, PagingDto pagingDto) {
        c.addOrder(Order.asc("createdOn"));
        return retrieve(c, pagingDto, false); // wont init anyway
    }

    protected PagingDto<T> findByCriteria(DetachedCriteria c, PagingDto<T> pagingDto,
            boolean fullInit) {
        c.addOrder(Order.asc("createdOn"));
        return retrieve(c, pagingDto, fullInit); // wont init anyway
    }

    @SuppressWarnings("unchecked")
    public PagingDto<T> retrieve(DetachedCriteria criteria, PagingDto<T> pagingDto,
            boolean fullInit) {
        List<T> results = null;

        if ((pagingDto != null) && (pagingDto.getIds() != null) && !pagingDto.getIds().isEmpty()) {
            criteria.add(Restrictions.in("id", pagingDto.getIds()));
        }

        Integer resultsSize = 0;
        if ((pagingDto == null) || (pagingDto.getResultsPerPage() == null)) {
            results = (List<T>) hibernateTemplate.findByCriteria(criteria);
            resultsSize = results.size();
        } else {
            // get original projection
            // Projection originalProjection = null;
            // Projection originalProjection =
            // ((Criteria)criteria.getExecutableCriteria(hibernateTemplate.getSessionFactory().getCurrentSession())).getProjection();
            // swap with count projection
            DetachedCriteria countCriteria = criteria.setProjection(Projections.rowCount());
            resultsSize = new Integer(
                    ((Long) hibernateTemplate.findByCriteria(countCriteria).get(0)).intValue());

            criteria.setProjection(null);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            // if (originalProjection == null)
            // criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            if (pagingDto.getCurrentPage() != null) {
                results = (List<T>) hibernateTemplate.findByCriteria(countCriteria,
                        pagingDto.getCurrentPage() * pagingDto.getResultsPerPage(),
                        pagingDto.getResultsPerPage());
            } else {
                results = (List<T>) hibernateTemplate.findByCriteria(criteria, 0,
                        pagingDto.getResultsPerPage());
            }
        }

        // hibernateTemplate.getSessionFactory().getStatistics().logSummary();
        if (results.size() > 0) {
            for (Iterator<T> itr = results.iterator(); itr.hasNext();) {
                Object o = itr.next();
                if (o instanceof AbstractModel) {
                    initProxies((T) o, fullInit);
                } else {
                    break; // not model type - noneed to init proxies
                }
            }
        }

        if (pagingDto == null) {
            pagingDto = new PagingDto<T>();
        }
        pagingDto.updateResults(resultsSize, results);
        return pagingDto;
    }


    // *************************** COMMON METHODS ****************************
    // when fullInit==true, this method does not recurse into composite/collection properties. this
    // is a single level init
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void initProxies(T entity, boolean fullInit) {
        // init proxies only applicable for model classes.
        if (entity.getClass().getName()
                .contains(AutoPopulateAuditFieldsAspect.AUDIT_FIELD_MODEL_PACKAGE_NAME)) {
            AbstractModel model = entity;
            if (fullInit) {
                Field[] fields = model.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    Class fieldType = field.getType();
                    // if is a composite property
                    if (fieldType.getName().contains(
                            AutoPopulateAuditFieldsAspect.AUDIT_FIELD_MODEL_PACKAGE_NAME)) {
                        try {
                            AbstractModel compositeObject = (AbstractModel) model.getClass()
                                    .getMethod(SystemUtils.getInstance()
                                            .buildFieldGetter(field.getName()), null)
                                    .invoke(model, null);
                            if (compositeObject != null) {
                                hibernateTemplate.initialize(compositeObject);
                            }
                        } catch (NoSuchMethodException e) {
                        } // ignore if no such method.
                        catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                    // if is a Set property, iterate
                    if (fieldType.getName().equals(
                            AutoPopulateAuditFieldsAspect.AUDIT_FIELD_COLLECTION_TYPE_NAME)) {
                        try {
                            Collection<AbstractModel> collectionValue =
                                    (Collection<AbstractModel>) model.getClass()
                                            .getMethod(SystemUtils.getInstance()
                                                    .buildFieldGetter(field.getName()), null)
                                            .invoke(model, null);
                            hibernateTemplate.initialize(collectionValue);
                        } catch (NoSuchMethodException e) {
                        } // ignore if no such method.
                        catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                }
            }
        }
    }

    protected void initialize(Object o) {
        hibernateTemplate.initialize(o);
    }

    protected Session getCurrentSession() {
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }

    protected int bulkUpdate(String hql, Object... model) {
        return hibernateTemplate.bulkUpdate(hql, model);
    }

    public void flush() {
        hibernateTemplate.flush();
    }

    public void evict(Object o) {
        hibernateTemplate.evict(o);
    }

    public Session getSessionFactory() {
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
}
