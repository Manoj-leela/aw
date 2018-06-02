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
                    ${not empty model.id ? 'Update' : 'Create'} External Fund Price
                </h3>
            </div>

	<div class="panel">
		<div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/externalFundPrice/${formPostUrl}"
			commandName="externalFundPrice" method="post" role="form" data-parsley-validate="true">
			
			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
			
			<fieldset>
				<div class="row">
					<div class="col-sm-8 pull-left">
						<div class="form-group">
							<label for="externalFund" class="col-md-4 control-label">Fund Name *</label>
							<div class="col-md-8">
								<div class="input-group">
						            <select id="externalFund" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="externalFund.id">
						            	<option value="">- External Fund -</option>
						                <c:forEach items="${externalFundList}" var="externalFund">
						                    <option class=" btn-group" value="${externalFund.id}"<c:if test="${model.externalFund.id eq externalFund.id}">selected='selected'</c:if>>${externalFund.name}</option>
						                </c:forEach>
						            </select>
						            <a href="${appContextName}/admin/externalFund/update" class="btn input-group-addon updateBtn" target="_blank">
	                                   	<i class="fa fa-pencil"></i>
	                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group <spring:bind path="buyPrice"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="buyPrice" class="col-md-4 control-label">Buy Price *</label>
                            <div class="col-md-8">
                                <form:input id="buyPrice" path="buyPrice" cssClass="form-control" data-parsley-required="true" pattern="${threeDecimalPattern}"/>
                            </div>
                        </div>
                        <div class="form-group <spring:bind path="sellPrice"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="sellPrice" class="col-md-4 control-label">Sell Price</label>
                            <div class="col-md-8">
                                <form:input id="sellPrice" path="sellPrice" cssClass="form-control" pattern="${threeDecimalPattern}"/>
                            </div>
                        </div>
                        <div class="form-group">
                             <label class="col-md-4 control-label">Dealing</label>
                             <div class="col-md-8">
                             <c:choose>
                                <c:when test="${model.id == null}">
                                    <div class="col-md-12">
                                        <label class="radio-inline"><input type="radio" id="dealingYes" name="dealingPrice" value="true" /> Yes </label> 
                                        <label class="radio-inline"><input type="radio" id="dealingNo" name="dealingPrice" value="false" checked/> No </label> 
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-12">
                                        <c:choose>
                                            <c:when test="${model.dealingPrice == true}">
                                                <label class="radio-inline"><input type="radio" id="dealingYes" name="dealingPrice" value="true" checked/> Yes </label> 
                                                <label class="radio-inline"><input type="radio" id="dealingNo" name="dealingPrice" value="false"/> No </label>
                                            </c:when>
                                            <c:otherwise>
                                                <label class="radio-inline"><input type="radio" id="dealingYes" name="dealingPrice" value="true" /> Yes </label> 
                                                <label class="radio-inline"><input type="radio" id="dealingNo" name="dealingPrice" value="false" checked/> No </label>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                             </c:choose>
                             </div>
                        </div>
                        <div class="form-group">
	                          <label for="priceDate" class="col-md-4 control-label">Date</label>
	                          <div class="col-md-8">
	                              <div id="priceDate" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
	                                  <span class="btn input-group-addon">
	                                      <i class="fa fa-calendar"></i>
	                                  </span>
	                                  <form:input id="priceDate" path="priceDate" cssClass="form-control" data-parsley-required="true" placeholder="2018-13-02"/>
	                              </div>
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
										<button type="submit" class="btn btn-primary <c:if test = "${isFundPriceUsed}"> hide</c:if>">Save</button>
										<c:if test="${not empty param['id']}">
		   	                            	<a class="btn btn-inverse deleteButton <c:if test = "${isFundPriceUsed}"> hide</c:if>"  data-id="${model.id}">
					                        	<i class="fa fa-trash" ></i>&nbsp;Delete
					                    	</a>
										</c:if>
										<a href="${appContextName}/admin/externalFundPrice/list" class="btn btn-primary">
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
		$('#priceDate').datepicker({
            format: 'yyyy-mm-dd',
            todayHighlight: true,
            calendarWeeks: true
        });
	});
</script>
<%@include file="../footer.jspf" %>
