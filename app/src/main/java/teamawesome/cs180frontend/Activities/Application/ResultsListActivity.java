package teamawesome.cs180frontend.Activities.Application;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class ResultsListActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_results) CoordinatorLayout parent;
    @Bind(R.id.results_list_view) ListView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

        } else {
            //finish(); //May want to change this later
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

}
