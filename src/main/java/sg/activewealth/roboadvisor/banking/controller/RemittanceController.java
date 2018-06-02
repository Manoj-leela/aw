package sg.activewealth.roboadvisor.banking.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.banking.enums.BrokerFundingStatus;
import sg.activewealth.roboadvisor.banking.enums.InvestorRemittanceStatus;
import sg.activewealth.roboadvisor.banking.model.Remittance;
import sg.activewealth.roboadvisor.banking.service.RemittanceService;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.service.AbstractService;
import sg.activewealth.roboadvisor.infra.utils.FileUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@Controller
@RequestMapping("/admin/remittance")
public class RemittanceController extends CRUDController<Remittance, RemittanceService> {

    @Autowired
    private UserPortfolioService userPortfolioService;

    @Autowired
    public void setService(RemittanceService service) {
        this.service = service;
    }

    @Autowired
    public RemittanceController(RemittanceService service) {
        super(Remittance.class, service);
    }

    @Override
    public Object[] preCreateUpdateGet(Remittance model, HttpServletRequest request) {

        Object[] status = { UserPortfolioStatus.Assigned };
        List<UserPortfolio> userPortfolios = getUserPortfolioList(status).getResults();
        return new Object[] { "userPortfolioList", userPortfolios };
    }

    @Override
    public Remittance preCreateUpdatePost(Remittance model, HttpServletRequest request) {
        super.preCreateUpdatePost(model, request);
        String fileName = model.getRemittanceSlipFileName();
        if (model.getAttachment() != null && model.getAttachment().getSize() > 0) {
            String extension = FilenameUtils.getExtension(model.getAttachment().getOriginalFilename());
            fileName = model.getId() + "_remittance." + extension;
        }
        model.setRemittanceSlipFileName(fileName);
        UserPortfolio userPortfolio = userPortfolioService.retrieve(model.getUserPortfolio().getId());
        model.setUserPortfolio(userPortfolio);
        return model;
    }

    /* @Override
    public Object redirect(Remittance model, HttpServletRequest request) {
        return "redirect:list?investorRemittanceStatus=Submitted";
    }*/

