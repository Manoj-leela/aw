package sg.activewealth.roboadvisor.portfolio.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolioQuestionAndAnswer;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioQuesAndAnsService;

@Controller
@RequestMapping("/admin/userPortfolioQuestionAndAnswer")
public class UserPortfolioQuestionAndAnswerController
		extends CRUDController<UserPortfolioQuestionAndAnswer, UserPortfolioQuesAndAnsService> {

	public UserPortfolioQuestionAndAnswerController() {
		super();
	}

	public UserPortfolioQuestionAndAnswerController(
			Class<UserPortfolioQuestionAndAnswer> userPortfolioQuestionAndAnswer,
			UserPortfolioQuesAndAnsService service) {
		super(userPortfolioQuestionAndAnswer, service);
	}

	@Autowired
	public void setService(UserPortfolioQuesAndAnsService service) {
		this.service = service;
	}

	@Autowired
	public UserPortfolioQuestionAndAnswerController(UserPortfolioQuesAndAnsService service) {
		super(UserPortfolioQuestionAndAnswer.class, service);
	}
	

	@RequestMapping("/generateReport")
	public void downloadReport(HttpServletResponse response) throws WriteException, IOException {
		service.exportAsExcel(((UserPortfolioQuesAndAnsService)service).new Report() {

			@Override
			protected void writeRow(WritableSheet sheet, UserPortfolioQuestionAndAnswer item, int row) throws WriteException {
				sheet.addCell(new Label(0, row, item.getId()));
				sheet.addCell(new Label(1, row, item.getCreatedBy()));
				sheet.addCell(new Label(2, row, item.getUpdatedBy() !=null ? item.getUpdatedBy() : " "));
				sheet.addCell(new Label(3, row, String.valueOf(item.getCreatedOn())));
				sheet.addCell(new Label(4, row, String.valueOf(item.getUpdatedOn() !=null ? item.getUpdatedOn() : " ")));
				
				sheet.addCell(new Label(6, row, item.getUserPortfolio().getId()));
				sheet.addCell(new Label(7, row, item.getQuestion()));
				sheet.addCell(new Label(8, row, item.getAnswer() !=null ? item.getAnswer() : " "));
			}

			@Override
			protected void writeMoreHeadings(WritableSheet sheet, int row) throws WriteException {
				return;
			}

			@Override
			protected void writeHeadings(WritableSheet sheet, int row) throws WriteException {
				sheet.addCell(new Label(0, row, "ID"));
				sheet.addCell(new Label(1, row, "Created By"));
				sheet.addCell(new Label(2, row, "Updated By"));
				sheet.addCell(new Label(3, row, "Created On"));
				sheet.addCell(new Label(4, row, "Updated On"));
				sheet.addCell(new Label(5, row, "Deleted"));
				
				sheet.addCell(new Label(6, row, "User Portfolio ID"));
				sheet.addCell(new Label(7, row, "Question"));
				sheet.addCell(new Label(8, row, "Answer"));
			}

			@Override
			protected String getSheetName() {
				return "User Portfolio Question and Answer";
			}

			@Override
			protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {
				return super.getWorkbook(response);
			}

		}, null, null, response);
	}
	
}
