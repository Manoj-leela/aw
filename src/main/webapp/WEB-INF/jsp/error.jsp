<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="admin/header_once.jspf" %>
<!-- begin page-header -->

<c:if test="${not empty msg}" >
    <h2>${msg}</h2>
</c:if>

<c:if test="${not empty requestScope.exceptionObject}">
<pre>
<%
Exception exception = (Exception) request.getAttribute("exceptionObject");
exception.printStackTrace(new java.io.PrintWriter(out));
%>
</pre>
</c:if>

<%@include file="admin/footer.jspf" %>