package sg.activewealth.roboadvisor.portfolio.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;

@Repository
public class PortfolioInstrumentDao extends AbstractDao<PortfolioInstrument> {

	@Override
	protected void initProxies(PortfolioInstrument entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	@SuppressWarnings("unchecked")
	public boolean isPortfolioInstrumentExists(BigDecimal weightage, String instrumentId, String portfolioId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PortfolioInstrument.class);
		criteria.add(Restrictions.eq("weightage", weightage));
		criteria.add(Restrictions.eq("instrument", instrumentId));
		criteria.add(Restrictions.eq("portfolio", portfolioId));
		List<PortfolioInstrument> portInstruments = findByCriteria(criteria);
		return portInstruments.size() > 0 ? true : false;
	}
	
	@SuppressWarnings("unchecked")
	public List<PortfolioInstrument> getPortfolioInstruments(String instrumentId){
		DetachedCriteria criteria = DetachedCriteria.forClass(PortfolioInstrument.class);
		criteria.add(Restrictions.eq("instrument.id", instrumentId));
		return findByCriteria(criteria);
	}
	
}
