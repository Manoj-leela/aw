<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<spring:eval expression="T(sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus).values()" var="executionStatuses" />

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
                    ${not empty model.id ? 'Update' : 'Create'} User Portfolio
                </h3>
            </div>

	<div class="panel">
		<div class="panel-body">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/userPortfolio/${formPostUrl}"
			commandName="userPortfolio" method="post" role="form" data-parsley-validate="true">
			
			<c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
			
			<fieldset>
				<div class="row">
					<div class="col-sm-8 pull-left">
						<div class="form-group">
							<label for="user" class="col-md-4 control-label">User *</label>
							<div class="col-md-8">
								<div class="input-group">
									<select id="user" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white"name="user" data-parsley-required="true">
					            	<option value="">- User -</option>
					                <c:forEach items="${userList}" var="user">
					                    <option value="${user.id}" <c:if test="${user.id eq model.user.id}">selected='selected'</c:if>>${user.firstName} - ${user.email}</option>
					                </c:forEach>
					            </select>
					            <a href="${appContextName}/admin/user/profile" class="btn input-group-addon updateBtn" target="_blank">
                                   	<i class="fa fa-pencil"></i>
                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group">
							<label for="portfolio" class="col-md-4 control-label">Portfolio *</label>
							<div class="col-md-8" >
								<div class="input-group">
									<select id="portfolio" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white"name="portfolio" data-parsley-required="true">
					            	<option value="">- Portfolio -</option>
					                <c:forEach items="${portfolioList}" var="portfolio">
					                    <option value="${portfolio.id}" <c:if test="${portfolio.id eq model.portfolio.id}">selected='selected'</c:if>>${portfolio.name}</option>
					                </c:forEach>
					            </select>
					            <a href="${appContextName}/admin/portfolio/update" class="btn input-group-addon updateBtn" target="_blank">
                                   	<i class="fa fa-pencil"></i>
                               	</a>
								</div>
					        </div>
						</div>
						<div class="form-group">
							<label for="goal" class="col-md-4 control-label">Goal</label>
							<div class="col-md-8" >
								<div class="input-group">
									<select id="goal" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white"name="goal" >
					            	<option value="">- Goal -</option>
					                <c:forEach items="${goalList}" var="goal">
					                    <option value="${goal.value}" <c:if test="${goal.label eq model.goal.label}">selected='selected'</c:if>>${goal.label}</option>
					                </c:forEach>
					            </select>
								</div>
					        </div>
						</div>
						<div class="form-group">
							<label for="fundStatus" class="col-md-4 control-label">Funding Status</label>
							<div class="col-md-8">
								<div class="input-group">
					            <select id="fundStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="portfolioFundingStatus" >
					            	<option value="">- Funding Status -</option>
					                <c:forEach items="${fundingStatusList}" var="fundingStatus">
					                    <option value="${fundingStatus}" <c:if test="${fundingStatus eq model.portfolioFundingStatus}">selected='selected'</c:if>>${fundingStatus.label}</option>
					                </c:forEach>
					            </select>
					            </div>
					        </div>
						</div>
						<div class="form-group">
							<label for="fundStatus" class="col-md-4 control-label">Execution Status</label>
							<div class="col-md-8">
								<div class="input-group">
					            <select id="fundStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="portfolioFundingStatus" >
					            	<option value="">- Execution Status -</option>
					                <c:forEach items="${executionStatuses}" var="executionStatus">
					                    <option value="${executionStatus}" <c:if test="${executionStatus eq model.executionStatus}">selected='selected'</c:if>>${executionStatus.label}</option>
					                </c:forEach>
					            </select>
					            </div>
					        </div>
						</div>
						<div class="form-group <spring:bind path="netInvestmentAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="netInvestmentAmount" class="col-md-4 control-label">Net Investment Amount</label>
                            <div class="col-md-8">
                                <form:input id="netInvestmentAmount" path="netInvestmentAmount" cssClass="form-control"/>
                            </div>
                       </div>
                       
                       <div class="form-group <spring:bind path="totalUninvestedAmount"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="totalUninvestedAmount" class="col-md-4 control-label">Total Uninvested Amount</label>
                            <div class="col-md-8">
                                <form:input id="totalUninvestedAmount" path="totalUninvestedAmount" cssClass="form-control"/>
                            </div>
                       </div>
                       
                       <div class="form-group <spring:bind path="realisedPnl"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="realisedPnl" class="col-md-4 control-label">Realised Pnl</label>
                            <div class="col-md-8">
                                <form:input id="realisedPnl" path="realisedPnl" cssClass="form-control"/>
                            </div>
                       </div>
                       
                       <div class="form-group <spring:bind path="unrealisedPnl"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="unrealisedPnl" class="col-md-4 control-label">Unrealised Pnl</label>
                            <div class="col-md-8">
                                <form:input id="unrealisedPnl" path="unrealisedPnl" cssClass="form-control"/>
                            </div>
                       </div>
                        <div class="form-group <spring:bind path="netAssetValue"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="netAssetValue" class="col-md-4 control-label">Net Asset Value</label>
                            <div class="col-md-8">
                                <form:input id="netAssetValue" path="netAssetValue" cssClass="form-control"/>
                            </div>
                       </div>
                       <div class="form-group <spring:bind path="returns"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            <label for="returns" class="col-md-4 control-label">Returns</label>
                            <div class="col-md-8">
                                <form:input id="returns" path="returns" cssClass="form-control"/>
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
								<a href="${appContextName}/admin/userPortfolio/list" class="btn btn-primary">
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
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
</script>
<%@include file="../footer.jspf" %>
