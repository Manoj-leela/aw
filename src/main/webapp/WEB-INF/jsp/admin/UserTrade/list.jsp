<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/userTrade/list" style="width:100%;">
	 <div class="row">
	 	
        <div class="col-md-2">
         	<input type="text" name="user.firstName" class="form-control" placeholder="User Name" value="${param['user.firstName']}"/>
   		</div>
   		
   		<div class="col-md-3">
           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="executionStatus" data-none-selected-text="- Trade Status -" multiple="multiple">
                <c:forEach items="${executionStatuses}" var="tradeStatus">
                    <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['executionStatus']}">
	                    <c:forEach items="${paramValues['executionStatus']}" var="executionStatus">
	                        <c:if test="${executionStatus eq tradeStatus.label}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option value="${tradeStatus.label}" ${selected}>${tradeStatus.label}</option>
                </c:forEach>
            </select>
        </div>
   		<div class="col-md-3">
            <div class="input-group date" data-provide="datepicker"
                 data-date-format="yyyy-mm-dd">
				<span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input value="${param['createdOn']}" type="text" name="createdOn" class="form-control" placeholder="Created On" />
            </div>
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
                    Trades
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
										<th>User</th>
										<th>Position</th>
										<th>Instrument</th>
										<th>Status</th>
										<th>Units</th>
										<th>Entered Price</th>
										<th>Closed Price</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="model" items="${list.results}"
										varStatus="status"> 
										<tr>
											<td class="col-md-1">${serialCount+status.index +1}.</td>
											<td><a href="${appContextName}/admin/userPortfolio/update?id=${model.userPortfolio.id}" target="_blank">${model.userPortfolio.user.firstName}</a></td>
											<td>${model.tradePosition.label}</td>
											<td>
											<a href="${appContextName}/admin/instrument/update?id=${model.portfolioInstrument.instrument.id}" target="_blank">${model.portfolioInstrument.instrument.name}</a>
											</td>
											<td>${model.executionStatus.label}</td>
											<td><fmt:formatNumber minFractionDigits="3" value="${model.enteredUnits}" /></td>
 											<td><fmt:formatNumber minFractionDigits="3" value="${model.enteredPrice}" /></td>
 											<td><fmt:formatNumber minFractionDigits="3" value="${model.closedPrice}" /></td>
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