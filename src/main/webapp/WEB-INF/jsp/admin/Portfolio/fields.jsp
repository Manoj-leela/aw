<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<spring:eval expression="T(sg.activewealth.roboadvisor.portfolio.enums.PortfolioStatus).values()" var="portfoliostatus" />
<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf"%>
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
	<div class="col-md-12">
		<form:form class="form-horizontal" name="mainForm"
        	action="${appContextName}/admin/portfolio/${formPostUrl}"
            commandName="${modelName}" method="post" role="form"
            data-parsley-validate="true">
            <c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
    		<div class="panel panel-inverse">
        		<div class="panel-heading">
             		<h3 class="panel-title">
                		Portfolio
             		</h3>
         		</div>
		    	<div class="panel">
		        	<div class="panel-body">
		            	<div class="row">
		                	<div class="col-sm-8 pull-left">
	                        	<fieldset>
	                        		<div class="form-group">
		                                <label for="assignmentCategory" class="col-md-4 control-label">Assignment Category *</label>
		                                <div class="col-md-8">
		                                     <form:select id="assignmentCategory" path="assignmentCategory" data-parsley-required="true" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="assignmentCategory">
		                                        <form:options path="assignmentCategory" items="${portfolioCategoryList}" itemLabel="label" itemValue="value"/>
		                                    </form:select>
		                                </div>
		                            </div>
		                            <div
		                                class="form-group <spring:bind path="riskProfile"><c:if test="${status.error}">has-error</c:if></spring:bind>">
		                                <label for="riskProfile" class="col-md-4 control-label">Risk Profile* </label>
		                                <div class="col-md-8">
		                                    <form:select id="riskProfile" path="riskProfile"
		                                        class="form-control selectpicker" data-parsley-required="true" data-live-search="true" data-style="btn-white">
		                                        <form:options path="riskProfile" items="${userProfileTypes}" itemLabel="label" itemValue="value"/>
		                                    </form:select>
		                                </div>
		                            </div>
		                            <div class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
		                                <label for="name" class="col-md-4 control-label">Name*</label>
		                                <div class="col-md-8">
		                                    <form:input id="name" path="name" cssClass="form-control"
		                                        data-parsley-required="true" />
		                                </div>
		                            </div>
		                            <div class="form-group">
		                                <label for="name" class="col-md-4 control-label">Description </label>
		                                <div class="col-md-8">
		                                    <form:textarea id="description" path="description" cssClass="form-control"/>
		                                </div>
                           			</div>
		                            <div
		                                class="form-group <spring:bind path="status"><c:if test="${status.error}">has-error</c:if></spring:bind>">
		                                <label for="status" class="col-md-4 control-label">Status* </label>
		                                <div class="col-md-8">
			                            <form:select  path="status" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" >
							                <c:forEach items="${portfoliostatus}" var="status">
							                    <form:option  value="${status}">${status.label}</form:option>
							                </c:forEach>
	           							</form:select>
	           							</div>
           							</div>
           							 <div class="form-group <spring:bind path="projectedReturns"><c:if test="${status.error}">has-error</c:if></spring:bind>">
                            			<label for="projectedReturns" class="col-md-4 control-label">Projected Returns</label>
                            			<div class="col-md-8">
                                			<form:input id="projectedReturns" path="projectedReturns" cssClass="form-control"/>
                            			</div>
                       				 </div>
                        		</fieldset>
		                	</div>
		           		</div>
		        	</div>
		    	</div>
    		</div>
	    	<div class="panel panel-inverse" id="portfolioAssignmentCategory">
            	<div class="panel-heading">
                	<h3 class="panel-title"> Add New Instruments </h3>
                 </div>
	             <div class="panel" id="portfolioAssignmentCategory">
                 	<div class="panel-body">
                    	<fieldset>
                        	<div class="row">
                            	<div class="col-sm-12 pull-left">
		                            <div class="form-group">
		                                <div class="col-md-12">
		                                    <table id="table" class="table table-hover table-bordered">
		                                        <thead>
		                                            <tr>
		                                                <th class="col-md-2">Position</th>
		                                                <th class="col-md-2">Instrument</th>
		                                                <th class="col-md-2">Percentage</th>
		                                                <th class="col-md-2">Action</th>
		                                            </tr>
		                                        </thead>
	                                        	<tbody class="_body">
		                                            <c:forEach var="portInstrument" items="${model.portfolioInstruments}"
		                                                varStatus="status">
		                                                <tr id="instr_${status.index}" class="entryRow">
		                                                    <form:hidden path="portfolioInstruments[${status.index}].id"/>
		                                                    <td class="type col-md-2">${portInstrument.tradePosition.label}</td>
		                                                    <form:hidden path="portfolioInstruments[${status.index}].tradePosition"/>
		                                                    <td class="name col-md-2">
			                                                    <form:hidden path="portfolioInstruments[${status.index}].instrument.id"/>
			                                                    <form:hidden path="portfolioInstruments[${status.index}].instrument.name"/>
		                                                        ${portInstrument.instrument.name}(${portInstrument.instrument.instrumentType.label})
		                                                    </td>
		                                                    <td class="weightage col-md-2"><form:hidden class="_instrument_weightage" path="portfolioInstruments[${status.index}].weightage"/>${portInstrument.weightage}</td>
		                                                    <td class="action col-md-2">
		                                                    <c:if test="${portInstrument.inUse ne true}">
		                                                        <a href="#"><i data-id="${portInstrument.id}" onclick="deleteJSRow('${status.index}');" class="fa fa-trash fa-2x text-danger" aria-hidden="true"></i></a>
		                                                    </c:if>   
		                                                    </td>
		                                                </tr>
		                                            </c:forEach>
		                                            <tr
		                                                class="entryNoneRow <c:if test="${not empty portfolioInstruments}">cpl-hidden-el</c:if>">
		                                                <td colspan="6">No record added yet.</td>
		                                            </tr>
		                                        </tbody>
		                                    </table>
		                                </div>
                           			</div>
