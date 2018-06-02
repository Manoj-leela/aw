package sg.activewealth.roboadvisor.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Authentication parameter not found or invalid")
public class UnauthorisedAccessException extends AbstractException {
}
