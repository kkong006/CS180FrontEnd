package teamawesome.cs180frontend.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.settings_school_tv) Button mChangeSchool;
    @Bind(R.id.settings_logout_bt) Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mChangeSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send request to change school
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send request to logout
            }
        });
    }
}
