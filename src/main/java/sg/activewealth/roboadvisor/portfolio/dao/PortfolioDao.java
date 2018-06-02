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
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;

@Repository
public class PortfolioDao extends AbstractDao<Portfolio> {

	@Override
	protected void initProxies(Portfolio entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
		initialize(entity.getPortfolioInstruments());
		initialize(entity.getAssetClassAllocations());
	}

	@SuppressWarnings("unchecked")
	public Portfolio isNameUsed(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class).add(Restrictions.eq("name", name));
		List<Portfolio> portfolios = findByCriteria(criteria);
		if (portfolios.size() > 0) {
			return portfolios.get(0);
		}
		return null;
	}

	public List<Portfolio> retrieveByRiskProfile(UserRiskProfile riskProfile, Boolean fullInt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class)
				.add(Restrictions.eq("riskProfile", riskProfile));
		List<Portfolio> res = findByCriteria(criteria, fullInt);
		return res;
	}

	public List<Portfolio> retrieveByRiskProfileAndAssignmentCategory(UserRiskProfile riskProfile,
			PortfolioAssignmentCategory assignmentCategory, boolean fullInt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class)
				.add(Restrictions.eq("riskProfile", riskProfile));
		criteria.add(Restrictions.eq("assignmentCategory", assignmentCategory));
		criteria.add(Restrictions.eq("status", PortfolioStatus.Active));
		List<Portfolio> res = findByCriteria(criteria, fullInt);
		return res;
	}
	
	public List<Portfolio> retrieveAllPortfoliosExceptPassedPorfolio(String portfolioId, boolean fullInt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class);
		criteria.add(Restrictions.not(Restrictions.in("id", portfolioId)));
		List<Portfolio> res = findByCriteria(criteria, fullInt);
		return res;
	}

	public PagingDto<Portfolio> retrieveForListPage(PagingDto<Portfolio> pagingDto, String name,String[] userRiskProfiles) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class);
		if (!ValidationUtils.getInstance().isEmptyString(name)) {
			criteria.add(Restrictions.like("name", name,MatchMode.START));
		}
		List<UserRiskProfile> userRiskProfileList = new ArrayList<>();
		if(userRiskProfiles != null && userRiskProfiles.length >0){
			for (String label : userRiskProfiles) {
				userRiskProfileList.add(UserRiskProfile.getByString(label));
			}
			criteria.add(Restrictions.in("riskProfile", userRiskProfileList));
		}
		criteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, pagingDto, false);
	}
	
	public List<Portfolio> retrieveActivePortfolio() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Portfolio.class);
			criteria.add(Restrictions.eq("status", PortfolioStatus.Active));
		return findByCriteria(criteria, false);
	}
}
