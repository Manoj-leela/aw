package sg.activewealth.roboadvisor.banking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.banking.enums.RedemptionStatus;
import sg.activewealth.roboadvisor.banking.model.Redemption;
import sg.activewealth.roboadvisor.banking.service.RedemptionService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.FormOptionDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@Controller
@RequestMapping("/admin/redemption")
public class RedemptionController extends CRUDController<Redemption, RedemptionService> {

    @Autowired
    private UserPortfolioService userPortfolioService;

    @Autowired
    public void setService(RedemptionService service) {
        this.service = service;
    }

    @Autowired
    public RedemptionController(RedemptionService service) {
        super(Redemption.class, service);
    }

    @Override
    public Object[] preCreateUpdateGet(Redemption model, HttpServletRequest request) {

        super.preCreateUpdateGet(model, request);
        Object[] status = { UserPortfolioStatus.Executed };
        PagingDto<UserPortfolio> userPortfolioPagingDto = getUserPortfolioList(status);
        return new Object[] { "userPortfolioList", userPortfolioPagingDto.getResults(), "redemptionStatusList", RedemptionStatus.values() };
    }

    @Override
    public Redemption preCreateUpdatePost(Redemption model, HttpServletRequest request) {
        super.preCreateUpdatePost(model, request);
        UserPortfolio userPortfolio = userPortfolioService.retrieve(model.getUserPortfolio().getId());
        model.setUserPortfolio(userPortfolio);
        return model;
    }

    /*@Override
    public Object redirect(Redemption model, HttpServletRequest request) {
        return "redirect:list?redemptionStatus=RequestedByInvestor";
    }*/

    // old code will confirm to rahul soni to remove this.
    /*@Override
    public Object list(PagingDto<Redemption> pagingDto, String ids, HttpServletRequest request) {
    	String[] queryUserPortfolio = request.getParameterValues("userPortfolio.id");
    	String queryRedemptionDate = request.getParameter("redemptionDate");
    	String[] queryRedemptionStatus = request.getParameterValues("redemptionStatus");
    	LocalDate redemptionDate = null;
    	//TODO can we throw the error when date is incorrect ?
    	if(!ValidationUtils.getInstance().isEmptyString(queryRedemptionDate)){
    		try{
    			redemptionDate = LocalDate.parse(queryRedemptionDate); 
    		} catch (DateTimeParseException e){
    			logger.error(e.getMessage());
    		}
    	}
    	String redemptionAmount = request.getParameter("redemptionAmount");
    	BigDecimal queryRedemptionAmount = null;
    	if(!ValidationUtils.getInstance().isEmptyString(redemptionAmount)){
    		queryRedemptionAmount = new BigDecimal(redemptionAmount);
    	}
    	pagingDto = service.retrieveForListPage(queryUserPortfolio,pagingDto,queryRedemptionAmount,redemptionDate,queryRedemptionStatus);
    	List<UserPortfolio> userPortfolios  = getUserPortfolioList(UserPortfolioStatus.Closed).getResults();
    	
    	List<FormOptionDto> redemptionStatusList = new ArrayList<FormOptionDto>();
    	for (RedemptionStatus redemptionStatus : RedemptionStatus.values()) {
    		redemptionStatusList.add(new FormOptionDto(redemptionStatus.name(), redemptionStatus.getLabel()));
    	}
    	
    	return modelAndView(getFullJspPath("list"), "list", pagingDto, "userPortfolios", userPortfolios,"redemptionStatusList",redemptionStatusList);
    }*/

    @Override
    public Object list(PagingDto<Redemption> pagingDto, String ids, HttpServletRequest request) {
        String[] queryUserPortfolio = request.getParameterValues("userPortfolio.id");
        String queryRedemptionDate = request.getParameter("redemptionDate");
        String queryRedemptionStatus = request.getParameter("redemptionStatus");
        LocalDate redemptionDate = null;
        if (!ValidationUtils.getInstance().isEmptyString(queryRedemptionDate)) {
            try {
                redemptionDate = LocalDate.parse(queryRedemptionDate);
            } catch (DateTimeParseException e) {
                logger.error(e.getMessage());
            }
        }

        String queryRedemptionAmount = request.getParameter("redemptionAmount");
        BigDecimal redemptionAmountVal = null;
        if (!ValidationUtils.getInstance().isEmptyString(queryRedemptionAmount)) {
            try {
                redemptionAmountVal = new BigDecimal(queryRedemptionAmount);
            } catch (NumberFormatException nfe) {
                logger.error(nfe.getMessage());
            }
        }
        pagingDto = service.retrieveForListPage(queryUserPortfolio, pagingDto, redemptionAmountVal, redemptionDate, queryRedemptionStatus);

        Object[] userPortFolioStatuses = { UserPortfolioStatus.Executed, UserPortfolioStatus.CloseRequested, UserPortfolioStatus.ReadyForClose, UserPortfolioStatus.PartiallyClosed, UserPortfolioStatus.Closed };
        List<UserPortfolio> userPortfolios = getUserPortfolioList(userPortFolioStatuses).getResults();

        List<FormOptionDto> redemptionStatusList = new ArrayList<FormOptionDto>();
        for (RedemptionStatus redemptionStatus : RedemptionStatus.values()) {
            redemptionStatusList.add(new FormOptionDto(redemptionStatus.name(), redemptionStatus.getLabel()));
        }

        return modelAndView(getFullJspPath("list"), "list", pagingDto, "userPortfolios", userPortfolios, "redemptionStatusList", redemptionStatusList);
    }

