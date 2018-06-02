<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="../header.jspf" %>
<%@taglib prefix="misc" uri="/roboadvisor-misc"%>

<c:set var="isFormsPage" value="true" />


<div class="row">
<!-- begin col-12 -->
	<div class="col-md-12">
		<form:form class="form-horizontal" name="mainForm"
            commandName="${modelName}"  role="form"
            data-parsley-validate="true" target="_blank">
            <c:set var="alertMessages">
				<form:errors path="*" cssClass="_spring_errors" />
			</c:set>
			<%@include file="../alert.jspf"%>
    		<div class="panel panel-inverse">
        		<div class="panel-heading">
             		<h3 class="panel-title">
                		Report
             		</h3>
         		</div>
		    	<div class="panel">
		        	<div class="panel-body">
		            	<div class="row">
		                	<div class="col-md-6 pull-left">
	                        	<fieldset>
	                        		<div class="form-group">
		                                <label for="framework" class="col-md-3 control-label">Report Type *</label>
		                                <div class="col-md-9">

		                                     <form:select id="reportType" path="reportType" data-parsley-required="true" class="form-control selectpicker" data-size="auto" data-live-search="true" data-style="btn-white" name="">
		                                        <form:option value="">- Selected -</form:option>
		                                        <form:options path="reportType" items="${reports}" itemLabel="label" itemValue="value"/>
		                                    </form:select>
		                                </div>
		                            </div>
                        		</fieldset>
		                	</div>
		                	<div class="m-r-10">
								<a id="btnGenerate" href="#" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;Generate</a>
							</div>
		           		</div>
		        	</div>
		    	</div>
    		</div>
   		</form:form>
       </div>
      </div> 		

<script type="text/javascript">
$(document).ready(function() {
	$('.selectpicker').selectpicker();
	
	$('#btnGenerate').click(function() {
			var reportClass = $('#reportType').find("option:selected").val();
			if(reportClass==''){
				$(this).attr("href", '#');
				return;
			}
			var app = '${appContextName}/admin';
		 	$(this).attr("href", app+'/'+reportClass);
		 	
		 	// reset the value.
		 	$('#reportType').selectpicker('val',''); 
		 	$('.selectpicker').selectpicker('refresh');
		 	
		});
});
	
</script>
<%@include file="../footer.jspf" %>