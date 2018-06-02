package sg.activewealth.roboadvisor.trade.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;

@Entity
@Table(name = "ib_req_tracker")
public class IBReqTracker extends AbstractModel {

    private Integer reqId;
    private String instrumentId;

    public IBReqTracker() {

    }

    public IBReqTracker(Integer reqId, String instrumentId) {
        this.reqId = reqId;
        this.instrumentId = instrumentId;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

}
