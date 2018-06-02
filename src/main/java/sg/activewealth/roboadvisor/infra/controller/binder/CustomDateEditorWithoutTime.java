package sg.activewealth.roboadvisor.infra.controller.binder;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;

public class CustomDateEditorWithoutTime extends CustomDateEditor {

    public CustomDateEditorWithoutTime(DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat, allowEmpty);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setAsText(String text) {
        super.setAsText(text);
        Date date = (Date) super.getValue();
        if (date != null) {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            super.setValue(date);
        }
    }

}
