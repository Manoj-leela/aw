package sg.activewealth.roboadvisor.common.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.portfolio.enums.UserRiskProfile;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;
import sg.activewealth.roboadvisor.portfolio.service.PortfolioService;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioRESTController extends AbstractController{
	
	@Autowired
	PortfolioService portfolioService;

	@RequestMapping(value = "/get/{portfolioId}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Portfolio> getPortfolio(@PathVariable String portfolioId){
		Portfolio portfolio = portfolioService.retrieve(portfolioId);
		if(portfolio == null){
			throw new JsonObjectNotFoundException("PortFolio Id: " + portfolioId+ " not found.");
		}
		portfolio = new Portfolio(portfolio, portfolio.getDescription());
		return new ResponseEntity<>(portfolio,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{riskProfile}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Portfolio> getProfileByRiskprofile(@PathVariable(value="riskProfile") String riskProfile){
		UserRiskProfile userRiskProfile = UserRiskProfile.getByString(riskProfile);
		Portfolio portfolio = new Portfolio();
		if(userRiskProfile == null){
			throw new JsonObjectNotFoundException("User Risk Profile: " + riskProfile + " not found.");
		}else{
			//TODO Need to send the calculated portfolio based on user's answers
			portfolio = portfolioService.retriveByRiskProfile(userRiskProfile,true).get(0);
		}
		portfolio = new Portfolio(portfolio, portfolio.getDescription());
		return new ResponseEntity<>(portfolio,HttpStatus.OK);
	}
}
