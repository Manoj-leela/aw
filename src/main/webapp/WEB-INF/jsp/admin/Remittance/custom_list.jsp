<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).values()" var="brokerStatusList" />

<spring:eval expression="{T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).Unprocessed,T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).Processed,T(sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus).Completed}" var="brokerStatuses" />

<spring:eval expression="T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).values()" var="investorStatusList" />
<spring:eval expression="{T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).Submitted,T(sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus).Received}" var="investorStatuses" />

<%@include file="../header.jspf"%>
<script src="${resourcesBase}/scripts/batch.js"></script>


&nbsp;
<form id="filterForm" class="list-filter-form m-b-20 pull-left" action="${appContextName}/admin/remittance/customlist" style="width:100%;">
	 <div class="row">
		<c:if test="${param['brokerFundingStatus'] eq 'Unprocessed' || param['brokerFundingStatus'] eq 'Processed' || param['brokerFundingStatus'] eq 'Completed'}">
		 	<div class="col-md-2">
	           <select class="selectpicker append fundingSubmittedClass" data-size="auto" data-live-search="true" data-style="btn-white" name="brokerFundingStatus">
	               <!--  <option value="">- Broker Status -</option> -->
	                <c:forEach items="${brokerStatuses}" var="status">
	                	<option class=" btn-group" value="${status}" <c:if test="${param['brokerFundingStatus'] eq status}">selected='selected'</c:if>>${status.label}</option>
	                </c:forEach>
	            </select>
	        </div>
        </c:if>
        
        <c:if test="${param['investorRemittanceStatus'] eq 'Submitted' || param['investorRemittanceStatus'] eq 'Received'}">
	        <div class="col-md-2">
	           <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="investorRemittanceStatus">
					 <!-- <option value="">- Investor status -</option> -->
					<c:forEach items="${investorStatuses}" var="status">
	                	<option class=" btn-group" value="${status}" <c:if test="${param['investorRemittanceStatus'] eq status}">selected='selected'</c:if>>${status.label}</option>
	             	</c:forEach>
	            </select>
	        </div>
        </c:if>
        
	 	<div class="col-md-3">
            <select class="selectpicker append" data-size="auto" data-live-search="true" data-style="btn-white" name="userPortfolio.id" data-none-selected-text="- Email -" multiple>
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
      	<div class="row"></div>
        <div class="col-md-10 padding-top-10" >
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
	 <c:if test="${param['brokerFundingStatus'] eq 'Issues'}">
	 	<input type="hidden" name="brokerFundingStatus" class="form-control brokerFundingStatusIssueStatus" value="${param['brokerFundingStatus']}"/>
 	</c:if>
 	 <c:if test="${param['investorRemittanceStatus'] eq 'Issues'}">
	 	<input type="hidden" name="investorRemittanceStatus" class="form-control investorRemittanceStatusIssueStatus" value="${param['investorRemittanceStatus']}"/>
 	</c:if>
