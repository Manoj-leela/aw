<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<div class="panel panel-inverse" data-sortable-id="form-validation-1">
	<div class="panel-heading">
		<h4 class="panel-title">User Portfolios - ${user.firstName}
			${user.lastName}</h4>
	</div>
	<div class="panel-body panel-form">
		<form class="form-horizontal form-bordered"
			data-parsley-validate="true" name="demo-form" novalidate="">
			<div class="form-group">
				<label class="control-label col-md-4 col-sm-4" for="email">Email
					:</label>
				<div class="col-md-6 col-sm-6">
					<input class="form-control" id="email" name="email"
						data-parsley-type="email" placeholder="Email..."
						readonly="readonly" data-parsley-required="true" type="text"
						value="${user.email}">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-4 col-sm-4" for="phone">
					Phone :</label>
				<div class="col-md-6 col-sm-6">
					<input class="form-control" id="phone" name="phone"
						placeholder="Phone Number..." data-parsley-required="true"
						type="text" value="${user.mobileNumber}" readonly="readonly">
				</div>
			</div>
		</form>
		<c:forEach items="${userPortfolios.results}" var="userPortfolios"
			varStatus="list">
			<div class="panel panel-inverse" style="padding: 25px;">
				<div class="panel-heading">
					<h4 class="panel-title">
						Portfolio : ${userPortfolios.portfolio.name} <span
							class="pull-right">Created On :
							${userPortfolios.createdOn.toLocalDate()}</span>
					</h4>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label
							class="control-label col-md-4 col-sm-4">Total Uninvested Amount:
							     <fmt:formatNumber minFractionDigits="3" value="${userPortfolios.totalUninvestedAmount}" /></label>
							 <label
                            class="control-label col-md-4 col-sm-4">Net Investment Amount :
                            <fmt:formatNumber minFractionDigits="3" value="${userPortfolios.netInvestmentAmount}" />    
                            </label>
							 <label
							class="control-label col-md-4 col-sm-4 pull-right">Status
							: ${userPortfolios.executionStatus.label} </label>
					</div>
					&nbsp;
					<div class="form-group">
						<label class="control-label col-md-4 col-sm-4">UnRealised
							Pnl : <fmt:formatNumber minFractionDigits="3" value="${userPortfolios.unrealisedPnl}" /> </label> <label
							class="control-label col-md-4 col-sm-4">Net Asset Value :
							<fmt:formatNumber minFractionDigits="3" value="${userPortfolios.netAssetValue}" />
							</label> <label
							class="control-label col-md-4 col-sm-4 pull-right">Realised
							Pnl : <fmt:formatNumber minFractionDigits="3" value="${userPortfolios.realisedPnl}" /> </label>
					</div>
					&nbsp;
					<div class="table-responsive">
						<table class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>SN</th>
									<th>Instrument</th>
									<th>Trade Status</th>
									<th>Entered Amount</th>
									<th>Closed Amount</th>
									<th>Fees (BothTradeLegs)</th>
									<th>Units</th>
									<th>Current Price</th>
									<th>Entered Price</th>
									<th>Close Price</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${userPortfolios.userTradeList}" var="trade"
									varStatus="list">
									<tr>
										<td>${list.count}</td>
										<td>${trade.portfolioInstrument.instrument.name}</td>
										<td>${trade.executionStatus.label}</td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.enteredAmount}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.closedAmount}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.feesBothTradeLegs}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.enteredUnits}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.portfolioInstrument.instrument.currentPrice}" /> </td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.enteredPrice}" /> </td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.closedPrice}" /> </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</c:forEach>
		<div class="row" style=" margin-right: 15px;margin-left: 15px;">
			<misc:printPagination urlPageVar="requestPage"
				pagingDtoPageVar="userPortfolios"
				ulClass="pagination pull-right m-t-5 m-l-0 m-r-0"
				printMode="both"></misc:printPagination>
		</div>
	</div>
</div>
<%@include file="../footer.jspf"%>