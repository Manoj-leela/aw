<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
<spring:eval expression="T(sg.activewealth.roboadvisor.trade.enums.TradeStatus).PlaceOrderRequest" var="placeOrderRequest" />
<spring:eval expression="T(sg.activewealth.roboadvisor.trade.enums.TradeStatus).PlaceOrderError" var="placeOrderError" />

<spring:eval expression="T(sg.activewealth.roboadvisor.trade.enums.TradeStatus).CloseOrderRequest" var="closeOrderRequest" />
<spring:eval expression="T(sg.activewealth.roboadvisor.trade.enums.TradeStatus).CloseOrderError" var="closeOrderError" />

<spring:eval expression="{placeOrderRequest, placeOrderError, closeOrderRequest, closeOrderError}" var="orderStatuses" />
&nbsp;
<form id="frmExecuteOrder" name="frmExecuteOrder" method="POST">

<div class="m-b-10">
  <a href="javascript:executeSelectedTrade()" class="btn btn-primary">
       <i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute Selected Trades
    </a>
</div>

<div class="panel panel-inverse" data-sortable-id="form-validation-1">
	<div class="panel-heading">
		<h4 class="panel-title">User Portfolios</h4>
	</div>
	<input type="hidden" name="queryParamStatus" value="${param['status']}"/>
	<div class="panel-body panel-form">
		<c:forEach items="${userPortfolios.results}" var="userPortfolios"
			varStatus="list">
			<div class="panel panel-inverse" style="padding: 25px;">
				<div class="panel-heading">
					<h4 class="panel-title">
						Portfolio : ${userPortfolios.portfolio.name}
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						User : ${userPortfolios.user.firstName} ${userPortfolios.user.lastName}
						 <span class="pull-right">Created On :${userPortfolios.createdOn.toLocalDate()}</span>
					</h4>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label
							class="control-label col-md-4 col-sm-4">Balance :
							     <fmt:formatNumber minFractionDigits="3" value="${userPortfolios.totalUninvestedAmount}" /></label>
							 <label
                            class="control-label col-md-4 col-sm-4">Amount Invested :
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
									<th>
										<%-- <c:if test="${trade.executionStatus == 'PlaceOrderRequest' || trade.executionStatus == 'PlaceOrderError'}"> --%>
											<input type="checkbox" class="allChecked" id="allChecked">
										<%-- </c:if> --%>
									</th>
									<th>Instrument</th>
									<th>Trade Status</th>
									<th>Allocation</th>
									<th>Units</th>
									<th>Current Price</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${userPortfolios.userTradeList}" var="trade"
									varStatus="list">
									<tr>
										<td>
                                           <c:if test="${orderStatuses.contains(trade.executionStatus)}">
                                                <input type="checkbox" class="tradeClass" name="tradeIds" value="${trade.id}">
                                           </c:if>
                                        </td>
										<td>${trade.portfolioInstrument.instrument.name}</td>
										<td>${trade.executionStatus.label}</td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.allocatedAmount}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.enteredUnits}" /></td>
										<td><fmt:formatNumber minFractionDigits="3" value="${trade.portfolioInstrument.instrument.currentPrice}" /> </td>
										<td>
                                            <c:if test="${orderStatuses.contains(trade.executionStatus)}">
                                                <a href="javascript:executeSingleTrade('${trade.id}')"
                                                   class="btn btn-primary">
                                                   <i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                                </a>
                                            </c:if>
                                       </td>
									</tr>
									
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<input type="hidden" name="status" value="${status}">
		</c:forEach>
		<div class="row" style=" margin-right: 15px;margin-left: 15px;">
			<misc:printPagination urlPageVar="requestPage"
				pagingDtoPageVar="userPortfolios"
				ulClass="pagination pull-right m-t-5 m-l-0 m-r-0"
				printMode="both"></misc:printPagination>
		</div>
	</div>
</div>
<input type="hidden" name="tradeIdList" id="tradeIdList"/>
</form>
<%@include file="../footer.jspf"%>

<script lang="javascript">

 //select all checkboxes 
 $("#allChecked").change(function(){   
    $(".tradeClass").prop('checked', $(this).prop("checked"));
 }); 

 $('.tradeClass').change(function(){ 
	if(false == $(this).prop("checked")){ 
		$("#allChecked").prop('checked', false); 
	}

	if ($('.tradeClass:checked').length == $('.tradeClass').length ){
		$("#allChecked").prop('checked', true);
	 }
 });

function executeSingleTrade(tradeId) {

document.getElementById("frmExecuteOrder").action = "${appContextName}/admin/userTrade/execute";
document.getElementById("tradeIdList").value =tradeId;
document.getElementById("frmExecuteOrder").submit();

}

function executeSelectedTrade() {

if($("input[type='checkbox'][name='tradeIds']:checked").length == 0 ) {
    alert('Please select atleast one trade to process');
    return false;
}

var selectedIds ="";
$("input[type='checkbox'][name='tradeIds']:checked").each(function () {selectedIds += $(this).val()+","});

document.getElementById("frmExecuteOrder").action = "${appContextName}/admin/userTrade/execute";
document.getElementById("tradeIdList").value = selectedIds;
document.getElementById("frmExecuteOrder").submit();

}


</script>