package teamawesome.cs180frontend.Activities.Application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.R;
import teamawesome.cs180frontend.Runnable.SearchRunnable;

public class ResultsListActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_results) CoordinatorLayout parent;
    @Bind(R.id.results_lv) ListView resultList;
    @Bind(R.id.results_pb) ProgressBar progressBar;

    SimpleListAdapter simpleListAdapter;

    String tag;
    String subjectIdent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tag = extras.getString(Constants.TAG);
            getSupportActionBar().setTitle(tag);
            subjectIdent = extras.getString(Constants.NAME); //iffy about reusing name as key val here

            new Handler().postDelayed(new SearchRunnable(this, tag, subjectIdent, progressBar), 250);
        } else {
            finish(); //May want to change this later
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

    @OnItemClick(R.id.results_lv)
    public void onResultClicked(AdapterView<?> parent, View view, final int pos, long id) {
        String className = simpleListAdapter.getItem(pos);
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(Constants.ID_TYPE, Constants.CLASS_ID)
                .putExtra(Constants.NAME, className)
                .putExtra(Constants.CLASS_ID, DataSingleton.getInstance().getClassId(className));
        startActivity(intent);
    }

    @Subscribe
    public void onResultList(List<String> results) {
        simpleListAdapter = new SimpleListAdapter(this, results);
        resultList.setAdapter(simpleListAdapter);
        resultList.setVisibility(View.VISIBLE);
    }

}
