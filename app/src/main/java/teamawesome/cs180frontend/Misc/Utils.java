package teamawesome.cs180frontend.Misc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.SchoolBundle;
import teamawesome.cs180frontend.API.Models.AccountModel.AccountRespBundle;
import teamawesome.cs180frontend.R;

public class Utils {
    public static void hideKeyboard(View v, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String getMD5Hash(String input) {
        String hashtext = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hashtext;
    }

    public static Snackbar showSnackBar(Context context, View parent, int colorId, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(colorId));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.colorWhite));
        snackbar.show();
        return snackbar;
    }

    public static int getUserId(Context context) {
        return SPSingleton.getInstance(context).getSp().getInt(Constants.USER_ID, 0);
    }

    public static int getSchoolId(Context context) {
        return SPSingleton.getInstance(context).getSp().getInt(Constants.SCHOOL_ID, 0);
    }

    public static String getSystemType(Context context) {
        return SPSingleton.getInstance(context).getSp().getString(Constants.SYSTEM_TYPE, "Quarter");
    }

    public static void setSystemType(Context context, String systemType) {
        SPSingleton.getInstance(context).getSp()
                .edit()
                .putString(Constants.SYSTEM_TYPE, systemType)
                .commit();
    }

    public static boolean isVerified(Context context) {
        return SPSingleton.getInstance(context).getSp().getBoolean(Constants.IS_VERIFIED, false);
    }

    public static void setVerified(Context context, boolean isVerified) {
        SPSingleton.getInstance(context).getSp()
                .edit()
                .putBoolean(Constants.IS_VERIFIED, isVerified)
                .commit();
    }

    public static void savePassword(Context context, String newPassword) {
        SPSingleton.getInstance(context).getSp()
                .edit()
                .putString(Constants.PASSWORD, newPassword)
                .commit();
    }

    public static String getPassword(Context context) {
        return SPSingleton.getInstance(context).getSp().getString(Constants.PASSWORD, "");
    }

    public static boolean doesSchoolExist(String school) {
        return DataSingleton.getInstance().getSchoolId(school) != null;
    }

    public static boolean changedSchool(Context context) {
        SharedPreferences sp = SPSingleton.getInstance(context).getSp();
        return sp.getBoolean(Constants.CHANGED_SCHOOL,true);
    }

    public static void setChangeSchool(Context context, boolean changedSchool) {
        SPSingleton.getInstance(context).getSp()
                .edit()
                .putBoolean(Constants.CHANGED_SCHOOL, changedSchool)
                .commit();
    }

    //save the user's info once they've logged in and/or registered
    public static void saveUserData(Context context, AccountRespBundle userInfo, String password, String number) {
        SharedPreferences sp = SPSingleton.getInstance(context).getSp();
        sp.edit().putInt(Constants.USER_ID, userInfo.getId()).commit();
        sp.edit().putBoolean(Constants.IS_VERIFIED, userInfo.isVerified()).commit();
        sp.edit().putInt(Constants.SCHOOL_ID, userInfo.getSchoolId()).commit();
        sp.edit().putString(Constants.SYSTEM_TYPE, userInfo.getSystemType()).commit();
        sp.edit().putString(Constants.PASSWORD, password).commit();
        sp.edit().putString(Constants.PHONE_NUMBER, number).commit();
    }

    public static void saveSchoolData(Context context, SchoolBundle newSchool) {
        SPSingleton.getInstance(context).getSp().edit()
                .putInt(Constants.SCHOOL_ID, newSchool.getSchoolId()).commit();
        SPSingleton.getInstance(context).getSp().edit().
                putString(Constants.SYSTEM_TYPE, newSchool.getSystemType()).commit();
    }

    //Good night sweet prince
    public static void nukeUserData(Context context) {
        SharedPreferences sp = SPSingleton.getInstance(context).getSp();
        sp.edit().remove(Constants.USER_ID).commit();
        sp.edit().remove(Constants.IS_VERIFIED).commit();
        sp.edit().remove(Constants.SCHOOL_ID).commit();
        sp.edit().remove(Constants.PASSWORD).commit();
        sp.edit().remove(Constants.PHONE_NUMBER).commit();
    }

    public static Set<String> getLikedList(Context context) {
        return SPSingleton.getInstance(context).getSp().getStringSet("liked_list", null);
    }

    public static Set<String> getDislikedList(Context context) {
        return SPSingleton.getInstance(context).getSp().getStringSet("disliked_list", null);
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /*
    //Unused for now
    public static String getLocalTimeString(String timestamp) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate = format.parse(timestamp);
            format = new SimpleDateFormat("MM/dd/yyyy");
            format.setTimeZone(Calendar.getInstance().getTimeZone());
            return format.format(utcDate);
        } catch (ParseException e) {
            return null;
        }
    }
    */

    //retrieve how the user voted on a review (up or down)
    public static int getReviewRating(int reviewId) {
        if (DataSingleton.getInstance().getLikedSet().contains(reviewId)) {
            return 1;
        } else if (DataSingleton.getInstance().getDislikedSet().contains(reviewId)) {
            return 2;
        } else {
            return 0;
        }
    }

    public static void failedVerifySnackBar(ProgressDialog progressDialog, CoordinatorLayout parent,
                                            int color, int status, Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (status== APIConstants.HTTP_STATUS_INVALID) {
            showSnackBar(context, parent, color, context.getString(R.string.invalid_pin));
        } else if (status == APIConstants.HTTP_STATUS_ERROR) {
            showSnackBar(context, parent, color, context.getString(R.string.server_error));
        } else {
            showSnackBar(context, parent, color, context.getString(R.string.unable_to_request));
        }
    }
}
