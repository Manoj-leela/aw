<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/instrument/list" style="width:100%;">
	 <div class="row">
      	<div class="col-md-2">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="type" data-none-selected-text="- Type-" multiple>
                <c:forEach items="${instrumentTypeList}" var="instrumentType">
                    <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['type']}">
	                    <c:forEach items="${paramValues['type']}" var="parType">
	                        <c:if test="${parType == instrumentType.value}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option value="${instrumentType.value}" ${selected}>${instrumentType.label}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
           	<input type="text" name=name class="form-control" placeholder="Name" value="${param['name']}"/>
      	</div>
      	<div class="col-md-3">
           	<input type="text" name=code class="form-control" placeholder="Instrument Code" value="${param['code']}"/>
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
			<a href="${appContextName}/admin/instrument/create"
				class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;New Instrument</a>
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
                    Instruments
                </h3>
            </div>
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12">
						<table id="table" class="table table-hover table-bordered">
						<thead>
							<tr>
								<th>#</th>
								<th>Name</th>
								<th>Instrument Code</th>
								<th>Type</th>
								<th>Fees (%)</th>
								<th class="_cell_15">Actions</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty list.results}">
								<c:forEach var="model" items="${list.results}" varStatus="status">
									<tr>
										<td>${serialCount+status.index +1}.</td>
										<td>${model.name}</td>
										<td>${model.code}</td>
										<td>${model.instrumentType.label}</td>
										<td>${model.feesPerTradeLeg}</td>
										<td>
			                            	<a href="${appContextName}/admin/instrument/update?id=${model.id}"  class="btn btn-primary">
				                                <i class="fa fa-pencil-square-o"></i>&nbsp;Edit
				                            </a>
			                           	</td>
									</tr>
								</c:forEach>
							</c:if>
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
    <!-- end col-12 -->
</div>
<!-- end row -->
<script type="text/javascript">
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});
</script>
<%@include file="../footer.jspf"%>