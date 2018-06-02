package sg.activewealth.roboadvisor.infra.controller.binder;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;

public class StringReplaceTrimmerEditor extends StringTrimmerEditor {

	private boolean replaceSpecialCharacters = false;
	
	private boolean doNotAllowNewLines = false;
	
	public StringReplaceTrimmerEditor(String requestPg) {
		super(true);
		
        boolean replaceSpecialCharacters = false;
        boolean doNotAllowNewLines = false;
        
		this.replaceSpecialCharacters = replaceSpecialCharacters;
		this.doNotAllowNewLines = doNotAllowNewLines;
	}

	public void setAsText(String text) {
		super.setAsText(text);

		String value = (String) super.getValue();
		if (replaceSpecialCharacters) {
			//remove restricted characters
			if (value != null) {
				String INTEMEDIARY_VALUE = "#^#";
				//transform &#1234; values away first
				value = value.replace("&#", INTEMEDIARY_VALUE); 
				value = value.replace("&", "n");
				//transform back
				value = value.replace(INTEMEDIARY_VALUE, "&#");
				value = value.replace("\"", "\'");
			}
		}
		if (doNotAllowNewLines) {
			if (value != null) {
				value = value.replace("\n", " ");
				value = value.replace("\r", " ");
				value = value.replace("\r\n", " ");
			}
		}
		super.setValue(value);
	}
}
