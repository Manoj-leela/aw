<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@page import="java.util.*"%>
<%@page import="sg.activewealth.roboadvisor.common.model.User"%>
<%@include file="../header.jspf"%>

<!-- begin page-header -->
<div class="row">
	<!-- begin col-12 -->
    <div class="col-md-12">
    	<div class="panel panel-inverse">
    		<div class="panel-heading">
                <h3 class="panel-title">
                    Investment Questionnaire
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<form:form class="form-horizontal" name="mainForm"
							action="${appContextName}/admin/userPortfolio/${user.id}/answers"
							commandName="userPortfolioDto" method="post" role="form"
							data-parsley-validate="true">
							<%@include file="../alert.jspf"%>
						<fieldset>
						<c:forEach var="userPortfolioDto" items="${userPortfolioDto.questionAndAnswerList}" varStatus="status">
							<div class="row">
								<div class="col-sm-12 pull-left">
									<div class="form-group">
										<label for="Question" class="col-md-2 control-label">Question:</label>
										<div class="col-md-8">
											<form:input id="Question" path="questionAndAnswerList[${status.index}].question" cssClass="form-control" readonly="true"/>
										</div>
									</div>
									<div class="form-group">
										<label for="Answer${status.index}" class="col-md-2 control-label">Answer:</label>
										<div class="col-md-8">
											<c:choose>
												<c:when test="${userPortfolioDto.question eq 'How much is your annual income?'}">
													<div class="col-md-5" style="margin-left: -2%">
														<form:input id="Answer${status.index}" path="questionAndAnswerList[${status.index}].answer" cssClass="col-md-3 form-control" data-parsley-required="true"/>
													</div>	
													<label class="control-label">${user.currency}</label>
												</c:when>
												<c:otherwise>
													<label class="col-md-5 m-t-5">
														<form:radiobutton id="Answer${status.index}Yes" path="questionAndAnswerList[${status.index}].answer" value="Yes" data-parsley-required="true"/>&nbsp;Yes
														<span style="margin-left: 10%"></span>
														<form:radiobutton id="Answer${status.index}No" path="questionAndAnswerList[${status.index}].answer" value="No" data-parsley-required="true"/>&nbsp;<label for="Answer${status.index}No">No</label> 
														<span style="margin-left: 10%"></span>
														<form:radiobutton id="Answer${status.index}Not" path="questionAndAnswerList[${status.index}].answer" value="Not Applicable" data-parsley-required="true"/>&nbsp;<label for="Answer${status.index}Not">Not Applicable</label>
													</label>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</fieldset>
						<fieldset>
							<div class="row">
								<div class="col-sm-8">
									<div class="form-group">
											<label for="type" class="col-md-4 control-label"></label>
											<div class="col-md-8">
												<button type="submit" class="btn btn-primary">Save</button>
												<a href="${appContextName}/admin/user/list/user"  
														class="btn btn-primary">
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
<%@include file="../footer.jspf"%>