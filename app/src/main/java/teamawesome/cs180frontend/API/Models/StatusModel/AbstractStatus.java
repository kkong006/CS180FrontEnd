package teamawesome.cs180frontend.API.Models.StatusModel;

public abstract class AbstractStatus {
    int status;

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