<!--                            			Start here -->
									<div class="form-group">
                                   		<div class="col-md-2 no-left-padding">
                                        	<label class="control-label">Position</label>
                                        	<select id="type" class="form-control selectpicker"
                                                data-parsley-required="true" data-live-search="true" data-style="btn-white">
                                            <option value="SHORT">Short</option>
                                            <option value="LONG">Long</option>
                                        	</select>
                                   		</div>
                                      	<div class="col-md-2 no-left-padding">
                                        	<label class="control-label">Instrument</label>
                                          	<select id="instrument" class="form-control selectpicker" 
                                                  data-parsley-required="true" data-live-search="true" data-style="btn-white">
                                                  <option id="default">- Instruments -</option>
                                              <c:forEach items="${instruments}" var="instrument">
                                                  <option value="${instrument.id}" data-type="${instrument.instrumentType.label}">${instrument.name}</option>
                                              </c:forEach>
                                          	</select>
                                      	</div>
                                      	<div class="col-md-2 no-left-padding">
                                          	<label class="control-label">Percentage </label>
                                          	<input class="form-control selectpicker" type="text" id="percentage">
                                      	</div>
                                      	<div class="col-md-2 no-left-padding" style="margin-top: 2%;">
                                        	<a class="addNewPortfolioInstrument btn btn-primary" tabindex="0"><i
                                                  class="glyphicon glyphicon-plus"></i>&nbsp; Add</a>
                                      	</div>
                   					</div>
                       			</div>
                   			</div>
                      	</fieldset>
                 	</div>
            	</div>
         	</div>
         	<div class="panel panel-inverse" id="portfolioAssignmentCategory">
            	<div class="panel-heading">
                	<h3 class="panel-title"> Add new Asset Class Allocation </h3>
                 </div>
	             <div class="panel" id="portfolioAssignmentCategory">
                 	<div class="panel-body">
                    	<fieldset>
                        	<div class="row">
                            	<div class="col-sm-12 pull-left">
		                            <div class="form-group">
		                                <div class="col-md-12">
		                                    <table id="assetTable" class="table table-hover table-bordered">
		                                        <thead>
		                                            <tr>
		                                            	<th class="col-md-2">Type</th>
		                                                <th class="col-md-2">Display Name</th>
		                                                <th class="col-md-1">Instrument Code</th>
		                                                <th class="col-md-1">Total Weightage</th>
		                                                <th class="col-md-2">Detail</th>
		                                                <th class="col-md-2">Description</th>
		                                                <th class="col-md-2">Color</th>
		                                                <th class="col-md-2">Action</th>
		                                            </tr>
		                                        </thead>
	                                        	<tbody class="_body">
		                                            <c:forEach var="asseetClassAllocation" items="${model.assetClassAllocations}"
		                                                varStatus="status">
		                                                <tr id="asset_${status.index}" class="assetEntryRow">
		                                                    <form:hidden path="assetClassAllocations[${status.index}].id"/>
		                                                    <td class="instrumentType col-md-2">${asseetClassAllocation.instrumentType.label}</td>
		                                                    <form:hidden path="assetClassAllocations[${status.index}].instrumentType"/>
		                                                    <td class="displayName col-md-2">
		                                                    	<form:hidden path="assetClassAllocations[${status.index}].displayName"/>
		                                                    	${asseetClassAllocation.displayName}
		                                                    </td>
		                                                     <td class="instrumentCode col-md-2">
		                                                    	<form:hidden path="assetClassAllocations[${status.index}].code"/>
		                                                    	${asseetClassAllocation.code}
		                                                    </td>
		                                                     <td class="totalWeightage col-md-2">
		                                                    	<form:hidden class="_assetClass_totalWeightage" path="assetClassAllocations[${status.index}].totalWeightage" />
		                                                    	${asseetClassAllocation.totalWeightage}
		                                                    </td>
		                                                    <td class="detail col-md-2">
		                                                    	<form:hidden path="assetClassAllocations[${status.index}].detail"/>
		                                                    	${asseetClassAllocation.detail}
		                                                    </td>
	                                                      	<td class="assetDescription col-md-2">
		                                                    	<form:hidden path="assetClassAllocations[${status.index}].description"/>
		                                                    	${asseetClassAllocation.description}
		                                                    </td>
		                                                    <td class="assetColor col-md-2">
		                                                    	<form:hidden path="assetClassAllocations[${status.index}].color"/>
		                                                    	${asseetClassAllocation.color}
		                                                    </td>
		                                                    <td class="action col-md-2">
	                                                        	<a href="#"><i data-id="${portInstrument.id}" onclick="deleteAssetJSRow('${status.index}');" class="fa fa-trash fa-2x text-danger" aria-hidden="true"></i></a>
		                                                    </td>
		                                                </tr>
		                                            </c:forEach>
		                                            <tr
		                                                class="assetEntryNoneRow <c:if test="${not empty assetClassAllocations}">cpl-hidden-el</c:if>">
		                                                <td colspan="8">No record added yet.</td>
		                                            </tr>
		                                        </tbody>
		                                    </table>
		                                </div>
                           			</div>
                       			</div>
                   			</div>
							<div class="row">
								<div class="col-sm-8 pull-left">
									<div class="form-group">
                                        <label for="instrumentType" class="col-md-4 control-label">Instrument Type *</label>
                                        <div class="col-md-8">
                                            	<select id="instrumentType" class="form-control selectpicker" 
                                                  data-parsley-required="true" data-live-search="true" data-style="btn-white">
                                                  <option id="default">- Instrument Type -</option>
                                              <c:forEach items="${instrumentTypeList}" var="instrumentType">
                                                  <option value="${instrumentType.value}" data-type="${instrumentType.label}">${instrumentType.label}</option>
                                              </c:forEach>
                                          	</select>
                                   		</div>
									</div>
									<div class="form-group">
										<label for="displayName" class="col-md-4 control-label">Display Name *</label>
										<div class="col-md-8">
											<input class="form-control " type="text" id="displayName">
										</div>
									</div>
									<div class="form-group">
										<label for="instrumentCode" class="col-md-4 control-label">Instrument Code *</label>
										<div class="col-md-8">
											<input class="form-control " type="text" id="instrumentCode">
										</div>
									</div>
									<div class="form-group">
										<label for="totalWeightage" class="col-md-4 control-label">Total Weightage *</label>
										<div class="col-md-8">
											<input class="form-control " type="text" id="totalWeightage" pattern="${threeDecimalPattern}">
										</div>
									</div>
									<div class="form-group">
										<label for="detail" class="col-md-4 control-label">Detail</label>
										<div class="col-md-8">
											<input class="form-control " type="text" id="detail">
										</div>
									</div>
									<div class="form-group">
										<label for="assetDescription" class="col-md-4 control-label">Description *</label>
										<div class="col-md-8">
											<textarea rows="5" cols="5" class="form-control "  id="assetDescription"></textarea>
										</div>
									</div>
									<div class="form-group">
										<label for="assetColor" class="col-md-4 control-label">Color *</label>
										<div class="col-md-8">
											<input type="text" value="#000000" class="form-control" id="assetColor" />
										</div>
									</div>
									<div class="form-group">
										<label for="assetDescription" class="col-md-4 control-label"></label>
										<div class="col-md-8">
											<a class="addNewAssetClassAllocation btn btn-primary " tabindex="0"><i
                                                  class="glyphicon glyphicon-plus"></i>&nbsp; Add</a>
										</div>
									</div>
							</div>
							</div>
                      	</fieldset>
                 	</div>
            	</div>
         	</div>
         	<div class="panel">
	            <div class="panel-body">
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
	                          	       <button type="submit" class="btn btn-primary <c:if test = "${isPortfolioInUse}"> hide</c:if>">Save</button>
	                                   <c:if test="${not empty param['id']}">
	        		                    	<a class="btn btn-inverse deleteButton <c:if test = "${isPortfolioInUse}"> hide</c:if>"  data-id="${model.id}" tabindex="0">
			        		                	<i class="fa fa-trash" ></i>&nbsp;Delete
				    	                	</a>
										</c:if>
	                                    <a href="${appContextName}/admin/portfolio/list" class="btn btn-primary">
	                                           <i class="fa -square-o"></i>Cancel
	                                    </a>
	                                </div>
	                           </div>
							</div>
						</div>
					</fieldset>
				</div>
         	</div>
        </form:form>
    </div>
