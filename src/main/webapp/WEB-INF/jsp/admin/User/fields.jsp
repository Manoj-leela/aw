<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<c:set var="isFormsPage" value="true" />
<%@include file="../header_once.jspf"%>

<!-- begin row -->
&nbsp;
<!-- begin row -->
<div class="row">
	<!-- begin col-12 -->
	<div class="col-md-9 m-l-25">
		<div class="panel panel-inverse">
			<div class="panel-heading">
				<h3 class="panel-title">User Registration</h3>
			</div>

			<div class="panel">
				<div class="panel-body">
					<form:form class="form-horizontal" name="mainForm"
						action="${appContextName}/admin/user/${formPostUrl}"
						commandName="${modelName}" method="post" role="form"
						data-parsley-validate="true">

						<c:set var="alertMessages">
							<form:errors path="*" cssClass="_spring_errors" />
						</c:set>
						<%@include file="../alert.jspf"%>

						<fieldset>
							<div class="row">
								<div class="col-sm-8">
									<div class="form-group">
										<label for="firstname" class="col-md-4 control-label">First
											Name</label>
										<div class="col-md-8">
											<form:input id="firstname" path="firstName"
												cssClass="form-control" data-parsley-required="true" />
										</div>
									</div>
									<div class="form-group">
										<label for="lastname" class="col-md-4 control-label">Last
											Name</label>
										<div class="col-md-8">
											<form:input id="lastname" path="lastName"
												cssClass="form-control" data-parsley-required="true" />
										</div>
									</div>
									<div class="form-group">
										<label for="email" class="col-md-4 control-label">Email</label>
										<div class="col-md-8">
											<div class="input-group">
												<span class="input-group-addon">@</span>
												<form:input id="email" path="email"
													cssClass="form-control" data-parsley-required="true"
													data-parsley-type="email" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for="mobileNumber" class="col-md-4 control-label">Phone
											Number</label>
										<div class="col-md-8">
											<div class="input-group">
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-phone-alt"></span>
												</span>
												<form:input id="mobileNumber" path="mobileNumber" cssClass="form-control"
													data-parsley-required="true" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for="isAdmin" class="col-md-4 control-label">Admin
											User</label>
										<div class="col-md-8">
											<div class="input-group">
												<form:checkbox id="isAdmin" path="isAdmin" value="false" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for="password" class="col-md-4 control-label">Password</label>
										<div class="col-md-8">
											<input type="password" id="password" name="password"
												class="form-control" data-parsley-minlength="8"
												<c:if test="${model.id == null || model.needToRehashPassword == true}">data-parsley-required="true"</c:if> />
											<div id="passwordStrength" class="is0 m-t-5"></div>
										</div>
									</div>
									<div class="form-group">
										<label for="repassword" class="col-md-4 control-label">Retype
											Password</label>
										<div class="col-md-8">
											<input type="password" id="repassword" name="repassword"
												class="form-control" data-parsley-minlength="8"
												<c:if test="${model.id == null || model.needToRehashPassword == true}">data-parsley-required="true"</c:if> />
											<div id="rePasswordStrength" class="is0 m-t-5"></div>
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
											<button type="submit"
												class="btn btn-primary btn-block btn-lg">Save</button>
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

<link rel="stylesheet" type="text/css"
	href="${resourcesBase}/venders/password-indicator/css/password-indicator.css" />
<script
	src="${resourcesBase}/venders/password-indicator/js/password-indicator.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#dob').datepicker({
			format : 'yyyy-mm-dd',
			todayHighlight : true
		});
		$('#password').passwordStrength({
			targetDiv : '#passwordStrength'
		});
		$('#repassword').passwordStrength({
			targetDiv : '#rePasswordStrength'
		});
		$('.selectpicker').selectpicker();
	});
</script>

<%@include file="../prelogin_footer.jspf"%>
