package sg.activewealth.roboadvisor.infra.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sg.activewealth.roboadvisor.infra.dao.AbstractDao;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.IAttachmentableHelper;
import sg.activewealth.roboadvisor.infra.helper.MailSenderHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.infra.model.IAttachmentable;
import sg.activewealth.roboadvisor.infra.utils.DateUtils;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

@Service
@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
public abstract class AbstractService<T extends AbstractModel> {

    protected Logger logger = Logger.getLogger(AbstractService.class);

    @Autowired
    @Qualifier("abstractDao")
    protected AbstractDao<T> dao;

    private Class<T> modelClass;

    @Autowired
    protected PropertiesHelper propertiesHelper;

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected UserOperationContextService userOperationContextService;

    @Autowired
    protected SecurityService securityService;

    @Autowired
    protected IAttachmentableHelper attachmentHelper;

    @Autowired
    protected MailSenderHelper mailSenderHelper;

    public AbstractService(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    private void setCreatingNewObject(T model) {
        if (model instanceof AbstractModel) {
            ((AbstractModel) model).setCreatingNewObject(((AbstractModel) model).getId() == null);
        }
    }

    // ################### OVERWRITEABLE METHODS ######################
    public ErrorsDto validateForSave(T model, ErrorsDto errors) throws ValidateException {
        setCreatingNewObject(model);
        return errors;
    }

    public T preSave(T model) {
        setCreatingNewObject(model);
        return model;
    }
    // ################### END OVERWRITEABLE METHODS ########################

    public T postSave(T obj) {
        if (obj instanceof IAttachmentable) {
            IAttachmentable attachmentable = (IAttachmentable) obj;
            // TODO Commenting if condition
            if (attachmentable.getAttachment() != null) {
                if (!attachmentable.getAttachment().isEmpty()) {
                    attachmentHelper.saveFile(attachmentable);
                    attachmentable.setImageUploaded(true); // noneed to explicitly save as object is
                                                           // not flushed yet. will auto flush by
                                                           // transaction.
                }
            }
            if (attachmentable.getImageToDelete()) {
                attachmentHelper.deleteFile(attachmentable.getId());
                attachmentable.setImageUploaded(false); // noneed to explicitly save as object is
                                                        // not flushed yet. will auto flush by
                                                        // transaction.
            }

        }

        return obj;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public T save(T model) throws ValidateException {
        setCreatingNewObject(model);

        ErrorsDto errors = validateForSave(model, new ErrorsDto());
        if (errors.hasErrors()) {
            logger.error(errors.toString());
            throw new ValidateException(errors);
        }

        model = preSave(model);
        model = dao.save(model);
        model = postSave(model);
        return model;
    }

    public T saveWithoutValidation(T model) {
        model = preSave(model);
        model = dao.save(model);
        model = postSave(model);
        return model;
    }

    public List<T> saveAllWithoutValidation(List<T> model) {
        return dao.saveAll(model);
    }
    
    public T saveWithoutPrePost(T model) {
      model = dao.save(model);
      return model;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(T model) {
        dao.delete(model);
    }

    public Integer retrieveVersion(String id) {
        return dao.retrieveVersion(getModelClass(), id);
    }

    public T retrieve(String id) {
        return dao.retrieve(getModelClass(), id);
    }

    public T retrieve(String id, boolean fullInit) {
        return dao.retrieve(getModelClass(), id, fullInit);
    }

    public PagingDto<T> retrieve(String sortBy, PagingDto<T> pagingDto) {
        return dao.retrieve(getModelClass(), sortBy, pagingDto);
    }

    public PagingDto<T> retrieve(String sortBy, PagingDto<T> pagingDto, boolean fullInit) {
        return dao.retrieve(getModelClass(), sortBy, pagingDto, fullInit);
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    protected static final long DELAY_IN_MS_NOTIFICATION = 1500;

    protected static final long NAP_IN_MS_NOTIFICATION = 1000;

    protected void napThreadForNotification() {
        try {
            Thread.sleep(NAP_IN_MS_NOTIFICATION);
        } catch (InterruptedException e) {
            userOperationContextService.error(e);
        }
    }

    protected void sleepThreadForNotification() {
        try {
            Thread.sleep(DELAY_IN_MS_NOTIFICATION);
        } catch (InterruptedException e) {
            userOperationContextService.error(e);
        }
    }

    // this is for exporting partially based on condition, thus workbook need to be created from
    // outside, don't use this for exporting the whole database
    public WritableWorkbook exportAsExcel(Report report, WritableWorkbook workbook,
            HttpServletResponse response, List<T> items) throws IOException, WriteException {

        if (workbook == null) {
            workbook = report.getWorkbook(response);
        }

        if (!items.isEmpty()) {
            report.writeResultsIntoSheets(report, workbook, items);
            workbook.write();
        }

        return workbook;
    }

    // this is used for exporting everything in the database
    public void exportAsExcel(Report report, Date startDate, Date endDate,
            HttpServletResponse response) throws WriteException, IOException {

        WritableWorkbook workbook = report.getWorkbook(response);
        int ITERATE_SIZE = 100;
        int currentPage = 0;

        while (true) {
            PagingDto<T> pagingDto = new PagingDto<T>(ITERATE_SIZE, currentPage++);

            pagingDto = dao.retrieveForReport(modelClass, startDate, endDate, pagingDto, true);

            exportAsExcel(report, workbook, response, pagingDto.getResults());

            if (pagingDto.getResultsSize() <= pagingDto.getResultsPerPage()
                    || pagingDto.getCurrentPage() * pagingDto.getResultsPerPage() >= pagingDto
                            .getResultsSize()) {
                break;
            }
        }

        workbook.close();
    }

    /** Abstract class in order to define the report template **/
    public abstract class Report {
        protected WritableWorkbook getWorkbook(HttpServletResponse response) throws IOException {

            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream());

            response.setHeader("Content-disposition", "attachment; filename=" + getSheetName()
                    + " (" + DateUtils.getInstance().buildStringFromCalendar(Calendar.getInstance())
                    + ").xls");
            response.setContentType("application/vnd.ms-excel");

            return workbook;
        }

        private void writeResultsIntoSheets(Report report, WritableWorkbook workbook, List<T> items)
                throws WriteException {
            String sheetName = report.getSheetName();

            WritableSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                int sheetIndex = 0;
                if (workbook.getNumberOfSheets() > 0) {
                    sheetIndex = workbook.getNumberOfSheets() - 1;
                }
                sheet = workbook.createSheet(sheetName, sheetIndex);
            }

            int row = 0;
            report.writeHeadings(sheet, row);

            for (T s : items) {
                row++;
                report.writeRow(sheet, s, row);
            }
            report.writeMoreHeadings(sheet, row);
        }

        protected abstract String getSheetName();

        protected abstract void writeHeadings(WritableSheet sheet, int row) throws WriteException;

        protected abstract void writeRow(WritableSheet sheet, T item, int row)
                throws WriteException;

        protected abstract void writeMoreHeadings(WritableSheet sheet, int row)
                throws WriteException;


    }
}
