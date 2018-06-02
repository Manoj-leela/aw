<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header.jspf"%>
<!-- begin row -->
<div class="row">
<!-- begin col-12 -->
    <div class="col-md-12">
		<div class="panel panel-inverse">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Portfolio Assignment
                </h3>
            </div>
			<div class="panel">
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-8 pull-left">
						<form:form class="form-horizontal" name="mainForm"
							action="${appContextName}/admin/userPortfolio/assign"
							commandName="${modelName}" method="post" role="form"
							data-parsley-validate="true">

							<fieldset>
								<div class="form-group">
									<label for="name" class="col-md-3 control-label">Name* :</label>
									<div class="col-md-9">
										<input class="form-control" type="text" id="portfolioName" name="portfolioName" value="${newPortfolio.name}" readonly />
										<input  type="hidden" id="newPortfolioId" name="newPortfolioId" value="${newPortfolio.id}" />
									</div>
								</div>
								<div class="form-group">
									<label for="description" class="col-md-3 control-label">Description :</label>
									<div class="col-md-9">
										<input class="form-control" type="text" id="description" name="description" value="${newPortfolio.description}" readonly />
									</div>
								</div>
								<div class="form-group">
									 <label class="col-md-3 control-label">Action:</label>
									 <div class="col-md-9">
									 	<div class="col-md-12">
											<label class="radio-inline"><input type="radio" id="AssignReplace" name="assignType" value="AssignReplace"/> Assign Replace </label>
											<label class="radio-inline"><input type="radio" id="Close" name="assignType" value="Close"/> Close</label>
										</div>	
									 </div>
								</div>
                                <div id="portfolioList" class="form-group">
                                    <label for="name" class="col-md-3 control-label">Portfolio </label>
                                    <div class="col-md-9">
                                    <select id="portfolioId" name="portfolioId"
                                        class="form-control selectpicker" data-size="10" data-style="btn-white" data-live-search="true">
                                        <c:forEach items="${portfolioList}" var="item">
                                            <option value="${item.id}" <c:if test="${not empty param.portfolioId and param.portfolioId eq item.id}">selected</c:if>>${item.name}</option>
                                        </c:forEach>
                                    </select>
                                    </div>
                                </div>

								<div class="form-group">
									<label for="name" class="col-md-3 control-label">Email Address </label>
									<div class="col-md-7">
										<input class="form-control" data-parsley-type="email" type="text" id="emailAddress" name="email" value="<c:if test="${not empty email}">${email}</c:if>" />
									</div>
									<div class="col-md-2">
										<button id="btnSearch" type="button" class="btn btn-primary">Search</button>
									</div>
								</div>
								<div
									class="form-group">
									<label for="name" class="col-md-3 control-label"> </label>
									<div class="col-md-9">
										<table id="table" class="table table-hover table-bordered">
											<thead>
												<tr>
													<th><input id="selectAll" type="checkbox" data-toggle='tooltip' data-original-title="Select All"/></th>
													<th>Portfolio Name</th>
													<th>First Name</th>
													<th>Last Name</th>
													<th>Email</th>
													<th>Status</th>
												</tr>
											</thead>
											<tbody class="_body" id="userPortfolios">
											<c:forEach items="${userPortfolios}" var="userPortfolio">
	                                			<tr>
													<td><input class="checkbox" type="checkbox" name="userPortfolio" value="${userPortfolio.id}"></td>
													<td>${userPortfolio.portfolio.name}</td>
													<td>${userPortfolio.user.firstName}</td>
													<td>${userPortfolio.user.lastName}</td>
													<td>${userPortfolio.user.email}</td>
													<td>${userPortfolio.user.status.label}</td>
												</tr>
	                                		</c:forEach>
												
											</tbody>
										</table>
									</div>
								</div>
								<div class="form-group">
									<label for="type" class="col-md-4 control-label"></label>
									<div class="col-md-8">
										<button type="submit" class="btn btn-primary">Save</button>
										 <a href="${appContextName}/admin/portfolio/list"  
												class="btn btn-primary">
					                                <i class="fa -square-o"></i>Cancel
					                    </a> 
									</div>
								</div>
							</fieldset>
						</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- end row -->
