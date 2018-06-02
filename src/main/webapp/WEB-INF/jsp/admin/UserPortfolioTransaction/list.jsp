<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/userPortfolioTransaction/list" style="width:100%;">
	 <div class="row">
        <div class="col-md-2">
         	<input type="text" name="userPortfolio.user.firstName" class="form-control" placeholder="User Name" value="${param['userPortfolio.user.firstName']}"/>
   		</div>
   		<div class="col-md-2">
         	<input type="text" name="userPortfolio.portfolio.name" class="form-control" placeholder="Portfolio Name" value="${param['userPortfolio.portfolio.name']}"/>
   		</div>  
   		
   		 <div class="col-md-3">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="status" data-none-selected-text="- User Portfolio Transaction Status -" multiple>
                <c:forEach items="${statuses}" var="transactionStatus">
                    <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['status']}">
	                    <c:forEach items="${paramValues['status']}" var="status">
	                        <c:if test="${status == transactionStatus.label}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option value="${transactionStatus.label}" ${selected}>${transactionStatus.label}</option>
                </c:forEach>
            </select>
        </div>
   		 
      	<div class="row"></div>
        <div class="col-md-8 m-t-10" >
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
	 </div>
</form>

<!-- begin row -->
<div class="row">
    <!-- begin col-12 -->
    <div class="col-md-12">
        <div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    User Portfolio Transaction
                </h3>
            </div>
            <div class="panel">
                <div class="panel-body">
                    <div class="row">
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <table id="table" class="table table-hover table-bordered">
                                <thead>
                                    <tr>
                                    	<th>#</th>
                                        <th>Username</th>
                                        <th>Portfolio Name</th>
                                        <th>Portfolio Transaction Status</th>
                                        <th>Created On</th>
                                    </tr>
                                </thead>
                                <tbody class="_body">
                                    <c:forEach var="model" items="${list.results}"
                                        varStatus="status"> 
                                        <tr>
                                        	<td class="col-md-1">${serialCount+status.index +1}.</td>
                                            <td><a href="${appContextName}/admin/userPortfolio/update?id=${model.userPortfolio.id}" target="_blank">${model.userPortfolio.user.firstName}</a></td>
                                            <td><a href="${appContextName}/admin/portfolio/update?id=${model.userPortfolio.portfolio.id}" target="_blank">${model.userPortfolio.portfolio.name}</a></td>
                                            <td>${model.status.label}</td>
                                            <td>${model.createdOn.toLocalDate()} ${model.createdOn.toLocalTime()}</td>
                                        </tr> 
                                    </c:forEach> 
                                </tbody> 
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <misc:printPagination urlPageVar="requestPage"
                            pagingDtoPageVar="list" 
                            ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0" 
                            printMode="both">
                        </misc:printPagination> 
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
</script>
    <!-- end row -->
<%@include file="../footer.jspf"%>