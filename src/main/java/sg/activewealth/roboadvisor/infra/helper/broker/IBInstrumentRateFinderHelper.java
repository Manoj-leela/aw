package sg.activewealth.roboadvisor.infra.helper.broker;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ib.client.Contract;
import com.ib.client.Types.SecType;

import sg.activewealth.roboadvisor.portfolio.model.Instrument;
import sg.activewealth.roboadvisor.trade.enums.BuySell;

@Component
public class IBInstrumentRateFinderHelper implements InstrumentRateFinderHelper {

    private Logger logger = Logger.getLogger(IBInstrumentRateFinderHelper.class);
    private static final String SEQ_DOMAIN_NAME = "InstrumentRateReqId";

    @Override
    public BigDecimal getCurrentRate(Instrument instrument, BuySell buySell) {
//        TwsApiHelper twsApiHelper = TwsApiFactory.getInstance(propertiesHelper.ibDemoInstanceHostName, propertiesHelper.ibDemoInstancePort, 1);
//        int reqId = customSequenceService.retrieveNextSeqByDomain(SEQ_DOMAIN_NAME);
//        if (logger.isDebugEnabled()) {
//            logger.debug("Insturment req Id for the transction is:" + reqId);
//        }
//        IBReqTracker ibReqTracker = iBReqTrackerService.save(new IBReqTracker(reqId, instrument.getInstrumentCode()));
//        if (logger.isDebugEnabled()) {
//            logger.debug("IBReqTracker Db id is:" + ibReqTracker.getId());
//            logger.debug("IBReqTracker req Id for getCurrentRate of Instrument is:" + ibReqTracker.getReqId());
//        }
//        twsApiHelper.reqMktData(reqId, createContract(instrument));
        return null;
    }

    private Contract createContract(Instrument instrument) {
        Contract contract = new Contract();
        contract.symbol(instrument.getCode());
        contract.secType(SecType.STK);
        contract.currency(InteractiveBrokerUtil.getInstance().getCurreny(instrument));
        contract.exchange(InteractiveBrokerUtil.getInstance().getExchange());
        contract.primaryExch(InteractiveBrokerUtil.getInstance().getExchange(instrument));
        return contract;
    }

}
