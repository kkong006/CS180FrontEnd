package teamawesome.cs180frontend.API.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * container for user data
 *
 * Created by nicholas on 10/9/16.
 */

public class User implements Parcelable {

    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("phone_number")
    @Expose
    public String phone_number;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("active")
    @Expose
    public boolean active;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(phone_number);
        dest.writeString(password);
        dest.writeByte((byte) (active ? 1 : 0));
    }

    public User(Parcel p) {
        id = p.readLong();
        phone_number = p.readString();
        password = p.readString();
        active = p.readByte() != 0;
    }

    public static final Parcelable.Creator<User> Creator = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel p) {
            return new User(p);
        }

        @Override
        public User[] newArray(int sz) {
            return new User[sz];
        }
    };

    public User() {
    }

    public User(String phone_number, String password) {
        this.phone_number = phone_number;
        this.password = password;
    }

    public User(long id, String phone_number, String password, boolean active) {
        this.id = id;
        this.phone_number = phone_number;
        this.password = password;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
