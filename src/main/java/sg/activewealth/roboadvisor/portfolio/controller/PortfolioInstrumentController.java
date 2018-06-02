package sg.activewealth.roboadvisor.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.portfolio.model.PortfolioInstrument;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioInstrumentService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/portfolioInstrument")
public class PortfolioInstrumentController extends CRUDController<PortfolioInstrument, PortfolioInstrumentService> {

	@Autowired
	public void setService(PortfolioInstrumentService service) {
		this.service = service;
	}

	@Autowired
	public PortfolioInstrumentController(PortfolioInstrumentService service) {
		super(PortfolioInstrument.class, service);
	}

	public PortfolioInstrumentController(Class<PortfolioInstrument> portfolioInstrument,
			PortfolioInstrumentService service) {
		super(portfolioInstrument, service);
	}

	@Override
	public Object redirect(final PortfolioInstrument model, final HttpServletRequest request) {
		return "redirect:/r/admin/portfolio/list";
	}
}
