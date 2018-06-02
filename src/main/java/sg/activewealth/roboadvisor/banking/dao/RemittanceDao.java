package sg.activewealth.roboadvisor.banking.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.FundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class RemittanceDao extends AbstractDao<Remittance> {
	@SuppressWarnings("unchecked")
	public Remittance retriveByUserPortfolio(String userPortfolioId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Remittance.class);
		criteria.createAlias("userPortfolio", "userPortfolio");
		criteria.add(Restrictions.eq("userPortfolio.id", userPortfolioId));
		criteria.addOrder(Order.desc("updatedOn"));
		List<Remittance> remitanceList = findByCriteria(criteria);
		if (remitanceList != null && remitanceList.size() > 0) {
			return remitanceList.get(0);
		}
		return null;
	}

	public PagingDto<Remittance> retrieveForListPage(String[] userPortfolioIds, PagingDto<Remittance> pagingDto,
			String fundingStatus, String queryReferenceNumber, String investorRemittance) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Remittance.class);
		criteria.createAlias("userPortfolio", "userPortfolio");

		List<String> userPortfolioIdList = new ArrayList<>();
		if (userPortfolioIds != null && userPortfolioIds.length > 0) {
			userPortfolioIdList = Arrays.asList(userPortfolioIds);
			criteria.add(Restrictions.in("userPortfolio.id", userPortfolioIdList));
		}
		if (!ValidationUtils.getInstance().isEmptyString(fundingStatus)) {
			BrokerFundingStatus brokerFunding = BrokerFundingStatus.fromString(fundingStatus);
			if(brokerFunding!=null) {
				criteria.add(Restrictions.eq("brokerFundingStatus", brokerFunding));
			}
		}

		if (!ValidationUtils.getInstance().isEmptyString(investorRemittance)) {
			InvestorRemittanceStatus inRemittance = InvestorRemittanceStatus.fromString(investorRemittance);
			if(inRemittance!=null) {
				criteria.add(Restrictions.eq("investorRemittanceStatus", inRemittance));
			}
		}

		if (!ValidationUtils.getInstance().isEmptyString(queryReferenceNumber)) {
			criteria.add(Restrictions.ilike("referenceNo", queryReferenceNumber, MatchMode.START));
		}

		criteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, pagingDto, false);
	}

	public List<Remittance> getRemittancesByUserId(String userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Remittance.class);
		criteria.createAlias("userPortfolio", "userPortfolio");
		criteria.createAlias("userPortfolio.user", "user");
		criteria.add(Restrictions.eq("user.id", userId));
		return findByCriteria(criteria, false);
	}

	public FundingStatus getFundingStatus(String id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Remittance.class)
				.setProjection(Projections.property("brokerFundingStatus")).add(Restrictions.eq("id", id));
		return (FundingStatus) findByCriteria(criteria).get(0);
	}

	@SuppressWarnings("unchecked")
	public List<LocalDate> getRemittanceBatchesDesc(String[] userPortfolioIds, String fundingStatus) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Remittance.class)
				.setProjection(Projections.distinct(Projections.property("brokerBatch")));
		criteria.createAlias("userPortfolio", "userPortfolio");

		List<String> userPortfolioIdList = new ArrayList<String>();
		if (userPortfolioIds != null && userPortfolioIds.length > 0) {
			userPortfolioIdList = Arrays.asList(userPortfolioIds);
			criteria.add(Restrictions.in("userPortfolio.id", userPortfolioIdList));
		}
		if (!ValidationUtils.getInstance().isEmptyString(fundingStatus)) {
			BrokerFundingStatus brokerFunding = BrokerFundingStatus.valueOf(fundingStatus);
			if(brokerFunding!=null) {
				criteria.add(Restrictions.eq("brokerFundingStatus", brokerFunding));
			}
		}

		criteria.add(Restrictions.isNotNull("brokerBatch"));
		criteria.addOrder(Order.desc("brokerBatch"));
		return findByCriteria(criteria);
	}

}
