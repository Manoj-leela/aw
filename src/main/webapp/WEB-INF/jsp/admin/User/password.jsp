<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<c:set var="isFormsPage" value="true"/>
<%@include file="../header.jspf" %>

<link href="${assetsBase}/plugins/password-indicator/css/password-indicator.css" rel="stylesheet" />

<form:form name="mainForm" action="${appContextName}/user/password" commandName="${modelName}" method="post" role="form" data-parsley-validate="true">
	<!-- need this because we lay down the url explicitly -->
	<input type="hidden" name="forwardUrl" value="${param.forwardUrl}"/>

			<!-- begin page-header -->
			<h1 class="page-header">Set Password</h1>
			<!-- end page-header -->
			
			<c:set var="alertMessages"><form:errors path="*" cssClass="_spring_errors" /></c:set>
			<%@include file="../alert.jspf" %>
			
			<!-- begin row -->
			<div class="row">
                <!-- begin col-12 -->
			    <div class="col-md-12">
			    	<!-- begin panel -->
					<div class="panel panel-inverse">
						<div class="panel-heading">
							<h3 class="panel-title">Basic Information</h3>
						</div>
						<div class="panel-body panel-form">
                            <div class="form-horizontal form-bordered">
								<div class="form-group <spring:bind path="newPassword"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="newPassword" class="${classControlLabel} control-label">Password</label>
									<div class="${classFormControlDiv}">
										<form:input type="password" path="newPassword" class="form-control" placeholder="at least 8 characters" data-parsley-minlength="8"/>
										<div id="passwordStrength" class="is0 m-t-5"></div>
									</div>
								</div>	
								<div class="form-group <spring:bind path="rePassword"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="rePassword" class="${classControlLabel} control-label">Password (again)</label>
									<div class="${classFormControlDiv}">
										<form:input type="password" path="rePassword" class="form-control" placeholder="at least 8 characters" data-parsley-minlength="8"/>
										<div id="rePasswordStrength" class="is0 m-t-5"></div>
									</div>
								</div>
								
								<div class="form-group">
									<div class="${classSubmitFormControlDiv}">
										<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span></button>
									</div>
								</div>
							</div>
						</div>
					</div>
			    	<!-- end panel -->
                </div>
                <!-- end col-12 -->
            </div>
            <!-- end row -->

</form:form>
	
<script src="${assetsBase}/plugins/password-indicator/js/password-indicator.js"></script>
<script>
$(document).ready(function() {
	$(document.mainForm.elements['newPassword']).passwordStrength({targetDiv: '#passwordStrength'});
	$(document.mainForm.elements['rePassword']).passwordStrength({targetDiv: '#rePasswordStrength'});
});
</script>	
	
<%@include file="../footer.jspf" %>