package sg.activewealth.roboadvisor.portfolio.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.dealing.model.ExternalFundPrice;
import sg.activewealth.roboadvisor.dealing.service.ExternalFundSubscriptionService;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.broker.BrokerIntegrationServicesFactory;
import sg.activewealth.roboadvisor.infra.helper.broker.InstrumentHelper;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.dao.InstrumentDao;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.trade.enums.BuySell;

@Service
public class InstrumentService extends AbstractService<Instrument> {

    public InstrumentService() {
        super(Instrument.class);
    }

    public InstrumentService(Class<Instrument> modelClass) {
        super(modelClass);
    }

    @Autowired
    public void setDao(InstrumentDao dao) {
        super.dao = dao;
    }

    @Autowired
    private BrokerIntegrationServicesFactory brokerIntegrationServicesFactory;

    @Autowired
    private InstrumentHelper instrumentFinderHelper;

    @Autowired
    private ExternalFundSubscriptionService externalFundSubscriptionService;

    @Override
    public ErrorsDto validateForSave(Instrument model, ErrorsDto errors) throws ValidateException {
        super.validateForSave(model, errors);
        if (ValidationUtils.getInstance().isEmptyString(model.getName())) {
            errors.add(new ErrorDto("name", "error.required", "Name"));
        }
        if (model.getInstrumentType() == null) {
            errors.add(new ErrorDto("instrumentType", "error.required", "Instrument Type"));
        }
        Instrument instrument = isNameUsed(model.getName());
        if (model.getCreatingNewObject()) {
            if (instrument != null) {
                errors.add(new ErrorDto("name", "error.duplicate.value", "Instrument", "Name"));
            }
        } else {
            if (instrument != null && !instrument.getId().equals(model.getId())) {
                errors.add(new ErrorDto("name", "error.duplicate.value", "Instrument", "Name"));
            }
        }
        if (model.getFeesPerTradeLeg() != null) {
            if (BigDecimal.ONE.compareTo(model.getFeesPerTradeLeg()) <= 0 || model.getFeesPerTradeLeg().precision() > 5 || model.getFeesPerTradeLeg().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(new ErrorDto("feesPerTradeLeg", "error.invalid", "Fees Per Trade Leg"));
            }
        }
        if (model.getCurrentPrice() != null) {
            if (model.getCurrentPrice().precision() > 15 || model.getCurrentPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(new ErrorDto("currentPrice", "error.invalid", "Current Price"));
            }
        }
        return errors;
    }

    public Instrument isNameUsed(String name) {
        return ((InstrumentDao) dao).isNameUsed(name);
    }

    public Instrument retrieveByName(String name) {
        return ((InstrumentDao) dao).retrieveByName(name);
    }

    public Instrument retrieveByInstrumentCode(String code) {
        return ((InstrumentDao) dao).retrieveByInstrumentCode(code);
    }

    public Map<String, BigDecimal> getCurrentRateOfInstrument(final List<Instrument> instruments, BuySell buySell) {
        final Map<String, BigDecimal> mapOfInstrumentToCurrentRate = new HashMap<>(instruments.size());
        for (final Instrument instrument : instruments) {
            BigDecimal updatedRate = null;
            if (InstrumentType.CPFund.equals(instrument.getInstrumentType())) {
                ExternalFundPrice externalFundPrice = externalFundSubscriptionService.getExternalFundPriceForInstrument(instrument.getName(), false);
                if (externalFundPrice != null) {
                    updatedRate = externalFundPrice.getSellPrice();
                }
            }
            mapOfInstrumentToCurrentRate.put(instrument.getId(), updatedRate);
        }
        return Collections.unmodifiableMap(mapOfInstrumentToCurrentRate);
    }

    public BigDecimal getCurrentRateOfInstrument(final Instrument instrument, BuySell buySell) {
        int retryMade = 0;
        while (retryMade <= 3) {
            try {
                return brokerIntegrationServicesFactory.getInstrumentRateFinderHelper().getCurrentRate(instrument, buySell);
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
                retryMade++;
            }
        }

        if (retryMade >= 4) {
            // TODO:Send email to admin
        }
        // TODO:
        return null;
    }

    /* Commented out by Jon 
    @Override
    public Instrument postSave(Instrument model) {
        if (model.getInstrumentId() == null && model.getInstrumentCode() != null && !InstrumentType.CPFund.equals(model.getType())) {
            Map<String, Object> instrumentMap = instrumentFinderHelper.getInstrument(model.getInstrumentCode());
            if (instrumentMap != null) {
                String instrumentId = instrumentMap.get("Identifier").toString();
                String assetType = instrumentMap.get("AssetType").toString();
                model.setInstrumentId(instrumentId);
                model.setAssetType(assetType);
            }
        }
    
        return super.postSave(model);
    }*/

    public PagingDto<Instrument> retrieveForListPage(PagingDto<Instrument> pagingDto, String name, String code, String[] instrumentType) {
        pagingDto = ((InstrumentDao) dao).retrieveForListPage(pagingDto, name, code, instrumentType);
        return pagingDto;
    }

}
