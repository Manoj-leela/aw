<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@page import="java.util.*"%>
<%@page import="sg.activewealth.roboadvisor.common.model.User"%>
<%@include file="../header.jspf"%>

<!-- begin #content -->
<div id="content" class="content">
	<!-- begin page-header -->
	<h1 class="page-header">Assign Portfolio</h1>
	<!-- end page-header -->

	<!-- begin row -->
	<div class="row">
		${list}
		<div class="panel">
			<div class="panel-body">
				<form:form class="form-horizontal" name="mainForm"
					action="${appContextName}/admin/userPortfolio/${formPostUrl}"
					commandName="${modelName}" method="post" role="form"
					data-parsley-validate="true">
					<!-- Title -->
					<div class="row">
						<div class="col-sm-12">
							<h4>Assign Users</h4>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<h5>Portfolio</h5>
						</div>
						<div class="col-sm-10">
							<input type="hidden" name="portfolio" value="${portfolio.id}"/>
							<p>${portfolio.name}</p>
						</div>
					</div>
					<!-- Assigned User table -->
					<div class="row" style="margin-bottom: 10px">
						<div class="form-group" id="inputPanel">
							<label class="control-label col-md-2"><h5>User :</h5></label>
							<div class="col-md-10">
								<select id="user" class="form-control selectpicker"
									data-parsley-required="true" data-live-search="true"
									data-style="btn-white" name ="user" >
									<option value="-1">-- Name --</option>
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.firstName}&nbsp;
											${user.lastName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-offset-5">
							<button type="submit" class="btn btn-primary btn-md">Assign</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<!-- end row -->
</div>
<!-- end #content -->

<script type="text/javascript">
$(document).ready(function() {
	$('.addUser').bind('click', function() {
		addUser();
		removeUser(this);
	});
	$('.removeRow').bind('click', function() {
		removeUser(this);
	});
});
<c:choose>
	<c:when test="${empty list.results}">
		var assignedUserStartingIndex = -1;
	</c:when>
	<c:otherwise>
		var assignedUserStartingIndex = ${list.results.size()-1};
		$('.assignedUserNoneRow').hide();
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty allUsers.results}">
		var allUserStartingIndex = -1;
	</c:when>
	<c:otherwise>
		var  allUserStartingIndex = ${allUsers.results.size()-1};
		$('.assignUserNoneRow').hide();
	</c:otherwise>
</c:choose>

function addUser() {
	var userId = $("#userId")[0].value;
	var name = $(".userName")[0].textContent;    
	var email = $(".userEmail")[0].textContent;
	var phone = $(".userPhone")[0].textContent;
	var regExp1 = new RegExp("\\[0\\]","g"); //replace [0]
	assignedUserStartingIndex++;
	var toReplace1 = "[" + assignedUserStartingIndex + "]";
	var table = $("#assignedUserTable")[0];
	var entryTemplate = '<tr class="assignedUserRow">'+
		'<input type="hidden" name="list.results[0].id" value="'+ userId +'" />' +
		'<td class="name">'+ name +"</td>"+
		'<td class="email">'+ email +"</td>"+
		'<td class="phone">'+ phone +"</td>"+
		'<td class="action"><a href="#" data-id="${model.id}">'+
		'<i class="fa fa-trash fa-2x text-danger deleteButton'+
			'aria-hidden="true"></i></a></td></tr>';
	
	var moduleEntry = entryTemplate.replace(regExp1,toReplace1);
	var row = table.insertRow(-1);
	row.innerHTML = moduleEntry;
	row.cells[0].innerHTML = name;
	row.cells[1].innerHTML = email;
	row.cells[2].innerHTML = phone;
	
	showNoneRow();
}
function removeUser(row){
	 $(row).closest ('tr.assignUserRow').remove ();
	 showNoneRow();
}

function showNoneRow(){
	var allUser = $("#table > tbody > tr.assignUserRow").length;
	<c:if test="${allUser > 0}">
		$('.assignedUserNoneRow').hide();
	</c:if>
	var user = $("#assignedUserTable > tbody > tr.assignedUserRow").length;
	<c:if test="${user > 0}">
		$('.assignUserNoneRow').hide();
	</c:if>
}
</script>
<%@include file="../footer.jspf"%>