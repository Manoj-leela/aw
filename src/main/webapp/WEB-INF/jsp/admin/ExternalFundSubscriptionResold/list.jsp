<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf" %>

&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/externalFundSubscriptionResold/list" style="width:100%;">
     <div class="row">
        <div class="col-md-3">
            <input type="text" name="name" class="form-control" placeholder="Fund Susbscription" value="${param['name']}"/>
        </div>
        <div class="col-md-2">
            <div class="input-group date" data-provide="datepicker"
                 data-date-format="yyyy-mm-dd">
				<span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input value="${param['transactionDate']}" type="text" name="transactionDate" class="form-control" placeholder="Date" />
            </div>
        </div>
        <div class="col-md-2">
            <input type="text" name="buyPrice" class="form-control" placeholder="Fund Buy Price" value="${param['buyPrice']}"/>
        </div>
        <div class="col-md-2">
            <input type="text" name="sellPrice" class="form-control" placeholder="Fund Sell Price" value="${param['sellPrice']}"/>
        </div>
		<div class="row m-t-10"></div>
        <div class="col-md-8 m-t-10" >
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
     </div>
</form>

<!-- begin row -->
<div class="row">
    <!-- begin col-12 -->
    <div class="col-md-12">
        <!-- begin panel -->
        <div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    External Fund Subscription Resold
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
				                  	    <th class="_cell_5">External Fund Subscription</th>
				                  	    <th class="_cell_5">Fund Buy Price</th>
				                  		<th class="_cell_5">Fund Sell Price</th>
				                  		<th class="_cell_5">Transaction Date</th>
				                  		<th class="_cell_5">Total Subscription Amount</th>
				                  		<th class="_cell_5">Net Invest Amount</th>
				                  		<th class="_cell_5">Shares</th>
				                    </tr>
                    			</thead>
                    			<tbody class="_body">
                    				<c:if test="${not empty list.results}">
					                    <c:forEach var="model" items="${list.results}" varStatus="status">
					                    <tr>
					                    	<td>${serialCount+status.index +1}.</td>
				                    		<td><a href="${appContextName}/admin/externalFundSubscription/update?id=${model.externalFundSubscription.id}" target="_blank">${model.externalFundSubscription.externalFund.name}</a></td>
				                    		<td><a href="${appContextName}/admin/externalFundPrice/update?id=${model.externalFundSubscription.externalFundPrice.id}" target="_blank"><fmt:formatNumber minFractionDigits="3" value="${model.externalFundPrice.buyPrice}" /></a></td>
				                    		<td><a href="${appContextName}/admin/externalFundPrice/update?id=${model.externalFundSubscription.externalFundPrice.id}" target="_blank"><fmt:formatNumber minFractionDigits="3" value="${model.externalFundPrice.sellPrice}" /></a></td>
				                    		<td>${model.transactionDate}</td>
				                    		<td><fmt:formatNumber minFractionDigits="3" value="${model.totalSubscriptionAmount}" /></td>
				                    		<td><fmt:formatNumber minFractionDigits="3" value="${model.netInvestAmount}" /></td>
				                    		<td><fmt:formatNumber minFractionDigits="3" value="${model.shares}" /></td>
					                    </tr>
					                    </c:forEach>
                    			</c:if>
			                    <c:if test="{${empty list.results}">
			                        <tr class="empty-row"><td colspan="3">No records found</td></tr>
			                    </c:if>
                    		</tbody>
                		</table>
                	</div>
                	</div>
	                <c:if test="${not empty list.results}">
		                <div class="row">
						<misc:printPagination urlPageVar="requestPage"
							pagingDtoPageVar="list"
							ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0"
							printMode="both"></misc:printPagination>
					</div>
	                </c:if>
           	 	</div>
            </div>
        </div>
        <!-- end panel -->
    </div>
    <!-- end col-12 -->
</div>
<%@include file="../footer.jspf"%>