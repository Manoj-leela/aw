package sg.activewealth.roboadvisor.portfolio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.portfolio.dao.PortfolioInstrumentDao;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;

@Service
public class PortfolioInstrumentService extends AbstractService<PortfolioInstrument> {

	public PortfolioInstrumentService() {
		super(PortfolioInstrument.class);
	}

	public PortfolioInstrumentService(Class<PortfolioInstrument> modelClass) {
		super(modelClass);
	}

	@Autowired
	public void setDao(PortfolioInstrumentDao dao) {
		super.dao = dao;
	}

	@Override
	public ErrorsDto validateForSave(PortfolioInstrument model, ErrorsDto errors) throws ValidateException {
		super.validateForSave(model, errors);
		if ((model.getWeightage() == null) && (model.getWeightage() == BigDecimal.valueOf(0))) {
			errors.add(new ErrorDto("weightage", "error.required", "Percentage"));
		} else if (!model.getCreatingNewObject() && isPoertfolioInstrumentExists(model.getWeightage(), model.getInstrument(), model.getPortfolio())) {
			errors.add(new ErrorDto("error.alreadyused", "Instrument Combination"));
		}
		return errors;
	}

	private boolean isPoertfolioInstrumentExists(BigDecimal weightage, Instrument instrument, Portfolio portfolio) {
		return ((PortfolioInstrumentDao)dao).isPortfolioInstrumentExists(weightage, instrument.getId(), portfolio.getId());
	}

	public boolean isInstrumentInUse(String id){
		List<PortfolioInstrument> list = ((PortfolioInstrumentDao) dao).getPortfolioInstruments(id);
		if(list!=null && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
}
