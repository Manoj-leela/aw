package sg.activewealth.roboadvisor.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.common.enums.AccountStatus;
import sg.activewealth.roboadvisor.common.enums.AgentOTPStatus;
import sg.activewealth.roboadvisor.common.enums.BankDetailsStatus;
import sg.activewealth.roboadvisor.common.enums.DeclarationStatus;
import sg.activewealth.roboadvisor.common.enums.KycStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Repository
public class UserDao extends AbstractDao<User> {

	@Override
	protected void initProxies(User entity, boolean fullInit) {
		super.initProxies(entity, fullInit);
	}

	@SuppressWarnings("unchecked")
	public boolean isEmailAddressUsed(String emailAddress) {
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class)
				.add(Restrictions.eq("email", emailAddress));
		List<User> users = findByCriteria(userCriteria);
		return users.size() > 0 ? true : false;
	}

	public User retrieveByEmailAddress(String emailAddress) {
		List<User> res = findByCriteria(
				DetachedCriteria.forClass(User.class).add(Restrictions.eq("email", emailAddress)), false);
		if (res.size() > 0) {
			return res.get(0);
		}
		return null;
	}
	

	public void updateLastLoggedIn(String userId, String ipAddress, String userAgent) {
		super.bulkUpdate(
				"UPDATE User AS o SET o.lastLoggedInRead = ?, o.lastLoggedInIpAddress = ?, o.lastLoggedInUserAgent = ? WHERE o.id = ?",
				new Date(), ipAddress, userAgent, userId);
	}

	public String retrievePassword(String id) {
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class)
				.setProjection(Projections.property("password")).add(Restrictions.eq("id", id));
		return (String) findByCriteria(userCriteria).get(0);
	}

	@SuppressWarnings("unchecked")
	public User retrieveByToken(String token) {
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("token", token));
		List<User> foundUsers = findByCriteria(userCriteria);
		if (foundUsers != null && foundUsers.size() == 1) {
			return foundUsers.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public User retrieveByPhone(String phone) {
		DetachedCriteria phoneCriteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("mobileNumber", phone));
		List<User> foundUser = findByCriteria(phoneCriteria);
		if (foundUser != null && foundUser.size() == 1) {
			return foundUser.get(0);
		}
		return null;
	}

	public User getUser(String id) {
		User user = null;
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("id", id));
		@SuppressWarnings("unchecked")
		List<User> users = findByCriteria(userCriteria);
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public User getUserByFBApiKey(String apiKey,String emailAddress){
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class).add(Restrictions.eq("socialId", apiKey)).add(Restrictions.eq("email", emailAddress));
		User user = null;
		List<User> users = findByCriteria(userCriteria);
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	public PagingDto<User> retrieveForListPage(String[] accountStatus, String[] agentOTPStatus, String[] kycStatus,
			String[] bankDetailsStatus, String[] declarationStatus,PagingDto<User> pagingDto, Boolean isAdmin,String emailAddress) {
		DetachedCriteria c = DetachedCriteria.forClass(User.class);
		if (null != accountStatus && accountStatus.length != 0) {
			List<AccountStatus> accountStatusList = new ArrayList<AccountStatus>(accountStatus.length);
			for (String s : accountStatus) {
				accountStatusList.add(AccountStatus.get(s));
			}
			c.add(Restrictions.in("accountStatus", accountStatusList));
		}
		if (null != agentOTPStatus && agentOTPStatus.length != 0) {
			List<AgentOTPStatus> agentOTPStatusList = new ArrayList<AgentOTPStatus>(agentOTPStatus.length);
			for (String s : agentOTPStatus) {
				agentOTPStatusList.add(AgentOTPStatus.get(s));
			}
			c.add(Restrictions.in("agentOTPStatus", agentOTPStatusList));
		}
		if (null != kycStatus && kycStatus.length != 0) {
			List<KycStatus> kycStatusList = new ArrayList<KycStatus>(kycStatus.length);
			for (String s : kycStatus) {
				kycStatusList.add(KycStatus.get(s));
			}
			c.add(Restrictions.in("kycStatus", kycStatusList));
		}
		if (null != bankDetailsStatus && bankDetailsStatus.length != 0) {
			List<BankDetailsStatus> bankDetailsStatusList = new ArrayList<BankDetailsStatus>(bankDetailsStatus.length);
			for (String s : bankDetailsStatus) {
				bankDetailsStatusList.add(BankDetailsStatus.get(s));
			}
			c.add(Restrictions.in("bankDetailsStatus", bankDetailsStatusList));
		}
		if (null != declarationStatus && declarationStatus.length != 0) {
			List<DeclarationStatus> declarationStatusList = new ArrayList<DeclarationStatus>(declarationStatus.length);
			for (String s : declarationStatus) {
				declarationStatusList.add(DeclarationStatus.get(s));
			}
			c.add(Restrictions.in("declarationStatus", declarationStatusList));
		}
		if(!ValidationUtils.getInstance().isEmptyString(emailAddress)){
			c.add(Restrictions.like("email", emailAddress,MatchMode.START));
		}
		c.add(Restrictions.eq("isAdmin", isAdmin));
		c.addOrder(Order.desc("createdOn"));

        return findByCriteria(c, pagingDto, false);
	}

	@SuppressWarnings("unchecked")
	public boolean isAgentUsed(String agentId) {
		DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class);
		userCriteria.createAlias("agent", "agent")
				.add(Restrictions.eq("agent.id", agentId));
		List<User> users = findByCriteria(userCriteria);
		return users.size() > 0 ? true : false;
	}

}
