package teamawesome.cs180frontend.API.Models.AccountModel;

public class UpdateSchoolStatus {
    int status;

    public UpdateSchoolStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
