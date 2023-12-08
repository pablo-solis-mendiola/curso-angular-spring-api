package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="This client does not match any of our records.")
public class ClienteNotFoundException extends RuntimeException {
}
