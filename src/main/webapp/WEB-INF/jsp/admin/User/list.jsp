<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/user/list" style="width:100%;">
		 <div class="row">
		 	<div class="col-md-2">
	            <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="- User Status -" name="accountStatus" multiple>
	                <c:forEach items="${accountStatusOpts}" var="dto">
	                    <c:set value="" var="selected" />
	                    <c:if test="${not empty paramValues['accountStatus']}">
		                    <c:forEach items="${paramValues['accountStatus']}" var="par">
		                        <c:if test="${par == dto.value}">
									<c:set value="selected" var="selected" />
								</c:if>
		                    </c:forEach>
	                    </c:if>
	                    <option class=" btn-group" value="${dto.value}" ${selected}>${dto.label}</option>
	                </c:forEach>
	            </select>
	        </div>
	        <div class="col-md-2">
	           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="agentOTPStatus" data-none-selected-text="- Agent OTP Status -" multiple>
	                <c:forEach items="${agentOTPStatusOpts}" var="dto">
	                    <c:set value="" var="selected" />
	                    <c:if test="${not empty paramValues['agentOTPStatus']}">
		                    <c:forEach items="${paramValues['agentOTPStatus']}" var="par">
		                        <c:if test="${par == dto.value}">
									<c:set value="selected" var="selected" />
								</c:if>
		                    </c:forEach>
	                    </c:if>
	                    <option value="${dto.value}" ${selected}>${dto.label}</option>
	                </c:forEach>
	            </select>
	        </div>
	        <div class="col-md-2">
	           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="kycStatus" data-none-selected-text="- Kyc Status -" multiple>
	                <c:forEach items="${kycStatusOpts}" var="dto">
	                    <c:set value="" var="selected" />
	                    <c:if test="${not empty paramValues['kycStatus']}">
		                    <c:forEach items="${paramValues['kycStatus']}" var="par">
		                        <c:if test="${par == dto.value}">
									<c:set value="selected" var="selected" />
								</c:if>
		                    </c:forEach>
	                    </c:if>
	                    <option value="${dto.value}" ${selected}>${dto.label}</option>
	                </c:forEach>
	            </select>
	        </div>
	        <div class="col-md-2">
	           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="bankDetailsStatus" data-none-selected-text="- Bank Details Status -" multiple>
	                <c:forEach items="${bankDetailsStatusOpts}" var="dto">
	                    <c:set value="" var="selected" />
	                     <c:if test="${not empty paramValues['bankDetailsStatus']}">
		                    <c:forEach items="${paramValues['bankDetailsStatus']}" var="par">
		                        <c:if test="${par == dto.value}">
									<c:set value="selected" var="selected" />
								</c:if>
		                    </c:forEach>
	                    </c:if>
	                    <option value="${dto.value}" ${selected}>${dto.label}</option>
	                </c:forEach>
	            </select>
	        </div>
			<div class="col-md-2">
	           	<input type="text" name="email" class="form-control" placeholder="Email Address" value="${param['email']}"/>
      		</div>        
	        <div class="col-md-2">
				<label class="checkbox checkbox-inline">
	                <input type="checkbox" name="isAdmin" <c:if test="${param['isAdmin'] == 'on'}">checked="checked"</c:if>>Is Admin
              	</label>
	        </div>
	     </div>
        <div class="row">
	        <div class="col-md-10 padding-top-10" >
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
   			<div class="col-md-2 pull-right"
				style="text-align: right; margin-bottom: 10px">
				<a href="${appContextName}/admin/user/profile/create"
					class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New User</a>
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
                    Users
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
										<th>Full Name</th>
										<th>Email Address</th>
										<th>User Status</th>
										<th>Kyc Status</th>
										<th>Bank Detail Status</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}" varStatus="status">
										<tr>
											<td>${serialCount+status.index +1}.</td>
											<td class="col-md-2"  style="padding: 10px 7px;">${model.firstName}&nbsp;${model.lastName}<c:if test="${model.isAdmin}"><strong>&nbsp;(admin)</strong></c:if></td>
											<td class="col-md-2"  style="padding: 10px 7px;">${model.email}</td>
											<td class="col-md-2"  style="padding: 10px 7px;">${model.accountStatus.label}</td>
											<td class="col-md-2"  style="padding: 10px 7px;">${model.kycStatus.label}</td>
											<td class="col-md-2"  style="padding: 10px 7px;">${model.bankDetailsStatus.label}</td>
											<td class="action col-md-2}" style="padding: 10px 7px;">
												<a href="${appContextName}/admin/user/profile?id=${model.id}"  class="btn btn-primary m-b-5">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
					                           
				                            	<c:if test="${model.isAdmin == false}">
					                            	<a href="${appContextName}/admin/userPortfolio/get/${model.id}"  class="btn btn-primary">
	                                                    <i class="fa fa-list-alt"></i>&nbsp;View Portfolio(s)
	                                                </a>
													<input type="hidden" id="userId" value="${model.id}" class="userId"  />
                                                </c:if>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<c:if test="${empty list.results}">
			                        <tr class="empty-row"><td colspan="7">No records found</td></tr>
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

<form action="../delete" name="deleteForm" method="post">
	<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
    <input type="hidden" name="id" />
</form>
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
</script>
<%@include file="../footer.jspf"%>