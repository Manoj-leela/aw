/*   
Template Name: Color Admin - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.1
Version: 1.5.0
Author: Sean Ngu
Website: http://www.seantheme.com/color-admin-v1.5/admin/
*/

var handleDataTableDefault = function() {
	"use strict";
    
    if ($('#data-table').length !== 0) {
        $('#data-table').DataTable();
    }
};

var TableManageDefault = function () {
	"use strict";
    return {
        //main function
        init: function () {
            $.getScript('assets/plugins/DataTables/js/jquery.dataTables.js').done(function() {
                handleDataTableDefault();
            });
        }
    };
}();