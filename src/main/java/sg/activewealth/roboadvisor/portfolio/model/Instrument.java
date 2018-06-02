package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.InstrumentType;

@Entity
@Table(name = "instrument")
@TypeDefs({ @TypeDef(name = "instrumentType", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.InstrumentType") }) })
public class Instrument extends AbstractModel {

    private String name;
    private InstrumentType instrumentType = InstrumentType.ETF;
    private String code;
    private BigDecimal feesPerTradeLeg;
    private BigDecimal currentPrice;
    private String saxoAssetType;
    private String saxoInstrumentId;

    @Transient
    @JsonIgnore
    private boolean inUse = false;

    public Instrument() {
    }

    public Instrument(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getFeesPerTradeLeg() {
        return feesPerTradeLeg;
    }

    public void setFeesPerTradeLeg(BigDecimal feesPerTradeLeg) {
        this.feesPerTradeLeg = feesPerTradeLeg;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getSaxoAssetType() {
        return saxoAssetType;
    }

    public void setSaxoAssetType(String assetType) {
        this.saxoAssetType = assetType;
    }

    public String getSaxoInstrumentId() {
        return saxoInstrumentId;
    }

    public void setSaxoInstrumentId(String saxoInstrumentId) {
        this.saxoInstrumentId = saxoInstrumentId;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

}
