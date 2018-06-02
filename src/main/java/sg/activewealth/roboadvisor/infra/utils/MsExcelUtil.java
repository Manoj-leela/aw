package sg.activewealth.roboadvisor.infra.utils;

import java.text.DecimalFormat;
import java.util.List;

import jxl.CellView;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MsExcelUtil {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private WritableCellFormat numberCellFormat;
	private WritableCellFormat currencyCellFormat;

	private static MsExcelUtil me;

	public static MsExcelUtil getInstance() {
		if (me == null) me = new MsExcelUtil();

		return me;
	}

	public void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	public void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		if (integer != null) {
//			addLabel(sheet, column, row, integer.toString());
			Number number;
			int i = (integer == null) ? 0 : integer;
			number = new Number(column, row, i, times);
			sheet.addCell(number);
		}
	}

	public void addCurrency(WritableSheet sheet, int column, int row,
			Float flt) throws WriteException, RowsExceededException {
		if (flt != null) {
//			addLabel(sheet, column, row, sdf.format(flt).toString());
			Number number;

			flt = (flt == null) ? 0f : flt;

			number = new Number(column, row, flt, (flt == 0f ? times:currencyCellFormat));
			sheet.addCell(number);
		}
	}

	public void addFloat(WritableSheet sheet, int column, int row,
			Float flt) throws WriteException, RowsExceededException {
		if (flt != null) {
//			addLabel(sheet, column, row, sdf.format(flt).toString());
			Number number;

			flt = (flt == null) ? 0f : flt;

			number = new Number(column, row, flt, (flt == 0f ? times:numberCellFormat));
			sheet.addCell(number);
		}
	}

	public void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, (s != null) ? s : "", times);
		sheet.addCell(label);
	}

	public void initialize(WritableSheet sheet, List<String> header) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10);
		times = new WritableCellFormat(times10pt);
		times.setAlignment(Alignment.LEFT);
		times.setWrap(false);

		// create create a bold font with underlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.ARIAL, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		timesBoldUnderline.setAlignment(Alignment.LEFT);
		timesBoldUnderline.setWrap(false);
		
		numberCellFormat = new WritableCellFormat(new NumberFormat("#.00"));
		numberCellFormat.setAlignment(Alignment.LEFT);
		numberCellFormat.setWrap(false);

		currencyCellFormat = new WritableCellFormat(new NumberFormat(NumberFormat.CURRENCY_DOLLAR + "#,###.00", NumberFormat.COMPLEX_FORMAT));
		currencyCellFormat.setAlignment(Alignment.LEFT);
		currencyCellFormat.setWrap(false);
		
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		for (int i = 0; i < header.size(); i++) {
			addCaption(sheet, i, 0, header.get(i));
		}

	}
	

}
