<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).values()" var="brokerStatusList" />
<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).values()" var="investorStatusList" />
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/remittance/list" style="width:100%;">
	 <div class="row">
	  	<div class="col-md-2">
	  		<select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="User Email-Porfolio Name" name="userPortfolio.id" multiple>
                <c:forEach items="${userPortfolios}" var="userPortfolio">
	                <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['userPortfolio.id']}">
	                    <c:forEach items="${paramValues['userPortfolio.id']}" var="par">
	                        <c:if test="${par == userPortfolio.id}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option class=" btn-group" value="${userPortfolio.id}" ${selected}>${userPortfolio.user.email}-${userPortfolio.portfolio.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="investorRemittanceStatus">
				 <option value="">- Investor status -</option>
				<c:forEach items="${investorStatusList}" var="status">
                	<option class=" btn-group" value="${status}" <c:if test="${param['investorRemittanceStatus'] eq status}">selected='selected'</c:if>>${status.label}</option>
             	</c:forEach>
            </select>
        </div>
        <div class="col-md-2">
           <select class="selectpicker append fundingSubmittedClass" data-size="auto" data-live-search="true" data-style="btn-white" name="brokerFundingStatus">
               <option value="">- Broker Status -</option>
                <c:forEach items="${brokerStatusList}" var="status">
                	<option class=" btn-group" value="${status}" <c:if test="${param['brokerFundingStatus'] eq status}">selected='selected'</c:if>>${status.label}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
           	<input type="text" name="referenceNumber" class="form-control" placeholder="Remittance Reference" value="${param['referenceNumber']}"/>
      	</div>
      	<div class="row"></div>
        <div class="col-md-10 padding-top-10 m-t-5" >
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
			<a href="${appContextName}/admin/remittance/create"
				class="btn btn-primary remittanceCookie"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New Remittance</a>
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
                    Remittance
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
										<th>User Email</th>
										<th>Portfolio Name</th>
										<th>Remittance Reference</th>
										<th>Investor Remittance</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}"
										varStatus="status">
										<tr>
											<td>${serialCount+status.index +1}.</td>
											<td class="col-md-2"><a href="${appContextName}/admin/user/profile?id=${model.userPortfolio.user.id}" target="_blank">${model.userPortfolio.user.email}</a></td>
											<td class="col-md-3"><a href="${appContextName}/admin/portfolio/update?id=${model.userPortfolio.portfolio.id}" target="_blank">${model.userPortfolio.portfolio.name}</a></td>
											<td class="col-md-2">${model.referenceNo}</td>
											<td class="col-md-2">${model.investorRemittanceStatus.label}</td>
											<td class="action col-md-2">
												<a href="${appContextName}/admin/remittance/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
											</td>
										</tr>
									</c:forEach>
									<c:if test="${empty list.results}">
				                        <tr class="empty-row"><td colspan="6">No records found</td></tr>
				                    </c:if>
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
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
	$('.remittanceCookie').on("click", function() {
		$.cookie("remittanceCookieUrl", '${requestPage}');
	});
</script>
<%@include file="../footer.jspf"%>