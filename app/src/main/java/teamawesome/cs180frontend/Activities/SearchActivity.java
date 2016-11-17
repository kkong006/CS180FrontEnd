package teamawesome.cs180frontend.Activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.API.Models.ProfessorBundle;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.activity_search) CoordinatorLayout mParent;
    @Bind(R.id.search_professor_et) AutoCompleteTextView mProfessorName;
//    @Bind(R.id.search_class_et) AutoCompleteTextView mClassName;
//    @Bind(R.id.search_school_et) AutoCompleteTextView mSchoolName;
    @Bind(R.id.search_search_bt) Button mSearch;

    private List<ProfessorBundle> mProfessors;
//    private List<ClassBundle> mClasses;
//    private List<SchoolBundle> mSchools;

    private String[] mProfessorNames;
//    private String[] mClassNames;
//    private String[] mSchoolNames;

    private String mUserProfessorName;
//    private String mUserClassName;
//    private String mUserSchoolName;

//    private ProgressDialog mProgressDialog;

//    private String mProfessor;
//    private ProfessorBundle mReturnedProfessor;

//    public static final String PROFESSOR_NAME = "PROFESSOR_NAME";
//    public static final String CLASS_NAME = "CLASS_NAME";

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
            mProfessorNames[i] = mProfessors.get(i).getProfessorName();
        }
        ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mProfessorNames);
        mProfessorName.setAdapter(profAdapter);
        mProfessorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUserProfessorName = parent.getItemAtPosition(position).toString();
            }
        });

//        mClasses = DataSingleton.getInstance().getClassCache();
//        mClassNames = new String[mClasses.size()];
//        for(int i = 0; i < mClasses.size(); i++) {
//            mClassNames[i] = mClasses.get(i).getClassName();
//        }
//        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mClassNames);
//        mClassName.setAdapter(classAdapter);
//        mClassName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mUserClassName = parent.getItemAtPosition(position).toString();
//            }
//        });
    }

    @OnClick(R.id.search_search_bt)
    public void search() {

        if(mUserProfessorName == "") {
            String professorName = mProfessorName.getText().toString();
//            String className = mClassName.getText().toString();
            Integer profId = DataSingleton.getInstance().getProfessorId(professorName);
//            Integer classId = DataSingleton.getInstance().getClassId(className);
            if(!profId.equals(null)) {
                mUserProfessorName = professorName;
            }
//            if(!classId.equals(null)) {
//                mUserClassName = className;
//            }
        }

        if(mUserProfessorName == "") {
            Utils.showSnackbar(this, mParent, getString(R.string.professor_dne));
        } else {
            Integer profId = DataSingleton.getInstance().getProfessorId(mUserProfessorName);
            System.out.println("PROFESSOR " + mUserProfessorName + " " + profId + "\nUSER ID " + Utils.getUserId(this));
            Intent i = new Intent(this, SearchResultsActivity.class);
            i.putExtra(getString(R.string.PROFESSOR_NAME), mUserProfessorName);
            i.putExtra(getString(R.string.PROFESSOR_ID), profId);
            startActivity(i);
        }
    }
}
