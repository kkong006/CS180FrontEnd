package teamawesome.cs180frontend.Activities.Application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;
import teamawesome.cs180frontend.Runnable.InitSearchRunnable;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.search_parent) CoordinatorLayout parent;
    @Bind(R.id.input_layout) LinearLayout inputLayout;
    @Bind(R.id.search_list) ListView searchList;
    @Bind(R.id.search_textview) TextView searchTextView;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind({R.id.spinner, R.id.query_text, R.id.search_button}) View[] inputViews;

    ArrayAdapter<CharSequence> adapter;
    SimpleListAdapter resultsAdapter;

    private Handler handler;
    private Context context = this;

    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = ArrayAdapter.createFromResource(this, R.array.search_array,
                R.layout.large_text_view);
        ((Spinner) inputViews[0]).setAdapter(adapter);
        resultsAdapter = new SimpleListAdapter(this, new ArrayList<String>());
        searchList.setAdapter(resultsAdapter);
        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(parent, this);
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

    @OnClick(R.id.search_button)
    public void onSearch() {
        if (isSearching) {
            return;
        }

        String queryText = ((EditText) inputViews[1]).getText().toString();
        if (queryText.isEmpty()) {
            return;
        }

        isSearching = true;
        Utils.hideKeyboard(parent, this);
        handler.postDelayed(new InitSearchRunnable(this, inputViews, searchTextView,
                searchList, progressBar), 125);
    }

    @OnItemClick(R.id.search_list)
    public void onResultClicked(AdapterView<?> parent, View view, final int pos, long id) {
        if (((Spinner) inputViews[0]).getSelectedItem().
                toString().equals(getString(R.string.classes))) {
            String className = resultsAdapter.getItem(pos);

            Intent intent = new Intent(this, ReviewsActivity.class);
            intent.putExtra(Constants.ID_TYPE, Constants.CLASS_ID)
                    .putExtra(Constants.NAME, className)
                    .putExtra(Constants.CLASS_ID, DataSingleton.getInstance().getClassId(className));
            startActivity(intent);
        } else {
            new AlertDialog.Builder(this)
                    .setItems(R.array.search_option_array, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            if (index == 0) {
                                String subjectIdent = resultsAdapter.getItem(pos).split(" ")[0];

                                Intent intent = new Intent(context, ReviewsActivity.class);
                                intent.putExtra(Constants.ID_TYPE, Constants.SUBJECT_ID)
                                        .putExtra(Constants.NAME, subjectIdent)
                                        .putExtra(Constants.SUBJECT_ID, DataSingleton.getInstance().getSubjectId(subjectIdent));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, ResultsListActivity.class);
                                //TODO: figure out what values to put in later
                                startActivity(intent);
                            }
                        }
                    }).create().show();
        }
    }

    @Subscribe
    public void finishedSearch(List<String> results) {
        for (View v : inputViews) {
            v.setEnabled(true);
        }

        if (results.isEmpty()) {
            searchTextView.setVisibility(View.VISIBLE);
            isSearching = false;
            return;
        }

        isSearching = false;
        resultsAdapter.loadNewList(results);
        searchList.setVisibility(View.VISIBLE);
    }
}
