package sg.activewealth.roboadvisor.trade.dao;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.trade.model.RateHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RateHistoryDao<T> {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public PagingDto<T> getAll(final String instrument, final LocalDateTime startTime, final LocalDateTime endTime,
			final Integer currentPage) {
		// return (List<T>) hibernateTemplate.loadAll(RateHistory.class);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RateHistory.class);
		detachedCriteria.addOrder(Order.desc("rateTimeStamp"));

		if (startTime != null) {
			detachedCriteria.add(Restrictions.ge("rateTimeStamp", startTime));
		}

		if (endTime != null) {
			detachedCriteria.add(Restrictions.le("rateTimeStamp", endTime));
		}

		if (instrument != null) {
			detachedCriteria.add(Restrictions.ilike("instrument", instrument, MatchMode.ANYWHERE));
		}
		PagingDto<T> pagingDto = new PagingDto<>();
		pagingDto.setCurrentPage(currentPage);
		return this.retrieve(detachedCriteria, pagingDto);
	}

	@SuppressWarnings("unchecked")
	private PagingDto<T> retrieve(DetachedCriteria criteria, PagingDto<T> pagingDto) {
		List<T> results;

		Integer resultsSize = 0;
		if (pagingDto == null || (pagingDto.getResultsPerPage() == null)) {
			results = (List<T>) hibernateTemplate.findByCriteria(criteria);
			resultsSize = results.size();
		} else {

			DetachedCriteria countCriteria = criteria.setProjection(Projections.rowCount());
			resultsSize = new Integer(((Long) hibernateTemplate.findByCriteria(countCriteria).get(0)).intValue());

			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

			if (pagingDto.getCurrentPage() != null) {
				results = (List<T>) hibernateTemplate.findByCriteria(countCriteria,
						pagingDto.getCurrentPage() * pagingDto.getResultsPerPage(), pagingDto.getResultsPerPage());
			} else {
				results = (List<T>) hibernateTemplate.findByCriteria(criteria, 0, pagingDto.getResultsPerPage());
			}
		}

		if (pagingDto == null) {
			pagingDto = new PagingDto<T>();
		}
		pagingDto.updateResults(resultsSize, results);
		return pagingDto;
	}

	public List<String> getInstruments() {
		DetachedCriteria criteria = DetachedCriteria.forClass(RateHistory.class)
				.setProjection(Projections.distinct(Projections.property("instrument")));
		criteria.add(Restrictions.isNotNull("instrument"));
		return (List<String>) hibernateTemplate.findByCriteria(criteria);
	}

}
