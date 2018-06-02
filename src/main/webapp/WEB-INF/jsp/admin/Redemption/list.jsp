<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>

<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.RedemptionStatus).values()" var="redemptionStatuses" />

&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/redemption/list" style="width:100%;">
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
           	<input type="text" name=redemptionAmount class="form-control" placeholder="Redemption Amount" value="${param['redemptionAmount']}"/>
      	</div>
      	<div class="col-md-2">
		   	<select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="redemptionStatus">
	             <option value="">- Redemption Status -</option>
	             <c:forEach items="${redemptionStatusList}" var="status">
	             	<option class=" btn-group" value="${status.value}" <c:if test="${param['redemptionStatus'] eq status.value}">selected='selected'</c:if>>${status.label}</option>
	          	</c:forEach>
         	</select>
        </div>
        <div class="col-md-2">
            <div class="input-group date" data-provide="datepicker"
                 data-date-format="yyyy-mm-dd">
				<span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input value="${param['redemptionDate']}" type="text" name="redemptionDate" class="form-control" placeholder="Redemption Date" />
            </div>
        </div>
      	<div class="row"></div>
        <div class="col-md-8 padding-top-10 m-t-5" >
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
			<a href="${appContextName}/admin/redemption/create"
				class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New Redemption</a>
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
                    Redemption
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
										<th>Redemption Amount</th>
										<th>Redemption Date</th>
										<th>Redemption Status</th>
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
											<td class="col-md-2"><fmt:formatNumber minFractionDigits="3" value="${model.redemptionAmount}" /></td>
											<td class="col-md-2">${model.redemptionDate}</td>
											<td class="col-md-2">${model.redemptionStatus.label}</td>
											<td class="action col-md-1">
												<a href="${appContextName}/admin/redemption/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
											</td>
										</tr>
									</c:forEach>
									<c:if test="${empty list.results}">
				                        <tr class="empty-row"><td colspan="7">No records found</td></tr>
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
</script>
<%@include file="../footer.jspf"%>