    @ResponseBody
    @RequestMapping("/download")
    public void downloadFile(@ModelAttribute Remittance model, HttpServletResponse response) {

        String fileName = model.getRemittanceSlipFileName();
        String filePath = propertiesHelper.appUploads + File.separator + fileName;
        File file = new File(filePath);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setHeader("content-disposition", "attachment; filename = \"" + fileName + "\"");
            InputStream io = new FileInputStream(file);
            out.write(FileUtils.getInstance().getBytes(io));
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    // @ResponseBody
    @RequestMapping(value = "/deletefile", method = RequestMethod.POST)
    public Object deleteFile(@ModelAttribute Remittance model, HttpServletRequest request) {

        String fileName = model.getRemittanceSlipFileName();
        String filePath = propertiesHelper.appUploads + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        model.setRemittanceSlipFileName(null);
        service.saveWithoutPrePost(model);
        return redirect(model, request);
    }

    @Override
    public Object list(PagingDto<Remittance> pagingDto, String ids, HttpServletRequest request) {
        String[] queryUserPortfolio = request.getParameterValues("userPortfolio.id");

        String queryFundingStatus = request.getParameter("brokerFundingStatus");
        String queryInvestorRemittanceStatus = request.getParameter("investorRemittanceStatus");

        String queryReferenceNumber = request.getParameter("referenceNumber");
        pagingDto = service.retrieveForListPage(queryUserPortfolio, pagingDto, queryFundingStatus, queryReferenceNumber, queryInvestorRemittanceStatus);

        Object[] userPortFolioStatuses = { UserPortfolioStatus.Assigned, UserPortfolioStatus.Funded, UserPortfolioStatus.NotExecuted, UserPortfolioStatus.PartiallyExecuted, UserPortfolioStatus.Executed };

        List<UserPortfolio> userPortfolios = getUserPortfolioList(userPortFolioStatuses).getResults();
        return modelAndView(getFullJspPath("list"), "list", pagingDto, "userPortfolios", userPortfolios);
    }

    @RequestMapping(value = "/customlist", method = RequestMethod.GET)
    public Object customList(HttpServletRequest request) {
        String[] queryUserPortfolio = request.getParameterValues("userPortfolio.id");

        String queryFundingStatus = request.getParameter("brokerFundingStatus");
        String queryInvestorRemittanceStatus = request.getParameter("investorRemittanceStatus");

        String queryReferenceNumber = request.getParameter("referenceNumber");
        PagingDto<Remittance> pagingDto = new PagingDto<>();
        pagingDto.setResultsPerPage(99999);

        pagingDto = service.retrieveForListPage(queryUserPortfolio, null, queryFundingStatus, queryReferenceNumber, queryInvestorRemittanceStatus);
        Object[] userPortFolioStatuses = { UserPortfolioStatus.Assigned, UserPortfolioStatus.Funded, UserPortfolioStatus.NotExecuted, UserPortfolioStatus.PartiallyExecuted, UserPortfolioStatus.Executed };

        List<UserPortfolio> userPortfolios = getUserPortfolioList(userPortFolioStatuses).getResults();
        List<LocalDate> remittanceBatches = service.getRemittanceBatchesDesc(queryUserPortfolio, queryFundingStatus);

        return modelAndView(getFullJspPath("custom_list"), "list", pagingDto.getResults(), "userPortfolios", userPortfolios, "remittanceBatches", remittanceBatches);
    }

    public PagingDto<UserPortfolio> getUserPortfolioList(Object[] userPortFolioStatus) {
        final List<FilterDto> filters = new ArrayList<>();
        PagingDto<UserPortfolio> userPortfolioPagingDto = new PagingDto<>();
        userPortfolioPagingDto.setResultsPerPage(null);
        filters.add(new FilterDto("executionStatus", FilterDto.Operetor.IN, userPortFolioStatus));
        userPortfolioPagingDto.setFilters(filters);
        userPortfolioPagingDto = userPortfolioService.retrieve("createdOn desc", userPortfolioPagingDto, true);
        return userPortfolioPagingDto;
    }

    @RequestMapping(value = { "/api/update/{id}" }, method = RequestMethod.PUT)
    public @ResponseBody Map<String, Object> updateRemittance(@PathVariable String id, @RequestBody Map<String, String> json, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if (id != null) {
            Remittance remittance = service.retrieve(id);

            BigDecimal investorRemittanceReceivedAmount = null;
            BigDecimal investorRemittanceRemittedAmount = null;
            BigDecimal investorRemittanceFees = null;
            BigDecimal brokerFundingReceivedAmount = null;
            BigDecimal brokerFundingFees = null;
            BigDecimal netInvestmentAmount = null;
            LocalDate brokerBatch = null;

            if (json.containsKey("investorRemittanceStatus") && !ValidationUtils.getInstance().isEmptyString(json.get("investorRemittanceStatus"))) {
                InvestorRemittanceStatus investorStatus = InvestorRemittanceStatus.valueOf(json.get("investorRemittanceStatus"));
                remittance.setInvestorRemittanceStatus(investorStatus);
            }
            if (json.containsKey("brokerFundingStatus") && !ValidationUtils.getInstance().isEmptyString(json.get("brokerFundingStatus"))) {
                BrokerFundingStatus brokerStatus = BrokerFundingStatus.valueOf(json.get("brokerFundingStatus"));
                remittance.setBrokerFundingStatus(brokerStatus);
            }
            if (json.containsKey("investorRemittanceRemittedAmount") && !ValidationUtils.getInstance().isEmptyString(json.get("investorRemittanceRemittedAmount"))) {
                String strRemittanceRemittedAmount = String.valueOf(json.get("investorRemittanceRemittedAmount"));
                if (strRemittanceRemittedAmount != null && !strRemittanceRemittedAmount.isEmpty()) {
                    investorRemittanceRemittedAmount = new BigDecimal(strRemittanceRemittedAmount);
                }
            }
            remittance.setInvestorRemittanceRemittedAmount(investorRemittanceRemittedAmount);

            if (json.containsKey("investorRemittanceReceivedAmount") && !ValidationUtils.getInstance().isEmptyString(json.get("investorRemittanceReceivedAmount"))) {
                String strInvestorRemittanceReceivedAmout = String.valueOf(json.get("investorRemittanceReceivedAmount"));
                if (strInvestorRemittanceReceivedAmout != null && !strInvestorRemittanceReceivedAmout.isEmpty()) {
                    investorRemittanceReceivedAmount = new BigDecimal(strInvestorRemittanceReceivedAmout);
                }
            }
            remittance.setInvestorRemittanceReceivedAmount(investorRemittanceReceivedAmount);

            if (json.containsKey("investorRemittanceFees") && !ValidationUtils.getInstance().isEmptyString(json.get("investorRemittanceFees"))) {
                String strInvestorRemittanceFees = String.valueOf(json.get("investorRemittanceFees"));
                if (strInvestorRemittanceFees != null && !strInvestorRemittanceFees.isEmpty()) {
                    investorRemittanceFees = new BigDecimal(strInvestorRemittanceFees);
                }
            }
            remittance.setInvestorRemittanceFees(investorRemittanceFees);

            if (json.containsKey("remarks")) {
                String remarks = String.valueOf(json.get("remarks"));
                remittance.setRemarks(remarks);
            }

            if (json.containsKey("brokerBatch") && !ValidationUtils.getInstance().isEmptyString(json.get("brokerBatch"))) {
                String strBrokerBatch = String.valueOf(json.get("brokerBatch"));
                if (strBrokerBatch != null && !strBrokerBatch.isEmpty()) {
                    try {
                        brokerBatch = LocalDate.parse(strBrokerBatch);
                    } catch (DateTimeParseException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            remittance.setBrokerBatch(brokerBatch);
            if (json.containsKey("brokerFundingReceivedAmount") && !ValidationUtils.getInstance().isEmptyString(json.get("brokerFundingReceivedAmount"))) {
                String strBrokerFundingReceivedAmount = String.valueOf(json.get("brokerFundingReceivedAmount"));
                if (strBrokerFundingReceivedAmount != null && !strBrokerFundingReceivedAmount.isEmpty()) {
                    brokerFundingReceivedAmount = new BigDecimal(strBrokerFundingReceivedAmount);
                }
            }
            remittance.setBrokerFundingReceivedAmount(brokerFundingReceivedAmount);
            if (json.containsKey("brokerFundingFees") && !ValidationUtils.getInstance().isEmptyString(json.get("brokerFundingFees"))) {
                String strBrokerFundingFees = String.valueOf(json.get("brokerFundingFees"));
                if (strBrokerFundingFees != null && !strBrokerFundingFees.isEmpty()) {
                    brokerFundingFees = new BigDecimal(strBrokerFundingFees);
                }
            }
            remittance.setBrokerFundingFees(brokerFundingFees);

            if (json.containsKey("netInvestmentAmount") && !ValidationUtils.getInstance().isEmptyString(json.get("netInvestmentAmount"))) {
                String strNetInvestmentAmount = String.valueOf(json.get("netInvestmentAmount"));
                if (strNetInvestmentAmount != null && !strNetInvestmentAmount.isEmpty()) {
                    netInvestmentAmount = new BigDecimal(strNetInvestmentAmount);
                }
            }
            remittance.setNetInvestmentAmount(netInvestmentAmount);
            remittance = service.save(remittance);
            map.put("success", "true");
            map.put("message", "Record successfully saved.");
        }
        return map;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/generateReport")
    public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
        AbstractService.Report report = ((RemittanceService) service).new Report() {
            @Override
            protected void writeRow(WritableSheet sheet, Remittance item, int row) throws WriteException {
                int c = 0;
                sheet.addCell(new Label(c++, row, item.getId()));
                sheet.addCell(new Label(c++, row, item.getCreatedBy()));
                sheet.addCell(new Label(c++, row, item.getUpdatedBy() != null ? item.getUpdatedBy() : " "));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getCreatedOn())));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getUpdatedOn() != null ? item.getUpdatedOn() : " ")));

                sheet.addCell(new Label(c++, row, item.getUserPortfolio().getId()));
                sheet.addCell(new Label(c++, row, item.getReferenceNo() != null ? item.getReferenceNo() : " "));
                sheet.addCell(new Label(c++, row, item.getRemittanceSlipFileName() != null ? item.getRemittanceSlipFileName() : " "));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getBrokerFundingStatus() != null ? item.getBrokerFundingStatus().getValue() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getInvestorRemittanceRemittedAmount() != null ? item.getInvestorRemittanceRemittedAmount() : " ")));

                sheet.addCell(new Label(c++, row, String.valueOf(item.getInvestorRemittanceStatus() != null ? item.getInvestorRemittanceStatus().getValue() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getReconciliationEmailSent() != null ? item.getReconciliationEmailSent() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getReconciliationAwaitEmailSent() != null ? item.getReconciliationAwaitEmailSent() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getBrokerFundedEmailSent() != null ? item.getBrokerFundedEmailSent() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getInvestorRemittanceReceivedAmount() != null ? item.getInvestorRemittanceReceivedAmount() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getBrokerBatch() != null ? item.getBrokerBatch() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getInvestorRemittanceFees() != null ? item.getInvestorRemittanceFees() : " ")));
                sheet.addCell(new Label(c++, row, String.valueOf(item.getRemarks() != null ? item.getRemarks() : " ")));
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
                sheet.addCell(new Label(c++, row, "Reference No"));
                sheet.addCell(new Label(c++, row, "Remittance Slip File Name"));
                sheet.addCell(new Label(c++, row, "Funding Status"));
                sheet.addCell(new Label(c++, row, "Remittance Amount"));

                sheet.addCell(new Label(c++, row, "Investor Remittance"));
                sheet.addCell(new Label(c++, row, "Reconciliation Email Sent"));
                sheet.addCell(new Label(c++, row, "Reconciliation Await Email Sent"));
                sheet.addCell(new Label(c++, row, "Funded Email Sent"));
                sheet.addCell(new Label(c++, row, "Received Amount"));
                sheet.addCell(new Label(c++, row, "Batch"));
                sheet.addCell(new Label(c++, row, "Fees"));
                sheet.addCell(new Label(c++, row, "Remarks"));
            }

            @Override
            protected String getSheetName() {
                return "Remittance";
            }

            @Override
            protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
                return super.getWorkbook(response);
            }

        };

        service.exportAsExcel(report, null, null, response);
    }

}
