package teamawesome.cs180frontend.API.Models.UserModel;

/**
 * Created by jonathan on 1/26/17.
 */

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