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

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.activity_search) CoordinatorLayout mParent;
    @Bind(R.id.search_professor_et) AutoCompleteTextView mProfessorName;
    @Bind(R.id.search_search_bt) Button mSearch;

    private List<ProfBundle> mProfessors;
    private String[] mProfessorNames;
    private String mUserProfessorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getString(R.string.search_reviews));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserProfessorName = "";
//        mUserClassName = "";

        //Fill autocomplete text views for professor and class choices
        mProfessors = DataSingleton.getInstance().getProfessorCache();
        mProfessorNames = new String[mProfessors.size()];
        for(int i = 0; i < mProfessors.size(); i++) {
            mProfessorNames[i] = mProfessors.get(i).getName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mProfessorNames);
        mProfessorName.setAdapter(profAdapter);
        mProfessorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUserProfessorName = parent.getItemAtPosition(position).toString();
            }
        });
    }

    @OnClick(R.id.search_search_bt)
    public void search() {

        if(mUserProfessorName == "") {
            String professorName = mProfessorName.getText().toString();
            Integer profId = DataSingleton.getInstance().getProfessorId(professorName);
            if(profId != null) {
                mUserProfessorName = professorName;
            } else {
                mProfessorName.setError(getString(R.string.prof_dne));
            }
        }

        if(mUserProfessorName == "") {
            mProfessorName.setError(getString(R.string.prof_dne));
        } else {
            Integer profId = DataSingleton.getInstance().getProfessorId(mUserProfessorName);
            System.out.println("PROFESSOR " + mUserProfessorName + " " + profId + "\nUSER ID " + Utils.getUserId(this));
            Intent i = new Intent(this, SearchResultsActivity.class);
            i.putExtra(getString(R.string.PROFESSOR_NAME), mUserProfessorName);
            i.putExtra(getString(R.string.PROFESSOR_ID), profId);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }
}
