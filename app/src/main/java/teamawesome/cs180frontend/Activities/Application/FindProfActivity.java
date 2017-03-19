package teamawesome.cs180frontend.Activities.Application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.DataModel.ProfBundle;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class FindProfActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_prof) CoordinatorLayout parent;
    @Bind(R.id.search_prof_et) AutoCompleteTextView profET;
    @Bind(R.id.search) Button search;

    private String[] mProfessorNames;

    private String mSearchProfessorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_prof);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchProfessorName = "";

        //Fill autocomplete text view for professor choices
        List<ProfBundle> professors = DataSingleton.getInstance().getProfessorCache();
        mProfessorNames = new String[professors.size()];
        for(int i = 0; i < professors.size(); i++) {
            mProfessorNames[i] = professors.get(i).getProfessorName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mProfessorNames);
        profET.setAdapter(profAdapter);
        profET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchProfessorName = parent.getItemAtPosition(position).toString();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(parent, this);
    }

    @OnClick(R.id.search)
    public void getProfessorStats() {
        profET.setError(null);
        Utils.hideKeyboard(parent, this);

        String profName = profET.getText().toString();

        if (profName.length() == 0) {
            profET.setError(getString(R.string.no_prof_entered));
            return;
        }

        Integer profId = DataSingleton.getInstance().getProfessorId(profName);

        if (profId == null) {
            profET.setError(getString(R.string.prof_dne));
            return;
        }

        Intent intent = new Intent(this, ProfSummaryActivity.class);
        intent.putExtra(getString(R.string.PROFESSOR_NAME), mSearchProfessorName);
        intent.putExtra(getString(R.string.PROFESSOR_ID), profId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_250, R.anim.slide_left_out_250);
        finish();
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
}
