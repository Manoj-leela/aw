<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf" %>
<link rel="stylesheet" href="${assetsBase}/plugins/jackocnr-intl-tel-input/build/css/intlTelInput.css">
<script src="${assetsBase}/plugins/jackocnr-intl-tel-input/build/js/intlTelInput.min.js"></script>
<!-- begin row -->
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Agent Profile
                </h3>
            </div>
            
            <div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/agent/${formPostUrl}"
			commandName="${modelName}" method="post" role="form" data-parsley-validate="true">
			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
			
			<fieldset>
				<div class="row">
					<div class="col-sm-8 pull-left">
						<div class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="name" class="col-md-4 control-label">Name *</label>
							<div class="col-md-8">
								<form:input id="name" path="name" cssClass="form-control" data-parsley-required="true"/>
							</div>
						</div>
						<div class="form-group <spring:bind path="mobileNumber"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="mobileNumber" class="col-md-4 control-label">Phone Number *</label>
							<div class="col-md-8">
								<div class="input-group">
									<form:input type="text" id="mobileNumber" path="mobileNumber" cssClass="form-control" data-parsley-required="true"/>
								</div>
							</div>
						</div>
                        <div class="form-group <spring:bind path="agentCode"><c:if test="${status.error}">has-error</c:if></spring:bind>">
							<label for="agentCode" class="col-md-4 control-label">Agent Code *</label>
							<div class="col-md-8">
								<form:input id="agentCode" path="agentCode" cssClass="form-control" data-parsley-required="true"/>
							</div>
						</div>
					</div>
				</div>
			</fieldset>
			<fieldset disabled>
				<div class="row">
					<div class="col-sm-8 pull-left">
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
		   	                            	<a class="btn btn-inverse deleteButton <c:if test = "${isAgentInUse}"> hide</c:if>"  data-id="${model.id}">
					                        	<i class="fa fa-trash" ></i>&nbsp;Delete
					                    	</a>
										</c:if>
										<a href="${appContextName}/admin/agent/list" class="btn btn-primary">
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
<!-- end row -->
<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<script type="text/javascript">
	$("#mobileNumber").intlTelInput({
		   //autoFormat: false,
		   //autoHideDialCode: false,
		   defaultCountry: "sg",
		   //nationalMode: true,
		   numberType: "MOBILE",
		   onlyCountries: ['sg', 'my', 'cn', 'jp'],
		   preferredCountries: ['sg'],
		   //responsiveDropdown: true,
		   utilsScript: "${assetsBase}/plugins/jackocnr-intl-tel-input/lib/libphonenumber/build/utils.js"
		 });
</script>

<%@include file="../footer.jspf" %>
