package teamawesome.cs180frontend.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.Adapters.SimpleListAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.R;

public class SettingsActivity extends AppCompatActivity {

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
        Intent intent = new Intent(this, SettingElemActivity.class);
        intent.putExtra(Constants.TAG, adapter.getItem(pos));
        intent.putExtra(Constants.INDEX, pos);
        startActivity(intent);
    }
}
