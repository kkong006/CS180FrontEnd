package teamawesome.cs180frontend.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    private String mProfessorName;
    private String mSchoolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Search Results");

        Bundle bundle = getIntent().getExtras();
        mProfessorName = bundle.getString(SearchActivity.PROFESSOR_NAME);
        mSchoolName = bundle.getString(SearchActivity.SCHOOL_NAME);


    }
}
