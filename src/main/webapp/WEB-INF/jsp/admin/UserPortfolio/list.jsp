<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/userPortfolio/list" style="width:100%;">
	 <div class="row">
	 	<div class="col-md-3">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="executionStatus" data-none-selected-text="- Portfolio Status -">
                <c:forEach items="${userPortfolioStatusList}" var="userPortfolioStatusList">
                    <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['executionStatus']}">
	                    <c:forEach items="${paramValues['executionStatus']}" var="executionStatus">
	                        <c:if test="${executionStatus eq userPortfolioStatusList.value}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option value="${userPortfolioStatusList.value}" ${selected}>${userPortfolioStatusList.label}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
         	<input type="text" name="user.email" class="form-control" placeholder="User Email" value="${param['user.email']}"/>
   		</div>
   		<div class="col-md-2">
         	<input type="text" name="portfolio.name" class="form-control" placeholder="Portfolio Name" value="${param['portfolio.name']}"/>
   		</div>   
      	<div class="row"></div>
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
			<a href="${appContextName}/admin/userPortfolio/create"
				class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New UserPortfolio</a>
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
                    User Portfolio
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
										<th>Net Investment Amount</th>
										<th>Execution Status</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}"
										varStatus="status">
										<tr>
											<td class="col-md-1">${serialCount+status.index +1}.</td>
											<td class="col-md-3">
											<a href="${appContextName}/admin/user/profile?id=${model.user.id}" target="_blank">
											${model.user.email}</a></td>
											<td class="col-md-2">
											<a href="${appContextName}/admin/portfolio/update?id=${model.portfolio.id}" target="_blank">
											${model.portfolio.name}</a></td>
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.netInvestmentAmount}" /></td>
											<td class="col-md-2">${model.executionStatus.label}</td>
											<td class="action col-md-2">
												<a href="${appContextName}/admin/userPortfolio/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<c:if test="${empty list.results}">
			                        <tr class="empty-row"><td colspan="6">No records found</td></tr>
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
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
</script>
<%@include file="../footer.jspf"%>