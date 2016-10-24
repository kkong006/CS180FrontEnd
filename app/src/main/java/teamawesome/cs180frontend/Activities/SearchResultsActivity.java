package teamawesome.cs180frontend.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.Adapters.SearchResultsAdapter;
import teamawesome.cs180frontend.Misc.Review;
import teamawesome.cs180frontend.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Bind(R.id.results_list_view) ListView mResultsList;

    private String mProfessorName;
    private String mClassName;
    private SearchResultsAdapter mAdapter;
    public static final String REVIEW = "REVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Search Results");

        Bundle bundle = getIntent().getExtras();
        mProfessorName = bundle.getString(SearchActivity.PROFESSOR_NAME);
//        mClassName = bundle.getString(SearchActivity.CLASS_NAME);

        //TODO: Pull search results
        //NOTE: stubbed information


        if(mProfessorName != "") {
            //TODO: Search by professor

        }
//        else if(mProfessorName != "") {
//            //TODO: Search by professor
//
//        } else if(mClassName != "") {
//            //TODO: Search by class
//
//        }
        else {
            Toast.makeText(getBaseContext(), "Please try again", Toast.LENGTH_SHORT).show();
        }

        mAdapter = new SearchResultsAdapter(getBaseContext(),
                getApplicationContext().getResources().getStringArray(R.array.class_names),
                getApplicationContext().getResources().getStringArray(R.array.ratings),
                getApplicationContext().getResources().getStringArray(R.array.review_dates),
                getApplicationContext().getResources().getStringArray(R.array.reviews),
                getApplicationContext().getResources().getStringArray(R.array.reviewIDs));

//        mResultsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mResultsList.setAdapter(mAdapter);
        mResultsList.setOnItemClickListener(new ReviewSelectClickListener());


    }

    public class ReviewSelectClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(getBaseContext(), ReadReviewActivity.class);
            Review r = mAdapter.getItem(position);
            i.putExtra(REVIEW, (Serializable)r);
            startActivity(i);
        }
    }


}
