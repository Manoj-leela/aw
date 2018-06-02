package sg.activewealth.roboadvisor.dealing.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.ExternalFundPriceDao;
import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Service
public class ExternalFundPriceService extends AbstractService<ExternalFundPrice> {

	public ExternalFundPriceService() {
		super(ExternalFundPrice.class);
	}
	
	public ExternalFundPriceService(Class<ExternalFundPrice> modelClass) {
		super(modelClass);
	}
	
	@Autowired
	public void setDao(ExternalFundPriceDao dao) {
		super.dao = dao;
	}

	@Override
	public ErrorsDto validateForSave(ExternalFundPrice model, ErrorsDto errors) throws ValidateException {
		super.validateForSave(model, errors);
		
		if(model.getExternalFund() == null) {
			errors.add(new ErrorDto("externalFund","error.required", "External Fund"));	
		}
		
		if(model.getBuyPrice() != null){
			if (BigDecimal.ZERO.compareTo(model.getBuyPrice()) > 0) {
                errors.add(new ErrorDto("buyPrice", "error.invalid", "Buy Price"));
            }
            if (model.getBuyPrice().precision() > 12) {
                errors.add(new ErrorDto("buyPrice", "error.invalid", "Buy Price"));
            }
		}
		
		if(model.getSellPrice() != null){
			if (BigDecimal.ZERO.compareTo(model.getSellPrice()) > 0) {
                errors.add(new ErrorDto("sellPrice", "error.invalid", "Sell Price"));
            }
            if (model.getSellPrice().precision() > 12) {
                errors.add(new ErrorDto("sellPrice", "error.invalid", "Sell Price"));
            }
		}
		return errors;
	}
	
	public PagingDto<ExternalFundPrice> retrieveForListPage(PagingDto<ExternalFundPrice> pagingDto, String name,
			LocalDateTime priceDate, List<Boolean> dealingList) {
		pagingDto = ((ExternalFundPriceDao) dao).retrieveForListPage(pagingDto,name,priceDate,dealingList);
		return pagingDto;
	}
	
	public List<ExternalFundPrice> retrieveFundPriceListByFundId(String fundId){
		List<ExternalFundPrice> externalFundPriceList = ((ExternalFundPriceDao) dao).retrieveFunPriceListByFundId(fundId);
		return externalFundPriceList;
	}
	
	public ExternalFundPrice isFundUsed(String fundId) {
		ExternalFundPrice externalFundPrice = ((ExternalFundPriceDao) dao).isFundUsed(fundId);
		return externalFundPrice;
	}
}
