<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
&nbsp;

<form id="filterForm" class="list-filter-form m-b-20 pull-left"  style="width:100%;">
	<!-- Search starts-->
 	<div class="row">
        <div class="col-md-2"> 
            <select class="form-control selectpicker append" data-live-search="true" data-style="btn-white" name="instrument">
               <option value="">-Instrument-</option> 
                <c:forEach items="${instrumentCodes}" var="instrumentCode">
                	<option value="${instrumentCode}" <c:if test="${param['instrument'] eq instrumentCode}">selected='selected'</c:if>>${instrumentCode}</option>
             	</c:forEach>
            </select>
        </div>
      	<div class="col-md-3">
      	    <div id="rateTimeStampStartDate" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
                <span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input id="rateTimeStampStartDate" name="rateTimeStampStartDate" class="form-control" placeholder="Start Date" value="${param['rateTimeStampStartDate']}" />
            </div>
      	</div>
        <div class="col-md-3">
            <div id="rateTimeStampEndDate" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
                <span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input id="rateTimeStampEndDate" name="rateTimeStampEndDate" class="form-control" placeholder="End Date" value="${param['rateTimeStampEndDate']}" />
            </div>
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
						  <button type="reset" class="btn btn-primary">Reset</button>
						</div>
        			</td>
        		</tr>
        	</table>
        </div>
	 </div>
</form>
<!-- Search ends-->
<div class="row">

	<div class="col-md-12">
    	<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Rate History
                </h3>
            </div>
            <div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Timestamp</th>
					                    <th>Instrument</th>
					                    <th>High</th>
					                    <th>Low</th>
					                    <th>Close</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach items="${rateHistories.results}" var="rateHistory" varStatus="list">
					                    <tr>
					                        <td>${rateHistory.rateTimeStamp.toLocalDate()}</td>
					                        <td>${rateHistory.instrument}</td>
					                        <td><fmt:formatNumber minFractionDigits="3" value="${rateHistory.high}"/></td>
					                        <td><fmt:formatNumber minFractionDigits="3" value="${rateHistory.low}"/></td>
					                        <td><fmt:formatNumber minFractionDigits="3" value="${rateHistory.close}"/> </td>
					                    </tr>
					                </c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<misc:printPagination urlPageVar="requestPage"
							pagingDtoPageVar="rateHistories"
							ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0"
							printMode="both"></misc:printPagination>
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

<%@include file="../footer.jspf"%>