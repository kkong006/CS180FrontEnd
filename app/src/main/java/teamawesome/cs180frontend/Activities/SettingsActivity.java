package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.School;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetSchoolsCallback;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.settings_school_tv) Button mChangeSchool;
    @Bind(R.id.settings_logout_bt) Button mLogout;
    @Bind(R.id.settings_university_et) EditText mSchoolName;

    private ProgressDialog progressDialog;
    private String schoolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mChangeSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO: send request to change school
            schoolName = mSchoolName.getText().toString();
            if(schoolName.length() > 0) {
                progressDialog = new ProgressDialog(getBaseContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                Callback callback = new GetSchoolsCallback();
                RetrofitSingleton.getInstance().getUserService()
                        .getSchools()
                        .enqueue(callback);
            } else {
                Toast.makeText(getBaseContext(), "Enter school name", Toast.LENGTH_SHORT).show();
            }
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send request to logout
            }
        });
    }

    @Subscribe
    public void schoolsResp(List<School> schoolList) {
        for(School school : schoolList) {
            if(school.getSchoolName() == schoolName) {
                getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
                        .edit().putInt(Constants.SCHOOL_ID, school.getSchoolId()).apply();
                Toast.makeText(getBaseContext(), "School name changed!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getBaseContext(), "University does not exist", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void schoolsInt(Integer i) {
        if(i == 0) {
            Toast.makeText(getBaseContext(), "Universities do not exist", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
