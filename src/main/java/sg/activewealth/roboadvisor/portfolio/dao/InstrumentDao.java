package sg.activewealth.roboadvisor.portfolio.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;

@Repository
public class InstrumentDao extends AbstractDao<Instrument> {

	@Override
	protected void initProxies(Instrument entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	@SuppressWarnings("unchecked")
	public Instrument isNameUsed(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Instrument.class).add(Restrictions.eq("name", name));
		List<Instrument> instruments = findByCriteria(criteria);
		if(instruments.size() > 0){
			return instruments.get(0);
		}
		return null;
	}

	public Instrument retrieveByName(String name) {
		List<Instrument> res = findByCriteria(
				DetachedCriteria.forClass(Instrument.class).add(Restrictions.eq("name", name)), false);
		if (res.size() > 0) {
			return res.get(0);
		}
		return null;
	}
	
	public Instrument retrieveByInstrumentCode(String code) {
		List<Instrument> res = findByCriteria(
				DetachedCriteria.forClass(Instrument.class).add(Restrictions.eq("code", code)), false);
		if (res.size() > 0) {
			return res.get(0);
		}
		return null;
	}

	public PagingDto<Instrument> retrieveForListPage(PagingDto<Instrument> pagingDto, String name,
			String code, String[] instrumentTypes) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Instrument.class);
		if (!ValidationUtils.getInstance().isEmptyString(name)) {
			criteria.add(Restrictions.like("name", name,MatchMode.START));
		}
		if (!ValidationUtils.getInstance().isEmptyString(code)) {
			criteria.add(Restrictions.like("code", code,MatchMode.START));
		}
		List<InstrumentType> instrumentList = new ArrayList<>();
		if(instrumentTypes != null && instrumentTypes.length >0){
			for (String label : instrumentTypes) {
				instrumentList.add(InstrumentType.get(label));
			}
			criteria.add(Restrictions.in("instrumentType", instrumentList));
		}
		criteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, pagingDto, false);
	}

}