    @RequestMapping(value = "/customlist", method = RequestMethod.GET)
    public Object customList(HttpServletRequest request) {
        String[] queryUserPortfolio = request.getParameterValues("userPortfolio.id");
        String queryRedemptionDate = request.getParameter("redemptionDate");
        String queryRedemptionStatus = request.getParameter("redemptionStatus");
        LocalDate redemptionDate = null;
        if (!ValidationUtils.getInstance().isEmptyString(queryRedemptionDate)) {
            try {
                redemptionDate = LocalDate.parse(queryRedemptionDate);
            } catch (DateTimeParseException e) {
                logger.error(e.getMessage());
            }
        }

        String queryRedemptionAmount = request.getParameter("redemptionAmount");
        BigDecimal redemptionAmountVal = null;
        if (!ValidationUtils.getInstance().isEmptyString(queryRedemptionAmount)) {
            try {
                redemptionAmountVal = new BigDecimal(queryRedemptionAmount);
            } catch (NumberFormatException nfe) {
                logger.error(nfe.getMessage());
            }
        }
        Object[] userPortFolioStatuses = { UserPortfolioStatus.Executed, UserPortfolioStatus.CloseRequested, UserPortfolioStatus.ReadyForClose, UserPortfolioStatus.PartiallyClosed, UserPortfolioStatus.Closed };
        List<UserPortfolio> userPortfolios = getUserPortfolioList(userPortFolioStatuses).getResults();

        List<FormOptionDto> redemptionStatusList = new ArrayList<FormOptionDto>();
        for (RedemptionStatus redemptionStatus : RedemptionStatus.values()) {
            if (!RedemptionStatus.Issues.getLabel().equals(redemptionStatus.getLabel())) {
                redemptionStatusList.add(new FormOptionDto(redemptionStatus.name(), redemptionStatus.getLabel()));
            }
        }
        PagingDto<Redemption> pagingDto = new PagingDto<>();
        pagingDto.setResultsPerPage(99999);
        pagingDto = service.retrieveForListPage(queryUserPortfolio, pagingDto, redemptionAmountVal, redemptionDate, queryRedemptionStatus);

        List<LocalDate> redemptionBatches = service.getRedemptionBatchesDesc(queryUserPortfolio, queryRedemptionStatus);
        
        for(Redemption redemption :pagingDto.getResults()) {
            redemption.setAmountRequestedFromBroker(redemption.getUserPortfolio().getNetAssetValue());
        }
        return modelAndView(getFullJspPath("custom_list"), "list", pagingDto.getResults(), "userPortfolios", userPortfolios, "redemptionStatusList", redemptionStatusList, "redemptionBatches", redemptionBatches);
    }

