package sg.activewealth.roboadvisor.common.dto;

import sg.activewealth.roboadvisor.infra.enums.ReportEnum;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;

public interface ManagementDataReportDto {

	public class Main extends AbstractModel {

		ReportEnum reportType;

		public ReportEnum getReportType() {
			return reportType;
		}

		public void setReportType(ReportEnum reportType) {
			this.reportType = reportType;
		}

	}
}
