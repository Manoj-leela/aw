package sg.activewealth.roboadvisor.banking.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import sg.activewealth.roboadvisor.infra.enums.EnumByteUserType;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.enums.PortfolioAssignmentCategory;

@Entity
@Table(name = "bank_detail")
@TypeDefs({ @TypeDef(name = "portfolioCategory", typeClass = EnumByteUserType.class, parameters = { @Parameter(name = "enumClassName", value = "sg.activewealth.roboadvisor.common.enums.PortfolioAssignmentCategory") }) })
public class BankDetail extends AbstractModel {

    private String name;
    private String address;
    private String swift;
    private String chips;
    private String aba;
    private String accountName;
    private String accountNumber;
    private String reference;
    private String charges;
    private String swiftSecondaryAddress;
    private PortfolioAssignmentCategory portfolioCategory = PortfolioAssignmentCategory.PublicBVI;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getChips() {
        return chips;
    }

    public void setChips(String chips) {
        this.chips = chips;
    }

    public String getAba() {
        return aba;
    }

    public void setAba(String aba) {
        this.aba = aba;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getSwiftSecondaryAddress() {
        return swiftSecondaryAddress;
    }

    public void setSwiftSecondaryAddress(String swiftSecondaryAddress) {
        this.swiftSecondaryAddress = swiftSecondaryAddress;
    }

    public PortfolioAssignmentCategory getPortfolioCategory() {
        return portfolioCategory;
    }

    public void setPortfolioCategory(PortfolioAssignmentCategory portfolioCategory) {
        this.portfolioCategory = portfolioCategory;
    }

}
