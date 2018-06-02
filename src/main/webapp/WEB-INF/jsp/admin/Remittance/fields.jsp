<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<link href="${assetsBase}/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet" />
<link href="${assetsBase}/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />
<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf" %>
<!-- begin row -->
<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).Received" var="fundingStatusFunded" />
<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).Completed" var="fundingStatusCompleted" />

<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).values()" var="investorRemittanceList" />
<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).values()" var="brokerFundingStatusList" />
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    ${not empty model.id ? 'Update' : 'Create' } Remittance
                </h3>
            </div>

	<div class="panel">
		<div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/remittance/${formPostUrl}"
			commandName="${modelName}" method="post" role="form" data-parsley-validate="true" enctype="multipart/form-data">
			
			<input type="hidden" id="remittanceSlipFileName" name="remittanceSlipFileName" value="${model.remittanceSlipFileName}"/>
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
									<select id="userPortfolio" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white"name="userPortfolio.id" data-parsley-required="true">
					            	<option value="">Email - User Portfolio</option>
					            	<c:if test = "${model.investorRemittanceStatus ne fundingStatusFunded}">
						                <c:forEach items="${userPortfolioList}" var="userPortfolio">
						                    <option class=" btn-group" value="${userPortfolio.id}" <c:if test="${model.userPortfolio.id eq userPortfolio.id}">selected='selected'</c:if>>${userPortfolio.user.email}-${userPortfolio.portfolio.name}</option>
						                </c:forEach>
					            	</c:if> 
					               <c:if test = "${(model.investorRemittanceStatus eq fundingStatusFunded) or (model.brokerFundingStatus eq fundingStatusCompleted)}">
					                	<option class=" btn-group" value="${model.userPortfolio.id}" selected='selected'>${model.userPortfolio.user.email}-${model.userPortfolio.portfolio.name}</option>
					                </c:if> 
					            </select>
					            <a href="${appContextName}/admin/userPortfolio/update" class="btn input-group-addon updateBtn" target="_blank">
                                   	<i class="fa fa-pencil"></i>
                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group  <spring:bind path="referenceNo"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="referenceNo" class="col-md-4 control-label">Remittance Reference No *</label>
							<div class="col-md-8">
								<form:input id="referenceNo" path="referenceNo" cssClass="form-control" data-parsley-required="true"/>
							</div>
						</div>
						<div class="form-group">
							<label for="remittanceSlipFileName" class="col-md-4 control-label">Remittance Slip *</label>
							<div class="col-md-8">
								<c:if test="${empty model.remittanceSlipFileName }">
									<span class="btn btn-white fileinput-button"> <i class="fa fa-plus"></i> <span>Add file...</span>
	                         			<input id="remittanceSlipFileName" class="userFile" type="file" name="attachment" accept="application/pdf,image/jpeg" data-parsley-required="true"/>
		                      		</span>
		                      		<a href="javascript:;" id="filename" style="display: none;"class="btn btn-success ">Invalid File Type</a>
		                      		<div style="clear:both;"></div>
									<div class="m-l-5 ">
										.jpg/.jpeg/.png /.pdf
									</div>
								</c:if>
								<c:if test="${not empty model.remittanceSlipFileName }">
			                        
	                            	<a href="${appContextName}/admin/remittance/download?id=${model.id}">
			                            <c:choose>
		                            	<c:when test="${fn:containsIgnoreCase(model.remittanceSlipFileName,'.pdf')}">
		                            	<img src="${assetsBase}/img/robo_pdf.jpg" class=" col-md-4">
		                            	</c:when>
		                            	<c:otherwise>
		                            		<img src="${appContextName}/admin/remittance/download?id=${model.id}" class="col-md-6"/>
		                            	</c:otherwise>
		                            	</c:choose>
		                         	</a>
		                         	<div class="form-group col-md-12 m-l-2 m-t-2">
		                         	<a class="btn btn-primary" href="${appContextName}/admin/remittance/download?id=${model.id}"
			                            class="btn btn-default dropdown-toggle" aria-expanded="false" target="_blank"><i class="fa fa-download"></i>&nbsp Download</a>
			                        <a class="btn btn-primary deleteRemittanceFile" data-id="${model.id}">
	                                	<i class="fa fa-download"></i>&nbsp;Delete
	                            	</a>
		                         	</div>
								</c:if>
                   			</div>
						</div>

						<div class="form-group">
							<label for="investorRemittance" class="col-md-4 control-label">Investor Remittance Status *</label>
							<div class="col-md-8">
					            <select id="investorRemittanceStatus" class="form-control selectpicker fundingSelector" data-size="auto" data-live-search="true" data-style="btn-white" name="investorRemittanceStatus" data-parsley-required="true">
					            	<option value="">- Investor Remittance -</option>
					                <c:forEach items="${investorRemittanceList}" var="remittanceStatus">
					                    <option class=" btn-group" value="${remittanceStatus}" <c:if test="${remittanceStatus.label eq model.investorRemittanceStatus.label}">selected='selected'</c:if>>${remittanceStatus.label}</option>
					                </c:forEach>
					            </select>
					        </div>
						</div>

						<div class="form-group <spring:bind path="investorRemittanceRemittedAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="investorRemittanceRemittedAmount" class="col-md-4 control-label">Amount on Remittance Slip *</label>
                            <div class="col-md-8">
                                <form:input id="investorRemittanceRemittedAmount" path="investorRemittanceRemittedAmount" cssClass="form-control" data-parsley-required="true"/>
                            </div>
                        </div>
                        
                        <div class="form-group <spring:bind path="investorRemittanceReceivedAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="investorRemittanceReceivedAmount" class="col-md-4 control-label">Amount in Online Banking</label>
                            <div class="col-md-8">
                                <form:input id="investorRemittanceReceivedAmount" path="investorRemittanceReceivedAmount" cssClass="form-control"/>
                            </div>
                        </div>
                        
                        <div class="form-group <spring:bind path="investorRemittanceFees"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="investorRemittanceFees" class="col-md-4 control-label">Investor Remittance Fees</label>
                            <div class="col-md-8">
                                <form:input id="investorRemittanceFees" path="investorRemittanceFees" cssClass="form-control"/>
                            </div>
                        </div>
                        
                        <div class="form-group">
							<label for="investorRemittance" class="col-md-4 control-label">Broker Funding Status</label>
							<div class="col-md-8">
					            <select id="brokerFundingStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="brokerFundingStatus">
					            	<option value="">- Broker Funding Status -</option>
					                <c:forEach items="${brokerFundingStatusList}" var="brokerFundingStatus">
					                    <option class=" btn-group" value="${brokerFundingStatus}" <c:if test="${brokerFundingStatus.label eq model.brokerFundingStatus.label}">selected='selected'</c:if>>${brokerFundingStatus.label}</option>
					                </c:forEach>
					            </select>
					        </div>
						</div>
                        
                         <div class="form-group <spring:bind path="brokerFundingReceivedAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="brokerFundingReceivedAmount" class="col-md-4 control-label">Broker Funding Received Amount</label>
                            <div class="col-md-8">
                                <form:input id="brokerFundingReceivedAmount" path="brokerFundingReceivedAmount" cssClass="form-control"/>
                            </div>
                        </div>
                        
                         <div class="form-group <spring:bind path="brokerFundingFees"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="brokerFundingFees" class="col-md-4 control-label">Broker Funding Fees</label>
                            <div class="col-md-8">
                                <form:input id="brokerFundingFees" path="brokerFundingFees" cssClass="form-control"/>
                            </div>
                        </div>
                         
                         <div class="form-group <spring:bind path="netInvestmentAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="netInvestmentAmount" class="col-md-4 control-label">Net Investment Amount</label>
                            <div class="col-md-8">
                                <form:input id="brokerFundingFees" path="netInvestmentAmount" cssClass="form-control"/>
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
							<label for="investorRemittanceIssueEmailSent" class="col-md-4 control-label">Investor Remittance Issues Email Sent</label>
							<div class="col-md-8">
								<form:input id="investorRemittanceIssueEmailSent" path="investorRemittanceIssueEmailSent"
									cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label for="reconciliationEmailSent" class="col-md-4 control-label">Reconciliation Email Sent</label>
							<div class="col-md-8">
								<form:input id="reconciliationEmailSent" path="reconciliationEmailSent"
									cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label for="reconciliationAwaitEmailSent" class="col-md-4 control-label">Reconciliation Await Email Sent</label>
							<div class="col-md-8">
								<form:input id="reconciliationAwaitEmailSent" path="reconciliationAwaitEmailSent"
									cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label for="brokerFundedEmailSent" class="col-md-4 control-label">Broker Funded Email Sent</label>
							<div class="col-md-8">
								<form:input id="brokerFundedEmailSent" path="brokerFundedEmailSent"
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
										<button type="submit" class="btn btn-primary <c:if test = "${model.investorRemittanceStatus eq fundingStatusFunded}"> hide</c:if>">Save</button>
										<c:if test="${not empty param['id']}">
		   	                            	<a class="btn btn-inverse deleteButton <c:if test = "${model.investorRemittanceStatus eq fundingStatusFunded}"> hide</c:if>"  data-id="${model.id}">
					                        	<i class="fa fa-trash" ></i>&nbsp;Delete
					                    	</a>
										</c:if>
										<a href="${appContextName}/admin/remittance/list" class="btn btn-primary cancelRemittanceBtn">
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

