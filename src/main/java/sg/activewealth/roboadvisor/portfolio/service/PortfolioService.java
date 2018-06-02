package sg.activewealth.roboadvisor.portfolio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dao.PortfolioDao;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.AssetClassAllocation;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;

@Service
public class PortfolioService extends AbstractService<Portfolio> {

    public PortfolioService() {
        super(Portfolio.class);
    }

    public PortfolioService(Class<Portfolio> modelClass) {
        super(modelClass);
    }

    @Autowired
    public void setDao(PortfolioDao dao) {
        super.dao = dao;
    }

    @Autowired
    private InstrumentService instrumentService;

    @Override
    public ErrorsDto validateForSave(Portfolio model, ErrorsDto errors) throws ValidateException {
        super.validateForSave(model, errors);

        // checking percentage validity
        BigDecimal totalWeightage = BigDecimal.ZERO;
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal assetTotalWeightage = BigDecimal.ZERO;
        BigDecimal assetHundred = new BigDecimal(100);
        for (PortfolioInstrument instrument : model.getPortfolioInstruments()) {
            totalWeightage = totalWeightage.add(instrument.getWeightage());
        }
        int validWeightage = totalWeightage.compareTo(hundred);

        for (AssetClassAllocation assetClassAllocation : model.getAssetClassAllocations()) {
            assetTotalWeightage = assetTotalWeightage.add(assetClassAllocation.getTotalWeightage());
        }
        int validAssetWeightage = assetTotalWeightage.compareTo(assetHundred);
        // checking uniqueness of instruments
        boolean duplicateInstrument = false;
        int instrumentListSize = model.getPortfolioInstruments().size();
        for (int i = 0; i < instrumentListSize; i++) {
            for (int j = i + 1; j < instrumentListSize; j++) {
                if (model.getPortfolioInstruments().get(i).getInstrument().getId().equalsIgnoreCase(model.getPortfolioInstruments().get(j).getInstrument().getId())) {
                    duplicateInstrument = true;
                    break;
                }
            }
        }
        if (ValidationUtils.getInstance().isEmptyString(model.getName())) {
            errors.add(new ErrorDto("name", "error.required", "Name"));
        }
        
        if(model.getRiskProfile() == null){
        	errors.add(new ErrorDto("riskProfile", "error.required", "Risk Profile"));
        }
        Portfolio portfolio = isNameUsed(model.getName());
        if (model.getCreatingNewObject()) {
            if (portfolio != null) {
                errors.add(new ErrorDto("name", "error.duplicate.value", "Portfolio", "Name"));
            }
        } else {
            if (portfolio != null && !portfolio.getId().equals(model.getId())) {
                errors.add(new ErrorDto("name", "error.duplicate.value", "Portfolio", "Name"));
            }
        }
        if (validWeightage == 1) {
            errors.add(new ErrorDto("portfolioInstruments", "error.invalidpercentage", "PortfolioInstrument"));
        }
        if (duplicateInstrument) {
            errors.add(new ErrorDto("portfolioInstruments", "error.instrumentduplication", "PortfolioInstrument"));
        }
        if (validAssetWeightage == 1) {
            errors.add(new ErrorDto("assetClassAllocations", "error.invaliddisplayweightage", "Asset Class Allocations"));
        }

        if (model.getProjectedReturns() != null) {
            if (BigDecimal.ZERO.compareTo(model.getProjectedReturns()) > 0) {
                errors.add(new ErrorDto("projectedReturns", "error.invalid", "Projected Returns"));
            }

            if (model.getProjectedReturns().precision() > 15) {
                errors.add(new ErrorDto("projectedReturns", "error.invalid", "Projected Returns"));
            }
        }

        return errors;
    }

    public Portfolio isNameUsed(String name) {
        return ((PortfolioDao) dao).isNameUsed(name);
    }

    @Override
    public Portfolio preSave(Portfolio model) {
        model = super.preSave(model);
        List<PortfolioInstrument> portfolioInstrumentsList = model.getPortfolioInstruments();
        for (PortfolioInstrument portInstrument : portfolioInstrumentsList) {
            String id = portInstrument.getInstrument().getId();
            Instrument instrument = instrumentService.retrieve(id);
            portInstrument.setInstrument(instrument);
            portInstrument.setPortfolio(model);
        }
        model.setPortfolioInstruments(portfolioInstrumentsList);
        return model;
    }

    /**
     * Retrive portfolio by risk profile
     * 
     * @param riskProfile
     * @return
     */
    public List<Portfolio> retriveByRiskProfile(UserRiskProfile riskProfile, Boolean fullInt) {
        return ((PortfolioDao) dao).retrieveByRiskProfile(riskProfile, fullInt);
    }

    /**
     * Retrive portfolio by risk profile and {@link PortfolioAssignmentCategory}
     * 
     * @param riskProfile
     * @return
     */
    public List<Portfolio> retrieveByRiskProfileAndAssignmentCategory(UserRiskProfile riskProfile, PortfolioAssignmentCategory assignmentCategory, Boolean fullInt) {
        return ((PortfolioDao) dao).retrieveByRiskProfileAndAssignmentCategory(riskProfile, assignmentCategory, fullInt);
    }

    public PagingDto<Portfolio> retrieveForListPage(PagingDto<Portfolio> pagingDto, String name, String[] riskProfile) {
        pagingDto = ((PortfolioDao) dao).retrieveForListPage(pagingDto, name, riskProfile);
        return pagingDto;
    }

    public List<Portfolio> retrieveAllPortfoliosExceptPassedPorfolio(String portfolioId, Boolean fullInt) {
        return ((PortfolioDao) dao).retrieveAllPortfoliosExceptPassedPorfolio(portfolioId, fullInt);
    }

    public List<Portfolio> retrieveActivePortfolio() {
        return ((PortfolioDao) dao).retrieveActivePortfolio();
    }
}