</form>
<c:if test="${list.size() > 0}">
	<div class="m-b-10">
		<c:if test="${param['brokerFundingStatus'] eq 'Unprocessed' || param['brokerFundingStatus'] eq 'Processed'}">
		    <div class="pull-right m-r-10">
		        <select class="selectpicker remittanceBatchPicker" id="remittanceBatchPicker" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="--Remittance Batch--">
		            <option value="">- Select Batch -</option>
		            <c:forEach items="${remittanceBatches}" var="remittanceBatch">
		                 <option class="btn-group" value="${remittanceBatch}">${remittanceBatch}</option>
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
                        <c:when test="${param['investorRemittanceStatus'] eq 'Submitted' || param['investorRemittanceStatus'] eq 'Received'}">
                             Awaiting Reconciliation 
                        </c:when>
                        <c:when test="${param['brokerFundingStatus'] eq 'Unprocessed' || param['brokerFundingStatus'] eq 'Processed'}">
	                         Broker Funding
                        </c:when>
                        <c:when test="${param['investorRemittanceStatus'] eq 'Issues'}">
                             Online Banking Issues
                        </c:when>
                        <c:when test="${param['brokerFundingStatus'] eq 'Issues'}">
                             Funding Status Issues   
                        </c:when>
                        <c:otherwise>
                            Remittance
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
									<table id="remittanceTable" class="table table-hover table-bordered">
										<thead>
											<tr>
												<th><input type="checkbox" class="allChecked" id="allChecked"></th>
												<th>&nbsp;</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody  class="_body">
											<c:forEach var="model" items="${list}"
												varStatus="status">
												<%-- <c:if test="${ not empty model.investorRemittanceReceivedAmount}">
													<c:set var="diffInvestorRemittanceFees" value="${model.investorRemittanceRemittedAmount - model.investorRemittanceReceivedAmount}"></c:set>
												</c:if> --%>
												<c:set var="diffBrokerFundingFees" value="${model.investorRemittanceReceivedAmount - model.brokerFundingReceivedAmount}"></c:set>
												<tr>
												    <td style="width:5px; padding-top: 2%"> <input type="checkbox" class="remittance_batch" name="modelId" value="${model.id}"></td>
												    <td style="padding: 0;">
												    	<table class="table table-bordered investorRemittanceTableClass" style="margin: 0;">
															<tbody>
													    		<tr>
													    			<td colspan="1"> <strong>Date</strong> </td>
													    			<td colspan="6"> <javatime:format value="${model.createdOn}" pattern="${commonProperties.DATE_FORMAT_FOR_FORM}"></javatime:format></td>
													    		</tr>
													    		<tr>
													    			<td colspan="1"><strong> Email </strong> </td>	
																	<td colspan="6"><a href="${appContextName}/admin/user/profile?id=${model.userPortfolio.user.id}" target="_blank">${model.userPortfolio.user.email}</a></td>
																</tr>
																
																 <%-- <c:if test="${param['investorRemittanceStatus'] eq 'Submitted' || param['investorRemittanceStatus'] eq 'Received' || param['investorRemittanceStatus'] eq 'Issues'}"> --%>
																<tr> 
											            			<td colspan="7" >
											            				<table class="table table-bordered" style="margin: 0;">
										            						<thead>
																				<tr>
																					<th>Investor Remittance</th>
																					<th>Amount on Remittance Slip</th>
																					<th>Amount in Online Banking</th>
																					<th>Investor Remittance Fees</th>
																				</tr>
																			</thead>
																			<tbody>
																				<tr>
																				<td>
																					<select id="investorRemittance" class="form-control selectpicker full-width-selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="investorRemittanceStatus">
																		                <c:forEach items="${investorStatusList}" var="investorStatus">
																		                    <option class=" btn-group" value="${investorStatus}" <c:if test="${investorStatus.label eq model.investorRemittanceStatus.label}">selected='selected'</c:if>>${investorStatus.label}</option>
																		                </c:forEach>
															            			</select> 
																				</td>
																				<td>
																					<input name="investorRemittanceRemittedAmount"  Class="form-control"  type="number" value="${model.investorRemittanceRemittedAmount}">
																				</td>
																				<td class="calculateRemittanceAmountClass">
																					<input name="investorRemittanceReceivedAmount" Class="form-control investorRemittanceReceivedAmount"  style="width: 100%; " type="number" value="${model.investorRemittanceReceivedAmount}">
																				</td>
																				<td class="calculateRemittanceAmountClass1">
																					<input name="investorRemittanceFees" Class="form-control investorRemittanceFees"  type="number" value="${model.investorRemittanceFees}">
																				</td>
																				</tr>
																			</tbody>
											            				</table>
											            			</td>
													    		</tr>
															 <%-- </c:if> --%>
															 <%-- <c:if test="${param['brokerFundingStatus'] eq 'Unprocessed' || param['brokerFundingStatus'] eq 'Processed' || param['brokerFundingStatus'] eq 'Completed' || param['brokerFundingStatus'] eq 'Issues'}"> --%>	
																<tr style="border-top-style: hidden;">
																	<td colspan="7">
																		<table class="table table-bordered" style="margin: 0;">
																			<thead>
																				<tr>
																					<th>Broker Funding Status</th>
																					<th>Batch</th>
																					<th>Broker Funding Received Amount</th>
																					<th>Broker Funding Fees</th>
																					<th>Net Investment Amount</th>
																				</tr>
																			</thead>
																			<tbody>
																				<tr>
																					<td><select id="fundStatus"
																						class="form-control selectpicker full-width-selectpicker"
																						data-live-search="true" data-style="btn-white"
																						name="brokerFundingStatus">
																							<option value="">- Funding Status -</option>
																							<c:forEach items="${brokerStatusList}"
																								var="fundingStatus">
																								<option class=" btn-group"
																									value="${fundingStatus}"
																									<c:if test="${fundingStatus.label eq model.brokerFundingStatus.label}">selected='selected'</c:if>>${fundingStatus.label}</option>
																							</c:forEach>
																					</select></td>
																					<td><input value="${model.brokerBatch}"
																						style="width: 100px;" type="text"
																						name="brokerBatch"
																						class="form-control brokerBatch"
																						placeholder="yyyy-mm-dd" data-provide="datepicker"
																						data-date-format="yyyy-mm-dd" /></td>
																					<td class="calculateBrokerAmountClass"><input
																						name="brokerFundingReceivedAmount"
																						Class="form-control brokerFundingReceivedAmount"
																						type="number"
																						value="${model.brokerFundingReceivedAmount}">
																					</td>
																					<td class="calculateBrokerAmountClass1"><input
																						name="brokerFundingFees"
																						Class="form-control brokerFundingFees"
																						style="width: 100%;" type="number"
																						value="${model.brokerFundingFees}"></td>
																					<td class="netFeeClass"><input
																						name="netInvestmentAmount"
																						class="form-control netInvestmentAmount"
																						type="number"
																						value="${model.brokerFundingReceivedAmount}">
																					</td>
																				</tr>
																				<tr>
																					<td colspan="1"><strong> Remarks </strong></td>
																					<td colspan="7"><textarea name="remarks"
																							Class="form-control" style="width: 100%"
																							placeholder="Remarks">${model.remarks}</textarea>
																					</td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																</tr>
															<%-- </c:if> --%>
															
															</tbody>
												    	</table>
												    </td>
													<td class="action"><input name="modelId" type="hidden" value="${model.id}">
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

         $('.remittanceBatchPicker').change(function() {
        	  if($(this).val()) {
               var distributeFees = prompt("Please enter total fee in number (without $ or comma)");
               if(distributeFees && Number(distributeFees)) {
            	   var selectedBatchDate = $(this).val();
            	   var rows = $('#remittanceTable > tbody > tr');
            	   var totalAmt = 0;
            	   var totalRecords = 0;
            	   rows.each(function() {
            		   var receivedDate = $(this).find('input.brokerBatch').val();
            		   if(receivedDate == selectedBatchDate) {
	            		   var receivedAmt = $(this).find('input.investorRemittanceReceivedAmount').val();
	            		   totalAmt += parseInt(receivedAmt);
	            		   totalRecords++;
            		   }
            	   });
            	   var calculationRows = 0;
            	   var remainingFees = distributeFees;
            	   rows.each(function() {
                       var receivedDate = $(this).find('input.brokerBatch').val();
                       if(receivedDate == selectedBatchDate) {
                    	   calculationRows++;
                           receivedAmt = parseInt($(this).find('input.investorRemittanceReceivedAmount').val());
                           // Parsing float and rounding down to 2 decimal points
                           var fees = parseFloat((Math.floor(((receivedAmt / totalAmt) * distributeFees) * 100) / 100).toFixed(2));
                           // Adding remaining fees back
                           remainingFees = parseFloat(remainingFees - fees);
                           // If remaining fees are there then add
                           if((totalRecords == calculationRows) && (remainingFees > 0)) {
                        	  fees = fees + remainingFees;
                           }
                           
                           $(this).find('input.brokerFundingFees').val(fees.toFixed(5));
                          // $(this).find('input.investorRemittanceFees').val(fees.toFixed(2));
                           var netInvestAmt = parseFloat((receivedAmt - fees).toFixed(5));
                           $(this).find('.netInvestmentAmount').val(netInvestAmt.toFixed(5));
                           $(this).find('.brokerFundingReceivedAmount').val(netInvestAmt.toFixed(5));
                       }
                   });
               }
        	}
         });
        $('.remittance_batch').change(function() {
        	checkboxCount = $('.remittance_batch:checked').length; 
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

        	if ($('.remittance_batch:checked').length == $('.remittance_batch').length ){
        		$("#allChecked").prop('checked', true);
        	}   
        });
	    
        $("#saveSelected").on("click", function() {
        	var saveButtons = [];
        	$('.remittance_batch:checked').each(function(index) {
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
        		alert('Please select Remittance');
        		return false;
        	} else {
        		timerclick(saveButtons,'${appContextName}/admin/remittance/api/update/');
        	}
        });
		// click save button will save row data.
		clickSaveButton('${appContextName}/admin/remittance/api/update/');
		
		 $("#allChecked").change(function(){
			 $(".remittance_batch").prop('checked', $(this).prop("checked"));
			 if(this.checked) {
				 $("#saveSelected").removeClass('disabled');
				 $("#saveSelected").removeAttr("disabled");
			 }else{
				 $("#saveSelected").addClass('disabled');
				 $("#saveSelected").attr("disabled");
			 }
		 }); 

		//calculate fee on received amount and fee amount when they changed.
		$(document).on('change', '.calculateRemittanceAmountClass,.calculateRemittanceAmountClass1', function() {
    		var investorRemittanceRemittedAmount = $(this).closest('tr').find('input[name=investorRemittanceRemittedAmount]').val();
			var investorRemittanceReceivedAmount = $(this).closest('tr').find('input[name="investorRemittanceReceivedAmount"]').val();
			var investorRemittanceFees = $(this).closest('tr').find('input[name="investorRemittanceFees"]').val();
			
			var calculateRemittanceFees;
			if($(this).attr("class")=='calculateRemittanceAmountClass'){
				if(investorRemittanceReceivedAmount==''){
					
					$(this).closest('tr').find('.investorRemittanceFees').val('');
				//	$(this).closest('.investorRemittanceTableClass').find('input[name=brokerFundingReceivedAmount]').val('');
					$(this).closest('.investorRemittanceTableClass').find('input[name=brokerFundingReceivedAmount]').val('');
					 $(this).closest('.investorRemittanceTableClass').find('input[name=brokerFundingFees]').val('');
					$(this).closest('.investorRemittanceTableClass').find('input[name=netInvestmentAmount]').val(''); 
					
					return;
				}
				calculateRemittanceFees = investorRemittanceRemittedAmount - investorRemittanceReceivedAmount;	
			}else{
				if(investorRemittanceFees==''){
					$(this).closest('tr').find('.investorRemittanceReceivedAmount').val('');
					return;
				}
				calculateRemittanceFees = investorRemittanceRemittedAmount - investorRemittanceFees;
			}
			
    		if(calculateRemittanceFees<0){
    			alert("Invalid Amount.");
    			$(this).closest('tr').find('input[name=investorRemittanceFees]').val('');
    			$(this).closest('tr').find('.investorRemittanceFees').val('');
    			return;
    		}
    		if($(this).attr("class")=='calculateRemittanceAmountClass'){
    			$(this).closest('tr').find('.investorRemittanceFees').val(calculateRemittanceFees.toFixed(5));	
    		}else{
    			$(this).closest('tr').find('.investorRemittanceReceivedAmount').val(calculateRemittanceFees.toFixed(5));
    		}
    		
		});
		
		// broker funding recieve amt
		$(document).on('change', '.calculateBrokerAmountClass,.calculateBrokerAmountClass1', function() {
			
			var investorRemittanceReceivedAmount = $(this).closest('.investorRemittanceTableClass').find('input[name="investorRemittanceReceivedAmount"]').val();
    		var brokerFundingFees = $(this).closest('tr').find('input[name=brokerFundingFees]').val();
			var brokerFundingReceivedAmount = $(this).closest('tr').find('input[name="brokerFundingReceivedAmount"]').val();

			if(investorRemittanceReceivedAmount.length==0){
				alert("Please Enter Amount in Online Banking First.");
				$(this).closest('tr').find('.brokerFundingReceivedAmount').val('');
				$(this).closest('tr').find('.brokerFundingFees').val('');
				$(this).closest('tr').find('.netInvestmentAmount').val('');
				return;
			}
			var calculateBrokerFees;
			if($(this).attr("class")=='calculateBrokerAmountClass'){
				if(brokerFundingReceivedAmount.length==''){
					$(this).closest('tr').find('.brokerFundingFees').val('');
					$(this).closest('tr').find('.netInvestmentAmount').val('');
					return;
				}
				calculateBrokerFees = investorRemittanceReceivedAmount - brokerFundingReceivedAmount;	
			}else{
				if(brokerFundingFees.length==0){
					$(this).closest('tr').find('.brokerFundingReceivedAmount').val(''); 
				 	$(this).closest('tr').find('.netInvestmentAmount').val('');
					return;
				 }
				calculateBrokerFees = investorRemittanceReceivedAmount - brokerFundingFees;
			}

			if(calculateBrokerFees<0){
				alert("Invalid Amount.");
				if($(this).attr("class")=='calculateBrokerAmountClass'){
					$(this).closest('tr').find('.brokerFundingReceivedAmount').val('');
					$(this).closest('tr').find('.netInvestmentAmount').val('');
				}else{
					$(this).closest('tr').find('.brokerFundingFees').val('');	
				}
				return;
			}
    		
			if($(this).attr("class")=='calculateBrokerAmountClass'){
				$(this).closest('tr').find('.brokerFundingFees').val(calculateBrokerFees.toFixed(5));
				$(this).closest('tr').find('.netInvestmentAmount').val(brokerFundingReceivedAmount);
			}else{
    			$(this).closest('tr').find('.brokerFundingReceivedAmount').val(calculateBrokerFees.toFixed(5));
    			$(this).closest('tr').find('.netInvestmentAmount').val(calculateBrokerFees.toFixed(5));
    		}
    		
		});
		
		// backup 
		/* $(document).on('change', '.calculateAmountClass', function() {
    		var receiveAmount = $(this).closest('tr').find('input[name=investorRemittanceReceivedAmount]').val();
			var fees = $(this).closest('tr').find('input[name=investorRemittanceFees]').val();
    		var fee = receiveAmount - fees;
    		if(fee<0){
    			alert("Amount in online Banking should be more than Fee Amount");
    			$(this).closest('tr').find('input[name=investorRemittanceReceivedAmount]').val('');
    			$(this).closest('tr').find('input[name=investorRemittanceFees]').val('');
    			$(this).closest('tr').find('.netInvestmentAmount').val('');
    			return;
    		}
    		$(this).closest('tr').find('.netInvestmentAmount').val(fee.toFixed(3));
		}); */
		
		$('.remittanceCookie').on("click", function() {
			$.cookie("remittanceCookieUrl", '${requestPage}');
		});
		
		$('#filterForm :reset').on('click', function(evt) {
		    <c:if test="${param['brokerFundingStatus'] eq 'Issues'}">
	 				$form.find('.brokerFundingStatusIssueStatus').val("Issues");
 			</c:if>
 			 <c:if test="${param['investorRemittanceStatus'] eq 'Issues'}">
				$form.find('.investorRemittanceStatusIssueStatus').val("Issues");
			</c:if>
		});
		
	});
</script>
<%@include file="../footer.jspf"%>