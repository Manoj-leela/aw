<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="prelogin_header.jspf" %>

<!-- begin login -->
<div class="login login-v2" data-pageload-addclass="animated flipInX">
	<div class="login-content">
	    <form:form commandName="resetPasswordForm" method="post" role="form" data-target="_top" data-parsley-validate="true">
	
	        <c:set var="alertMessages"><form:errors path="*" cssClass="_spring_errors" /></c:set>
	        <%@include file="alert.jspf" %>
	        <c:if test="${userOperationContext.messageFromBundle eq null or userOperationContext.resultType != 'Success'}">
		        <div class="form-group m-b-20 <spring:bind path="email"><c:if test="${status.error}">has-error</c:if></spring:bind>">
		            <form:input type="email" path="email" class="form-control input-lg" placeholder="Please insert your email" data-parsley-required="true"/>
		        </div>
		
		        <div class="login-buttons">
		            <button type="submit" class="btn btn-primary btn-block btn-lg"> Submit</button>
		        </div>
	        </c:if>
	    </form:form>
	</div>
</div>
<!-- end login -->

<%@include file="prelogin_footer.jspf" %>