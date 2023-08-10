package at.ac.tuwien.sepm.groupphase.backend.exception;

import java.util.List;

/**
 * Exception that signals, that data,
 * which was accessed by a user, is not allowed to be accessed by this user.
 * Contains a list of all conflict checks that failed when validating the piece of data in question.
 */
public class UnauthorizedException extends ErrorListException {
    public UnauthorizedException(String messageSummary, List<String> errors) {
        super("Conflicts", messageSummary, errors);
    }
}
