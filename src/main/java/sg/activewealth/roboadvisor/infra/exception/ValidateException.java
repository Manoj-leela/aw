package sg.activewealth.roboadvisor.infra.exception;

import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;

@SuppressWarnings("serial")
public class ValidateException extends AbstractException {

	private String errorKey;
	
	private ErrorsDto errors;

	public ValidateException(ErrorsDto errors) {
		super();
		this.errors = errors;
	}
	
	public ValidateException(Throwable t, ErrorsDto errors) {
		super(t);
		this.errors = errors;
	}

	public ValidateException(String errorKey) {
		super(errorKey);
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}
	
	public ErrorsDto getErrors() {
		return errors;
	}

}
