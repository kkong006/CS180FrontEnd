package teamawesome.cs180frontend.Activities.Application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import teamawesome.cs180frontend.Adapters.SimpleACAdapter;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SubmitDataActivity extends AppCompatActivity {
    @Bind(R.id.activity_submit_data)
    CoordinatorLayout parent;
    @Bind(R.id.data_school_ac)
    AutoCompleteTextView schoolAC;
    @Bind(R.id.subject_data)
    LinearLayout subjectData;
    @Bind(R.id.class_data)
    LinearLayout classData;
    @Bind(R.id.prof_data)
    LinearLayout profData;
    @Bind(R.id.system_type_spinner)
    Spinner systemTypeSpinner;
    @Bind(R.id.new_school_tv)
    TextView newSchoolTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_data);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleACAdapter adapter = new SimpleACAdapter(this, R.layout.simple_list_item,
                DataSingleton.getInstance().getSchoolCache());
        schoolAC.setAdapter(adapter);
        SimpleListAdapter systemChoices = new SimpleListAdapter(this,
                Arrays.asList(getResources().getStringArray(R.array.system_type_array)));
        systemTypeSpinner.setAdapter(systemChoices);
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
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.add_subjects, R.id.add_classes, R.id.add_profs})
    public synchronized void addDataView(View v) {
        Utils.hideKeyboard(parent, this);

        ViewGroup parent;
        int hint;

        if (v.getId() == R.id.add_subjects) {
            parent = subjectData;
            hint = R.string.subject_name;
        } else if (v.getId() == R.id.add_classes) {
            parent = classData;
            hint = R.string.class_name;
        } else {
            parent = profData;
            hint = R.string.prof_name;
        }

        createView(parent, hint);
    }

    @OnClick(R.id.submit_data_button)
    public void submitData() {
        Utils.hideKeyboard(parent, this);

        StringBuilder sb = new StringBuilder();
        String school = schoolAC.getText().toString();

        if (school.isEmpty()) {
            Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.enter_school));
            return;
        }

        boolean schoolExists = Utils.doesSchoolExist(school);
        if (!schoolExists) {
            if (newSchoolTV.getVisibility() == View.GONE) {
                newSchoolTV.setVisibility(View.VISIBLE);
                systemTypeSpinner.setVisibility(View.VISIBLE);
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.what_system_type));
                return;
            }
        } else {
            newSchoolTV.setVisibility(View.GONE);
            systemTypeSpinner.setVisibility(View.GONE);
        }

        sb.append(String.format(getString(R.string.submit_data_header), school)).append("\n\n");

        if (schoolExists) {
            sb.append(String.format(getString(R.string.school_to_add), school))
                    .append(String.format("Quarters or semesters: %s\n\n",
                            systemTypeSpinner.getSelectedItem().toString()));
        } else {
            sb.append(String.format("School name: %s\n\n", school));
        }

        int validRowCnt = 0; //checking to see if the user actually entered in data
        validRowCnt += parseDataIntoSb(sb, subjectData, getString(R.string.subjects));
        validRowCnt += parseDataIntoSb(sb, classData, getString(R.string.classes));
        validRowCnt += parseDataIntoSb(sb, profData, getString(R.string.professors));

        if (validRowCnt == 0) {
            Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.empty_data));
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, String.format("Missing data for %s", school));
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void createView(ViewGroup parent, int hint) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.data_edit_text, null);
        ((EditText) v.findViewById(R.id.data_edit_text)).setHint(hint);
        parent.addView(v);
    }

    public int parseDataIntoSb(StringBuilder sb, ViewGroup parent, String tag) {
        sb.append(tag).append(":\n");

        int validDataCnt = 0;
        for (int i = 0; i < parent.getChildCount(); ++i) {
            String data = ((EditText) parent.getChildAt(i)
                    .findViewById(R.id.data_edit_text))
                    .getText().toString();

            if (!data.isEmpty()) {
                ++validDataCnt;
                sb.append(data).append("\n");
            }
        }

        sb.append("\n");
        return validDataCnt;
    }
}
