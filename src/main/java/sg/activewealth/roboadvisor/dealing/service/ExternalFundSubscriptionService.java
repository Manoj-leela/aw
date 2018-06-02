package sg.activewealth.roboadvisor.dealing.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.ExternalFundPriceDao;
import sg.activewealth.roboadvisor.dealing.dao.ExternalFundSubscriptionDao;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundSubscription;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ServiceException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class ExternalFundSubscriptionService extends AbstractService<ExternalFundSubscription> {

	public ExternalFundSubscriptionService() {
		super(ExternalFundSubscription.class);
	}

	public ExternalFundSubscriptionService(Class<ExternalFundSubscription> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(ExternalFundSubscriptionDao dao) {
		super.dao = dao;
	}
	
	@Autowired
	private ExternalFundPriceDao externalFundPriceDao;
	
	@Override
	public ErrorsDto validateForSave(ExternalFundSubscription model, ErrorsDto errors) throws ValidateException {
		errors = super.validateForSave(model, errors);
		
		if(model.getExternalFund() == null){
			errors.add(new ErrorDto("externalFund","error.required", "External Fund"));
		}
		
		if(model.getExternalFundPrice() == null){
			errors.add(new ErrorDto("externalFundPrice","error.required", "External Fund Price"));
		}
		
		if(model.getTotalSubscriptionAmount() != null){
			if(model.getTotalSubscriptionAmount().precision() > 12 || model.getTotalSubscriptionAmount().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("totalSubscriptionAmount","error.invalid", "Total Subscription Amount"));
			}
		}
		
		if(model.getNetInvestAmount() != null){
			if(model.getNetInvestAmount().precision() > 12 || model.getNetInvestAmount().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("netInvestAmount","error.invalid", "NetInvest Amount"));
			}
		}
		
		if(model.getShares() != null){
			if(model.getShares().precision() > 10 || model.getShares().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("shares","error.invalid", "Shares"));
			}
		} 
		
		if(model.getBalanceShares() != null){
			if(model.getBalanceShares().precision() > 10 || model.getShares().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ErrorDto("balanceShares","error.invalid", "Balance Shares"));
			}
		}
		return errors;
	}

	public PagingDto<ExternalFundSubscription> retrieveForListPage(PagingDto<ExternalFundSubscription> pagingDto,
			String fundName) {
		pagingDto = ((ExternalFundSubscriptionDao) dao).retrieveForListPage(pagingDto,fundName);
		return pagingDto;
	}
	
	public ExternalFundSubscription isFundUsed(String fundId) {
		ExternalFundSubscription externalFund = ((ExternalFundSubscriptionDao) dao).isFundUsed(fundId);
		return externalFund;
	}
	
	public ExternalFundSubscription isFundPriceUsed(String fundPriceId) {
		ExternalFundSubscription externalFund = ((ExternalFundSubscriptionDao) dao).isFundPriceUsed(fundPriceId);
		return externalFund;
	}
	
	public ExternalFundPrice getExternalFundPriceForInstrument(String fundName) {
		ExternalFundPrice externalFundPrice = externalFundPriceDao.retrieveClosestPriceBeforeNowWithDealingTrueForFund(fundName);
		if(externalFundPrice == null) {
			throw new ServiceException();
		}
		return externalFundPrice;
	}
	
	public ExternalFundPrice getExternalFundPriceForInstrument(String fundName,Boolean dealing) {
		return  externalFundPriceDao.retrieveClosestPriceBeforeNowWithDealingForFund(fundName,dealing);
	}

}
