<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<c:if test="${not empty userSession}">
	<%
		response.sendRedirect(pageContext.findAttribute("appContextName").toString() + "/");
	%>
</c:if>
<%@include file="prelogin_header.jspf"%>

<!-- begin login -->
<div class="login login-v2" data-pageload-addclass="animated flipInX">
	<div class="login-header">
		<div class="brand">
			<span>
			 <img alt="FutureWealth" src="${assetsBase}/img/logo_active_wealth.PNG">
			</span> 
		</div>
		<div class="icon">
			<i class="fa fa-sign-in"></i>
		</div>
	</div>
	<div class="login-content">
		<form:form commandName="loginForm" method="post" role="form"
			data-target="_top">
			<%-- <input type="hidden" name="forwardUrl" value="${param.forwardUrl}"/> --%>
			<!-- noneed this because forward url is in url -->

			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="alert.jspf"%>
			<div class="form-group m-t-20 m-b-20"
				<spring:bind path="emailAddress"><c:if test="${status.error}">has-error</c:if></spring:bind>>
				<form:input type="text" path="emailAddress"
					class="form-control input-lg" placeholder="Email Address" />
			</div>
			<div
				class="form-group m-b-20 <spring:bind path="password"><c:if test="${status.error}">has-error</c:if></spring:bind>">
				<form:input type="password" path="password"
					class="form-control input-lg" placeholder="Password" />
			</div>
			<div class="login-buttons">
				<button type="submit" class="btn btn-primary btn-block btn-lg">Sign
					me in</button>
			</div>
			<div class="m-t-20">
				If you forgot your password, reset <a
					href="${appContextName}/admin/resetPassword">here</a>.
			</div>
		</form:form>
	</div>
</div>
<!-- end login -->

<%@include file="prelogin_footer.jspf"%>