package sg.activewealth.roboadvisor.portfolio.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.StringUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;

@Repository
@SuppressWarnings("unchecked")
public class UserPortfolioDao extends AbstractDao<UserPortfolio> {

	@Override
	protected void initProxies(UserPortfolio entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
		if (fullInit) {
			initialize(entity.getUserTradeList());
		}
	}

	public List<UserPortfolio> retrieveByStatus(final UserPortfolioStatus[] userPortfolioStatus) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
		List<UserPortfolioStatus> userPortfolioStatusList = new ArrayList<UserPortfolioStatus>();
		if (userPortfolioStatus != null && userPortfolioStatus.length > 0) {
			for (UserPortfolioStatus lUserPortfolioStatus : userPortfolioStatus) {
				userPortfolioStatusList.add(lUserPortfolioStatus);
			}
            criteria.add(Restrictions.in("executionStatus", userPortfolioStatusList));
		}
		return findByCriteria(criteria);
	}

	public List<String> retrieveIdsByStatus(final UserPortfolioStatus userPortfolioStatus) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
		criteria.setProjection(Projections.property("id"));
        criteria.add(Restrictions.eq("executionStatus", userPortfolioStatus));
		return findByCriteria(criteria);
	}

	public List<UserPortfolio> retrieveByUserIdAndStatus(final UserPortfolioStatus userPortfolioStatus, String userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class).createAlias("user", "_u");
		criteria.add(Restrictions.eq("_u.id", userId));
        criteria.add(Restrictions.eq("executionStatus", userPortfolioStatus));
		return findByCriteria(criteria, true);
	}

	public PagingDto<UserPortfolio> retrive(String id, PagingDto<UserPortfolio> pagingDto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class).createAlias("user", "_u");
		criteria.add(Restrictions.eq("_u.id", id));
		PagingDto<UserPortfolio> userPortfolio = findByCriteria(criteria, pagingDto);
		return userPortfolio;
	}

	public List<UserPortfolio> retriveByUser(String id, PagingDto<UserPortfolio> pagingDto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class).createAlias("user", "_u");
		criteria.add(Restrictions.eq("_u.id", id));
		criteria.addOrder(Order.desc("createdOn"));
		PagingDto<UserPortfolio> userPortfolio = findByCriteria(criteria, pagingDto);
		return userPortfolio.getResults().size() > 0 ? userPortfolio.getResults() : new ArrayList<UserPortfolio>();
	}

	public List<UserPortfolio> retrieve(UserPortfolioStatus status) {
		return retrieve(status, null);
	}

	public List<UserPortfolio> retrieve(UserPortfolioStatus userPortfolioStatus, String email) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
        criteria.add(Restrictions.eq("executionStatus", userPortfolioStatus));
		List<UserPortfolio> userPortfolio = new ArrayList<UserPortfolio>();
		if (StringUtils.getInstance().hasValue(email)) {
			criteria.createAlias("user", "_u");
			criteria.add(Restrictions.eq("_u.emailAddress", email));
			criteria.add(Restrictions.eq("_u.admin", Boolean.FALSE)); // Only
																		// allow
																		// non-admin
																		// users
			userPortfolio = findByCriteria(criteria);
		} else {
			userPortfolio = findByCriteria(criteria);
		}
		return userPortfolio;
	}

	public List<UserPortfolio> retrieve(UserPortfolioStatus userPortfolioStatus, String email, Portfolio portfolio) {
		List<UserPortfolio> userPortfolio = new ArrayList<UserPortfolio>();

		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
        criteria.add(Restrictions.eq("executionStatus", userPortfolioStatus));

		if (StringUtils.getInstance().hasValue(email) && portfolio != null) {

			criteria.createAlias("portfolio", "_p");
			criteria.add(Restrictions.eq("_p.id", portfolio.getId()));
			criteria.createAlias("user", "_u");
			criteria.add(Restrictions.eq("_u.emailAddress", email));
			userPortfolio = findByCriteria(criteria);

		} else if (StringUtils.getInstance().hasValue(email)) {

			criteria.createAlias("user", "_u");
			criteria.add(Restrictions.eq("_u.emailAddress", email));
			userPortfolio = findByCriteria(criteria);
		} else if (portfolio != null) {

			criteria.createAlias("portfolio", "_p");
			criteria.add(Restrictions.eq("_p.id", portfolio.getId()));
			userPortfolio = findByCriteria(criteria);
		} else {
			userPortfolio = findByCriteria(criteria);
		}
		return userPortfolio;
	}

	public boolean isExists(String userId, String portfolioId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class).createAlias("user", "_u").createAlias("portfolio", "_p");
		criteria.add(Restrictions.eq("_u.id", userId));
		criteria.add(Restrictions.eq("_p.id", portfolioId));
        criteria.add(Restrictions.ne("executionStatus", UserPortfolioStatus.Closed));
        criteria.add(Restrictions.ne("executionStatus", UserPortfolioStatus.ClosedAndReleased));
		List<UserPortfolio> UserPortfolio = findByCriteria(criteria);
		return UserPortfolio.size() > 0;
	}

	public List<UserPortfolio> getUserPortfolios(String portfolioId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class).createAlias("user", "_u").createAlias("portfolio", "_p");
		criteria.add(Restrictions.eq("_p.id", portfolioId));
		return findByCriteria(criteria);
	}

	public List<Map<String, Object>> getPortfolioSummary(String userId) {
		List<Map<String, Object>> listUserPortfolio = new ArrayList<>();
		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
		criteria.createAlias("user", "user");
        criteria.setProjection(Projections.projectionList().add(Projections.sum("netAssetValue").as("totalBalance")).add(Projections.sum("totalUninvestedAmount").as("totalInvested")).add(Projections.sum("returns").as("totalReturns"))
                .add(Projections.sum("unrealisedPnl").as("totalEarned")));
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		listUserPortfolio = findByCriteria(criteria);
		return listUserPortfolio;
	}

    public PagingDto<UserPortfolio> retrieveForListPage(PagingDto<UserPortfolio> pagingDto, UserPortfolioStatus userPortfolioStatus, String quryEmailAddress, String queryPortfolioName) {

		DetachedCriteria criteria = DetachedCriteria.forClass(UserPortfolio.class);
		criteria.createAlias("user", "user");
		criteria.createAlias("portfolio", "portfolio");
		
		if (userPortfolioStatus!=null) {
            criteria.add(Restrictions.eq("executionStatus", userPortfolioStatus));
		}
		if (!ValidationUtils.getInstance().isEmptyString(quryEmailAddress)) {
			criteria.add(Restrictions.ilike("user.emailAddress", quryEmailAddress, MatchMode.START));
		}
		if (!ValidationUtils.getInstance().isEmptyString(queryPortfolioName)) {
			criteria.add(Restrictions.ilike("portfolio.name", queryPortfolioName, MatchMode.START));
		}
		criteria.addOrder(Order.desc("createdOn"));
		return findByCriteria(criteria, pagingDto, false);

	}
}
