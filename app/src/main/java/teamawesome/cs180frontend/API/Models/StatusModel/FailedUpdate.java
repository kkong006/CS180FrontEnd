package teamawesome.cs180frontend.API.Models.StatusModel;

//Broadcasted when user account fails to update
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
