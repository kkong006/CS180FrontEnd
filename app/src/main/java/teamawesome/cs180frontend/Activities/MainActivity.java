package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Callback;
import teamawesome.cs180frontend.API.Models.ClassBundle;
import teamawesome.cs180frontend.API.Models.Professor;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetClassesCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
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
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProgressDialog2;
    private int mSchoolId;

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
//                @Override
//                public void onDrawerStateChanged(int state) {
//                    Utils.hideKeyboard(mainLayout, getApplicationContext());
//                }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        if (Utils.getUserId(this) != 0) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage(getString(R.string.loading));
//            progressDialog.show();

            System.out.println(Utils.getSchoolId(this));
//            RetrofitSingleton.getInstance().
//                    getMatchingService().
//                    getClasses(Utils.getSchoolId(this)).
//                    enqueue(new GetClassesCallback());
        }

        mSchoolId = SPSingleton.getInstance(this).getSp().getInt(Constants.SCHOOL_ID, -1);
        if(mSchoolId != -1) {
//            getClasses();
//            getProfessors();
        }
    }

    public void getClasses() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .getClasses(mSchoolId)
                .enqueue(callback);
    }

    public void getProfessors() {
        mProgressDialog2 = new ProgressDialog(this);
        mProgressDialog2.setCancelable(false);
        mProgressDialog2.setMessage(getResources().getString(R.string.loading));
        mProgressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog2.setIndeterminate(true);
//        mProgressDialog2.show();
        Callback callback = new GetReviewsCallback();
        RetrofitSingleton.getInstance().getMatchingService()
                .getProfs(mSchoolId)
                .enqueue(callback);
    }

//    @Subscribe
//    public void classResp(List<ClassBundle> classes) {
////        mProgressDialog.dismiss();
//        DataSingleton.getInstance().cacheClasses(classes);
//    }
//
//    @Subscribe
//    public void profResp(List<Professor> profs) {
////        mProgressDialog2.dismiss();
//        DataSingleton.getInstance().cacheProfessors(profs);
//    }
//
//    @Subscribe
//    public void intResp(Integer i) {
//        if(i == 0) {
////            Toast.makeText(this, getResources().getString(R.string.data_doesnt_exist), Toast.LENGTH_SHORT).show();
//        } else if(i == -1) {
////            Toast.makeText(this, getResources().getString(R.string.error_getting_data), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Subscribe
//    public void stringResp(String s) {
//        if(s == "ERROR") {
////            Toast.makeText(this, getResources().getString(R.string.error_getting_data), Toast.LENGTH_SHORT).show();
//        }
//    }


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
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
            }
        }
    }

    @Subscribe
    public void saveData(List<ClassBundle> classes) {
        progressDialog.dismiss();
        DataSingleton.getInstance().cacheClasses(classes);
        Utils.showSnackbar(this, parent, getString(R.string.data_loaded));
    }

//    @Subscribe
//    public void saveData(List<ClassBundle> classes) {
//        progressDialog.dismiss();
//        DataSingleton.getInstance().cacheClasses(classes);
//        Utils.showSnackbar(this, parent, getString(R.string.data_loaded));
//    }

    @Subscribe
    public void getClassesFailure(Integer code) {
        progressDialog.dismiss();
    }

    @Subscribe
    public void onError(String error) {
        progressDialog.dismiss();
        Utils.showSnackbar(this, parent, error);
    }

    private void logout() {
        Utils.nukeUserData(this);
        mAdapter.changeLoginElem();
        //TODO: THIS TOAST MSG NEEDS TO BE CONSTANT
        //USING TOAST SINCE THEY'RE CONTEXT INSENSITIVE
        Toast.makeText(getBaseContext(), "Logging out", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
