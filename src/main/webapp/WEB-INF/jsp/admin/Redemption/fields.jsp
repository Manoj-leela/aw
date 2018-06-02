<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf" %>

<!-- begin row -->
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    ${not empty model.id ? 'Update' : 'Create'} Redemption
                </h3>
            </div>

	<div class="panel">
		<div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/redemption/${formPostUrl}"
			commandName="${modelName}" method="post" role="form" data-parsley-validate="true">
			
			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
			
			<fieldset>
				<div class="row">
					<div class="col-sm-8 pull-left">
						<div class="form-group">
							<label for="userPortfolio" class="col-md-4 control-label">User Portfolio</label>
							<div class="col-md-8">
								<div class="input-group">
									<select id="userPortfolio" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="userPortfolio.id" data-parsley-required="true">
					            	<option value="">Email - User Portfolio</option>
					                <c:forEach items="${userPortfolioList}" var="userPortfolio">
					                    <option class=" btn-group" value="${userPortfolio.id}" <c:if test="${model.userPortfolio.id eq userPortfolio.id}">selected='selected'</c:if>>${userPortfolio.user.email} - ${userPortfolio.portfolio.name}</option>
					                </c:forEach>
					                <c:if test="${not empty param['id']}">
					                	<option class=" btn-group" value="${model.userPortfolio.id}" selected>${model.userPortfolio.user.email}-${model.userPortfolio.portfolio.name}</option>
					                </c:if>
					            </select>
					            <a href="${appContextName}/admin/userPortfolio/update" class="btn input-group-addon updateBtn" target="_blank">
                                   	<i class="fa fa-pencil"></i>
                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group">
							<label for="redemptionDate" class="col-md-4 control-label">Redemption Date *</label>
							<div class="col-md-8">
								<div class="input-group">
									<div id="redemptionDate" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
									<span class="btn input-group-addon">
					                    <i class="fa fa-calendar"></i>
					                </span>
					                <form:input id="redemptionDate" path="redemptionDate" cssClass="form-control" data-parsley-required="true" placeholder="2018-13-02"/>
								</div>								
								</div>
							</div>
						</div>
						<div class="form-group <spring:bind path="redemptionAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="redemptionAmount" class="col-md-4 control-label">Redemption Amount *</label>
							<div class="col-md-8">
								<form:input id="redemptionAmount" path="redemptionAmount" cssClass="form-control" data-parsley-required="true"/>
							</div>
						</div>
						<div class="form-group">
							<label for="redemptionStatus" class="col-md-4 control-label">Redemption Status</label>
							<div class="col-md-8">
					            <select id="redemptionStatus" class="form-control selectpicker fundingSelector" data-size="auto" data-live-search="true" data-style="btn-white"name="redemptionStatus">
					            	<option value="">- Redemption Status -</option>
					                <c:forEach items="${redemptionStatusList}" var="redemptionStatus">
					                    <option class=" btn-group" value="${redemptionStatus}" <c:if test="${model.redemptionStatus.label eq redemptionStatus.label}">selected='selected'</c:if>>${redemptionStatus.label}</option>
					                </c:forEach>
					            </select>
					        </div>
						</div>
						<div class="form-group">
							<label for="dob" class="col-md-4 control-label">Broker Batch</label>
							<div class="col-md-8">
								<div id="brokerBatch" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
									<span class="btn input-group-addon">
					                    <i class="fa fa-calendar"></i>
					                </span>
					                <form:input id="brokerBatch" path="brokerBatch" cssClass="form-control" placeholder="yyyy-mm-dd"/>
								</div>
							</div>
						</div>
						<div class="form-group <spring:bind path="amountRequestedFromBroker"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="amountRequestedFromBroker" class="col-md-4 control-label"> Amount Request From Broker</label>
							<div class="col-md-8">
								<form:input id="amountRequestedFromBroker" path="amountRequestedFromBroker" cssClass="form-control"/>
							</div>
						</div>
						<div class="form-group <spring:bind path="amountReceivedFromBroker"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="amountReceivedFromBroker" class="col-md-4 control-label"> Amount Received From Broker</label>
							<div class="col-md-8">
								<form:input id="amountReceivedFromBroker" path="amountReceivedFromBroker" cssClass="form-control"/>
							</div>
						</div>
						<div class="form-group <spring:bind path="amountReceivedFees"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="amountReceivedFees" class="col-md-4 control-label"> Amount Received Fees</label>
							<div class="col-md-8">
								<form:input id="amountReceivedFees" path="amountReceivedFees" cssClass="form-control"/>
							</div>
						</div>
						<div class="form-group <spring:bind path="netRedemptionAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="netRedemptionAmount" class="col-md-4 control-label"> Net Redemption Amount</label>
							<div class="col-md-8">
								<form:input id="netRedemptionAmount" path="netRedemptionAmount" cssClass="form-control"/>
							</div>
						</div>
						<div class="form-group <spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="remarks" class="col-md-4 control-label">Remarks</label>
                            <div class="col-md-8">
                                <form:textarea id="remarks" path="remarks" cssClass="form-control"/>
                            </div>
                        </div>
						<div class="form-group">
							<span class=" col-md-offset-4 m-b-5 label label-info fundingDescriptor"></span>
						</div>
					</div>
				</div>
			</fieldset>
			<fieldset disabled>
				<div class="row">
					<div class="col-sm-8 pull-left">
					
						<div class="form-group">
							<label for="redemptionCompletedEmailSent" class="col-md-4 control-label">Redemption Completed Email Sent</label>
							<div class="col-md-8">
								<form:input id="redemptionCompletedEmailSent" path="redemptionCompletedEmailSent"
									cssClass="form-control" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="createdOn" class="col-md-4 control-label">Created
								on</label>
							<div class="col-md-8">
								<form:input id="createdOn" path="createdOn"
									cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label for="updatedOn" class="col-md-4 control-label">Updated
								on</label>
							<div class="col-md-8">
								<form:input id="updatedOn" path="updatedOn"
									cssClass="form-control" />
							</div>
						</div>
					</div>
				</div>
			</fieldset>
			<fieldset>
				<div class="row">
					<div class="col-sm-8">
						<div class="form-group">
									<label for="type" class="col-md-4 control-label"></label>
									<div class="col-md-8">
										<button type="submit" class="btn btn-primary">Save</button>
										<c:if test="${not empty param['id']}">
		   	                            	<a class="btn btn-inverse deleteButton"  data-id="${model.id}">
					                        	<i class="fa fa-trash" ></i>&nbsp;Delete
					                    	</a>
										</c:if>
										<a href="${appContextName}/admin/redemption/list" class="btn btn-primary cancelRedemptionBtn">
					                                <i class="fa -square-o"></i>Cancel
					                    </a>
									</div>
							</div>
					</div>
				</div>
			</fieldset>
		</form:form>
		</div>
	</div>
	</div>
	</div>
</div>
<!-- end row -->
<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
		setDescription();
	});
	function setDescription(){
	 	var fundingDescription = $('.fundingSelector option:selected').data('description');	
	 	if(fundingDescription){
	 		 $('.fundingDescriptor').text(fundingDescription);	 
	 	 	}else{
	 	 		$('.fundingDescriptor').text('');
	 	 	}	 
	 }

	 $('.fundingSelector').bind('change', function() {
		 setDescription();
	 });
	 
	 var redemptionCookie =  $.cookie("redemptionCookieUrl");
	 /* $('.cancelRedemptionBtn').on("click", function() {
		 $(this).attr('href',redemptionCookie);
		$.cookie("remittanceCookieUrl", '${requestPage}');
	 }); */	
</script>
<%@include file="../footer.jspf" %>
