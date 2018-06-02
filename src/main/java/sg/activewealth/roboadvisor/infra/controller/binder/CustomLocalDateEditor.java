/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sg.activewealth.roboadvisor.infra.controller.binder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.util.StringUtils;

/**
 * Property editor for {@code java.util.Date},
 * supporting a custom {@code java.text.DateFormat}.
 *
 * <p>
 * This is not meant to be used as system PropertyEditor but rather
 * as locale-specific date editor within custom controller code,
 * parsing user-entered number strings into Date properties of beans
 * and rendering them in the UI form.
 *
 * <p>
 * In web MVC code, this editor will typically be registered with
 * {@code binder.registerCustomEditor}.
 *
 * @author Juergen Hoeller
 * @since 28.04.2003
 * @see java.util.Date
 * @see java.text.DateFormat
 * @see org.springframework.validation.DataBinder#registerCustomEditor
 */
public class CustomLocalDateEditor extends PropertyEditorSupport {

    private final DateTimeFormatter dateFormat;

    private final boolean allowEmpty;

    private final int exactDateLength;

    public CustomLocalDateEditor(DateTimeFormatter dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }


    public CustomLocalDateEditor(DateTimeFormatter dateFormat, boolean allowEmpty,
            int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0
                && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException("Could not parse date: it is not exactly"
                    + this.exactDateLength + "characters long");
        } else {
            try {
                LocalDate date = LocalDate.parse(text);
                setValue(date);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }

}
