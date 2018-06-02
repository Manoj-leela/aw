<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.RedemptionStatus).values()" var="redemptionStatuses" />

<%@include file="../header.jspf"%>
<script src="${resourcesBase}/scripts/batch.js"></script>
&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/redemption/customlist" style="width:100%;">
	  <div class="row">
	 	<div class="col-md-3">
            <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="- Email -" name="userPortfolio.id" multiple>
                <c:forEach items="${userPortfolios}" var="userPortfolio">
	                <c:set value="" var="selected" />
                    <c:if test="${not empty paramValues['userPortfolio.id']}">
	                    <c:forEach items="${paramValues['userPortfolio.id']}" var="paramValue">
	                        <c:if test="${paramValue eq userPortfolio.id}">
								<c:set value="selected" var="selected" />
							</c:if>
	                    </c:forEach>
                    </c:if>
                    <option class=" btn-group" value="${userPortfolio.id}" ${selected}>${userPortfolio.user.email}</option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${param['redemptionStatus'] ne 'Issues'}">
        <div class="col-md-3">
            <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="redemptionStatus">
                <!-- <option value="">- Redemption Status -</option> -->
                <c:forEach items="${redemptionStatusList}" var="status">
                	<option class=" btn-group" value="${status.value}" <c:if test="${param['redemptionStatus'] eq status.value}">selected='selected'</c:if>>${status.label}</option>
             	</c:forEach>
            </select>
        </div>
        </c:if>
        <div class="col-md-2">
            <div class="input-group date" data-provide="datepicker"
                 data-date-format="yyyy-mm-dd">
				<span class="btn input-group-addon">
                    <i class="fa fa-calendar"></i>
                </span>
                <input value="${param['redemptionDate']}" type="text" name="redemptionDate" class="form-control" placeholder="Redemption Date" />
            </div>
        </div>
         <c:if test="${param['redemptionStatus'] eq 'Issues'}">
         	<input type="hidden" name="redemptionStatus" value="${param['redemptionStatus']}"  class="form-control redemptionStatusIssueStatus" />
         </c:if>
      	<div class="row"></div>
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
	 </div>
</form>
<c:if test="${list.size() > 0}">
	<div class="m-b-10">
		<c:if test="${param['redemptionStatus'] eq 'ReceivedFromBroker'}">
		     <div class="pull-right m-r-10">
		         <select class="selectpicker redemptionBatchPicker" id="redemptionBatchPicker" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="--Redemption Batch--">
		             <option value="">- Select Batch -</option>
		              <c:forEach items="${redemptionBatches}" var="redemptionBatch">
		                  <option class="btn-group" value="${redemptionBatch}">${redemptionBatch}</option>
		             </c:forEach>
		         </select>
		     </div> 
	     </c:if>
		 <div class="pull-right m-r-10 m-b-10">
		 	<button class="btn btn-primary" id="saveSelected" disabled="disabled">Save Selected</button>
		 </div>
	</div>
</c:if>
<!-- begin row -->

