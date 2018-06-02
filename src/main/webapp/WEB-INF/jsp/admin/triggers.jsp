<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>


<%@page import="java.time.LocalDateTime"%>


<%@include file="header.jspf"%>
&nbsp;
<!-- begin row -->
<div class="row">
	<!-- begin col-12 -->
	<div class="col-md-12">
		<div class="panel panel-inverse">
			<div class="panel-heading">
			
				<h3 class="panel-title">Triggers </h3>
			</div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
					</div>
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Trigger Name(s)</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<tr>
                                        <td><strong>Trade Creation</strong></td>
                                        <td class="action">
                                        <a
                                        	href="${appContextName}/admin/userPortfolio/list?executionStatus=Funded" target="_blank"
                                            class="btn btn-primary"> &nbsp;Funded	
                                        </a>
                                        <a data-url="${appContextName}/admin/userTrade/createTradeTrigger" class="btn btn-primary executeButtonClass"> 
                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                        </a>
                                        <a
                                        	href="${appContextName}/admin/userPortfolio/list?executionStatus=Not Executed&transactionDate=transactionDateValue" target="_blank"
                                            class="btn btn-primary transactionDate">&nbsp;After Funded	
                                        </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Place Order</strong></td>
                                        <td class="action">
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Not Executed" target="_blank"
                                            class="btn btn-primary">&nbsp; Not Executed	
                                        </a>
                                        <a data-url="${appContextName}/admin/userTrade/placeOrderTrigger/executePlaceOrderRequest" class="btn btn-primary executeButtonClass">
                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                        </a>
 										<a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Executed&transactionDate=transactionDateValue" target="_blank"
                                            class="btn btn-primary transactionDate">&nbsp; After Executed	
                                        </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Place Order Error</strong></td>
                                        <td class="action">
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Partially Executed" target="_blank"
                                            class="btn btn-primary">&nbsp; Partially Executed	
                                        </a>
                                        <a data-url="${appContextName}/admin/userTrade/placeOrderTrigger/executePlaceOrderError" class="btn btn-primary executeButtonClass"> 
                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                        </a>
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Partially Executed&transactionDate=transactionDateValue" target="_blank"
                                            class="btn btn-primary transactionDate">&nbsp; Executed	
                                        </a> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Close Order Request</strong></td>
                                        <td class="action">
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Ready For Close" target="_blank"
                                            class="btn btn-primary">&nbsp;Closed Request	
                                        </a>
                                        <a data-url="${appContextName}/admin/userTrade/placeOrderTrigger/executeCloseOrderRequest" class="btn btn-primary executeButtonClass"> 
                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                        </a>
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Closed&transactionDate=transactionDateValue" target="_blank"
                                            class="btn btn-primary transactionDate">&nbsp;Closed	
                                        </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Close Order Error</strong></td>
                                        <td class="action">
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Partially Closed" target="_blank"
                                            class="btn btn-primary">&nbsp;Partially Closed	
                                        </a>
                                        <a data-url="${appContextName}/admin/userTrade/placeOrderTrigger/executeCloseOrderError" class="btn btn-primary executeButtonClass"> 
                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
                                        </a>
                                        <a
                                            href="${appContextName}/admin/userPortfolio/list?executionStatus=Partially Closed&transactionDate=transactionDateValue" target="_blank"
                                            class="btn btn-primary transactionDate">&nbsp;Closed	
                                        </a> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Update Rate</strong></td>
                                        <td class="action">
	                                        <a data-url="${appContextName}/admin/userTrade/updateRateTrigger" class="btn btn-primary executeButtonClass"> 
	                                        	<i class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute
	                                        </a>
                                        </td>
                                    </tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<misc:printPagination urlPageVar="requestPage"
							pagingDtoPageVar="list"
							ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0"
							printMode="both"></misc:printPagination>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- end row -->
</div>
<form name="executeForm" method="post">
</form> 
<script src="${assetsBase}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript">
	$('.transactionDate').click(function() {
		var dateValue = '<%= java.time.LocalDateTime.now().withNano(0) %>';
	 	var oldUrl = $(this).attr("href"); // Get current url
        var newUrl = oldUrl.replace("transactionDateValue", dateValue);
        $(this).attr("href", newUrl);
	});
	
	$('.executeButtonClass').on("click", function() {
		var url = $(this).data("url");
		var executeForm = document.executeForm;
		executeForm.action = url;
		executeForm.submit();
	});	
	
</script>
<%@include file="footer.jspf"%>