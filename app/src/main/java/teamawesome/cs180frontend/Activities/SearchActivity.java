package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.R;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.search_professor_et) EditText mProfessorNameET;
//    @Bind(R.id.school_et) EditText mSchoolName;
//    @Bind(R.id.class_et) EditText mClassName;
//    @Bind(R.id.search_search_bt) Button mSearch;

    private String mProfessor;
    private ProfessorBundle mReturnedProfessor;
    private ProgressDialog mProgressDialog;

//    public static final String PROFESSOR_NAME = "PROFESSOR_NAME";
//    public static final String CLASS_NAME = "CLASS_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.search_search_bt)
    public void search() {
        mProfessor = mProfessorNameET.getText().toString();
        //                String schoolName = mClassName.getText().toString();
        if(mProfessor.length() > 0) {
            mProfessorNameET.setText("");
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();

            //TODO: Client-based sorting?
            /*Callback callback = new GetProfessorsCallback();
            RetrofitSingleton.getInstance().getMatchingService()
                    .search(mProfessor, getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE).getInt(Constants.SCHOOL_ID, -1))
                    .enqueue(callback);*/
//            //TODO: remove once data is placed in the database
//            Intent i = new Intent(getApplicationContext(), SearchResultsActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(getResources().getString(R.string.professor_name), mProfessor);
//            bundle.putInt(getResources().getString(R.string.professor_id), 1);
//            i.putExtras(bundle);
//            startActivity(i);
        } else {
            Toast.makeText(this, getResources().getString(R.string.enter_professor_name), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void professorResp(ProfessorBundle p) {
        mProgressDialog.dismiss();
        mReturnedProfessor = p;

        Intent i = new Intent(this, SearchResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.professor_name), mReturnedProfessor.getProfessorName());
        bundle.putInt(getResources().getString(R.string.professor_id), mReturnedProfessor.getProfessorId());
        i.putExtras(bundle);
        startActivity(i);
    }

    @Subscribe
    public void professorInt(Integer i) {
        mProgressDialog.dismiss();
        if(i == 0) {
            Toast.makeText(this, getResources().getString(R.string.professors_dne), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void professorString(String s) {
        mProgressDialog.dismiss();
        if(s == "ERROR") {
            Toast.makeText(this, getResources().getString(R.string.error_retrieving_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
