package sg.activewealth.roboadvisor.infra.exception;

@SuppressWarnings("serial")
public class JsonObjectNotFoundException extends AbstractException {

	public JsonObjectNotFoundException() {
		super();
	}

	public JsonObjectNotFoundException(String errorKey) {
		super(errorKey);
		// TODO Auto-generated constructor stub
	}
}

