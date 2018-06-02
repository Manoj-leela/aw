package sg.activewealth.roboadvisor.portfolio.enums;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;

public enum UserPortfolioTransactionStatus implements ByteEnum {
	
    UserPortfolioCreated(0, "User Portfolio Created", null),
    UserPortfolioAssigned(1, "User Portfolio Assigned", UserPortfolioStatus.Assigned),
    UserPortfolioFunded(2, "User Portfolio Funded", UserPortfolioStatus.Funded),
    UserPortfolioNotExecuted(3, "User Portfolio Not Executed", UserPortfolioStatus.NotExecuted),
    UserPortfolioPartiallyExecuted(4, "User Portfolio Partially Executed", UserPortfolioStatus.PartiallyExecuted),
    UserPortfolioExecuted(5, "User Portfolio Executed", UserPortfolioStatus.Executed),
    UserPortfolioCloseRequested(6, "User Portfolio Close Requested", UserPortfolioStatus.CloseRequested),
    UserPortfolioReadyForClose(7, "User Portfolio Ready To Close", UserPortfolioStatus.ReadyForClose),
    UserPortfolioPartiallyClosed(8, "User Portfolio Partially Closed", UserPortfolioStatus.PartiallyClosed),
    UserPortfolioFundReleased(9, "User Portfolio Fund Released", UserPortfolioStatus.ClosedAndReleased),
    UserPortfolioClosed(10, "User Portfolio Closed", UserPortfolioStatus.Closed),
    UserPortfolioAssignedLater(11, "User Portfolio Assigned Later", UserPortfolioStatus.AssignedForLater),
    UserPortfolioAssignedReplace(12, "User Portfolio Assign Replace", UserPortfolioStatus.AssignedForReplace),
    UserPortfolioReadyRebalance(13, "User Portfolio Ready Replace", UserPortfolioStatus.ReadyForRebalance),
    UserPortfolioReturnsUpdated(14, "User Portfolio Returns Updated", null);

    private byte value;
    private String label;
    private UserPortfolioStatus userPortfolioStatus;

    private UserPortfolioTransactionStatus(int value, String label, UserPortfolioStatus userPortfolioStatus) {
        this.value = (byte) value;
        this.label = label;
        this.userPortfolioStatus = userPortfolioStatus;
    }

    public static UserPortfolioTransactionStatus getStatusByUserPortfolioStatus(UserPortfolioStatus status) {
        UserPortfolioTransactionStatus portfolioTransactionStatus = null;
        for (UserPortfolioTransactionStatus statuses : UserPortfolioTransactionStatus.values()) {
            if (statuses.userPortfolioStatus != null && statuses.userPortfolioStatus.getLabel().equalsIgnoreCase(status.getLabel())) {
                portfolioTransactionStatus = statuses;
                break;
            }
        }
        return portfolioTransactionStatus;
    }

    public static UserPortfolioTransactionStatus get(String statusValue) {
        UserPortfolioTransactionStatus status = null;
        for (UserPortfolioTransactionStatus statusValues : UserPortfolioTransactionStatus.values()) {
            if (statusValues.label.equals(statusValue)) {
                status = statusValues;
                break;
            }
        }
        return status;
    }

    @Override
    public byte getValue() {
        return this.value;
    }

    public String getLabel() {
        return label;
    }

    public UserPortfolioStatus getUserPortfolioStatus() {
        return userPortfolioStatus;
    }
	
}

