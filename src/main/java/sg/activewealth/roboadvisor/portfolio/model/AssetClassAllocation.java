package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "asset_class_allocation")
@TypeDefs({ @TypeDef(name = "instrumentType", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.InstrumentType") }) })

public class AssetClassAllocation extends AbstractModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnore
    public Portfolio portfolio;

    private String displayName;
    private InstrumentType instrumentType;
    private String code;
    private BigDecimal totalWeightage;
    private String detail;
    private String description;
    private String color;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public BigDecimal getTotalWeightage() {
        return totalWeightage;
    }

    public void setTotalWeightage(BigDecimal totalWeightage) {
        this.totalWeightage = totalWeightage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Do not delete, Implemented for the backward compatibility for mobile.
    @Transient
    private String instrumentCode;

    @Transient
    private InstrumentType type;

    public String getInstrumentCode() {
        return getCode();
    }

    public void setInstrumentCode(String instrumentCode) {
        setCode(instrumentCode);
    }

    public InstrumentType getType() {
        return getInstrumentType();
    }

    public void setType(InstrumentType type) {
        setInstrumentType(type);
    }
}
