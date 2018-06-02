package sg.activewealth.roboadvisor.portfolio.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;

@Entity
@Table(name = "portfolio")
@TypeDefs({ @TypeDef(name = "riskProfile", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.UserRiskProfile") }),
        @TypeDef(name = "assignmentCategory", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.PortfolioAssignmentCategory") }),
        @TypeDef(name = "status", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.PortfolioStatus") }) })
public class Portfolio extends AbstractModel {

    private String name;
    private String description;
    private UserRiskProfile riskProfile;
    private BigDecimal projectedReturns;
    private PortfolioAssignmentCategory assignmentCategory = PortfolioAssignmentCategory.PublicBVI;
    private PortfolioStatus status = PortfolioStatus.Draft;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "portfolio_id")
    private List<PortfolioInstrument> portfolioInstruments = new ArrayList<>();

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "portfolio_id")
    private List<AssetClassAllocation> assetClassAllocations = new ArrayList<>();

    @Transient
    @JsonIgnore
    private boolean inUse = false;

    @Transient
    private BigDecimal totalCurriencesWeightage = new BigDecimal(0);

    @Transient
    private BigDecimal totalEquitiesWeightage = new BigDecimal(0);

    @Transient
    private BigDecimal totalCommoditiesWeightage = new BigDecimal(0);

    @Transient
    private BigDecimal totalFixedIncomeWeightage = new BigDecimal(0);

    public Portfolio() {
    }

    public Portfolio(String id) {
        super(id);
    }

    public Portfolio(String id, String name, UserRiskProfile userRiskProfile, List<PortfolioInstrument> portfolioInstrumentList, List<AssetClassAllocation> assetClassAllocations) {
        super(id);
        this.name = name;
        this.riskProfile = userRiskProfile;
        //this.portfolioInstruments = portfolioInstrumentList;
        this.assetClassAllocations = assetClassAllocations;
    }

    public Portfolio(Portfolio portfolio, String description) {
        this(portfolio.getId(), portfolio.getName(), portfolio.getRiskProfile(), portfolio.getPortfolioInstruments(), portfolio.getAssetClassAllocations());
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the userRiskProfile
     */
    public UserRiskProfile getRiskProfile() {
        return riskProfile;
    }

    /**
     * @param riskProfile
     *            the userRiskProfile to set
     */
    public void setRiskProfile(UserRiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    public BigDecimal getProjectedReturns() {
        return projectedReturns;
    }

    public void setProjectedReturns(BigDecimal projectedReturns) {
        this.projectedReturns = projectedReturns;
    }

    /**
     * @return the assignmentCategory
     */
    public PortfolioAssignmentCategory getAssignmentCategory() {
        return assignmentCategory;
    }

    /**
     * @param assignmentCategory
     *            the assignmentCategory to set
     */
    public void setAssignmentCategory(PortfolioAssignmentCategory assignmentCategory) {
        this.assignmentCategory = assignmentCategory;
    }

    public PortfolioStatus getStatus() {
        return status;
    }

    public void setStatus(PortfolioStatus status) {
        this.status = status;
    }

    public List<PortfolioInstrument> getPortfolioInstruments() {
        return portfolioInstruments;
    }

    public void setPortfolioInstruments(List<PortfolioInstrument> portfolioInstruments) {
        this.portfolioInstruments = portfolioInstruments;
    }

    public List<AssetClassAllocation> getAssetClassAllocations() {
        return assetClassAllocations;
    }

    public void setAssetClassAllocations(List<AssetClassAllocation> assetClassAllocations) {
        this.assetClassAllocations = assetClassAllocations;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public BigDecimal getTotalCurriencesWeightage() {
        return totalCurriencesWeightage;
    }

    public void setTotalCurriencesWeightage(BigDecimal totalCurriencesWeightage) {
        this.totalCurriencesWeightage = totalCurriencesWeightage;
    }

    public BigDecimal getTotalEquitiesWeightage() {
        return totalEquitiesWeightage;
    }

    public void setTotalEquitiesWeightage(BigDecimal totalEquitiesWeightage) {
        this.totalEquitiesWeightage = totalEquitiesWeightage;
    }

    public BigDecimal getTotalCommoditiesWeightage() {
        return totalCommoditiesWeightage;
    }

    public void setTotalCommoditiesWeightage(BigDecimal totalCommoditiesWeightage) {
        this.totalCommoditiesWeightage = totalCommoditiesWeightage;
    }

    public BigDecimal getTotalFixedIncomeWeightage() {
        return totalFixedIncomeWeightage;
    }

    public void setTotalFixedIncomeWeightage(BigDecimal totalFixedIncomeWeightage) {
        this.totalFixedIncomeWeightage = totalFixedIncomeWeightage;
    }

    // Do not delete, Implemented for the backward compatibility for mobile.
    @Transient
    private UserRiskProfile userRiskProfile;
    @Transient
    private PortfolioStatus portfolioStatus;

    public UserRiskProfile getUserRiskProfile() {
        return getRiskProfile();
    }

    public void setUserRiskProfile(UserRiskProfile userRiskProfile) {
        setRiskProfile(userRiskProfile);
    }

    public PortfolioStatus getPortfolioStatus() {
        return getStatus();
    }

    public void setPortfolioStatus(PortfolioStatus portfolioStatus) {
        setStatus(portfolioStatus);
    }

}