<form action="deletefile" name="deleteFileForm" method="post">
	 <%-- <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/> --%>
	<input type="hidden" name="id" />
</form>
<script type="text/javascript">
$(document).ready(function() {
	$('.selectpicker').selectpicker();
	setDescription();
});

 $('input[type=file]').change(function(){
	var filename = $(this).val().split('\\').pop();
	var filenameContainer = (typeof $(this).data("filename-container") === "undefined") ? $(this).parent().parent().parent().find("#filename") : $("#"+$(this).data("filename-container"));
	if(($('input[type=file]').attr('class').includes('userFile'))) {
		var fileExtension = ['jpeg','jpg','png','pdf'];
		if ($.inArray($(this).val().split('.').pop().toLowerCase(), fileExtension) == -1) {
			$(filenameContainer).html("Invalid File Type");
			$('#enabled-button').attr('disabled','disabled'); 
			$(filenameContainer).attr('class','btn btn-danger');
			$(this).val('');
		}
		else {
			$("#enabled-button").removeAttr('disabled');
			$(filenameContainer).html(filename);
			$(filenameContainer).attr('class','btn btn-success');
		}
	}
	else {
		$(filenameContainer).html(filename);
	}
	$(filenameContainer).removeAttr("style");
}); 
 
 
 $('.deleteRemittanceFile').on("click", function() {
		if (confirm('Confirm delete?')) {
	        var id = $(this).data('id');
			var deleteForm = document.deleteFileForm;
			if (!deleteForm.id) { //means its an array
				deleteForm = deleteForm[0];
			}
			deleteForm.id.value = id;
			deleteForm.submit();

	    }
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
	
 var remittanceCooke =  $.cookie("remittanceCookieUrl");
 $('.cancelRemittanceBtn').on("click", function() {
	// $(this).attr('href',remittanceCooke)
	//$.cookie("remittanceCookieUrl", '${requestPage}');
});	
</script>
<%@include file="../footer.jspf" %>
