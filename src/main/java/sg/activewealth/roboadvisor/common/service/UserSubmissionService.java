package sg.activewealth.roboadvisor.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.common.dao.UserSubmissionDao;
import sg.activewealth.roboadvisor.common.enums.UserSubmissionType;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.model.UserSubmission;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class UserSubmissionService  extends AbstractService<UserSubmission> {

  @Autowired
  private UserService userService;

  @Autowired
  private RemittanceService remittanceService;

  @Autowired
  private RedemptionService redemptionService;

  @Autowired
  public void setDao(UserSubmissionDao dao) {
    super.dao = dao;
  }

  public UserSubmissionService() {
    super(UserSubmission.class);
  }

  public UserSubmissionService(final Class<UserSubmission> modelClass) {
    super(modelClass);
  }


  /**
   * Compare updated model with saved one and insert into table if change detected.
   * @param updatedModel Updated Model of User
   */
  public void logUserStatusChange(final User updatedModel) {

    final User existingUser = updatedModel.getId() == null ? null : getExistingUserById(updatedModel.getId());
    //TODO : Fix this if we need to add a record on new user creation. 
    if (existingUser != null && updatedModel.getKycStatus() != existingUser.getKycStatus()) {
      //Insert for Kyc

      final UserSubmission userSubmission = new UserSubmission();
      userSubmission.setUser(updatedModel);
      userSubmission.setType(UserSubmissionType.Kyc);
      if(updatedModel.getKycStatus() != null){
    	  userSubmission.setStatus(updatedModel.getKycStatus().getLabel());
      }
      userSubmission.setDescription(updatedModel.getKycRemarks());
      this.save(userSubmission);
    }
    if (existingUser != null && updatedModel.getBankDetailsStatus() != existingUser.getBankDetailsStatus()) {
      //Insert for Bank

      final UserSubmission userSubmission = new UserSubmission();
      userSubmission.setUser(updatedModel);
      userSubmission.setType(UserSubmissionType.BankDetail);
      if(updatedModel.getBankDetailsStatus() != null){
    	  userSubmission.setStatus(updatedModel.getBankDetailsStatus().getLabel()); 
      }
      userSubmission.setDescription(updatedModel.getBankDetailsRemarks());
      this.save(userSubmission);
    }

  }

  public void logRemittanceChange(final Remittance updatedModel) {

    final Remittance existingRemittance = updatedModel.getId() == null ? null : getExistingRemittanceById(updatedModel.getId());

    if (updatedModel.getBrokerFundingStatus() != null &&  (existingRemittance == null ||  updatedModel.getBrokerFundingStatus() != existingRemittance.getBrokerFundingStatus())) {
      //Insert for fundingStatus

      final UserSubmission userSubmission = new UserSubmission();
      userSubmission.setUser(updatedModel.getUserPortfolio().getUser());
      userSubmission.setType(UserSubmissionType.Remittance);
      userSubmission.setStatus(updatedModel.getBrokerFundingStatus().getLabel());
      userSubmission.setDescription(updatedModel.getRemarks());
      this.save(userSubmission);
    }

  }

  public void logRedemptionChange(final Redemption updatedModel) {

    final Redemption existingRedemption = updatedModel.getId() == null ? null : getExistingRedemptionById(updatedModel.getId());

    if (updatedModel.getRedemptionStatus() != null  && (existingRedemption == null ||  updatedModel.getRedemptionStatus() != existingRedemption.getRedemptionStatus())) {
      //Insert for redemptionStatus

      final UserSubmission userSubmission = new UserSubmission();
      userSubmission.setUser(updatedModel.getUserPortfolio().getUser());
      userSubmission.setType(UserSubmissionType.Redemption);
      userSubmission.setStatus(updatedModel.getRedemptionStatus().getLabel());
      userSubmission.setDescription(updatedModel.getRemarks());
      this.save(userSubmission);
    }

  }
  
  public List<UserSubmission> getUserSubmissionsByUserId(String userId){
	  return ((UserSubmissionDao) dao).getUserSubmissionsByUserId(userId);
  }

  private Redemption getExistingRedemptionById(final String id) {
    return redemptionService.retrieveAndEvictIt(id);
  }

  private User getExistingUserById (final String id) {
    return userService.getUserAndEvictIt(id);
  }

  private Remittance getExistingRemittanceById (final String id) {
    return remittanceService.retrieveAndEvictIt(id);
  }
}
