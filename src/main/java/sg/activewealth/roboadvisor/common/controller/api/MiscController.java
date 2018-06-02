package sg.activewealth.roboadvisor.common.controller.api;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sg.activewealth.roboadvisor.banking.dto.BankDetailsDto;
import sg.activewealth.roboadvisor.banking.model.BankDetail;
import sg.activewealth.roboadvisor.banking.service.BankDetailService;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.enums.MobilePlatform;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@RestController
@RequestMapping("/api/v1/misc")
public class MiscController extends AbstractController {
	
	@Autowired
	private UserPortfolioService userPortfolioService;

	@Autowired
	private BankDetailService bankDetailService;

	@RequestMapping(value = "/apiVersion", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, String> apiVersion(
			@RequestParam(value = "c") sg.activewealth.roboadvisor.infra.enums.MobilePlatform mobilePlatform,
			@RequestParam(value = "cv") String contextVersion) {
		Map<String, String> ret = new HashMap<String, String>();
		String apiVersions = propertiesHelper.apiVersion;
		String [] versions = apiVersions.split(",");

		for (String version : versions) {
			String[] v = version.split("\\|") ;
			addIfLesser(ret, contextVersion, mobilePlatform, v[0], v[1]);
		}
		return ret;
	}

	private Map<String, String> addIfLesser(Map<String, String> ret, String acv,
			MobilePlatform mobilePlatform, String acvToCompare, String forceUpgrade) {
		if (Double.parseDouble(acv) <= Double.parseDouble(acvToCompare)) {
			String appStoreUrl = "";
			if(mobilePlatform.equals(MobilePlatform.iOS)) {
				appStoreUrl = propertiesHelper.iOSAppUrl;
			} else if(mobilePlatform.equals(MobilePlatform.Android)) {
				appStoreUrl = propertiesHelper.androidAppUrl;
			}
			ret = sg.activewealth.roboadvisor.infra.utils.SystemUtils.getInstance().buildMap(
					ret, "apiVersion", acvToCompare, "buildVersion",
					acvToCompare, "forceUpgrade", forceUpgrade, "appStoreUrl", appStoreUrl);
		}
		return ret;
	}

	@RequestMapping(value = "/staticcontent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getTermsAndCondition() {
		Map<String, Object> ret = new LinkedHashMap<>(1);
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(this.getClass(), "/");

		HashMap<Object, Object> blankMap = new HashMap<Object, Object>();

		StringWriter termsAndconditionWriter = new StringWriter();
		StringWriter accreditedInvestorDeclarationWriter = new StringWriter();
		StringWriter antiMoneyLaunderingWriter = new StringWriter();
		StringWriter cppTacticalIncomefundWriter = new StringWriter();

		Template template = null;
		try {
			template = cfg.getTemplate("email_tpl" + File.separator + "termsAndCondition.html");
			template.process(blankMap, termsAndconditionWriter);

			template = cfg.getTemplate("email_tpl" + File.separator + "accreditedInvestorDeclaration.html");
			template.process(blankMap, accreditedInvestorDeclarationWriter);

			template = cfg.getTemplate("email_tpl" + File.separator + "antiMoneyLaundering.html");
			template.process(blankMap, antiMoneyLaunderingWriter);

			template = cfg.getTemplate("email_tpl" + File.separator + "cpTacticalIncomeFund.html");
			template.process(blankMap, cppTacticalIncomefundWriter);

		} catch (IOException | TemplateException e) {
			logger.error(e.getMessage());
			ret.put("success", Boolean.FALSE);
			ret.put("message", "File not Found");
		}
		ret.put("success", Boolean.TRUE);
		ret.put("termsAndConditions", termsAndconditionWriter.toString());
		ret.put("accreditedInvestorDeclarion", accreditedInvestorDeclarationWriter.toString());
		ret.put("antiMoneyLaundering", antiMoneyLaunderingWriter.toString());
		ret.put("cpTacticalIncomeFund", cppTacticalIncomefundWriter.toString());
		ret.put("instrument_VOO", "Test VOO");
		ret.put("instrument_EZU", "Test EZU");
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/bankdetails/{userPortfolioId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getBankDetails(@PathVariable String userPortfolioId) {
		Map<String, Object> ret = new LinkedHashMap<>(3);
		UserPortfolio userPortfolio = userPortfolioService.retrieve(userPortfolioId);
		if(userPortfolio == null) {
			throw new JsonObjectNotFoundException("User Portfolio id: " + userPortfolioId + " not found.");
		}
		User user = userPortfolio.getUser();
		BankDetail bankDetail = bankDetailService.retrieveByCategory(user.getPortfolioCategory());
		
		if(bankDetail!=null) {
			BankDetailsDto bankDetailsDto = new BankDetailsDto();
			bankDetailsDto.setName(bankDetail.getName());
			bankDetailsDto.setAddress(bankDetail.getAddress());
			bankDetailsDto.setAba(bankDetail.getAba());
			bankDetailsDto.setChips(bankDetail.getChips());
			bankDetailsDto.setSwift(bankDetail.getSwift());
			bankDetailsDto.setAccountName(bankDetail.getAccountName());
			bankDetailsDto.setAccountNumber(bankDetail.getAccountNumber());
			bankDetailsDto.setReference(bankDetail.getReference());
			bankDetailsDto.setCharges(bankDetail.getCharges());
			bankDetailsDto.setSwiftSecondaryAddress(bankDetail.getSwiftSecondaryAddress());
			
			ret.put("bankDetails", bankDetailsDto);		
			ret.put("success", Boolean.TRUE);
			ret.put("message", "Bank Details returned");
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

}
