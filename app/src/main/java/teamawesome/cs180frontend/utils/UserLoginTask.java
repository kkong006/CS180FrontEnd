package teamawesome.cs180frontend.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import teamawesome.cs180frontend.LoginActivity;
import teamawesome.cs180frontend.R;

/**
 * Created by nicholas on 10/9/16.
 */
public class UserLoginTask extends AsyncTask<void, void, boolean> {
    private final String mEmail;
    private final String mPassword;
    private final Context mContext;

    UserLoginTask(String email, String password, Context context) {
        this.mEmail = email;
        this.mPassword = password;
        this.mContext = context;
    }

    @Override
    protected boolean doInBackground(void... params) {
        DBTools dbTools = null;
        try {
            dbTools = new DBTools(mContext);
            myUser = dbTools.getUser(mEmail);

            if (myUser.userId > 0) {
                // Account exists
                if (myUser.password.equals(mPassword))
                    return true;
                else
                    return false;
            } else {
                myUser.password = mPassword;
                return true;
            }
        } finally {
            if (dbTools != null)
                dbTools.close();
        }
    }

    @Override
    protected void onPostExecute(boolean b) {
        mAuthTask = null;
        showProgress(false);

        if (success) {
            if (myUser.userId > 0) {
                finish();
                Intent myIntent = new Intent(LoginActivity.this, ReportListsActivity.class);
                LoginActivity.this.startActivity(myIntent);
            } else {
                DialogInterface.OnClickListener dialogClickListener
                        = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // Create new user
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                mPasswordView.setError(
                                        getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                builder.setMessage(R.string.confirm_registry).setPositiveButton(R.string.yes,
                        dialogClickListener).setNegativeButton(R.string.no, dialogClickListener)
                        .show();
            }
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mAuthTask = null;
        showProgress(false);
    }
}