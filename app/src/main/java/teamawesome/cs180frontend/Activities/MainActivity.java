package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.Adapters.NavDrawerAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.SPSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.drawer_list) ListView mDrawerList;
    @Bind(R.id.main_layout) CoordinatorLayout parent;
    @Bind(R.id.fab) FloatingActionButton mFab;

    private NavDrawerAdapter mAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";
    private int schoolId;

    ProgressDialog progressDialog;
    private Integer apiCnt = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);

        mNavTitles = getResources().getStringArray(R.array.nav_drawer_array);
        mIconTitles = getResources().getStringArray(R.array.nav_drawer_icon_array);

        mAdapter = new NavDrawerAdapter(mIconTitles, mNavTitles, this);

        mDrawerList.setAdapter(mAdapter);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerStateChanged(int state) {
                    Utils.hideKeyboard(parent, getApplicationContext());
                }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //PREVENT USER FROM GETTING ACCESS TO THE WRITE REVIEW ACTIVITY
        if (Utils.getUserId(this) == 0) {
            mFab.setVisibility(View.GONE);
        }

        schoolId = SPSingleton.getInstance(this).getSp().getInt(Constants.SCHOOL_ID, -1);
        if(schoolId != -1) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();

            RetrofitSingleton.getInstance()
                    .getMatchingService()
                    .getData(schoolId)
                    .enqueue(new GetCacheDataCallback());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void classResp(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(data);
        progressDialog.dismiss();
        Utils.showSnackbar(this, parent, getString(R.string.data_loaded));
    }

    @Subscribe
    public void intResp(Integer i) {
        if(i == 0) {
            Toast.makeText(this, getResources().getString(R.string.data_doesnt_exist), Toast.LENGTH_SHORT).show();
        } else if(i == -1) {
            Toast.makeText(this, getResources().getString(R.string.error_getting_data), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        //TODO: Write review activity
        Intent i = new Intent(this, WriteReviewActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
//        else {
//            //Logout
//            logout();
////            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @OnItemClick(R.id.drawer_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //Home
        } else if (position == 1) {
            //Search
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);
        } else if (position == 2) {
            //My Reviews
//            Intent i = new Intent(getApplicationContext(), MyReviewsActivity.class);
//            startActivity(i);
            Intent i = new Intent(getApplicationContext(), SearchResultsActivity.class);
            startActivity(i);
        } else if (position == 3) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            if (mAdapter.getItem(position).equals(getString(R.string.login))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 1);
            } else {
                logout();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mAdapter.changeLoginElem();
                System.out.println(Utils.getSchoolId(this));

                //Creating a new one since progressDialog could still possibly be null
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(schoolId)
                        .enqueue(new GetCacheDataCallback());
            }
        }
    }

    private void logout() {
        Utils.nukeUserData(this);
        mAdapter.changeLoginElem();
        //TODO: THIS TOAST MSG NEEDS TO BE CONSTANT
        //USING TOAST SINCE THEY'RE CONTEXT INSENSITIVE
        Toast.makeText(getBaseContext(), "Logging out", Toast.LENGTH_SHORT).show();
    }
}
