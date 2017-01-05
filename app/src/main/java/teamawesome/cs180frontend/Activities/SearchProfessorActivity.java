package teamawesome.cs180frontend.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SearchProfessorActivity extends AppCompatActivity {

    @Bind(R.id.activity_search_professor) CoordinatorLayout mParent;
    @Bind(R.id.search_professor_professor_et) AutoCompleteTextView mProfessorET;
    @Bind(R.id.search_professor_search_bt) Button mSearchBT;

    private String[] mProfessorNames;

    private String mSearchProfessorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_professor);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchProfessorName = "";

        //Fill autocomplete text view for professor choices
        List<ProfessorBundle> professors = DataSingleton.getInstance().getProfessorCache();
        mProfessorNames = new String[professors.size()];
        for(int i = 0; i < professors.size(); i++) {
            mProfessorNames[i] = professors.get(i).getProfessorName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mProfessorNames);
        mProfessorET.setAdapter(profAdapter);
        mProfessorET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchProfessorName = parent.getItemAtPosition(position).toString();
            }
        });
    }

    @OnClick(R.id.search_professor_search_bt)
    public void getProfessorStats() {
        if(mSearchProfessorName == "") {
            String professorName = mProfessorET.getText().toString();
            Integer profId = DataSingleton.getInstance().getProfessorId(professorName);
            if(profId != null) {
                mSearchProfessorName = professorName;
            } else {
                mProfessorET.setError(getString(R.string.professor_dne));
            }
        }

        if(mSearchProfessorName == "") {
            mProfessorET.setError(getString(R.string.professor_dne));
        } else {
            Integer profId = DataSingleton.getInstance().getProfessorId(mSearchProfessorName);
            System.out.println("PROFESSOR " + mSearchProfessorName + " " + profId + "\nUSER ID " + Utils.getUserId(this));
            Intent intent = new Intent(this, SearchProfessorResultsActivity.class);
            intent.putExtra(getString(R.string.PROFESSOR_NAME), mSearchProfessorName);
            intent.putExtra(getString(R.string.PROFESSOR_ID), profId);
            startActivity(intent);
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
