package sg.activewealth.roboadvisor.banking.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sg.activewealth.roboadvisor.banking.model.BankDetail;
import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

@Repository
public class BankDetailDao extends AbstractDao<BankDetail> {
	
	
	public BankDetail retrieveByCategory(PortfolioAssignmentCategory category) {
		List<BankDetail> res = findByCriteria(
				DetachedCriteria.forClass(BankDetail.class).add(Restrictions.eq("portfolioCategory", category)), false);
		if (res.size() > 0) {
			return res.get(0);
		}
		return null;
	}

}
