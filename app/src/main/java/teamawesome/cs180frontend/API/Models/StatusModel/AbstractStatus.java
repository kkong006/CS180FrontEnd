package teamawesome.cs180frontend.API.Models.StatusModel;

//Abstract status class
//Broadcasting errors as statuses to avoid activities handling errors for other activities
public abstract class AbstractStatus {
    private int status;

    public AbstractStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
