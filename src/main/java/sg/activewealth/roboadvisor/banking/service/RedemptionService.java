package sg.activewealth.roboadvisor.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.dao.RedemptionDao;
import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.common.service.UserSubmissionService;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.notification.UserNotificationDispatcher;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioDao;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;

@Service
public class RedemptionService extends AbstractService<Redemption> {

    @Autowired
    private UserPortfolioDao userPortfolioDao;

    @Autowired
    private UserSubmissionService userSubmissionService;

    @Autowired
    private UserNotificationDispatcher userNotificationDispatcher;

    public RedemptionService() {
        super(Redemption.class);
    }

    @Autowired
    public void setDao(RedemptionDao dao) {
        super.dao = dao;
    }

    /**
     * This is required since we need to load existing record from DB and compare it with updatedModel which requires two objects with same ID
     * And it cause NonUniqueObjectException: A different object with the same identifier value was already associated with the session
     * @param id redemptionId
     * @return evicted redemption Object
     */
    public Redemption retrieveAndEvictIt(final String id) {
        final Redemption redemption = super.retrieve(id, false);
        dao.evict(redemption);
        dao.evict(redemption.getUserPortfolio());
        dao.evict(redemption.getUserPortfolio().getUser());
        return redemption;
    }

    @Override
    public ErrorsDto validateForSave(Redemption model, ErrorsDto errors) throws ValidateException {
        errors = super.validateForSave(model, errors);

        if (model.getRedemptionDate() == null) {
            errors.add(new ErrorDto("redemptionDate", "error.required", "Redemption Date"));
        }
        if (model.getRedemptionAmount() == null) {
            errors.add(new ErrorDto("redemptionAmount", "error.required", "Redemption Amount"));
        } else if (model.getRedemptionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ErrorDto("redemptionAmount", "error.invalid", "Redemption Amount"));
        }
        if (model.getUserPortfolio() == null) {
            errors.add(new ErrorDto("userPortfolio", "error.required", "User Portfolio"));
        }

        if (model.getAmountReceivedFees() != null) {
            if (BigDecimal.ZERO.compareTo(model.getAmountReceivedFees()) > 0) {
                errors.add(new ErrorDto("amountReceivedFees", "error.invalid", "Amount Received Fees"));
            }
            if (model.getAmountReceivedFees().precision() > 15) {
                errors.add(new ErrorDto("amountReceivedFees", "error.invalid", "Amount Received Fees"));
            }
        }

        if (model.getAmountRequestedFromBroker() != null) {
            if (BigDecimal.ZERO.compareTo(model.getAmountRequestedFromBroker()) > 0) {
                errors.add(new ErrorDto("amountRequestedFromBroker", "error.invalid", "Amount Requested From Broker"));
            }
            if (model.getAmountRequestedFromBroker().precision() > 15) {
                errors.add(new ErrorDto("amountRequestedFromBroker", "error.invalid", "Amount Requested From Broker"));
            }
        }
        if (model.getAmountReceivedFromBroker() != null) {
            if (BigDecimal.ZERO.compareTo(model.getAmountReceivedFromBroker()) > 0) {
                errors.add(new ErrorDto("amountReceivedFromBroker", "error.invalid", "Amount Received From Broker"));
            }
            if (model.getAmountReceivedFromBroker().precision() > 15) {
                errors.add(new ErrorDto("amountReceivedFromBroker", "error.invalid", "Amount Received From Broker"));
            }
        }
        if (model.getNetRedemptionAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getNetRedemptionAmount()) > 0) {
                errors.add(new ErrorDto("netRedemptionAmount", "error.invalid", "Net Redemption Amount"));
            }
            if (model.getNetRedemptionAmount().precision() > 15) {
                errors.add(new ErrorDto("netRedemptionAmount", "error.invalid", "Net Redemption Amount"));
            }
        }

        return errors;
    }

    @Override
    public Redemption preSave(Redemption model) {
        super.preSave(model);
        if (!model.getCreatingNewObject()) {
            Redemption dbModel = retrieve(model.getId());
            if (RedemptionStatus.RequestedByInvestor.equals(dbModel.getRedemptionStatus()) 
                    && RedemptionStatus.SentToBroker.equals(model.getRedemptionStatus())) {
                model.getUserPortfolio().setExecutionStatus(UserPortfolioStatus.CloseRequested);
            }
        }
        userSubmissionService.logRedemptionChange(model);
        userNotificationDispatcher.notifyRedemptionChange(model);
        return model;
    }

    @Override
    public Redemption postSave(Redemption model) {
        super.postSave(model);
        userPortfolioDao.save(model.getUserPortfolio());
        return model;
    }

    public PagingDto<Redemption> retrieveForListPage(String[] userPortfolioIds, PagingDto<Redemption> pagingDto, BigDecimal queryRedemptionAmount, LocalDate redemptionDate, String redemptionStatus) {
        pagingDto = ((RedemptionDao) dao).retrieveForListPage(userPortfolioIds, pagingDto, queryRedemptionAmount, redemptionDate, redemptionStatus);
        return pagingDto;
    }

    public Redemption getRedemptionsByUserPortfolio(String userId) {
        return ((RedemptionDao) dao).getRedemptionsByUserPortfolio(userId);
    }

    public List<Redemption> getRedemptionsByUserId(String userId) {
        return ((RedemptionDao) dao).getRedemptionsByUserId(userId);
    }

    public List<LocalDate> getRedemptionBatchesDesc(String[] userPortfolioIds, String strRedemptionStatus) {
        return ((RedemptionDao) dao).getRedemptionBatchesDesc(userPortfolioIds, strRedemptionStatus);
    }
}
