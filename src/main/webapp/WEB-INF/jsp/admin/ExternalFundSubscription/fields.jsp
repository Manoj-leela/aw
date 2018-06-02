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
                    ${not empty model.id ? 'Update' : 'Create'} External Fund Subscription
                </h3>
            </div>

	<div class="panel">
		<div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/externalFundSubscription/${formPostUrl}"
			commandName="externalFundSubscription" method="post" role="form" data-parsley-validate="true">
			
			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
			
			<fieldset>
				<div class="row">
					<div class="col-sm-8 pull-left">
						<div class="form-group ">
							<label for="externalFund" class="col-md-4 control-label">Fund Name *</label>
							<div class="col-md-8">
								<div class="input-group">
						            <select id="fund" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="externalFund.id">
						            	<option value="">- Fund -</option>
						                <c:forEach items="${fundList}" var="fund">
						                    <option class=" btn-group" value="${fund.id}"<c:if test="${model.externalFund.id eq fund.id}">selected='selected'</c:if>>${fund.name}</option>
						                </c:forEach>
						            </select>
						            <a href="${appContextName}/admin/externalFund/update" class="btn input-group-addon updateBtn" target="_blank">
	                                   	<i class="fa fa-pencil"></i>
	                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group">
                            <label for="fundPrice" class="col-md-4 control-label">Fund Price *</label>
                            <div class="col-md-8">
                            	<div class="input-group">
	                                <select id="fundPrice" class="form-control selectpicker fundPrice" data-size="auto" data-live-search="true" data-style="btn-white" name="externalFundPrice.id">
	                                    <option value="">- Fund Price -</option>
	                                    <c:forEach items="${fundPriceList}" var="fundPrice">
	                                        <option class=" btn-group" value="${fundPrice.id}"<c:if test="${model.externalFundPrice.id eq fundPrice.id}">selected='selected'</c:if>>( ${fundPrice.priceDate} ) ${fundPrice.buyPrice}</option>
	                                    </c:forEach>
	                                </select>
	                                 <a href="${appContextName}/admin/externalFundPrice/update" class="btn input-group-addon updateBtn" target="_blank">
	                                   	<i class="fa fa-pencil"></i>
	                               	</a>
                            	</div>
                            </div>
                        </div>
						<div class="form-group <spring:bind path="totalSubscriptionAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="totalSubscriptionAmount" class="col-md-4 control-label">Total Subscription Amount</label>
                            <div class="col-md-8">
                                <form:input id="totalSubscriptionAmount" path="totalSubscriptionAmount" cssClass="form-control" pattern="${threeDecimalPattern}"/>
                            </div>
                        </div>
                        <div class="form-group <spring:bind path="netInvestAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="netInvestAmount" class="col-md-4 control-label">Net Invest Amount</label>
                            <div class="col-md-8">
                                <form:input id="netInvestAmount" path="netInvestAmount" cssClass="form-control" pattern="${threeDecimalPattern}"/>
                            </div>
                        </div>
                        <div class="form-group <spring:bind path="shares"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="shares" class="col-md-4 control-label">Shares</label>
                            <div class="col-md-8">
                                <form:input id="shares" path="shares" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group <spring:bind path="balanceShares"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="balanceShares" class="col-md-4 control-label">Balance Shares</label>
                            <div class="col-md-8">
                                <form:input id="balanceShares" path="balanceShares" cssClass="form-control"/>
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
		   	                            	<a class="btn btn-inverse deleteButton"  data-id="${model.id}">
					                        	<i class="fa fa-trash" ></i>&nbsp;Delete
					                    	</a>
										</c:if>
										<a href="${appContextName}/admin/externalFundSubscription/list" class="btn btn-primary">
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
	});
	$('#fund').on("change",function(){
		debugger
  		var url = "${appContextName}/admin/externalFundPrice/getfundprice?fundId="+ $(this).val();
  		$.ajax({
  			type:'GET',
   		url:url,
   		success:function(data){
				 var fundPrice = $("select.fundPrice");
  				fundPrice.empty();
  				$.each( data, function( index, fund ) {
  					fundPrice.append("<option value="+fund.id+">"+"( "+fund.priceDate +" ) "+ fund.buyPrice+"</option>");
				});
  				fundPrice.selectpicker("refresh");
  				console.log(data);
   		},
   		error: function(jqXHR, textStatus, errorThrown) {
               alert('Something Went Wrong');
           }
         });
	});
</script>
<%@include file="../footer.jspf" %>
