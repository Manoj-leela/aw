<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="../header_base.jspf"%>
</body>
<body class="_body" style="background: white;">
	<!-- begin row -->
	<div class="row">
		<!-- begin col-12 -->
		<div class="col-md-12">
			<div class="panel panel-inverse">
				<div class="panel-heading">
					<h3 class="panel-title">User Payment Transaction</h3>
				</div>
				<div class="panel">
					<div class="panel-body">
						<div class="row">
							<span class="pull-right p-r-10"> <a
								href="${appContextName}/admin/userPayment/initiatePayment/readyPayment"
								class="btn btn-primary" target="_parent"> <i
									class="fa fa-bolt" aria-hidden="true"></i>&nbsp;Execute Ready
									Payment(s)
							</a>
							</span> <span class="pull-right p-r-10"> <a
								href="${appContextName}/admin/userPayment/initiatePayment/paymentError"
								class="btn btn-primary" target="_parent"> <i
									class="fa fa-repeat" aria-hidden="true"></i>&nbsp;Retry Error
									Payment(s)
							</a>
							</span>
						</div>
						&nbsp;
						<div class="row">
							<div class="col-sm-12">
								<table id="table" class="table table-hover table-bordered">
									<thead>
										<tr>
											<th>#</th>
											<th>Username</th>
											<th>Amount</th>
											<th>Status</th>
											<th>Created On</th>
										</tr>
									</thead>
									<tbody class="_body">
										<c:forEach var="model" items="${list.results}"
											varStatus="status">
											<tr>
												<td>${serialCount+status.index +1}.</td>
												<td>${model.userPayment.user.firstName}</td>
												<td>${model.userPayment.amount}</td>
												<td>${model.status.label}</td>
												<td>${model.createdOn.toLocalDate()}
													${model.createdOn.toLocalTime()}</td>
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
	<!-- end row -->
	<%@include file="../footer.jspf"%>