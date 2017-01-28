package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import teamawesome.cs180frontend.API.APIConstants;
import teamawesome.cs180frontend.API.Models.DataModel.CacheData.CacheDataBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.CacheReqStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRespBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewRatingStatus;
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
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.main_srl) SwipeRefreshLayout mainSWL;
    @Bind(R.id.feed_list_view) ListView mainFeedList;
    @Bind(R.id.error_tv) TextView errorTV;

    private NavDrawerAdapter drawerAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";

    private MainFeedAdapter mainFeedAdapter;

    ProgressDialog progressDialog;
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

        if (Utils.getUserId(this) <= 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        mainSWL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TO-DO: Implement refreshing
            }
        });

        setOnScrollListener();
        setUpNavBar();

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
        setButtons();
        loadProgressDialog();

        adRequest = new AdRequest.Builder()
                .addKeyword("college")
                .addKeyword("university")
                .build();

        setUpAdapter();
        getData();
    }

    public void setUpNavBar() {
        String[] navTitles = getResources().getStringArray(R.array.nav_drawer_array);
        String[] iconTitles = getResources().getStringArray(R.array.nav_drawer_icon_array);

        drawerAdapter = new NavDrawerAdapter(iconTitles, navTitles, this);
        mDrawerList.setAdapter(drawerAdapter);
    }

    public void loadProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    public void setUpAdapter() {
        mainFeedAdapter = new MainFeedAdapter(this, adRequest, new ArrayList<ReviewRespBundle>());
        mainFeedList.setAdapter(mainFeedAdapter);
    }

    private void getData() {
        progressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .getData(Utils.getSchoolId(this), Utils.getUserId(this))
                .enqueue(new GetCacheDataCallback());
    }

    private void getFeed(int offset) {
        progressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviews(Utils.getSchoolId(this),
                        null, null, null,
                        Utils.getUserId(this), offset)
                .enqueue(new GetReviewsCallback());
    }

    //Show/hide the FAB and tool bar
    private void setButtons() {
        if (Utils.getUserId(this) < 1) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setOnScrollListener() {
        final Context context = this;
        mainFeedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if ((lastVisibleIndex == (totalItemCount - 1)) && totalItemCount != 0 &&
                        mainFeedAdapter.getItem(lastVisibleIndex) == null) {
                    if (lastSelected != lastVisibleIndex) {
                        lastSelected = lastVisibleIndex;

                        RetrofitSingleton.getInstance()
                                .getMatchingService()
                                .reviews(Utils.getSchoolId(context),
                                        null, null, null,
                                        Utils.getUserId(context), offset)
                                .enqueue(new GetReviewsCallback());
                    }
                }
            }
        });
    }

    @Subscribe
    public void dataResp(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(this, data);
        System.out.println("Liked: " + data.getReviewRatings().getLiked().size());
        System.out.println("Disliked: " + data.getReviewRatings().getDisliked().size());
        getFeed(offset);
    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        System.out.println("REVIEW COUNT " + reviewList.size());
        offset += reviewList.size();

        if (mainFeedAdapter.getCount() == 0 && offset == 0) {
            mainFeedList.setVisibility(View.GONE);
            errorTV.setText(getString(R.string.fetch_feed_again));
            errorTV.setVisibility(View.VISIBLE);
        }

        if (reviewList.size() > 2) {
            reviewList.add(2, null);
        }

        System.out.println("offset: " + offset);

        mainFeedAdapter.append(reviewList);
        mainFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressDialog.dismiss();
        mainFeedList.setVisibility(View.VISIBLE);

            /*if(reviewList.size() == 0) {
                Utils.showSnackbar(this, parent, getString(R.string.reviews_dne));
            }*/
    }

    @Subscribe
    public void failedDataResp(CacheReqStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() != -1) {
            Utils.showSnackbar(this, parent, getString(R.string.data_doesnt_exist));
        } else {
            Utils.showSnackbar(this, parent, getString(R.string.error_getting_data));
        }

        mainFeedList.setVisibility(View.GONE);
        errorTV.setText(getString(R.string.fetch_data_again));
        errorTV.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void failedReviewFetch(ReviewFetchStatus failedFetch) {
        progressDialog.dismiss();
        if (failedFetch.getStatus() != -1) {
            Utils.showSnackbar(this, parent, getString(R.string.invalid_review_request));
        } else {
            Utils.showSnackbar(this, parent, getString(R.string.failed_review_request));
        }

        mainFeedList.setVisibility(View.GONE);
        errorTV.setText(getString(R.string.fetch_feed_again));
        errorTV.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onFailedResp(ReviewRatingStatus status) {
        if (status.getStatus() == APIConstants.HTTP_STATUS_INVALID) {
            Toast.makeText(this, getString(R.string.INVALID_REVIEW_RATING_REQ), Toast.LENGTH_SHORT).show();
        } else if (status.getStatus() == APIConstants.HTTP_STATUS_ERROR) {
            Toast.makeText(this, getString(R.string.SERVER_ERROR), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.FAILED_REVIEW_RATING), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void reviewRatingResp(ReviewRatingResp resp) {
        int rating = resp.getReviewRatingVal();
        if (rating == 0) {
            data.getLikedSet().remove(resp.getReviewId());
            data.getDislikedSet().remove(resp.getReviewId());
        } else if (rating == 1) {
            data.getLikedSet().add(resp.getReviewId());
            data.getDislikedSet().remove(resp.getReviewId());
        } else if (rating == 2) {
            data.getLikedSet().remove(resp.getReviewId());
            data.getDislikedSet().add(resp.getReviewId());
        }
    }

    @OnClick(R.id.error_tv)
    public void onErrorTVClick() {
        String errorTVText = errorTV.getText().toString();

        if (errorTVText.equals(getString(R.string.fetch_data_again))) {
            getData();
        } else if (errorTVText.equals(getString(R.string.fetch_feed_again))) {
            getFeed(offset);
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Intent intent = new Intent(this, WriteReviewActivity.class);
        startActivity(intent);
    }

    @OnItemClick(R.id.feed_list_view)
    public void onReviewClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
        ReviewRespBundle review = mainFeedAdapter.getItem(position);
        if (review != null) {
            Intent intent = new Intent(this, ReadReviewActivity.class);
            intent.putExtra("review", (Parcelable) review);
            intent.putExtra("yourRating", Utils.getReviewRating(review.getReviewId()));
            startActivity(intent);
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
        if (Utils.getSchoolId(this) < 1) {
            Utils.showSnackbar(this, parent, getString(R.string.please_sign_in));
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 2);
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
        } else if (position == 3) {

        } else if (position == 4) {
            //Login or logout
            if (drawerAdapter.getItem(position).equals(getString(R.string.login))) {
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

        if (progressDialog != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                drawerAdapter.changeLoginElem();
                progressDialog.show();

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(Utils.getSchoolId(this), Utils.getUserId(this))
                        .enqueue(new GetCacheDataCallback());
            }
            setButtons();
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                offset = 0;
                System.out.println(Utils.getSchoolId(this));
                mainFeedAdapter.clear(); //Need to reload the schools
                progressDialog.show();

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(Utils.getSchoolId(this), Utils.getUserId(this))
                        .enqueue(new GetCacheDataCallback());
            }
        }
    }

    private void logout() {
        Utils.nukeUserData(this);
        drawerAdapter.changeLoginElem();
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
