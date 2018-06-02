<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf" %>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/portfolio/list" style="width:100%;">
	 <div class="row">
      	<div class="col-md-2">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="userRiskProfile" data-none-selected-text="- Risk Profile-" multiple>
                <c:forEach items="${userRiskProfileList}" var="riskProfile">
                    <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['userRiskProfile']}">
	                    <c:forEach items="${paramValues['userRiskProfile']}" var="parRiskProfile">
	                        <c:if test="${parRiskProfile == riskProfile.value}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option value="${riskProfile.value}" ${selected}>${riskProfile.label}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
           	<input type="text" name=name class="form-control" placeholder="Name" value="${param['name']}"/>
      	</div>
   	</div>
  	<div class="row m-t-10">
        <div class="col-md-8 padding-top-10" >
        	<table>
        		<tr style="padding: 15px;">
        			<td>
                         <div class="btn-group">
                             <button class="btn btn-success" type="submit">
                                 <i class="fa fa-search"></i> Search
                             </button>
						</div>
        			</td>
        			<td>
        				<div class="btn-group">
						  <button type="reset" class="btn btn-primary">
						  	Reset
						  </button>
						</div>
        			</td>
        		</tr>
        	</table>
        </div>
       	<div class="pull-right m-r-10">
			<a href="${appContextName}/admin/portfolio/create"
				class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New Portfolio</a>
		</div>
	 </div>
</form>
<!-- begin row -->
<div class="row">
	<!-- begin col-12 -->
    <div class="col-md-12">
    	<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Portfolios
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-responsive table-bordered">
								<thead>
									<tr>
										<th>#</th>
										<th>Risk Profile</th>
										<th>Name</th>
										<th>Assignment Category</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
										<c:forEach var="model" items="${list.results}"
										varStatus="status">
										<tr>
											<td class="col-md-1">${serialCount+status.index +1}.</td>
											<td class="col-md-1">${model.riskProfile.key}</td>
											<td class="col-md-3" style="word-wrap: break-word;min-width: 160px;max-width: 160px;">${model.name}</td>
											<td class="col-md-3" style="word-wrap: break-word;min-width: 160px;max-width: 160px;">${model.assignmentCategory.label}</td>
											<td class="action col-md-4" style="word-wrap: break-word;min-width: 175px;max-width: 175px;">
												<a href="${appContextName}/admin/userPortfolio/assign?newPortfolioId=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Assignment
					                            </a>
				                            	<a href="${appContextName}/admin/portfolio/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
				                            </td>
										</tr>
									</c:forEach>
									<c:if test="${empty list.results}">
				                        <tr class="empty-row"><td colspan="5">No records found</td></tr>
				                    </c:if>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<misc:printPagination urlPageVar="requestPage"
						pagingDtoPageVar="list"
						ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0"
						printMode="both"></misc:printPagination>
					</div>
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
<%@include file="../footer.jspf"%>