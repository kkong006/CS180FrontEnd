package teamawesome.cs180frontend.Activities.Application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.Adapters.SimpleACAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class FindProfActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_prof) CoordinatorLayout parent;
    @Bind(R.id.search_prof_et) AutoCompleteTextView profAC;
    @Bind(R.id.search) Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_prof);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Fill autocomplete text view for professor choices
        SimpleACAdapter profNameAdapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getProfessorCache());
        profAC.setAdapter(profNameAdapter);
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
                finish();
                return true;
            default:
                return true;
        }
    }

    @OnClick(R.id.search)
    public void getProfessorStats() {
        profAC.setError(null);
        Utils.hideKeyboard(parent, this);

        String profName = profAC.getText().toString();

        if (profName.length() == 0) {
            profAC.setError(getString(R.string.no_prof_entered));
            return;
        }

        Integer profId = DataSingleton.getInstance().getProfessorId(profName);

        if (profId == null) {
            profAC.setError(getString(R.string.prof_dne));
            return;
        }

        Intent intent = new Intent(this, ProfSummaryActivity.class);
        intent.putExtra(Constants.PROF_NAME, profName);
        intent.putExtra(Constants.PROF_ID, profId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_250, R.anim.slide_left_out_250);
        finish();
    }
}
