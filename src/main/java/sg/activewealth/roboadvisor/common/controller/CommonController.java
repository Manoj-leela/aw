package sg.activewealth.roboadvisor.common.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.helper.IAttachmentableHelper;

@Controller
public class CommonController extends AbstractController {

	@Autowired
	IAttachmentableHelper attachmentHelper;

	@RequestMapping("/admin")
	public Object empty(HttpServletRequest request) {
		return new ModelAndView("admin/index");
	}

	@RequestMapping("/admin/triggers/list")
	public Object redirectTriggers(HttpServletRequest request) {
		return new ModelAndView("admin/triggers");
	}

	@RequestMapping(value = { "/showImage/{id}" }, method = RequestMethod.GET)
	public void showImage(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") String id) throws Exception {
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("image/jpeg"); // @TODO: don't hard code
													// this
			IOUtils.copy(attachmentHelper.retrieveFileInputStream(id), out);
			out.flush();
			out.close();
		} catch (Exception e) {
			// throw new SystemException(e); //ignore if cannot find file on
			// local
		}
	}

	@RequestMapping("admin/transaction/list")
	public Object getPayment() {
		return new ModelAndView("admin/transaction");
	}

}
