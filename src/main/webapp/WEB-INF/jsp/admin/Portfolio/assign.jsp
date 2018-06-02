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
		<div class="panel">
			<div class="panel-body">
				<form:form class="form-horizontal" name="mainForm"
					action="${appContextName}/admin/portfolio/${formPostUrl}"
					commandName="${modelName}" method="post" role="form"
					data-parsley-validate="true">
					<!-- Title -->
					<div class="row">
						<div class="col-sm-12">
							<h4>Assigned Users</h4>
						</div>
					</div>
					<!-- Assigned User table -->
					<div class="row">
						<div class="col-sm-12">
							<table id="assignedUserTable"
								class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Name</th>
										<th>E-Mail</th>
										<th>Phone No.</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="assignedUser" items="${list.results}"
										varStatus="status">
										<tr class="assignedUserRow">
											<td class="name">${assignedUser.firstName}&nbsp;${assignedUser.lastName}</td>
											<td class="email"><input type="hidden" id="assignedUserId" name="list.results[${status.index}].id" />
												${assignedUser.email}</td>
											<td class="phone">${assignedUser.mobileNumber}</td>
											<td class="action"><a href="#" data-id="${assignedUser.id}"><i
													class="fa fa-trash fa-2x text-danger removeRow"
													aria-hidden="true"></i></a></td>
										</tr>
									</c:forEach>
									<tr
										class="assignedUserNoneRow <c:if test="${not empty assignedUsers.results}">cpl-hidden-el</c:if>">
										<td colspan="4">No users has been assigned yet.</td>
									</tr>
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
					<!-- Title -->
					<div class="row">
						<div class="col-sm-12">
							<h4>Available Users</h4>
						</div>
					</div>
					<div class="row" style="margin-bottom: 10px">
						<div class="form-group" id="inputPanel">
							<label class="control-label col-md-1"><h5>Search :</h5></label>
							<div class="col-md-3">
								<input type="text" id="nameInput" class="form-control input"
									placeholder="Name" data-parsley-required="true" />
							</div>
							<div class="col-md-3">
								<input type="text" id="emailInput" class="form-control input"
									placeholder="Email" data-parsley-required="true" />
							</div>
							<div class="col-md-3">
								<input type="text" id="phoneInput" class="form-control input"
									placeholder="Phone No." data-parsley-required="true" />
							</div>
							<div class="col-md-2">
								<a class="btn btn-primary btn-md"><i
									class="fa fa-search fa-1x"></i>&nbsp; Search</a>
							</div>
						</div>
					</div>
					<div class="row"></div>
					<!-- Assign User table -->
					<div class="row">
						<div class="col-sm-12">
							<table id="table" class="table table-hover table-bordered">
								<thead>
									<tr>
										<th>Assign</th>
										<th>Name</th>
										<th>E-Mail</th>
										<th>Phone No.</th>
									</tr>
								</thead>
								<tbody class="_body">
									<c:forEach var="user" items="${allUsers.results}"
										varStatus="status">
										<tr class="assignUserRow">
											<td class="action"><a href="#" data-id="${user.id}"><i
													class="fa fa-plus fa-2x text-success addUser"
													aria-hidden="true"></i></a></td>
											<td class="userName"><input type="hidden" id="userId"
												name="allUsers.results['${status.index}'].id"
												value="allUsers.results['${status.index}'].id" />
												${user.firstName}&nbsp;${user.lastName}</td>
											<td class="userEmail">${user.email}</td>
											<td class="userPhone">${user.mobileNumber}</td>
										</tr>
									</c:forEach>
									<tr class="assignUserNoneRow <c:if test="${not empty allUsers.results}">cpl-hidden-el</c:if>">
										<td colspan="4">No users found</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="row">
						<misc:printPagination urlPageVar="requestPage"
							pagingDtoPageVar="allUsers"
							ulClass="pagination pull-right m-t-5 m-b-5 m-l-0 m-r-0"
							printMode="both"></misc:printPagination>
					</div>
					<div class="row">
						<div class="col-md-offset-5" style="margin-bottom: 10px">
							<a href="${appContextName}/admin/portfolio/assign"
								class="btn btn-primary btn-sm">Assign Portfolio</a>
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