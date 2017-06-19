package teamawesome.cs180frontend.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.activity_settings) CoordinatorLayout parent;
    @Bind(R.id.settings_lv) ListView settings;

    SimpleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> settingItems = Arrays.asList(getResources().getStringArray(R.array.settings_array));
        adapter = new SimpleListAdapter(this, settingItems);
        settings.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
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

    @OnItemClick(R.id.settings_lv)
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 2 && Utils.isVerified(this)) {
            Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.already_verified));
            return;
        }

        Intent intent = new Intent(this, SettingElemActivity.class);
        intent.putExtra(Constants.TAG, adapter.getItem(pos));
        intent.putExtra(Constants.INDEX, pos);
        startActivityForResult(intent, 1);
    }
}
