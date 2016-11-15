package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class SearchProfessorResultsActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private String mProfessorName;
    private int mProfessorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_professor_results);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);

        Intent intent = getIntent();
        mProfessorName = intent.getStringExtra(getString(R.string.PROFESSOR_NAME));
        mProfessorId = intent.getIntExtra(getString(R.string.PROFESSOR_ID), 0);

        if(mProfessorId > 0) {
            getSupportActionBar().setTitle(mProfessorName);
            //TODO: Make API call for prof stats
        }
    }
}
