<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<%@include file="header.jspf"%>

<!-- begin page-header -->
&nbsp;
<div class="row">
	<!-- begin col-12 -->
	<div class="col-md-12">
		<div class="panel panel-inverse">
			<div class="panel-heading">
				<h1 class="panel-title">Transactions</h1>
			</div>
			<!-- end page-header -->
			<div class="row">
				<!-- begin col-12 -->
				<div class="col-md-12">
					<!-- begin panel -->

					<div class="panel panel-inverse">
						<div class="panel-body panel-form">
							<div class="col-md-12">
								<!-- begin row -->
								<br />
								<ul class="pull-right nav nav-pills">
									<li class="active"><a href="#nav-pills-tab-1"
										data-toggle="tab" aria-expanded="true">Portfolio</a></li>
									<li class=""><a href="#nav-pills-tab-3" data-toggle="tab"
										aria-expanded="true">Trade</a></li>

								</ul>

								<div class="tab-content">
									<div class="tab-pane fade active in" id="nav-pills-tab-1">
										<iframe onload="resizeIframe(this)" scrolling="no"
											frameborder="0"
											src="${appContextName}/admin/userPortfolioTransaction/list"></iframe>
									</div>
									<div class="tab-pane fade" id="nav-pills-tab-3">
										<iframe onload="resizeIframe(this)" scrolling="no"
											frameborder="0"
											src="${appContextName}/admin/userTradeTransaction/list"></iframe>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- end row -->
<script type="text/javascript">
   function resizeIframe(obj){
       obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
       obj.style.width = "100%";
   }
</script>

<%@include file="footer.jspf"%>