<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>
<%@taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>

<c:set var="isFormsPage" value="true" />
<%@include file="../header.jspf" %>
<link href="${assetsBase}/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet" />
<link href="${assetsBase}/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />
<spring:eval expression="T(sg.activewealth.roboadvisor.common.enums.AgentOTPStatus).SentToAgent" var="AgentOTPStatusSentToAgent" />
<spring:eval expression="T(sg.activewealth.roboadvisor.common.enums.AgentOTPStatus).Completed" var="AgentOTPStatusCompleted" />
<spring:eval expression="T(sg.activewealth.roboadvisor.common.enums.KycStatus).Completed" var="KYCStatusCompleted" />
<!-- begin row -->
&nbsp;
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<form:form class="form-horizontal" name="mainForm" action="${appContextName}/admin/user/profile"
			commandName="${modelName}" method="post" role="form" data-parsley-validate="true" enctype="multipart/form-data">
			<input type="hidden" id="kycFileName1" name="kyc1FileName" value="${model.kyc1FileName}"/>
			<input type="hidden" id="kycFileName2" name="kyc2FileName" value="${model.kyc2FileName}"/>
			<input type="hidden" id="kycFileName3" name="kyc3FileName" value="${model.kyc3FileName}"/>
			<input type="hidden" id="bankSignatureFileName1" name="declarationsSignatureFileName" value="${model.declarationsSignatureFileName}"/>
			<input type="hidden" id="userKYCStatus" value="${model.kycStatus}"/>
			<div class="panel panel-inverse">
	            <div class="panel-heading">
	                <h3 class="panel-title">
	                    User Profile
	                </h3>
	            </div>
				<div class="panel">
					<div class="panel-body">
						<form:hidden path="id"/>
						<c:set var="alertMessages">
							<form:errors path="*" cssClass="_spring_errors" />
						</c:set>
						<%@include file="../alert.jspf"%>
						
						<fieldset>
							<div class="row">
								<div class="col-sm-8 pull-left">
									<div class="form-group <spring:bind path="firstName"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="firstName" class="col-md-4 control-label">First Name *</label>
										<div class="col-md-8">
											<form:input id="firstName" path="firstName" cssClass="form-control" data-parsley-required="true"/>
										</div>
									</div>
									<div class="form-group <spring:bind path="lastName"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="lastName" class="col-md-4 control-label">Last Name *</label>
										<div class="col-md-8">
											<form:input id="lastName" path="lastName" cssClass="form-control" data-parsley-required="true"/>
										</div>
									</div>
									<div class="form-group <spring:bind path="email"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="email" class="col-md-4 control-label">Email *</label>
										<div class="col-md-8">
											<div class="input-group">
												<span class="input-group-addon">@</span>
												<form:input id="email" path="email" cssClass="form-control" 
												data-parsley-required="true"/>
											</div>
										</div>
									</div>
									<div class="form-group <spring:bind path="mobileNumber"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="mobileNumber" class="col-md-4 control-label">Phone Number</label>
										<div class="col-md-8">
											<div class="input-group">
												<span class="input-group-addon">
													<span class="glyphicon glyphicon-phone-alt"></span>
												</span>
												<form:input type="text" id="mobileNumber" path="mobileNumber" cssClass="form-control" data-parsley-maxlength="20"/>
											</div>
										</div>
									</div>
									<div class="form-group <spring:bind path="password"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="password" class="col-md-4 control-label">Password
										<c:if test="${model.id == null || model.needToRehashPassword == true}"> *</c:if> </label>
										<div class="col-md-8">
											<input type="password" id="password" name="password" class="form-control" data-parsley-minlength="8" <c:if test="${model.id == null || model.needToRehashPassword == true}">data-parsley-required="true"</c:if>/>
											<div id="passwordStrength" class="is0 m-t-5"></div>                                   
										</div>
									</div>  
									<div class="form-group">
										<label for="repassword" class="col-md-4 control-label">Retype Password
										<c:if test="${model.id == null || model.needToRehashPassword == true}"> *</c:if></label>
										<div class="col-md-8">
											<input type="password" id="repassword" name="repassword" class="form-control" data-parsley-minlength="8" <c:if test="${model.id == null || model.needToRehashPassword == true}">data-parsley-required="true"</c:if>/>
											<div id="rePasswordStrength" class="is0 m-t-5"></div>
										</div>
									</div>
									<div class="form-group">
			                             <label class="col-md-4 control-label">Is Admin</label>
			                             <div class="col-md-8">
			                             <c:choose>
			                                <c:when test="${model.id == null}">
			                                    <div class="col-md-12">
			                                        <label class="radio-inline"><input type="radio" id="adminYes" name="isAdmin" value="true" /> Yes </label> 
			                                        <label class="radio-inline"><input type="radio" id="adminNo" name="isAdmin" value="false" checked/> No </label> 
			                                    </div>
			                                </c:when>
			                                <c:otherwise>
			                                    <div class="col-md-12">
			                                        <c:choose>
			                                            <c:when test="${model.isAdmin == true}">
			                                                <label class="radio-inline"><input type="radio" id="adminYes" name="isAdmin" value="true" checked/> Yes </label> 
			                                                <label class="radio-inline"><input type="radio" id="adminNo" name="isAdmin" value="false"/> No </label>
			                                            </c:when>
			                                            <c:otherwise>
			                                                <label class="radio-inline"><input type="radio" id="adminYes" name="isAdmin" value="true" /> Yes </label> 
			                                                <label class="radio-inline"><input type="radio" id="adminNo" name="isAdmin" value="false" checked/> No </label>
			                                            </c:otherwise>
			                                        </c:choose>
			                                    </div>
			                                </c:otherwise>
			                             </c:choose>
			                                  
			                             </div>
			                        </div>
			                        <div class="form-group">
										<label for="accountKey" class="col-md-4 control-label">Broker Account Key</label>
										<div class="col-md-8">
										<form:input id="accountKey" path="brokerSaxoApiKey"
												cssClass="form-control"/>
										</div>
									</div>
									<div class="form-group">
										<label for="brokerSaxoApiSecret" class="col-md-4 control-label">Broker Secret Token</label>
										<div class="col-md-8">
										<form:input id="brokerSaxoApiSecret" path="brokerSaxoApiSecret"
												cssClass="form-control"/>
										</div>
									</div>
									<div class="form-group">
											<label for="residenceCountry" class="col-md-4 control-label">Residence Country</label>
											<div class="col-md-8">
									            <form:select id="residenceCountry" path="residenceCountry" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" data-none-selected-text="- Residence Country -" name="residenceCountry">
									            	<form:option value="">- Residence Country -</form:option>
									                <form:options path="residenceCountry" items="${residenceContryList}" itemLabel="label" itemValue="value"/>
									            </form:select>
									        </div>
									 </div>
									 <div class="form-group">
										<label for="annualIncome" class="col-md-4 control-label">Annual Income</label>
										<div class="col-md-8">
											<form:input id="annualIncome" path="annualIncome" cssClass="form-control" pattern="${threeDecimalPattern}"/>
										</div>
									</div>
									<div class="form-group">
										<label for="dob" class="col-md-4 control-label">Date Of Birth</label>
										<div class="col-md-4">
											<div id="dateOfBirth" class="input-group date" data-provide="datepicker" data-date-format="yyyy-mm-dd">
												<span class="btn input-group-addon">
								                    <i class="fa fa-calendar"></i>
								                </span>
								                <form:input id="dob" path="dateOfBirth" cssClass="form-control" placeholder="2018-13-02"/>
											</div>
										</div>
									</div>
									<div class="form-group">
                                            <label for="accountStatus" class="col-md-4 control-label">Account Status *</label>
                                            <div class="col-md-8">
                                                <form:select id="accountStatus" path="accountStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-parsley-required="true" data-style="btn-white" name="accountStatus">
                                                    <form:options path="accountStatus" items="${accountStatusOpts}" itemLabel="label" itemValue="value"/>
                                                </form:select>
                                            </div>
                                     </div>
								</div>
							</div>
							<c:if test="${portfolioList.size() > 0}">
								<legend>User Portfolio</legend>
								<div class="row">
									<div class="col-sm-8 pull-left">
										<c:forEach  items="${portfolioList}" var="portfolio" varStatus="status">
											<div class="form-group">
												<label class="col-md-4 control-label">${portfolio.name}</label>
													<div class="col-md-8">
														<table id="table" class="table table-hover table-bordered">
															<thead>
																<tr>
																	<th>Position</th>
																	<th>Instrument</th>
																	<th>Percentage</th>
																</tr>
															</thead>
															<tbody>
																<c:forEach items="${portfolio.portfolioInstrument}" var="portInstrument" varStatus="status">
																	<tr class="entryRow">
																		<td class="type">${portInstrument.instrument.instrumentType}</td>
																		<td class="name">${portInstrument.instrument.name}</td>
																		<td class="weightage">${portInstrument.weightage}</td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</div>
												</div>
										</c:forEach>
									</div>
								</div>
							</c:if>
						</fieldset>
					</div>
				</div>
			</div>
			<div class="panel panel-inverse">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            Portfolio Category
                        </h3>
                    </div>
                    <div class="panel" id="portfolioCategory">
                        <div class="panel-body">
                            <fieldset>
                                <div class="row">
                                    <div class="col-sm-8 pull-left">
                                        <div class="form-group">
                                            <label for="portfolioCategory" class="col-md-4 control-label">Portfolio Category</label>
                                            <div class="col-md-8">
                                                 <form:select id="portfolioCategory" path="portfolioCategory" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="portfolioCategory">
                                                    <form:options path="portfolioCategory" items="${portfolioAssignmentCategoryList}" itemLabel="label" itemValue="value"/>
                                                </form:select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
				<input type="hidden" class="userId"  value="${model.id}"/>
				<div class="panel panel-inverse">
		            <div class="panel-heading">
		                <h3 class="panel-title">
		                    Notify Agent
		                </h3>
		            </div>
					<div class="panel">
						<div class="panel-body">
							<fieldset>
								<div class="row">
									<div class="col-sm-8 pull-left">
										<div class="form-group">
												<label for="agentCode" class="col-md-4 control-label">Code</label>
												<div class="col-md-3">
													<form:input id="agentCode" path="agent.agentCode" cssClass="form-control"/>
												</div>
												<div class="btn-group">
				                                    <button id="agentCodeButton" type="button" class="btn btn-primary sendCodeToAgent" hide>Send Code to Agent</button>
					                             </div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
				<div class="panel panel-inverse" id="otpConfirmation">
	            	<div class="panel-heading">
		                <h3 class="panel-title">
		                    OTP Confirmation
		                </h3>
		            </div>
					<div class="panel" id="otpConfirmation">
						<div class="panel-body">
							<fieldset>
								<div class="row">
									<div class="col-sm-8 pull-left">
										<div class="form-group">
											<label for="agentOtp" class="col-md-4 control-label">OTP</label>
											<div class="col-md-3">
												<form:input id="agentOtp" path="agentOtp" 
														cssClass="form-control" maxlength="4" type="number"/>
											</div>
										</div>
										<div class="form-group">
                                            <label for="agentOtpStatus" class="col-md-4 control-label">OTP Status</label>
                                            <div class="col-md-8">
                                                <form:select id="agentOtpStatus" path="agentOTPStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="agentOTPStatus">
                                                    <form:options path="agentOtpStatus" items="${agentOTPStatusList}" itemLabel="label" itemValue="value"/>
                                                </form:select>
                                        </div>
									</div>
								</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
				<div class="panel panel-inverse" id="kycDocument">
		            <div class="panel-heading">
		                <h3 class="panel-title">
		                   KYC Documents
		                </h3>
		            </div>
					<div class="panel kycDocument">
						<div class="panel-body">
							<fieldset>
								<div class="row">
									<div class="col-sm-8 pull-left">
										<div class="form-group">
											<label class="col-md-4 control-label"></label>
											<div class="col-md-8">
												<span class="label label-info" style="vertical-align: baseline;">Supported File Type " .jpg / .jpeg / .png / .pdf "</span>
											</div>
										</div>
										<div class="form-group">
											<label for="kycDoc1" class="col-md-4 control-label">Upload Identity Card (Front) </label>
											<div class="col-md-8">
												<span class="btn btn-white fileinput-button showAddFile hide"> <i class="fa fa-plus"></i> <span>Add file...</span>
				                           			<input id="kycDoc1" class="userFile kyc1FileName" type="file" name="multipartFiles" accept="application/pdf,image/jpeg"/>
					                       		</span>
					                       		<a href="javascript:;" id="filename" style="display: none;"class="btn btn-success showAddFile hide">Invalid File Type</a>
					                       		<div style="clear:both;"></div>
					                       		
					                       		<a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc1FileName}&view=" class="hideDownloadDelete hide" data-lightbox="gallery-group-1" target="_blank">
						                            <c:choose>
						                            	<c:when test="${fn:containsIgnoreCase(model.kyc1FileName,'.pdf')}">
						                            		<img src="${assetsBase}/img/robo_pdf.jpg" class="hideDownloadDelete col-md-4">
						                            	</c:when>
						                            	<c:otherwise>
						                            		<img src="${appContextName}/admin/user/downloadfile?fileName=${model.kyc1FileName}" style="width: 50%"/>
						                            	</c:otherwise>
						                            </c:choose>
						                         </a>
			                       			</div>
										</div>
										<div class="form-group">
											<label class="col-md-4 control-label"></label>
											<div class="col-md-8">
												<a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc1FileName}"
				                            class="btn btn-default dropdown-toggle hideDownloadDelete hide" aria-expanded="false" target="_blank"><i class="fa fa-download"></i>&nbsp; Download</a>
					                         	<button type="button" class="btn btn-primary deleteFile hideDownloadDelete hide" value="fileName=${model.kyc1FileName}&userId=${model.id}"><i class="fa fa-trash"></i>&nbsp; Delete</button>
											</div>
				                         </div>
										<div class="form-group">
											<label for="kycDoc2" class="col-md-4 control-label">Upload Identity Card (Back) </label>
											<div class="col-md-8">
												<span class="btn btn-white fileinput-button showAddFile hide"> <i class="fa fa-plus"></i> <span>Add file...</span>
				                           			<input id="kycDoc2" class="userFile kyc2FileName" type="file" name="multipartFiles" accept="application/pdf,image/jpeg" />
					                       		</span>
					                       		<a href="javascript:;" id="filename" style="display: none;"class="btn btn-success showAddFile hide">Invalid File Type</a>
					                       		<div style="clear:both;"></div>
					                            <a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc2FileName}&view=" class="hideDownloadDelete hide" data-lightbox="gallery-group-1" target="_blank">
						                        	<c:choose>
						                            	<c:when test="${fn:containsIgnoreCase(model.kyc2FileName,'.pdf')}">
						                            		<img src="${assetsBase}/img/robo_pdf.jpg" class="hideDownloadDelete col-md-4">
						                            	</c:when>
						                            	<c:otherwise>
						                            		<img src="${appContextName}/admin/user/downloadfile?fileName=${model.kyc2FileName}" style="width: 50%"/>
						                            	</c:otherwise>
						                            </c:choose>
						                         </a>
			                       			</div>
										</div>
										<div class="form-group">
											<label class="col-md-4 control-label"></label>
											<div class="col-md-8">
												<a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc2FileName}"
					                            class="btn btn-default dropdown-toggle hideDownloadDelete hide" aria-expanded="false" target="_blank"><i class="fa fa-download"></i>&nbsp; Download</a>
												<button type="button" class="btn btn-primary deleteFile hideDownloadDelete hide" value="fileName=${model.kyc2FileName}&userId=${model.id}"><i class="fa fa-trash"></i>&nbsp; Delete</button>
											</div>
			                       		</div>
										<div class="form-group">
											<label for="kycDoc3" class="col-md-4 control-label">Upload Proof-of-Residence</label>
											<div class="col-md-8">
												<span class="btn btn-white fileinput-button showAddFile hide"> <i class="fa fa-plus"></i> <span>Add file...</span>
				                           			<input id="kycDoc3" class="userFile kyc3FileName" type="file" name="multipartFiles" accept="application/pdf,image/jpeg" />
					                       		</span>
					                       		<a href="javascript:;" id="filename" style="display: none;"class="btn btn-success showAddFile hide">Invalid File Type</a>
					                       		<div style="clear:both;"></div>
					                            <a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc3FileName}&view=" class="hideDownloadDelete hide" data-lightbox="gallery-group-1" target="_blank">
						                            <c:choose>
						                            	<c:when test="${fn:containsIgnoreCase(model.kyc3FileName,'.pdf')}">
							                            	<img  src="${assetsBase}/img/robo_pdf.jpg" class="hideDownloadDelete col-md-4">
						                            	</c:when>
						                            	<c:otherwise>
						                            		<img src="${appContextName}/admin/user/downloadfile?fileName=${model.kyc3FileName}" style="width: 50%" />
						                            	</c:otherwise>
						                            </c:choose>
						                         </a>
			                       			</div>
										</div>
										<div class="form-group">
											<label class="col-md-4 control-label"></label>
											<div class="col-md-8">
					                        	<a href="${appContextName}/admin/user/downloadfile?fileName=${model.kyc3FileName}"
					                            	class="btn btn-default dropdown-toggle hideDownloadDelete hide" aria-expanded="false" target="_blank"><i class="fa fa-download"></i>&nbsp; Download</a>
					                         	<button type="button" class="btn btn-primary deleteFile hideDownloadDelete hide" value="fileName=${model.kyc3FileName}&userId=${model.id}"><i class="fa fa-trash"></i>&nbsp; Delete</button>
											</div>
				                         </div>
										<div class="form-group">
                                            <label for="kycStatus" class="col-md-4 control-label">KYC Status</label>
                                            <div class="col-md-8">
                                                <form:select id="kycStatus" path="kycStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="kycStatus">
                                                    <form:options path="kycStatus" items="${kycStatusList}" itemLabel="label" itemValue="value"/>
                                                </form:select>
                                        </div>
									</div>
									 <div class="form-group">
                                        <label for="kycRemarks" class="col-md-4 control-label">Description</label>
                                        <div class="col-md-8">
                                           <form:textarea id="kycRemarks" placeholder="Remarks" path="kycRemarks" cssClass="form-control"/>
                                        </div>
                                     </div>
								</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
				<div class="panel panel-inverse" id="bankDetail">
		            <div class="panel-heading">
		                <h3 class="panel-title">
		                    Bank Details
		                </h3>
		            </div>
					<div class="panel" id="bankDetail">
						<div class="panel-body">
							<fieldset>
								<div class="row">
									<div class="col-sm-8 pull-left">
										<div class="form-group">
											<label for="bankDetailsBankName" class="col-md-4 control-label">Bank Name</label>
											<div class="col-md-8">
												<form:input id="bankDetailsBankName" path="bankDetailsBankName"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsBankAddress" class="col-md-4 control-label">Bank Address</label>
											<div class="col-md-8">
												<form:input id="bankDetailsBankAddress" path="bankDetailsBankAddress"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsAba" class="col-md-4 control-label">ABA</label>
											<div class="col-md-8">
												<form:input id="bankDetailsAba" path="bankDetailsAba"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsChips" class="col-md-4 control-label">Chips</label>
											<div class="col-md-8">
												<form:input id="bankDetailsChips" path="bankDetailsChips"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsSwiftNumber" class="col-md-4 control-label">Swift Number</label>
											<div class="col-md-8">
												<form:input id="bankDetailsSwiftNumber" path="bankDetailsSwiftNumber"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsAccountName" class="col-md-4 control-label">Account Name</label>
											<div class="col-md-8">
												<form:input id="bankDetailsAccountName" path="bankDetailsAccountName"
														cssClass="form-control" />
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsAccountNumber" class="col-md-4 control-label">Account Number</label>
											<div class="col-md-8">
												<form:input id="bankDetailsAccountNumber" path="bankDetailsAccountNumber"
														cssClass="form-control" />
											</div>
										</div>
										<div class="form-group">
											<label for="bankDetailsReference" class="col-md-4 control-label">Reference</label>
											<div class="col-md-8">
												<form:input id="bankDetailsReference" path="bankDetailsReference"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="declarationsAi" class="col-md-4 control-label">Is Accredited Investor</label>
											<div class="col-md-1 pull-left">
												<form:checkbox class="m-t-10" path="declarationsAi" id="declarationsAi"/>
											</div>
										</div>
										<div class="form-group">
											<label for="declarationsPep" class="col-md-4 control-label">Not PEP</label>
											<div class="col-md-1 pull-left">
												<form:checkbox class="m-t-10" path="declarationsPep" id="declarationsPep"/>
											</div>
										</div>
										<div class="form-group">
											<label for="declarationsCrc" class="col-md-4 control-label">Not CRC</label>
											<div class="col-md-1 pull-left">
												<form:checkbox class="m-t-10" path="declarationsCrc" id="declarationsCrc"/>
											</div>
										</div>
										<div class="form-group">
                                            <label for="taxCrine" class="col-md-4 control-label">Not Involved in Tax Crime</label>
                                            <div class="col-md-1 pull-left">
                                                <form:checkbox class="m-t-10" path="declarationsTaxCrime" id="declarationsTaxCrime"/>
                                            </div>
                                        </div>
										<div class="form-group">
											<label for="declarationsUsCitizen" class="col-md-4 control-label">US Citizen</label>
											<div class="col-md-1 pull-left">
												<form:checkbox class="m-t-10" path="declarationsUsCitizen" id="declarationsUsCitizen"/>
											</div>
										</div>
										<div class="form-group">
											<label for="declarationsSourceOfIncome" class="col-md-4 control-label">Source Of Income</label>
											<div class="col-md-8">
												<form:input id="declarationsSourceOfIncome" path="declarationsSourceOfIncome"
														cssClass="form-control"/>
											</div>
										</div>
										<div class="form-group">
											<label for="signature" class="col-md-4 control-label">Upload Signature</label>
											<div class="col-md-8">
													<span class="btn btn-white fileinput-button showAddFile hide"> <i class="fa fa-plus"></i> <span>Add file...</span>
					                           			<input id="signature" class="userFile signatureFileName" type="file" name="multipartFiles" accept="application/pdf,image/jpeg" />
						                       		</span>
						                       		<a href="javascript:;" id="filename" style="display: none;"class="btn btn-success showAddFile hide">Invalid File Type</a>
						                       		<div style="clear:both;"></div>
													<div class="m-t-5 showAddFile hide">
														<span class="label label-info">Supported File
															Type " .jpg / .jpeg / .png / .pdf "</span> </div>
													<a href="${appContextName}/admin/user/downloadfile?fileName=${model.declarationsSignatureFileName}&view=" class="hideDownloadDelete hide" data-lightbox="gallery-group-1" >
						                            <c:choose>
						                            	<c:when test="${fn:containsIgnoreCase(model.declarationsSignatureFileName,'.pdf')}">
							                            	<img src="${assetsBase}/img/robo_pdf.jpg" class="hideDownloadDelete col-md-4">
						                            	</c:when>
						                            	<c:otherwise>
						                            		<img src="${appContextName}/admin/user/downloadfile?fileName=${model.declarationsSignatureFileName}" style="width: 50%"/>
					    	                        	</c:otherwise>
					                            	</c:choose>
						                         	</a>
			                       			
										</div>
										</div>
										<div class="form-group">
											<label class="col-md-4 control-label"></label>
											<div class="col-md-8">
					                        	<a href="${appContextName}/admin/user/downloadfile?fileName=${model.declarationsSignatureFileName}"
				                        	    class="btn btn-default dropdown-toggle hideDownloadDelete hide" aria-expanded="false" target="_blank"><i class="fa fa-download"></i>&nbsp; Download</a>
												<button type="button" class="btn btn-primary deleteFile hideDownloadDelete hide" value="fileName=${model.declarationsSignatureFileName}&userId=${model.id}"><i class="fa fa-trash"></i>&nbsp; Delete</button>
											</div>
			                         	</div>
										<div class="form-group">
                                            <label for="bankDetailsStatus" class="col-md-4 control-label">Bank Details Status</label>
                                            <div class="col-md-8">
                                                <form:select id="bankDetailsStatus" path="bankDetailsStatus" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="bankDetailsStatus">
                                                    <form:options path="bankDetailsStatus" items="${bankDetailStatus}" itemLabel="label" itemValue="value"/>
                                                </form:select>
                                        </div>
									</div>
                                     <div class="form-group">
                                        <label for="bankDetailsRemarks" class="col-md-4 control-label">Description</label>
                                        <div class="col-md-8">
                                           <form:textarea id="bankDetailsRemarks" placeholder="Remarks" path="bankDetailsRemarks" cssClass="form-control"/>
                                        </div>
                                     </div>
								</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
				<div class="panel panel-inverse" id="termsAndConditions">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            Terms &amp; Conditions
                        </h3>
                    </div>
                    <div class="panel" id="termsAndConditionsPanel">
                        <div class="panel-body">
                            <fieldset>
                                <div class="row">
                                    <div class="col-sm-8 pull-left">
                                        <div class="form-group">
                                            <label for="agreementUserAgreement" class="col-md-4 control-label">Agree with terms?</label>
                                            <div class="col-md-1 pull-left">
                                                <form:checkbox class="m-t-10" path="agreementUserAgreement" id="agreementUserAgreement"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="agreementUserAgreementAcknowledged" class="col-md-4 control-label">Acknowledge Recommendation</label>
                                            <div class="col-md-1 pull-left">
                                                <form:checkbox class="m-t-10" path="agreementUserAgreementAcknowledged" id="agreementUserAgreementAcknowledged"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
			<div class="panel panel-inverse" id="remittances">
				<div class="panel-heading">
					<h3 class="panel-title">Remittance</h3>
				</div>
				<div class="panel-body">
					<c:choose>
						<c:when test="${remittanceList.size() > 0}">
							<table class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Portfolio Name</th>
										<th>Remittance Reference</th>
										<th>Funding Status</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach items="${remittanceList}" var="remittance"
										varStatus="status">
										<tr>
											<td class="col-md-3"><a
												href="${appContextName}/admin/portfolio/update?id=${remittance.userPortfolio.portfolio.id}"
												target="_blank">${remittance.userPortfolio.portfolio.name }</a></td>
											<td class="col-md-3">${remittance.referenceNo }</td>
											<td class="col-md-3">${remittance.brokerFundingStatus.label }</td>
											<td class="action col-md-3"><a
												href="${appContextName}/admin/remittance/update?id=${remittance.id}"
												class="btn btn-primary" target="_blank"> <i
													class="fa fa-pencil-square-o"></i>&nbsp;Edit
											</a></td>
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
			<div class="panel panel-inverse" id="redemptions">
				<div class="panel-heading">
					<h3 class="panel-title">Redemption</h3>
				</div>
				<div class="panel-body">
					<c:choose>
						<c:when test="${redemptionList.size() > 0}">
							<table class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Portfolio Name</th>
										<th>Amount</th>
										<th>Date</th>
										<th>Status</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${redemptionList}" var="redemption"
										varStatus="status">
										<tr>
											<td class="col-md-3"><a
												href="${appContextName}/admin/portfolio/update?id=${redemption.userPortfolio.portfolio.id}"
												target="_blank">${redemption.userPortfolio.portfolio.name }</a></td>
											<td class="col-md-3"><fmt:formatNumber minFractionDigits="3" value="${redemption.redemptionAmount}" /></td>
											<td class="col-md-2">${redemption.redemptionDate }</td>
											<td class="col-md-2">${redemption.redemptionStatus.label }</td>
											<td class="col-md-2"><a
												href="${appContextName}/admin/redemption/update?id=${redemption.id}"
												class="btn btn-primary" target="_blank"> <i
													class="fa fa-pencil-square-o"></i>&nbsp;Edit
											</a></td>
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
			<c:choose>
				<c:when test="${userPortfolioList.size() > 0}">
					<c:forEach items="${userPortfolioList}" var="userPortfolio"
						varStatus="list">
						<div class="panel panel-inverse ">
							<div class="panel-heading">
								<h4 class="panel-title">
									Portfolio : ${userPortfolio.portfolio.name} <span
										class="pull-right">Created On :
										${userPortfolio.createdOn	.toLocalDate()}</span>
								</h4>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="form-group">
										<span class="col-md-1">&nbsp;</span><span class="col-md-3">Balance
											: <fmt:formatNumber minFractionDigits="3"
												value="${userPortfolio.totalUninvestedAmount}" />
										</span> <span class="col-md-3">Amount Invested : <fmt:formatNumber
												minFractionDigits="3"
												value="${userPortfolio.netInvestmentAmount}" /></span> <span
											class="col-md-5">Status :
											${userPortfolio.executionStatus.label}</span>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<span class="col-md-1">&nbsp;</span><span class="col-md-3">
											UnRealised Pnl : <fmt:formatNumber minFractionDigits="3"
												value="${userPortfolio.unrealisedPnl}" />
										</span> <span class="col-md-3"> Net Asset Value : <fmt:formatNumber
												minFractionDigits="3" value="${userPortfolio.netAssetValue}" />
										</span> <span class="col-md-5"> Realised Pnl : <fmt:formatNumber
												minFractionDigits="3" value="${userPortfolio.realisedPnl}" />
										</span>
									</div>
								</div>
								<c:choose>
									<c:when test="${userPortfolio.userTradeList.size() > 0}">
										<div class="table-responsive">
											<table class="table table-hover table-bordered">
												<thead>
													<tr>
														<th>SN</th>
														<th>Instrument</th>
														<th>Trade Status</th>
														<th>Allocation</th>
														<th>Units</th>
														<th>Entered Price</th>
														<th>Current Price</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${userPortfolio.userTradeList}"
														var="trade" varStatus="list">
														<tr>
															<td>${list.count}</td>
															<td><a href="${appContextName}/admin/instrument/update?id=${trade.portfolioInstrument.instrument.id}" target="_blank">${trade.portfolioInstrument.instrument.name}</a></td>
															<td>${trade.executionStatus.label}</td>
															<td><fmt:formatNumber minFractionDigits="3"
																	value="${trade.allocatedAmount}" /></td>
															<td><fmt:formatNumber minFractionDigits="3"
																	value="${trade.enteredUnits}" /></td>
															<td><fmt:formatNumber minFractionDigits="3"
																	value="${trade.enteredPrice}" /></td>
															<td><fmt:formatNumber minFractionDigits="3"
																	value="${trade.portfolioInstrument.instrument.currentPrice}" />
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:when>
								</c:choose>
							</div>
						</div>
					</c:forEach>
				</c:when>
			</c:choose>
			<div class="panel panel-inverse" id="userSubmissions">
				<div class="panel-heading">
					<h3 class="panel-title">Submissions</h3>
				</div>
				<div class="panel-body">
					<c:choose>
						<c:when test="${userSubmissions.size() > 0}">
							<table class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>SN</th>
										<th>Created On</th>
										<th>Status</th>
										<th>Type</th>
										<th>Description</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userSubmissions}" var="submission"
										varStatus="status">
										<tr>
											<td>${status.count}</td>
											<td class="col-md-2"><javatime:format value="${submission.createdOn}" pattern="M/dd/yy hh:mm a"></javatime:format></td>
											<td class="col-md-1">${submission.status }</td>
											<td class="col-md-1">${submission.type.label }</td>
											<td class="col-md-8">${submission.description }</td>
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
										<button type="submit" class="btn btn-primary">Save</button>
										<c:if test="${not empty param['id']}">
											<a class="btn btn-inverse deleteButton" data-id="${model.id}">
												<i class="fa fa-trash"></i>&nbsp;Delete
											</a>
										</c:if>
										<a href="${appContextName}/admin/user/list"
											class="btn btn-primary"> <i class="fa -square-o"></i>Cancel
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
<link rel="stylesheet" type="text/css" href="${resourcesBase}/venders/password-indicator/css/password-indicator.css" />
<script src="${resourcesBase}/venders/password-indicator/js/password-indicator.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
    	$('.selectpicker').selectpicker();
    	$('#dob').datepicker({
    		format: 'yyyy-mm-dd',
            todayHighlight: true,
            calendarWeeks: true,
        });
    	$('#password').passwordStrength({targetDiv: '#passwordStrength'});
        $('#repassword').passwordStrength({targetDiv: '#rePasswordStrength'});
        showHide('${model.kyc1FileName}','.kyc1FileName');
        showHide('${model.kyc2FileName}','.kyc2FileName');
        showHide('${model.kyc3FileName}','.kyc3FileName');
        showHide('${model.declarationsSignatureFileName}','.signatureFileName');
    });
    function showHide(fileName,className) {
    	if(fileName){
    		$(className).closest('.form-group').next().find('.hideDownloadDelete').removeClass('hide');
    		$(className).closest('.form-group').find('.hideDownloadDelete').removeClass('hide');
    	} else {
    		$(className).closest('.form-group').find('.showAddFile').removeClass('hide');
    	}
    }
    $('input[type=file]').change(function(){
		var filename = $(this).val().split('\\').pop();
		var filenameContainer = (typeof $(this).data("filename-container") === "undefined") ? $(this).parent().parent().parent().find("#filename") : $("#"+$(this).data("filename-container"));
		
		if(($('input[type=file]').attr('class').includes('userFile'))) {
	        var fileExtension = ['jpeg','jpg','png','pdf'];
		    if ($.inArray($(this).val().split('.').pop().toLowerCase(), fileExtension) == -1) {
		    	alert('Invalid File Type')
		    } else {	
			        $(filenameContainer).html(filename);
		            $(filenameContainer).attr('class','btn btn-success');
		            
		            if(($(this).attr('class').includes('kyc1FileName'))){
		    			$('#kycFileName1').val(filename);
		    		}
		    		if(($(this).attr('class').includes('kyc2FileName'))){
		    			$('#kycFileName2').val(filename);
		    		}
		    		if(($(this).attr('class').includes('kyc3FileName'))){
		    			$('#kycFileName3').val(filename);
		    		}
		    		if(($(this).attr('class').includes('signatureFileName'))){
		    			$('#bankSignatureFileName1').val(filename);
		    		}
			    }
		} else {
		        $(filenameContainer).html(filename);
				}
		 $(filenameContainer).removeAttr("style");
	});
    $(".sendCodeToAgent").on("click",function(){
    	debugger
    	var agentCode = $(this).parent().parent().parent().find('#agentCode').val();
    	if(!agentCode){
    		alert('Please Enter Agent Code');
    		return false;
    	}
    	var userId = $('.userId').val();
    	var data = new FormData();
    	data.append("userId", "");
    	data.append("agentCode", "");
       	if (confirm('Confirm send OTP to agent ?')){
       		var url = "${appContextName}/admin/user/notifyagent";
                $.ajax({
                    type: 'POST',
                    url: url,
                    data : 'userId=' + userId + '&agentCode=' + agentCode,
                    dataType: 'json',
                    success: function(data) {
						console.log(data);
						if(data.agentOtp){
							alert('OTP Sent Successfully');		
						}
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert('Unable to Send OTP');
                    }
                });
       	}
       });
    $(".deleteFile").on("click",function(){
       	if (confirm('Confirm Delete?')){
       		var url = "${appContextName}/admin/user/deletefile?"+ $(this).val();
       		$.ajax({
       			type:'GET',
        		url:url,
        		success:function(data){
       				$('#kycFileName1').val(data.kyc1FileName);
       				$('#kycFileName2').val(data.kyc2FileName);
       				$('#kycFileName3').val(data.kyc3FileName);
       				$('#bankSignatureFileName1').val(data.declarationsSignatureFileName);
       				$('#userKYCStatus').val(data.kycStatus.name);
	       		},
        		error: function(jqXHR, textStatus, errorThrown) {
                    alert('Unable to Send OTP');
                }
              });
        	$(this).closest('.form-group').find('.hideDownloadDelete').addClass('hide');
            $(this).closest('.form-group').prev().find('.hideDownloadDelete').addClass('hide');
            $(this).closest('.form-group').prev().find('.showAddFile').removeClass('hide');
       	}
       });
</script>

<%@include file="../footer.jspf" %>
