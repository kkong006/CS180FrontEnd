package teamawesome.cs180frontend.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.internal.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.SchoolBundle;
import teamawesome.cs180frontend.API.Models.UpdateUserBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.PostUpdateAccountCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.activity_settings) LinearLayout mParent;
    @Bind(R.id.settings_university_tv) TextView mUniversityTV;
    @Bind(R.id.settings_password_tv)TextView mPasswordTV;
    @Bind(R.id.settings_university_sp) Spinner mSpinner;
    @Bind(R.id.settings_school_bt) Button mSchoolButton;
    @Bind(R.id.old_password_et) EditText mOldPasswordET;
    @Bind(R.id.new_password_et) EditText mNewPasswordET;
    @Bind(R.id.settings_password_bt) Button mPasswordButton;
    @Bind(R.id.settings_old_password_til) TextInputLayout mOldPasswordTIL;
    @Bind(R.id.settings_new_password_til) TextInputLayout mNewPasswordTIL;

    private String mSchoolName;
    private int mSchoolId;
    private String mNewSchoolName;
    private int mNewSchoolId;
    private String[] mSchoolNames;
    private ArrayAdapter<String> mAdapter;
    private String mPassword;
    private String mNewPassword;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        mSchoolId = Utils.getSchoolId(this);

        if(mSchoolId == 0) {
            mSchoolButton.setVisibility(View.GONE);
            mPasswordButton.setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);
            mOldPasswordET.setVisibility(View.GONE);
            mNewPasswordET.setVisibility(View.GONE);
            mUniversityTV.setText(getString(R.string.please_sign_in));
            mPasswordTV.setVisibility(View.GONE);
            mOldPasswordTIL.setVisibility(View.GONE);
            mNewPasswordTIL.setVisibility(View.GONE);
        } else {
            ArrayList<SchoolBundle> schools = DataSingleton.getInstance().getSchoolCache();
            if(schools.size() > 0) {
                mSchoolNames = new String[schools.size()];
                for(int i = 0; i < schools.size(); i++) {
                    mSchoolNames[i] = schools.get(i).getSchoolName();
                    if(schools.get(i).getSchoolId() == mSchoolId) {
                        mSchoolName = schools.get(i).getSchoolName();
                    }
                }
                System.out.println("Current school " + mSchoolName + " " + mSchoolId);
                mPassword = Utils.getPassword(this);
                mNewSchoolName = mSchoolName;
                mNewSchoolId = mSchoolId;
                mNewPassword = mPassword;
                fillSpinner();
            }
        }
    }

    public void fillSpinner() {
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mSchoolNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        for(int i = 0; i < mSchoolNames.length; i++) {
            if(mSpinner.getItemAtPosition(i) == mSchoolName) {
                mSpinner.setSelection(i);
                break;
            }
        }
        mSpinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String result = parent.getItemAtPosition(position).toString();
            mNewSchoolName = result;
            mNewSchoolId = DataSingleton.getInstance().getSchoolId(mNewSchoolName);
            System.out.println("New school set to " + mNewSchoolName + " " + mNewSchoolId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }

    @OnClick(R.id.settings_school_bt)
    public void selectSchool() {
        if(mSchoolId != mNewSchoolId) {
            UpdateUserBundle user = new UpdateUserBundle(Utils.getUserId(this), mPassword, mPassword, mNewSchoolId);
            updateUserAccount(user);
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.select_new_school));
        }
    }

    @OnClick(R.id.settings_password_bt)
    public void changePassword() {
        String newPassword = mNewPasswordET.getText().toString();
        if(newPassword.length() > 8 && Utils.getMD5Hash(newPassword) != mPassword) {
            mNewPassword = Utils.getMD5Hash(newPassword);
            UpdateUserBundle user = new UpdateUserBundle(Utils.getUserId(this), mPassword, mNewPassword, mSchoolId);
            updateUserAccount(user);
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.enter_new_password));
        }
    }

    private void updateUserAccount(UpdateUserBundle user) {
        mProgressDialog.show();
        Callback callback = new PostUpdateAccountCallback();
        RetrofitSingleton.getInstance().getUserService()
                .updateAccount(user)
                .enqueue(callback);
    }

    @Subscribe
    public void respInt(Integer i) {
        mProgressDialog.dismiss();
        if(i.equals(1)) {
            mSchoolName = mNewSchoolName;
            mSchoolId = mNewSchoolId;
            mPassword = mNewPassword;
            SPSingleton.getInstance(this).getSp().edit().putInt(Constants.SCHOOL_ID, mSchoolId).commit();
            SPSingleton.getInstance(this).getSp().edit().putString(Constants.PASSWORD, mPassword).commit();
            mOldPasswordET.setText("");
            mNewPasswordET.setText("");
            fillSpinner();
            Utils.showSnackbar(this, mParent, getString(R.string.account_update_success));
        } else {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Subscribe
    public void respString(String s) {
        mProgressDialog.dismiss();
        if(s.equals("ERROR")) {
            Utils.showSnackbar(this, mParent, getString(R.string.error_retrieving_data));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
