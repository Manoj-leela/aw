<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf"%>
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Instrument
                </h3>
            </div>

			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-8 pull-left">
							<form:form 	class="form-horizontal" name="mainForm"
										action="${appContextName}/admin/instrument/${formPostUrl}"
										commandName="${modelName}" method="post" role="form" data-parsley-validate="true">

							<c:set var="alertMessages">
								<form:errors path="*" cssClass="_spring_errors" />
							</c:set>
							<%@include file="../alert.jspf"%>

							<fieldset>
								
								<div class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="name" class="col-md-4 control-label">Instrument Name*</label>
									<div class="col-md-8">
										<form:input id="name" path="name" cssClass="form-control"
											data-parsley-required="true" />
									</div>
								</div>
								<div class="form-group <spring:bind path="code"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                                    <label for="code" class="col-md-4 control-label">Instrument Code*</label>
                                    <div class="col-md-8">
                                        <form:input id="code" path="code" cssClass="form-control"/>
                                    </div>
                                </div>
								<div class="form-group <spring:bind path="instrumentType"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="type" class="col-md-4 control-label">Instrument type* </label>
									<div class="col-md-8">
										<form:select id="instrumentType" path="instrumentType"
											class="form-control selectpicker" data-live-search="true" data-parsley-required="true">
											<form:options path="instrumentType" items="${instrumentTypes}" itemLabel="label" itemValue="value"/>
										</form:select>
									</div>
								</div>
								<div class="form-group <spring:bind path="feesPerTradeLeg"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                                    <label for="feesPerTradeLeg" class="col-md-4 control-label">Fees (Percentage)</label>
                                    <div class="col-md-8">
                                       <form:input id="feesPerTradeLeg" path="feesPerTradeLeg" cssClass="form-control"
                                            data-style="btn-white"/>
                                    </div>
                                </div>
							</fieldset>
								<fieldset disabled>
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
								</fieldset>
								<fieldset>
									<div class="form-group">
										<label for="type" class="col-md-4 control-label"></label>
										<div class="col-md-8">
											<button type="submit" class="btn btn-primary<c:if test = "${isInstrumentInUse}"> hide</c:if>">Save</button>
											<c:if test="${not empty param['id']}">
			   	                            	<a class="btn btn-inverse deleteButton <c:if test = "${isInstrumentInUse}"> hide</c:if>"  data-id="${model.id}">
						                        	<i class="fa fa-trash" ></i>&nbsp;Delete
						                    	</a>
											</c:if>
											<a href="${appContextName}/admin/instrument/list"  
													class="btn btn-primary">
						                                <i class="fa -square-o"></i>Cancel
						                    </a>
										</div>
									</div>
								</fieldset>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	    <!-- end col-12 -->
	</div>
</div>
<!-- end row -->
<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<!-- page script -->
<script type="text/javascript">
    $(document).ready(function() {
        $('.selectpicker').selectpicker();
    });
</script>
<%@include file="../prelogin_footer.jspf"%>