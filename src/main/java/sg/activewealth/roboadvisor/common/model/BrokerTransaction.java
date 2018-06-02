package sg.activewealth.roboadvisor.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.portfolio.model.Instrument;

@Entity
@Table(name = "broker_transactions")
public class BrokerTransaction extends AbstractModel {

	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;

	@ManyToOne
	@JoinColumn(name = "instrument_id")
	private Instrument instrument;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "total_investment_amount")
	private BigDecimal totalInvestmentAmount;
	
	@Column(name = "total_fees")
	private BigDecimal totalFees;
	
	@Column(name = "net_investment_amount")
	private BigDecimal netInvestmentAmount;
	
	@Column(name = "profit_and_loss")
	private BigDecimal profitAndLoss;
	
	public BrokerTransaction() {
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalInvestmentAmount() {
		return totalInvestmentAmount;
	}

	public void setTotalInvestmentAmount(BigDecimal totalInvestmentAmount) {
		this.totalInvestmentAmount = totalInvestmentAmount;
	}

	public BigDecimal getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}

	public BigDecimal getNetInvestmentAmount() {
		return netInvestmentAmount;
	}

	public void setNetInvestmentAmount(BigDecimal netInvestmentAmount) {
		this.netInvestmentAmount = netInvestmentAmount;
	}

	public BigDecimal getProfitAndLoss() {
		return profitAndLoss;
	}

	public void setProfitAndLoss(BigDecimal profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BrokerTransaction [transactionDate=");
		builder.append(transactionDate);
		builder.append(", instrument=");
		builder.append(instrument);
		builder.append(", price=");
		builder.append(price);
		builder.append(", totalInvestmentAmount=");
		builder.append(totalInvestmentAmount);
		builder.append(", totalFees=");
		builder.append(totalFees);
		builder.append(", netInvestmentAmount=");
		builder.append(netInvestmentAmount);
		builder.append(", profitAndLoss=");
		builder.append(profitAndLoss);
		builder.append("]");
		return builder.toString();
	}
	
}