    @RequestMapping(value = { "/api/update/{id}" }, method = RequestMethod.PUT)
    public @ResponseBody Map<String, Object> updateRedemption(@PathVariable String id, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if (id != null) {
            Redemption redemption = service.retrieve(id);

            BigDecimal amountRequestedFromBroker = null;
            BigDecimal netRedemptionAmount = null;
            BigDecimal amountReceivedFees = null;
            BigDecimal amountReceivedFromBroker = null;
            LocalDate brokerBatch = null;

            if (requestBody.containsKey("redemptionStatus") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("redemptionStatus"))) {
                RedemptionStatus strRedemptionStatus = RedemptionStatus.valueOf(requestBody.get("redemptionStatus"));
                redemption.setRedemptionStatus(strRedemptionStatus);
            }

            if (requestBody.containsKey("amountRequestedFromBroker") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("amountRequestedFromBroker"))) {
                String strAmountRequestedFromBroker = String.valueOf(requestBody.get("amountRequestedFromBroker"));
                if (strAmountRequestedFromBroker != null && !strAmountRequestedFromBroker.isEmpty()) {
                    amountRequestedFromBroker = new BigDecimal(strAmountRequestedFromBroker);
                }
            }
            redemption.setAmountRequestedFromBroker(amountRequestedFromBroker);
            if (requestBody.containsKey("amountReceivedFromBroker") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("amountReceivedFromBroker"))) {
                String strAmountReceivedFromBroker = String.valueOf(requestBody.get("amountReceivedFromBroker"));
                if (strAmountReceivedFromBroker != null && !strAmountReceivedFromBroker.isEmpty()) {
                    amountReceivedFromBroker = new BigDecimal(strAmountReceivedFromBroker);
                }
            }
            redemption.setAmountReceivedFromBroker(amountReceivedFromBroker);
            if (requestBody.containsKey("amountReceivedFees") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("amountReceivedFees"))) {
                String strAmountReceivedFees = String.valueOf(requestBody.get("amountReceivedFees"));
                if (strAmountReceivedFees != null && !strAmountReceivedFees.isEmpty()) {
                    amountReceivedFees = new BigDecimal(strAmountReceivedFees);
                }
            }
            redemption.setAmountReceivedFees(amountReceivedFees);
            if (requestBody.containsKey("netRedemptionAmount") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("netRedemptionAmount"))) {
                String strNetRedemptionAmount = String.valueOf(requestBody.get("netRedemptionAmount"));
                if (strNetRedemptionAmount != null && !strNetRedemptionAmount.isEmpty()) {
                    netRedemptionAmount = new BigDecimal(strNetRedemptionAmount);
                }
            }
            redemption.setNetRedemptionAmount(netRedemptionAmount);
            if (requestBody.containsKey("brokerBatch") && !ValidationUtils.getInstance().isEmptyString(requestBody.get("brokerBatch"))) {
                String strBrokerBatch = String.valueOf(requestBody.get("brokerBatch"));
                if (strBrokerBatch != null && !strBrokerBatch.isEmpty()) {
                    try {
                        brokerBatch = LocalDate.parse(strBrokerBatch);
                    } catch (DateTimeParseException e) {
                        logger.error(e.getMessage());
                    }
                }
                redemption.setBrokerBatch(brokerBatch);
            }

            if (requestBody.containsKey("remarks")) {
                String remarks = String.valueOf(requestBody.get("remarks"));
                redemption.setRemarks(remarks);
            }

            redemption = service.save(redemption);
            map.put("success", "true");
            map.put("message", "Record successfully saved.");
        }
        return map;
    }

    public PagingDto<UserPortfolio> getUserPortfolioList(Object[] userPortFolioStatus) {
        final List<FilterDto> filters = new ArrayList<>();
        PagingDto<UserPortfolio> pagingDto = new PagingDto<>();
        pagingDto.setResultsPerPage(null);
        filters.add(new FilterDto("executionStatus", FilterDto.Operetor.IN, userPortFolioStatus));
        pagingDto.setFilters(filters);
        pagingDto = userPortfolioService.retrieve("createdOn desc", pagingDto, true);
        return pagingDto;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/generateReport")
    public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
        AbstractService.Report report = ((RedemptionService) service).new Report() {

            @Override
            protected void writeRow(WritableSheet sheet, Redemption item, int row) throws WriteException {
                int c = 0;
                sheet.addCell(new Label(c++, row, item.getId()));
                sheet.addCell(new Label(c++, row, item.getCreatedBy()));
                sheet.addCell(new Label(c++, row, item.getUpdatedBy() != null ? item.getUpdatedBy() : " "));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getCreatedOn())));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getUpdatedOn() != null ? item.getUpdatedOn() : " ")));

                sheet.addCell(new Label(c++, row, item.getUserPortfolio().getId()));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getRedemptionAmount() != null ? item.getRedemptionAmount() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getRedemptionDate() != null ? item.getRedemptionDate() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getRedemptionStatus() != null ? item.getRedemptionStatus().getValue() : " ")));

                sheet.addCell(new Label(c++, row, String.valueOf(item.getBrokerBatch() != null ? item.getBrokerBatch() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getAmountReceivedFees() != null ? item.getAmountReceivedFees() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getAmountReceivedFromBroker() != null ? item.getAmountReceivedFromBroker() : " ")));
                sheet.addCell(new Label(c++, row, item.getRemarks() != null ? item.getRemarks() : " "));
            }

            @Override
            protected void writeMoreHeadings(WritableSheet sheet, int row) throws WriteException {
                return;
            }

            @Override
            protected void writeHeadings(WritableSheet sheet, int row) throws WriteException {
                int c = 0;
                sheet.addCell(new Label(c++, row, "ID"));
                sheet.addCell(new Label(c++, row, "Created By"));
                sheet.addCell(new Label(c++, row, "Updated By"));
                sheet.addCell(new Label(c++, row, "Created On"));
                sheet.addCell(new Label(c++, row, "Updated On"));

                sheet.addCell(new Label(c++, row, "User Portfolio ID"));
                sheet.addCell(new Label(c++, row, "Redemption Amount"));
                sheet.addCell(new Label(c++, row, "Redemption Date"));
                sheet.addCell(new Label(c++, row, "Redemption Status"));

                sheet.addCell(new Label(c++, row, "Batch"));
                sheet.addCell(new Label(c++, row, "Fees"));
                sheet.addCell(new Label(c++, row, "Amount From Broker"));
                sheet.addCell(new Label(c++, row, "Remarks"));
            }

            @Override
            protected String getSheetName() {
                return "Redemption";
            }

            @Override
            protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
                return super.getWorkbook(response);
            }

        };
        service.exportAsExcel(report, null, null, response);
    }

}
