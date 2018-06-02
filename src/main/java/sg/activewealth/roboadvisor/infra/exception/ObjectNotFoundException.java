package sg.activewealth.roboadvisor.infra.exception;

@SuppressWarnings("serial")
public class ObjectNotFoundException extends AbstractException {

	public ObjectNotFoundException(Throwable t) {
		super(t.getMessage(), t);
	}

	public ObjectNotFoundException(String errorKey, Throwable t) {
		super(errorKey, t);
	}
	
	public ObjectNotFoundException(String errorKey) {
		super(errorKey);
	}
	
}
