package sg.activewealth.roboadvisor.banking.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.banking.dao.RemittanceDao;
import sg.activewealth.roboadvisor.banking.dto.RemittanceDto;
import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.common.dao.UserDao;
import sg.activewealth.roboadvisor.common.enums.UserProgressStatus;
import sg.activewealth.roboadvisor.common.service.UserSubmissionService;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.notification.UserNotificationDispatcher;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dao.UserPortfolioDao;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RemittanceService extends AbstractService<Remittance> {

    public RemittanceService() {
        super(Remittance.class);
    }

    @Autowired
    public void setDao(RemittanceDao dao) {
        super.dao = dao;
    }

    @Autowired
    private UserPortfolioDao userPortfolioDao;

    @Autowired
    private UserNotificationDispatcher userNotificationDispatcher;

    @Autowired
    private UserSubmissionService userSubmissionService;

    @Autowired
    private UserDao userDao;

    /**
     * This is required since we need to load existing record from DB and compare it with updatedModel which requires two objects with same ID
     * And it cause NonUniqueObjectException: A different object with the same identifier value was already associated with the session
     * @param id remittanceId
     * @return evicted Remittance Object
     */
    public Remittance retrieveAndEvictIt(final String id) {
        final Remittance remittance = super.retrieve(id, false);
        dao.evict(remittance);
        dao.evict(remittance.getUserPortfolio());
        dao.evict(remittance.getUserPortfolio().getUser());
        return remittance;
    }

    @Override
    public ErrorsDto validateForSave(Remittance model, ErrorsDto errors) throws ValidateException {
        errors = super.validateForSave(model, errors);

        if (InvestorRemittanceStatus.Submitted.equals(model.getInvestorRemittanceStatus())) {
            if (ValidationUtils.getInstance().isEmptyString(model.getReferenceNo())) {
                errors.add(new ErrorDto("referenceNo", "error.required", "Reference No"));
            }

            if (!ValidationUtils.getInstance().isEmptyString(model.getRemittanceSlipFileName())) {
                File file = new File(propertiesHelper.appUploads + File.separator + model.getRemittanceSlipFileName());
                if (file.length() > RemittanceDto.UPLOAD_MAX_FILESIZE) {
                    errors.add(new ErrorDto("remittanceSlipFileName", "upload.maxuploadsizeexceeded", "Remittance Slip File"));
                }
            } else {
                errors.add(new ErrorDto("remittanceSlipFileName", "error.required", "Remittance Slip File"));
            }
            if (model.getUserPortfolio() == null) {
                errors.add(new ErrorDto("userPortfolio", "error.required", "User Portfolio"));
            }
        }
        if (!ValidationUtils.getInstance().isEmptyString(model.getRemittanceSlipFileName()) && !RemittanceDto.UPLOAD_ALLOWED_EXTENSIONS.contains(FilenameUtils.getExtension(model.getRemittanceSlipFileName()))) {
            errors.add(new ErrorDto("remittanceSlipFileName", "upload.unacceptableextensionsexception", "Remittance Slip File"));
        }

        if (model.getInvestorRemittanceRemittedAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getInvestorRemittanceRemittedAmount()) > 0) {
                errors.add(new ErrorDto("investorRemittanceRemittedAmount", "error.invalid", "Amount on Remittance Slip"));
            }
            if (model.getInvestorRemittanceRemittedAmount().precision() > 15) {
                errors.add(new ErrorDto("investorRemittanceRemittedAmount", "error.invalid", "Amount on Remittance Slip"));
            }
        }
        if (model.getInvestorRemittanceFees() != null) {
            if (BigDecimal.ZERO.compareTo(model.getInvestorRemittanceFees()) > 0) {
                errors.add(new ErrorDto("investorRemittanceFees", "error.invalid", "Investor Remittance Fees"));
            }
            if (model.getInvestorRemittanceFees().precision() > 15) {
                errors.add(new ErrorDto("investorRemittanceFees", "error.invalid", "Investor Remittance Fees"));
            }
        }

        if (model.getInvestorRemittanceReceivedAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getInvestorRemittanceReceivedAmount()) > 0) {
                errors.add(new ErrorDto("investorRemittanceReceivedAmount", "error.invalid", "Amount in Online Banking"));
            }

            if (model.getInvestorRemittanceReceivedAmount().precision() > 15) {
                errors.add(new ErrorDto("investorRemittanceReceivedAmount", "error.invalid", "Amount in Online Banking"));
            }
        }

        if (model.getBrokerFundingFees() != null) {
            if (BigDecimal.ZERO.compareTo(model.getBrokerFundingFees()) > 0) {
                errors.add(new ErrorDto("brokerFundingFees", "error.invalid", "Broker Funding Fees"));
            }
            if (model.getBrokerFundingFees().precision() > 15) {
                errors.add(new ErrorDto("brokerFundingFees", "error.invalid", "Broker Funding Fees"));
            }
        }

        if (model.getBrokerFundingReceivedAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getBrokerFundingReceivedAmount()) > 0) {
                errors.add(new ErrorDto("brokerFundingReceivedAmount", "error.invalid", "Broker Funding Receive Amount"));
            }
            if (model.getBrokerFundingReceivedAmount().precision() > 15) {
                errors.add(new ErrorDto("brokerFundingReceivedAmount", "error.invalid", "Broker Funding Receive Amount"));
            }
        }

        if (model.getNetInvestmentAmount() != null) {
            if (BigDecimal.ZERO.compareTo(model.getNetInvestmentAmount()) > 0) {
                errors.add(new ErrorDto("netInvestmentAmount", "error.invalid", "Net Investment Amount"));
            }
            if (model.getNetInvestmentAmount().precision() > 15) {
                errors.add(new ErrorDto("netInvestmentAmount", "error.invalid", "Net Investment Amount"));
            }
        }

        /*if (model.getInvestorRemittanceReceivedAmount() != null && model.getInvestorRemittanceFees() != null) {
            if (model.getInvestorRemittanceFees().compareTo(model.getInvestorRemittanceReceivedAmount()) > 0) {
                errors.add(new ErrorDto("investorRemittanceReceivedAmount", "error.minvalue", "Amount in Online Banking", "Fees"));
            }
        }*/

        return errors;
    }

    @Override
    public Remittance preSave(Remittance model) {
        model = super.preSave(model);

        if (model.getCreatingNewObject() && model.getInvestorRemittanceStatus() == null) {
            model.setInvestorRemittanceStatus(InvestorRemittanceStatus.Submitted);
        }

        if (model.getAttachment() != null) {
            String fileName = model.getAttachment().getName();
            model.setRemittanceSlipFileName(fileName);
        }

        if (!model.getCreatingNewObject()) {
            Remittance dbModel = retrieve(model.getId());
            if (dbModel.getBrokerFundingStatus() == null && InvestorRemittanceStatus.Received.equals(model.getInvestorRemittanceStatus())) {
                model.setBrokerFundingStatus(BrokerFundingStatus.Unprocessed);
            }
            if (dbModel.getBrokerBatch() == null && model.getBrokerBatch() != null) {
                model.setBrokerFundingStatus(BrokerFundingStatus.Processed);
            }
            if (BrokerFundingStatus.Processed.equals(dbModel.getBrokerFundingStatus()) && BrokerFundingStatus.Completed.equals(model.getBrokerFundingStatus())) {
                if (model.getNetInvestmentAmount() != null && model.getNetInvestmentAmount().compareTo(BigDecimal.ZERO) > 0) {
                    model.getUserPortfolio().setNetInvestmentAmount(model.getNetInvestmentAmount());
                    model.getUserPortfolio().setExecutionStatus(UserPortfolioStatus.Funded);
                    model.getUserPortfolio().getUser().setProgressStatus(UserProgressStatus.Home);
                }
            }
        }
        userSubmissionService.logRemittanceChange(model);
        userNotificationDispatcher.notifyRemittanceChange(model);
        return model;
    }

    @Override
    public Remittance postSave(Remittance model) {
        model = super.postSave(model);

        if (model.getAttachment() != null && model.getAttachment().getSize() > 0) {
            String filePath = model.getAttachment().getOriginalFilename();
            String extension = FilenameUtils.getExtension(filePath);
            String fileName = model.getId() + "_remittance." + extension;
            filePath = propertiesHelper.appUploads + File.separator + fileName;
            model.setRemittanceSlipFileName(fileName);
            try {
                File fileToCreate = new File(filePath);
                if (!fileToCreate.exists()) {
                    fileToCreate.createNewFile();
                } else {
                    fileToCreate.delete();
                }
                model.getAttachment().transferTo(fileToCreate);
            } catch (Exception e) {
                logger.error("File not uploaded");
            }
        }
        userPortfolioDao.save(model.getUserPortfolio());
        userDao.save(model.getUserPortfolio().getUser());
        return model;
    }

    public Remittance getRemittanceByUserPortfolioId(String userPortfolioId) {

        Remittance userPortfolioRemittance = ((RemittanceDao) dao).retriveByUserPortfolio(userPortfolioId);
        return userPortfolioRemittance;
    }

    public PagingDto<Remittance> retrieveForListPage(String[] userPortfolioIds, PagingDto<Remittance> pagingDto, String fundingStatus, String queryReferenceNumber, String investorRemittance) {
        pagingDto = ((RemittanceDao) dao).retrieveForListPage(userPortfolioIds, pagingDto, fundingStatus, queryReferenceNumber, investorRemittance);
        return pagingDto;
    }

    public List<LocalDate> getRemittanceBatchesDesc(String[] userPortfolioIds, String brokerFundingStatus) {
        return ((RemittanceDao) dao).getRemittanceBatchesDesc(userPortfolioIds, brokerFundingStatus);
    }

    public List<Remittance> getRemittancesByUserId(String userId) {
        return ((RemittanceDao) dao).getRemittancesByUserId(userId);
    }

}
