package at.ac.tuwien.sepm.groupphase.backend.exception;

import java.util.List;

/**
 * Exception that signals, that data,
 * that came from outside the backend, is invalid.
 * Contains a list of all validation checks that failed when validating the piece of data in question.
 */
public class ValidationException extends ErrorListException {

    public ValidationException(String messageSummary, List<String> errors) {
        super("Conflicts", messageSummary, errors);
    }
}
