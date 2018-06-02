package sg.activewealth.roboadvisor.dealing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.dao.ExternalFundDao;
import sg.activewealth.roboadvisor.dealing.model.ExternalFund;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Service
public class ExternalFundService extends AbstractService<ExternalFund> {
	
	public ExternalFundService() {
		super(ExternalFund.class);
	}

	public ExternalFundService(Class<ExternalFund> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(ExternalFundDao dao) {
		super.dao = dao;
	}
	
	@Override
	public ErrorsDto validateForSave(ExternalFund model, ErrorsDto errors) throws ValidateException {
		super.validateForSave(model, errors);
		
		if (ValidationUtils.getInstance().isEmptyString(model.getName())) {
            errors.add(new ErrorDto("name", "error.required", "Name"));
        }
		
		ExternalFund dbFund = isFundNameUsed(model.getName());
		if(model.getCreatingNewObject()) {
			if(dbFund !=null) {
				errors.add(new ErrorDto("name", "error.duplicate.value","Fund" , "FundName"));
			}
		} else {
			if(dbFund != null && !dbFund.getId().equals(model.getId())) {
				errors.add(new ErrorDto("name", "error.duplicate.value","Fund" , "fundName"));
			}
		}
		return errors;
	}

	public PagingDto<ExternalFund> retrieveForListPage(PagingDto<ExternalFund> pagingDto, String name) {
		pagingDto = ((ExternalFundDao) dao).retrieveForListPage(pagingDto,name);
		return pagingDto;
	}
	
	public ExternalFund isFundNameUsed(String fundName) {
		ExternalFund externalFund = ((ExternalFundDao) dao).isFundNameUsed(fundName);
		return externalFund;
	}
}
