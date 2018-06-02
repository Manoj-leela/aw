function successMessage(message) {
	$.gritter.add({
		title : message,
		class_name : 'gritter-center',
		time : '2000'
	})
}

function errorMessage(data) {
	var errors = [];
	$.each(data, function(index, data) {
		if (data != false) {
			errors.push(data);
		}
	});

	$.gritter.add({
		title : 'Error Occured',
		class_name : 'gritter-info',
		sticky : 'false',
		text : errors.join(" ")
	})
}

function save(obj, url, success, error) {
	var $fields = $(obj).closest('tr').find('input, select, textarea');
	var modelId = $(obj).closest('tr').find('input[name=modelId]').val();
	var formData = {};
	$fields.each(function(index, field) {
		if (field.name != '' && field.name != 'modelId') {
			formData[field.name] = field.value;
		}
	});
	url = url+modelId;
	console.log(url+modelId);
	$.ajax({
		type : "PUT",
		url : url,
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(formData),
	}).success(function(data) {
		if (data.success) {
			successMessage(data.message);
			if(success) {
				success();
			}
		} else {
			errorMessage(data);
			if(error) {
				error();
			}
		}
	}).error(function() {
		error();
		alert("Some thing went wrong");
	});
}
var clickSaveButton =  function(url){
	$(document).on('click', '.SaveButton:not(.clicked)', function() {
		save($(this), url);
	});
}

function enableField(obj) {
    var fields = $(obj).closest('tr').find('input, select');
    fields.each(function(index) {
    	$(this).removeAttr('disabled');
    });
}

function timerclick(saveButtons, url) {
	   var saveButton = saveButtons.shift();
	   save(saveButton, url,  function() {
	        enableField(saveButton);
	        debugger;
	        if (saveButtons.length > 0) {
	            setTimeout(function() {
	                timerclick(saveButtons, url);
	            }, 1000);
	        }
	   }, function() {
	        enableField(saveButton);
	        saveButtons.forEach(function(saveButton){
	        	enableField(saveButton);
	        });
	   });
	}
