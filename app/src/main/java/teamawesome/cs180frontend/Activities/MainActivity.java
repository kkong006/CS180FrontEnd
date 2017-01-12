package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.Models.DataModel.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
import teamawesome.cs180frontend.Adapters.NavDrawerAdapter;
import teamawesome.cs180frontend.Misc.DataSingleton;
import teamawesome.cs180frontend.Misc.Utils;
import teamawesome.cs180frontend.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.drawer_list) ListView mDrawerList;
    @Bind(R.id.main_layout) CoordinatorLayout parent;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.main_srl) SwipeRefreshLayout mainSWL;
    @Bind(R.id.feed_list_view) ListView mFeedList;

    private NavDrawerAdapter mAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";

    private MainFeedAdapter mainFeedAdapter;

    ProgressDialog mProgressDialog;
    DataSingleton data;

    //private Integer apiCnt = new Integer(0);
    int offset = 0;
    int lastSelected = 0;

    AdRequest adRequest = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        data = DataSingleton.getInstance();
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.home));

        mainSWL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TO-DO: Implement refreshing
            }
        });

        setOnScrollListener();

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

        adRequest = new AdRequest.Builder().build();
        mainFeedAdapter = new MainFeedAdapter(this, adRequest, new ArrayList<ReviewRespBundle>());
        mFeedList.setAdapter(mainFeedAdapter);

        System.out.println("GETTING DATA");
        getData();
    }

    private void getData() {
        mProgressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .getData(Utils.getSchoolId(this))
                .enqueue(new GetCacheDataCallback());
    }

    private void getFeed(int offset) {
        mProgressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviews(null, Utils.getSchoolId(this), Utils.getUserId(this), offset)
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

    //TODO: Make this more concise & reuse callback later!!
    private void setOnScrollListener() {
        mFeedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if ((lastVisibleIndex == (totalItemCount - 1)) && totalItemCount != 0 &&
                        mainFeedAdapter.getItem(lastVisibleIndex) == null) {
                    if (lastSelected != lastVisibleIndex) {
                        lastSelected = lastVisibleIndex;

                        RetrofitSingleton.getInstance()
                                .getMatchingService()
                                .reviews(null, Utils.getSchoolId(getApplicationContext()),
                                        Utils.getUserId(getApplicationContext()), offset)
                                .enqueue(new GetReviewsCallback());
                    }
                }
            }
        });
    }

    @Subscribe
    public void dataResp(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(data);
        getFeed(offset);
    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        System.out.println("REVIEW COUNT " + reviewList.size());
        if(reviewList != null) {
            offset += reviewList.size();

            if (reviewList.size() > 2) {
                reviewList.add(2, null);
            }

            System.out.println("offset: " + offset);

            mainFeedAdapter.append(reviewList);
            mFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mProgressDialog.dismiss();
            mFeedList.setVisibility(View.VISIBLE);

            /*if(reviewList.size() == 0) {
                Utils.showSnackbar(this, parent, getString(R.string.reviews_dne));
            }*/
        }
    }

    @OnItemClick(R.id.feed_list_view)
    public void onReviewClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
        ReviewRespBundle review = mainFeedAdapter.getItem(position);
        if (review != null) {
            Intent intent = new Intent(this, ReadReviewActivity.class);
            intent.putExtra("review", (Parcelable) review);
            startActivity(intent);
        }
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
            return;
        } else if (position == 1) {
            /*Search for reviews
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);*/
        } else if (position == 2) {
            //Search for professor stats
            Intent intent = new Intent(getApplicationContext(), SearchProfessorActivity.class);
            startActivity(intent);
        }
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
                System.out.println("ONACTIITYRESULT");

                //Creating a new one since progressDialog could still possibly be null
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                System.out.println("OnActivityResult");
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
