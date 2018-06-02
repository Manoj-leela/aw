<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/externalFundSubscription/list" style="width:100%;">
     <div class="row">
        <div class="col-md-3">
            <input type="text" name="name" class="form-control" placeholder="External Fund Name" value="${param['name']}"/>
        </div>
        </div>
		<div class="row m-t-10">
        <div class="col-md-8 padding-top-10" >
            <table>
                <tr style="padding: 15px;">
                    <td>
                         <div class="btn-group">
                             <button class="btn btn-success" type="submit">
                                 <i class="fa fa-search"></i> Search
                             </button>
                        </div>
                    </td>
                    <td>
                        <div class="btn-group">
                          <button type="reset" class="btn btn-primary">
                            Reset
                          </button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="pull-right m-r-10">
            <a href="${appContextName}/admin/externalFundSubscription/create"
                class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New External Fund Subscription</a>
        </div>
     </div>
</form>
<!-- begin row -->
<div class="row">
	<!-- begin col-12 -->
    <div class="col-md-12">
    	<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    External Fund Subscription
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>#</th>
										<th>Fund Name</th>
										<th>Fund Price</th>
										<th>Subscription Amount</th>
										<th>Invest Amount</th>
										<th>Shares</th>
										<th>Balance Shares</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}"
										varStatus="status">
										<tr>
											<td class="col-md-1">${serialCount+status.index +1}.</td>
											<td class="col-md-2"><a href="${appContextName}/admin/externalFund/update?id=${model.externalFund.id}" target="_blank">${model.externalFund.name}</a></td>
											<td class="col-md-2">
											<a href="${appContextName}/admin/externalFundPrice/update?id=${model.externalFundPrice.id}" target="_blank">
											<fmt:formatNumber minFractionDigits="3" value="${model.externalFundPrice.buyPrice}" /></a></td>
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.totalSubscriptionAmount}" /></td>
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.netInvestAmount}" /></td>
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.shares}" /></td>
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.balanceShares}" /></td>
											<td class="action col-md-3">
												<a href="${appContextName}/admin/externalFundSubscription/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<c:if test="${empty list.results}">
			                        <tr class="empty-row"><td colspan="8">No records found</td></tr>
			                    </c:if>
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

<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<script type="text/javascript">
$(document).ready(function() {
	$('.selectpicker').selectpicker();
});
$('#filterForm :reset').on('click', function(evt) {
    evt.preventDefault()
    $form = $(evt.target).closest('form')
    $form[0].reset()
    $form.find('input').prop('checked', false); // Unchecks it
    $form.find('input').val("");
    $form.find('select').val("");
    $form.find('select').selectpicker('refresh');
});
</script>
<%@include file="../footer.jspf"%>