<script type="text/javascript">
$(document).ready(function() {
	var initState = { "assignType": "Assign" };
	renderPage(initState);
    // Initialize select picker
	$('.selectpicker').selectpicker();
	
	loadSavedValue();
	var actionbutton = $("input[name='assignType']:checked");
    $('#Assign,#AssignLater,#AssignReplace,#Close').change(function() {
    	updatePage();
	});
    $("#profileSelect").change(function(){
    	console.log("ProfileSelect:"+$("#profileSelect").val());
    	updatePage();
    });
    $("#portfolioId").change(function(){
    	updatePage();
    });
    $('#btnSearch').click(function() {
    	updatePage("op=Search");
    });
    
    // Select All checkboxes from the list
    $('#selectAll').change(function(){  //"select all" change 
        var status = this.checked; // "select all" checked status
        $('.checkbox').each(function(){ //iterate all listed checkbox items
            this.checked = status; //change ".checkbox" checked status
        });
    });
    
    // Handles Select All by click on checkboxes
    $('.checkbox').change(function(){ //".checkbox" change 
        //uncheck "select all", if one of the listed checkbox item is unchecked
        if(this.checked == false){ //if this item is unchecked
            $("#selectAll")[0].checked = false; //change "select all" checked status to false
        }
        //check "select all" if all checkbox items are checked
        if ($('.checkbox:checked').length == $('.checkbox').length ){ 
            $("#selectAll")[0].checked = true; //change "select all" checked status to true
        }
    });
});

function loadSavedValue(){
	var urlElm = getUrlVars();
	console.log("getUrlVars():"+getUrlVars());
	var assign = urlElm['assign'];
	if(assign != undefined){
		$("#"+assign).prop('checked', true);
	}
	
	var profileSelect = urlElm['profile'];
	if(profileSelect != undefined){
		$("#profileSelect").val(profileSelect);
	}
	
	var portfolioId = urlElm['portfolioId'];
	if(portfolioId != undefined){
		$("#portfolioId").val(portfolioId);
	}
}

function updatePage(params) {
	var url = window.location;
	url = refineUrl();
	url = "?newPortfolioId="+$("#newPortfolioId").val();
	if(params) {
		url += "&op=search";
	}
	var profile = $("#profileSelect").val();
	if(profile) {
		url = url + '&profile='+profile;
	}	
	var emailAddress = $("#emailAddress").val();
	if(emailAddress){
		url = url + '&email='+emailAddress;
	}
	var assign = $("input[name='assignType']:checked").val();
	if(assign){
		url = url + '&assign='+assign;
	}
	var portfolioId = $("#portfolioId").val();
	console.log("portfolioId:"+portfolioId);
	if(portfolioId){
		url = url + '&portfolioId='+portfolioId;
	}

	$.get("assign/query" + url, function(data) {
        renderPage(data)
	})
}

function renderPage(data) {
    //let selectPortfolio = $("select#portfolioId");
    //selectPortfolio.empty();
   /* data['portfolioList'] && data['portfolioList'].forEach(function(item) {
        selectPortfolio.empty().append($('<option></option>').val(item.id).html(item.name));
    });
    selectPortfolio.selectpicker('refresh');*/

    data['userPortfolios'] && $("tbody#userPortfolios").empty() && data['userPortfolios'].forEach(function(userPortfolio) {
        let tr = $("<tr></tr>");
        [
            $('<td></td>').append($('<input class="checkbox" type="checkbox" name="userPortfolio"/>').val(userPortfolio.id)),
            $('<td></td>').html(userPortfolio.portfolioName),
            $('<td></td>').html(userPortfolio.userFirstName),
            $('<td></td>').html(userPortfolio.userLastName),
            $('<td></td>').html(userPortfolio.userEmailAddress),
            $('<td></td>').html(userPortfolio.portfolioStatus),
        ].forEach(function(td) {
            tr.append(td);
        });
        $("tbody#userPortfolios").append(tr);
    });
}

function refineUrl() {
    //get full url
    var url = window.location.href;
    //get url after/  
    var value = url.substring(url.lastIndexOf('/') + 1);
    //get the part after before ?
    value  = value.split("?")[0];   
    return value;     
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,    
    function(m,key,value) {
      vars[key] = value;
    });
    return vars;
  }
</script>
<%@include file="../footer.jspf"%>