package teamawesome.cs180frontend.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.R;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.professor_et) EditText mProfessorName;
    @Bind(R.id.school_et) EditText mSchoolName;
//    @Bind(R.id.class_et) EditText mClassName;
    @Bind(R.id.search_bt) Button mSearch;

    public static final String PROFESSOR_NAME = "PROFESSOR_NAME";
    public static final String SCHOOL_NAME = "SCHOOL_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String professorName = mProfessorName.getText().toString();
                String schoolName = mSchoolName.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(PROFESSOR_NAME, professorName);
                bundle.putString(SCHOOL_NAME, schoolName);

                Intent i = new Intent(getApplicationContext(), SearchResultsActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}
