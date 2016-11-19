package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.okhttp.internal.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.Models.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.ReviewRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
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
    @Bind(R.id.feed_list_view) ListView mFeedList;

    AdView ad;

    private NavDrawerAdapter mAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";

    private int mPosition;

    private MainFeedAdapter mFeedAdapter;

    ProgressDialog mProgressDialog;
    DataSingleton data;

    private Integer apiCnt = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        data = DataSingleton.getInstance();
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        ad = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        ad.loadAd(request);

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
//        setButtons();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mPosition = 0;

        getData();
    }

    private void getData() {
        mProgressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .getData(Utils.getSchoolId(this))
                .enqueue(new GetCacheDataCallback());
    }

    private void getFeed() {
        System.out.println("MAKING CALL TO GET FEED REVIEWS " + Utils.getSchoolId(this) + " " + DataSingleton.getInstance().getSchoolName(Utils.getSchoolId(this)) + " " +  Utils.getUserId(this));
        mProgressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviewsSchool(Utils.getSchoolId(this), Utils.getUserId(this))
                .enqueue(new GetReviewsCallback());
    }

    //Show/hide the FAB and tool bar
    private void setButtons() {
        if (Utils.getUserId(this) < 1) {
            mFab.setVisibility(View.GONE);
        } else {
            mFab.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void dataResp(CacheDataBundle data) {
        mProgressDialog.dismiss();
        DataSingleton.getInstance().cacheDataBundle(data);
        System.out.println("SCHOOL ID " + Utils.getSchoolId(this));
        System.out.println("SCHOOL SIZE " + DataSingleton.getInstance().getSchoolCache().size());
        System.out.println("PROFESSOR SIZE " + DataSingleton.getInstance().getProfessorCache().size());
        System.out.println("CLASS SIZE " + DataSingleton.getInstance().getClassCache().size());
        System.out.println("SUBJECT SIZE " + DataSingleton.getInstance().getSubjectCache().size());
        getFeed();
    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        mProgressDialog.dismiss();
        System.out.println("REVIEW COUNT " + reviewList.size());
        if(reviewList != null) {

            mFeedAdapter = new MainFeedAdapter(this, reviewList);
            mFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mFeedList.setAdapter(mFeedAdapter);

            if(mPosition >= 0 && mPosition < reviewList.size()) {
                mFeedList.setSelection(mPosition);
                mPosition = 0;
            }

            if(reviewList.size() == 0) {
                Utils.showSnackbar(this, parent, getString(R.string.reviews_dne));
            }
        }
    }

    @OnItemClick(R.id.feed_list_view)
    public void onReviewClick(AdapterView<?> parent, View view, int position, long id) {
        ReviewRespBundle review = mFeedAdapter.getItem(position);
        Intent intent = new Intent(getBaseContext(), ReadReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.REVIEW_ID), review.getReviewId());
        bundle.putInt(getString(R.string.REVIEW_RATING), review.getRating());
        System.out.println("PASSING USER RATING " + review.getReviewRating());
        bundle.putString(getString(R.string.REVIEW_CONTENT), review.getMessage());
        bundle.putString(getString(R.string.REVIEW_CLASS_NAME), data.getClassName(review.getClassId()));
        bundle.putString(getString(R.string.REVIEW_DATE), review.getReviewDate());
        bundle.putInt(getString(R.string.REVIEW_USER_RATING), review.getReviewRating());
        bundle.putString(getString(R.string.PROFESSOR_NAME), data.getProfessorName(review.getProfId()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Subscribe
    public void intResp(Integer i) {
        mProgressDialog.dismiss();
        if (i.equals(0)) {
            Utils.showSnackbar(this, parent, getString(R.string.data_doesnt_exist));
        } else if (i.equals(-1)) {
            Utils.showSnackbar(this, parent, getString(R.string.error_getting_data));
        }
    }

    @Subscribe
    public void stringResp(String s) {
        mProgressDialog.dismiss();
        if(s.equals("ERROR")) {
            Utils.showSnackbar(this, parent, getString(R.string.error_getting_data));
        }
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
        if(Utils.getSchoolId(this) < 1) {
            Utils.showSnackbar(this, parent, getString(R.string.please_sign_in));
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnItemClick(R.id.drawer_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (position == 0) {
            //Home Feed
        } else if (position == 1) {
            //Search for reviews
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);
        } else if (position == 2) {
            //Search for professor stats
            Intent intent = new Intent(getApplicationContext(), SearchProfessorActivity.class);
            startActivity(intent);

        }
//        else if (position == 3) {
//            //Show user reviews
////            Intent i = new Intent(getApplicationContext(), MyReviewsActivity.class);
////            startActivity(i);
//            Intent i = new Intent(getApplicationContext(), SearchResultsActivity.class);
//            startActivity(i);
//        }
        else if (position == 3) {
            //Login or logout
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
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(Utils.getSchoolId(this))
                        .enqueue(new GetCacheDataCallback());
            }
        }
        setButtons();
    }

    private void logout() {
        Utils.nukeUserData(this);
        mAdapter.changeLoginElem();
        setButtons();
        Toast.makeText(this, getString(R.string.log_out), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        //TODO: Write review activity
        Intent i = new Intent(this, WriteReviewActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
        getData();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
}
