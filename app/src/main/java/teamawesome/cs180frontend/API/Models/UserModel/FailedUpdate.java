package teamawesome.cs180frontend.API.Models.UserModel;

/**
 * Created by jonathan on 1/26/17.
 */

public class FailedUpdate {
    String stackTrace;

    public FailedUpdate(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