</div>
<!-- end row -->
<form action="delete" name="deleteForm" method="post">
	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<input type="hidden" name="id" />
</form>
<script type="text/javascript">
    $(document).ready(function() {
        $('.addNewPortfolioInstrument').bind('click', function() {
            addNewPortfolioInstrument();
        });
        $('.addNewAssetClassAllocation').bind('click', function() {
        	addNewAssetClassAllocation();
        });
     // Initialize select picker
    	$('.selectpicker').selectpicker();
    });
    <c:choose>
    <c:when test="${empty model.portfolioInstruments}">
        var startingIndex = -1;
    </c:when>
    <c:otherwise>
        var startingIndex = ${model.portfolioInstruments.size()-1};
        $('.entryNoneRow').hide();
    </c:otherwise>
    </c:choose>
    
    <c:choose>
    <c:when test="${empty model.assetClassAllocations}">
        var assetStartingIndex = -1;
    </c:when>
    <c:otherwise>
        var assetStartingIndex = ${model.assetClassAllocations.size()-1};
        $('.assetEntryNoneRow').hide();
    </c:otherwise>
    </c:choose>
    
    function addNewPortfolioInstrument() {
    	debugger
        var mainFormElements = document.mainForm.elements;
        var portfolioName = $("#name")[0].value;
        var table = $("#table")[0];
        var type = $('#type').find("option:selected").text();
        var typeValue =  $('#type').find("option:selected").val();
        var instrument = $("#instrument")[0];
        var instrumentText = $("#instrument").find("option:selected").text();
        var instrumentType = $("#instrument").find("option:selected").data('type');
        var percentage = $.trim($("#percentage")[0].value);
        if(!instrumentType){
        	alert('Please Select Instrument');
            $('#instrument').focus();
            return;
        }
        if(percentage == ''){
            alert('Please Enter Percentage');
            $('#percentage').focus();
            return;
        }
        var percentageRegex =new RegExp("(^100(\.0{1,2})?$)|(^([1-9]([0-9])?|0)(\.[0-9]{1,2})?$)");
        if(isNaN(percentage) || percentage < 0 || percentage > 100 || !percentageRegex.test(percentage)){
            alert('Please enter valid value of Percentage with maximum two decimal digits');
            $('#percentage').focus();
            return;
        }
        //validation
        if(portfolioName.trim().length == 0){
            alert('Please Input Name');
            $('#name').focus();
            return;
        }   else {
            var regExp1 = new RegExp("\\[0\\]","g"); //replace [0]
            startingIndex++;
            var toReplace1 = "[" + startingIndex + "]";
            var entryTemplate = '<tr id="instr_"'+startingIndex+' class="entryRow" > ' +
            '<input type="hidden" name="portfolioInstruments[0].instrument.id" value="'+ instrument.value +'" />' +
            '<input type="hidden" name="portfolioInstruments[0].instrument.name" value="'+ instrumentText +'">'+
            '<input type="hidden" class="_instrument_weightage" name="portfolioInstruments[0].weightage" value="'+ percentage +'"/>' +
            '<input type="hidden" name="portfolioInstruments[0].tradePosition" value="'+ typeValue +'"/>' +
            '<td class="type"></td>' +
            '<td class="name"></td>' +
            '<td class="weightage"></td>' +
            '<td class="action">' +
            '<div class="media-option btn-group shaded-icon">' +
            '<a href=\"#\"">'+
            '<i class="fa fa-trash fa-2x text-danger" aria-hidden="true" onclick="deleteJSRow('+startingIndex+');">' +
            '</div>' +
            '</td>' +
            '</tr>';
            var moduleEntry = entryTemplate.replace(regExp1,toReplace1);
            var row = table.insertRow(-1);
            row.innerHTML = moduleEntry;
            row.id='instr_'+startingIndex;
            row.cells[0].innerHTML = type;
            row.cells[1].innerHTML = instrumentText + "(" + instrumentType + ")";
            row.cells[2].innerHTML = percentage;
            mainFormElements['portfolioInstruments[' + startingIndex + '].instrument.id'].value = instrument.value;
            mainFormElements['portfolioInstruments[' + startingIndex + '].weightage'].value = percentage; //its just saved opposite in db
            mainFormElements['portfolioInstruments[' + startingIndex + '].instrument.name'].value = instrumentText;
            mainFormElements['portfolioInstruments[' + startingIndex + '].tradePosition'].value = typeValue;
            $('.entryNoneRow').hide();
            $("#percentage").val('');
            $("#instrument").val('#default');
            $("#instrument").selectpicker("refresh");
        }
    }
    function addNewAssetClassAllocation() {
    	debugger
        var mainFormElements = document.mainForm.elements;
        var portfolioName = $("#name")[0].value;
        var table = $("#assetTable")[0];
        var type = $('#instrumentType').find("option:selected").text();
        var typeValue = $('#instrumentType').find("option:selected").val();
        var instrumentTypeValue = $('#instrumentType').find("option:selected").data('type');
        var displayName = $("#displayName").val();
        var instrumentCode = $("#instrumentCode").val();
        var totalWeightage = $("#totalWeightage").val();
        var detail = $("#detail").val();
        var assetDescription = $("#assetDescription").val();
        var assetColor = $("#assetColor").val();
        //validation
        if(!instrumentTypeValue){
        	alert('Please Select Instrument Type');
            $('#instrumentType').focus();
            return;
        }
        if(displayName.trim().length == 0) {
        	alert('Please Enter Display Name');
            $('#displayName').focus();
            return;
        }
        if(instrumentCode.trim().length == 0) {
        	alert('Please Enter Instrument Code');
            $('#instrumentCode').focus();
            return;
        }
        if(totalWeightage == ''){
            alert('Please Enter Total Weightage');
            $('#totalWeightage').focus();
            return;
        } else if(! /${threeDecimalPattern}/.test(totalWeightage)){
        	alert('Total Weightage is Invalid');
            $('#totalWeightage').focus();
            return;
        }
        if(assetDescription.trim().length == 0) {
        	alert('Please Enter Asset Description');
            $('#assetDescription').focus();
            return;
        }
        if(assetColor.trim().length == 0) {
        	alert('Please Enter Asset Color');
            $('#assetColor').focus();
            return;
        }
        
        if(portfolioName.trim().length == 0){
            alert('Please Input Name');
            $('#name').focus();
            return;
        }   else {
            var regExp1 = new RegExp("\\[0\\]","g"); //replace [0]
            assetStartingIndex++;
            var toReplace1 = "[" + assetStartingIndex + "]";
            var entryTemplate = '<tr id="asset_"'+assetStartingIndex+' class="assetEntryRow" > ' +
            '<input type="hidden" name="assetClassAllocations[0].instrumentType" value="'+ typeValue +'" />' +
            '<input type="hidden" name="assetClassAllocations[0].displayName" value="'+ displayName +'" />' +
            '<input type="hidden" name="assetClassAllocations[0].code" value="'+ instrumentCode +'">'+
            '<input type="hidden" class="_assetClass_totalWeightage" name="assetClassAllocations[0].totalWeightage" value="'+ totalWeightage +'"/>' +
            '<input type="hidden" name="assetClassAllocations[0].detail" value="'+ detail +'"/>' +
            '<input type="hidden" name="assetClassAllocations[0].description" value="'+ assetDescription +'"/>' +
            '<input type="hidden" name="assetClassAllocations[0].color" value="'+ assetColor +'"/>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="type"></td>' +
            '<td class="action">' +
            '<div class="media-option btn-group shaded-icon">' +
            '<a href=\"#\"">'+
            '<i class="fa fa-trash fa-2x text-danger" aria-hidden="true" onclick="deleteAssetJSRow('+assetStartingIndex+');">' +
            '</div>' +
            '</td>' +
            '</tr>';
            var moduleEntry = entryTemplate.replace(regExp1,toReplace1);
            var row = table.insertRow(-1);
            row.innerHTML = moduleEntry;
            row.id='asset_'+assetStartingIndex;
            row.cells[0].innerHTML = type;
            row.cells[1].innerHTML = displayName;
            row.cells[2].innerHTML = instrumentCode;
            row.cells[3].innerHTML = totalWeightage;
            row.cells[4].innerHTML = detail;
            row.cells[5].innerHTML = assetDescription;
            row.cells[6].innerHTML = assetColor;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].instrumentType'].value = typeValue;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].displayName'].value = displayName;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].code'].value = instrumentCode;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].totalWeightage'].value = totalWeightage;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].detail'].value = detail;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].description'].value = assetDescription;
            mainFormElements['assetClassAllocations[' + assetStartingIndex + '].color'].value = assetColor;
            $('.assetEntryNoneRow').hide();
            $("#displayName").val('');
            $("#instrumentCode").val('');
            $("#totalWeightage").val('');
            $("#detail").val('');
            $("#assetDescription").val('');
            $("#assetColor").val('');
            $("#instrumentType").val('#default');
            $("#instrumentType").selectpicker("refresh");
        }
    }
	function deleteJSRow(trIDToBeDeleted) {
	    if (confirm('Confirm delete?')) {
	        //$('table#table tr#instr_'+trIDToBeDeleted).closest('.entryRow').find('._instrument_weightage').val('');
	        $('table#table tr#instr_'+trIDToBeDeleted).find('._instrument_weightage').val('');
	        $('table#table tr#instr_'+trIDToBeDeleted).hide();
	    }
	}
    function deleteAssetJSRow(trIDToBeDeleted) {
    	debugger
   		if (confirm('Confirm delete?')) {
       		//$('table#assetTable tr#asset_'+trIDToBeDeleted).closest('.assetEntryRow)'.find('._assetClass_totalWeightage').val('');
       		$('table#assetTable tr#asset_'+trIDToBeDeleted).find('._assetClass_totalWeightage').val('');
       		$('table#assetTable tr#asset_'+trIDToBeDeleted).hide();
   		}
   		$('#assetTable').focus();
    }
</script>
<link href="${assetsBase}/plugins/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css" rel="stylesheet" />
<script src="${assetsBase}/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.min.js"></script>
<script type="text/javascript">
	$("#assetColor").colorpicker({format:"hex"});
</script>
<%@include file="../footer.jspf"%>