<div class="row">
	<!-- begin col-12 -->
    <div class="col-md-12">
    	<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <c:choose>
                        <c:when test="${param['redemptionStatus'] eq 'RequestedByInvestor'}">
                            Redemption Request
                        </c:when>
                        <c:when test="${param['redemptionStatus'] eq 'SentToBroker' or param['redemptionStatus'] eq 'ReceivedFromBroker'}">
                            Broker Redemption
                        </c:when>
                        <c:when test="${param['redemptionStatus'] eq 'Completed'}">
                            Broker Redemption
                        </c:when>
                        <c:when test="${param['redemptionStatus'] eq 'Issues'}">
                            Having Issues
                        </c:when>
                        <c:otherwise>
                            Redemption
                        </c:otherwise>
                    </c:choose>
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<c:choose>
								<c:when test="${list.size() > 0}">
									<table id="mainTable"  class="table table-bordered">
										<thead>
											<tr>
												<th><input type="checkbox" class="allChecked" id="allChecked"></th>
												<th>&nbsp;</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody class="_body">
											<c:forEach var="model" items="${list}" varStatus="status">
											<c:set var="netRedemptionAmount" value="${model.amountReceivedFromBroker - model.amountReceivedFees}"></c:set>
												<tr>
													<td style="width:5px; padding-top: 2%"> 
													<input type="checkbox" class="redemption_batch" name="modelId" value="${model.id}"></td>
													<td style="padding: 0;">
													     <table id="redemptionTable" class="table table-bordered" style="margin: 0;">
		                                                    <tbody>
		                                                        <tr>
		                                                            <td colspan="1"> <strong>Date </strong> </td>
		                                                            <td colspan="4">${model.redemptionDate}</td>
		                                                        </tr>
		                                                        <tr>
		                                                            <td colspan="1"><strong> Email </strong></td>
		                                                            <td colspan="4"><a href="${appContextName}/admin/user/profile?id=${model.userPortfolio.user.id}" target="_blank">${model.userPortfolio.user.email}</a></td>
		                                                        </tr>
		                                                        <tr class="redemptionRow">
		                                                           <td colspan="5">
		                                                           		<table class="table table-bordered" style="margin: 0;">
		                                                           			<thead>	
		                                                           				<tr>
																					<th>Redemption Status</th>
																					<th>Batch</th>
																					<th>Amount Requested From Broker</th>
																					<th>Amount Received From Broker</th>
																					<th>Amount Received Fees</th>
																					<th>Net Redemption Amount</th>
																				</tr>
		                                                           			</thead>
		                                                           			<tbody>
		                                                           				<tr>
		                                                           					<td>
		                                                           						<select id="redemptionStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="redemptionStatus">
										                                                    <c:forEach items="${redemptionStatuses}" var="status">
										                                                        <option class=" btn-group" value="${status}"  <c:if test="${status.label eq model.redemptionStatus.label}">selected='selected'</c:if>>${status.label}</option>
										                                                    </c:forEach>
						                                                				</select>
		                                                           					</td>
		                                                           					<td>
		                                                           						<input value="${model.brokerBatch}" style="width: 100%" type="text" name="brokerBatch" class="form-control brokerBatch" placeholder="yyyy-mm-dd"  data-provide="datepicker" data-date-format="yyyy-mm-dd"/>
		                                                           					</td>
		                                                           					<td>
		                                                           						<input name="amountRequestedFromBroker" class="form-control amountRequestedFromBroker" type="number" value="${model.amountRequestedFromBroker}">
		                                                           					</td>
		                                                           					<td class="calculateAmountReceivedClass">
		                                                           						<input name="amountReceivedFromBroker" class="form-control amountReceivedFromBroker" type="number" value="${model.amountReceivedFromBroker}">
		                                                           					</td>
		                                                           					<td class="calculateAmountReceivedClass1">
		                                                           						<input name="amountReceivedFees" Class="form-control amountReceivedFees"  style="width: 100%" type="number" value="${model.amountReceivedFees}">
		                                                           					</td>
		                                                           					<td>
		                                                           						<input name="netRedemptionAmount" class="form-control netRedemptionAmount" type="number" value="${model.netRedemptionAmount}">
		                                                           					</td>
		                                                           				</tr>
		                                                           				<tr>
																	    			<td colspan="1"><strong> Remarks </strong></td>
																	    			<td colspan="5">
																						<textarea name="remarks" Class="form-control"  style="width: 100%" placeholder="Remarks">${model.remarks}</textarea>
															            			</td>
													    						</tr>
		                                                           			</tbody>
		                                                           		</table>
						                                            </td>
		                                                        </tr>
		                                                    </tbody>
		                                                 </table>       
													</td>
													<td class="action col-md-1">
													     <input name="modelId" type="hidden" value="${model.id}">
														 <button class="btn btn-primary SaveButton">Save</button>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:when>
								<c:otherwise>
									<span>No records found</span>		
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- end row -->
</div>
<script type="text/javascript">
	$(document).ready(function() {
		
		$('.selectpicker').selectpicker();
	        
		$('.redemption_batch').change(function() {
			checkboxCount = $('.redemption_batch:checked').length; 
			if(this.checked) {
				$("#saveSelected").removeClass('disabled');
				$("#saveSelected").removeAttr("disabled");
			}
            if(checkboxCount <= 0){
                $("#saveSelected").attr("disabled", true);
            }
            if(false == $(this).prop("checked")){ 
        		$("#allChecked").prop('checked', false); 
        	}

        	if ($('.redemption_batch:checked').length == $('.redemption_batch').length ){
        		$("#allChecked").prop('checked', true);
        	} 
		});
		
        $("#saveSelected").on("click", function() {
           var saveButtons = [];
           $('.redemption_batch:checked').each(function(index) {
        	   var button = $(this).closest('tr').find('button.SaveButton');
        	   saveButtons.push(button.get(0));
              });
           saveButtons.forEach(function (obj) {
               var fields = $(obj).closest('tr').find('input, select');
               fields.each(function(index){
                   $(this).attr('disabled', true);
               });
           });
           if(saveButtons.length===0){
        	    alert('Please select Redemption');
        	    return false;
              } else {
           	   timerclick(saveButtons,'${appContextName}/admin/redemption/api/update/');
              }
        });
		
		// Check all the check boxes
		$("#allChecked").change(function(){
		    $(".redemption_batch").prop('checked', $(this).prop("checked"));
		    if(this.checked) {
		    	$("#saveSelected").removeClass('disabled');
                $("#saveSelected").removeAttr("disabled");
		    }else{
		    	$("#saveSelected").addClass('disabled');
                $("#saveSelected").attr("disabled");
		    }
		}); 
		
		//calculate fee on received amount and fee amount when they changed.
        $(document).on('change', '.calculateAmountReceivedClass,.calculateAmountReceivedClass1', function() {
        	var amountRequestedFromBroker = $(this).closest('tr').find('.amountRequestedFromBroker').val();
        	var amountReceivedFromBroker = $(this).closest('tr').find('.amountReceivedFromBroker').val();
            var amountReceivedFees = $(this).closest('tr').find('.amountReceivedFees').val();
            
            var fee;
            if($(this).attr("class")=='calculateAmountReceivedClass'){
            	 if(amountReceivedFromBroker==''){
                 	$(this).closest('tr').find('.netRedemptionAmount').val('');
                 	$(this).closest('tr').find('.amountReceivedFees').val('');
                 	return;
                 }
            	fee = amountRequestedFromBroker - amountReceivedFromBroker;
			}else{
				if(amountReceivedFees==''){
					$(this).closest('tr').find('.amountReceivedFromBroker').val('');
					$(this).closest('tr').find('.netRedemptionAmount').val('');
	            	return;
	            }
				fee = amountRequestedFromBroker - amountReceivedFees;
			}
            
            if(fee<0){
                alert("Invalid Amount.");
                if($(this).attr("class")=='calculateAmountReceivedClass'){
                	$(this).closest('tr').find('.amountReceivedFromBroker').val('');
                	$(this).closest('tr').find('.netRedemptionAmount').val('');
                }else{
                	$(this).closest('tr').find('.amountReceivedFees').val('');
                }
                return;
            }
            if($(this).attr("class")=='calculateAmountReceivedClass'){
            	$(this).closest('tr').find('.amountReceivedFees').val(fee.toFixed(5));
                $(this).closest('tr').find('.netRedemptionAmount').val(amountReceivedFromBroker);
    		}else{
    			$(this).closest('tr').find('.amountReceivedFromBroker').val(fee.toFixed(5));
    			$(this).closest('tr').find('.netRedemptionAmount').val(fee.toFixed(5));
    		}
        });
		
        $('.redemptionBatchPicker').change(function() {
        	debugger;
            if($(this).val()) {
                var distributeFees = prompt("Please enter total fee in number (without $ or comma)");
                if(distributeFees && Number(distributeFees)) {
                var selectedBatchDate = $(this).val();
                var rows = $('#redemptionTable > tbody > tr.redemptionRow');
                var totalAmt = 0;
                var totalRecords = 0;
               
                rows.each(function() {
                    var batchDate = $(this).find('input.brokerBatch').val();
                    if(batchDate == selectedBatchDate) {
                        var brokerAmt = $(this).find('.amountRequestedFromBroker').val();
                        totalAmt += parseFloat(brokerAmt);
                        totalRecords++;
                    }
                });
                var calculationRows = 0;
                var remainingFees = distributeFees;
                rows.each(function() {
                    var batchDate = $(this).find('input.brokerBatch').val();
                    if(batchDate == selectedBatchDate) {
                        calculationRows++;
                        brokerAmt = parseFloat($(this).find('.amountRequestedFromBroker').val());
                        if(brokerAmt!=0) { 
	                        var amountReceivedFees = parseFloat((Math.floor(((brokerAmt / totalAmt) * distributeFees) * 100) / 100).toFixed(2));
	                        console.log("fees is:"+amountReceivedFees);
	                        remainingFees = parseFloat(remainingFees - amountReceivedFees);
	                        //console.log("remainingFees is:"+remainingFees);
	                        //console.log("calculationRows is:"+calculationRows);
	                        if((totalRecords == calculationRows) && (remainingFees > 0)) {
	                            amountReceivedFees = amountReceivedFees + remainingFees;
	                            //console.log("final fees is:"+amountReceivedFees);
	                        }
	                        $(this).find('input.amountReceivedFees').val(amountReceivedFees);
	                        var netRedemptionAmount = parseFloat(brokerAmt - amountReceivedFees).toFixed(2);
	                        $(this).find('.netRedemptionAmount').val(netRedemptionAmount);
	                        $(this).find('.amountReceivedFromBroker').val(netRedemptionAmount);
                        }
                    }
                });
                }
            }
        });
		
		// click save button will save row data.
		clickSaveButton('${appContextName}/admin/redemption/api/update/');
		
		$('.redemptionCookie').on("click", function() {
			$.cookie("redemptionCookieUrl", '${requestPage}');
		});	
		
		$('#filterForm :reset').on('click', function(evt) {
		    <c:if test="${param['redemptionStatus'] eq 'Issues'}">
	 				$form.find('.redemptionStatusIssueStatus').val("Issues");
 			</c:if>
		});
		
		
	});
</script>
<%@include file="../footer.jspf"%>