<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<!-- begin row -->
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/agent/list" style="width:100%;">
	 <div class="row">
        <div class="col-md-3">
           	<input type="text" name=name class="form-control" placeholder="Name" value="${param['name']}"/>
      	</div>
      	<div class="col-md-3">
           	<input type="text" name=mobileNumber class="form-control" placeholder="Mobile Number" value="${param['mobileNumber']}"/>
      	</div>
      	<div class="col-md-3">
           	<input type="text" name=agentCode class="form-control" placeholder="Code" value="${param['agentCode']}"/>
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
			<a href="${appContextName}/admin/agent/create"
				class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New Agent</a>
		</div>
	 </div>
</form>
<div class="row">
	<!-- begin col-12 -->
    <div class="col-md-12">
    	<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Agent
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>#</th>
										<th>Name</th>
										<th>Mobile Number</th>
										<th>Code</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}"
										varStatus="status">
										<tr>
											<td class="col-md-1">${serialCount+status.index +1}.</td>
											<td class="col-md-3">${model.name}</td>
											<td class="col-md-3">${model.mobileNumber}</td>
											<td class="col-md-3">${model.agentCode}</td>
											<td class="action col-md-2">
												<a href="${appContextName}/admin/agent/update?id=${model.id}"  class="btn btn-primary">
					                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
					                            </a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<c:if test="${empty list.results}">
			                        <tr class="empty-row"><td colspan="5">No records found</td></tr>
			                    </c:if>
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
	<!-- end row -->
</div>

<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<%@include file="../footer.jspf"%>