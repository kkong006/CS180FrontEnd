package teamawesome.cs180frontend.API.Models.AccountModel;

public class UpdatePasswordStatus {
    private int status;

    public UpdatePasswordStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
