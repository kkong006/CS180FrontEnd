package teamawesome.cs180frontend.Activities.Onboarding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import teamawesome.cs180frontend.Activities.Application.MainActivity;
import teamawesome.cs180frontend.Misc.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (Utils.getUserId(this) <= 0) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();

    }
}
