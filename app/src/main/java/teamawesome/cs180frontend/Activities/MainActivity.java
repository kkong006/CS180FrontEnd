package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewBundle;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewPageBundle;
import teamawesome.cs180frontend.API.Models.StatusModel.CacheReqStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
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

    //Verify layout bindings
    @Bind(R.id.verify_acc) LinearLayout mainVerifyLayout;
    @Bind(R.id.verify_snippet_text) TextView verifySnippetText;
    @Bind(R.id.verify_body) LinearLayout verifyBody;
    @Bind(R.id.later) Button later;

    private NavDrawerAdapter drawerAdapter;
    private MainFeedAdapter mainFeedAdapter;

    AbsListView.OnScrollListener mainLVScroll = null;

    ProgressDialog progressDialog;

    DataSingleton data;

    int offset = 0;
    int lastSelected = 0;

    AdRequest adRequest = null;

    boolean isLoading = false;
    boolean isRefreshing = false;
    boolean initialLoad = true;

    final Context context = this;

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
                if (!isLoading) {
                    mainFeedList.setOnScrollListener(null);
                    isLoading = true;
                    isRefreshing = true;
                    offset = 0;

                    RetrofitSingleton.getInstance()
                            .getMatchingService()
                            .reviews(Utils.getSchoolId(context),
                                    null, null, null,
                                    null, offset)
                            .enqueue(new GetReviewsCallback(context));
                    //refresh
                }
            }
        });

        initOnScrollListener();
        mainFeedList.setOnScrollListener(mainLVScroll);
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
        mainFeedAdapter = new MainFeedAdapter(this, adRequest, new ArrayList<ReviewBundle>());
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
        isLoading = true;

        progressDialog.show();
        RetrofitSingleton.getInstance()
                .getMatchingService()
                .reviews(Utils.getSchoolId(this),
                        null, null, null,
                        null, offset)
                .enqueue(new GetReviewsCallback(this));
    }

    //Show/hide the FAB and tool bar
    private void setButtons() {
        if (Utils.getUserId(this) < 1) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void initOnScrollListener() {
        mainLVScroll = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if ((lastVisibleIndex == (totalItemCount - 1))
                        && totalItemCount != 0
                        && mainFeedAdapter.getItem(lastVisibleIndex) == null) {
                    if (!isLoading && lastSelected != lastVisibleIndex) {
                        isLoading = true;
                        lastSelected = lastVisibleIndex;

                        RetrofitSingleton.getInstance()
                                .getMatchingService()
                                .reviews(Utils.getSchoolId(context),
                                        null, null, null,
                                        null, offset)
                                .enqueue(new GetReviewsCallback(context));
                    }
                }
            }
        };
    }

    public void addToFeed(ReviewPageBundle page) {
        List<ReviewBundle> reviews = page.getReviews();

        if (mainFeedAdapter.getCount() == 0 && offset == 0) {
            progressDialog.dismiss();
            mainFeedList.setVisibility(View.GONE);
            errorTV.setText(getString(R.string.fetch_feed_again));
            errorTV.setVisibility(View.VISIBLE);
            return;
        }

        if (reviews.size() > 2) {
            reviews.add(2, null);
        }

        System.out.println("offset: " + offset);

        mainFeedAdapter.append(reviews);
        mainFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressDialog.dismiss();

        if (initialLoad) {
            initialLoad = false;
            if (Utils.isVerified(this)) {
                mainVerifyLayout.setVisibility(View.GONE);
            } else {
                mainVerifyLayout.setVisibility(View.VISIBLE);
            }
        }

        mainFeedList.setVisibility(View.VISIBLE);

        isLoading = false;
        isRefreshing = false;

        mainFeedList.setOnScrollListener(mainLVScroll);
    }

    private void shrinkVerifyLayout() {
        Utils.hideKeyboard(parent, this);
        verifySnippetText.setText(getString(R.string.please_verify_not_clicked));
        verifyBody.setVisibility(View.GONE);
    }

    @Subscribe
    public void dataResp(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(this, data);
        System.out.println("Liked: " + data.getReviewRatings().getLiked().size());
        System.out.println("Disliked: " + data.getReviewRatings().getDisliked().size());
        getFeed(offset);
    }

    @Subscribe
    public synchronized void reviewsResp(final ReviewPageBundle page) {
        if (page.getContext().equals(this)) {
            System.out.println("REVIEW COUNT " + page.getReviews().size());
            offset += page.getReviews().size();

            if (isRefreshing) {
                mainFeedList.setEnabled(false);
                mainFeedList.setSelection(0);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainFeedList.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lastSelected = 0;
                                mainSWL.setRefreshing(false);
                                mainFeedAdapter.clear();
                                addToFeed(page);
                                mainFeedList.setEnabled(true);
                            }
                        }, 150);
                    }
                }, 250);
                return;
            }

            addToFeed(page);
        }
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
    public void failedPageFetch(ReviewFetchStatus failedFetch) {
        initialLoad = false;
        if (failedFetch.getContext().equals(this)) {
            progressDialog.dismiss();
            if (failedFetch.getStatus() != -1) {
                Utils.showSnackbar(this, parent, getString(R.string.invalid_review_request));
            } else {
                Utils.showSnackbar(this, parent, getString(R.string.failed_review_request));
            }

            mainFeedList.setVisibility(View.GONE);
            errorTV.setText(getString(R.string.fetch_feed_again));
            errorTV.setVisibility(View.VISIBLE);

            isLoading = false;
        }
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
        isLoading = false;

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

    @OnClick(R.id.verify_acc)
    public void onVerifyLayoutClick() {
        if (verifyBody.getVisibility() == View.GONE) {
            verifySnippetText.setText(getString(R.string.please_verify_clicked));
            verifyBody.setVisibility(View.VISIBLE);
        } else {
            shrinkVerifyLayout();
        }
    }

    @OnClick(R.id.later)
    public void onLater() {
        if (verifyBody.getVisibility() == View.VISIBLE) {
            shrinkVerifyLayout();
        }
    }

    @OnClick(R.id.close_verify_view)
    public void hideVerifyLayout() {
        AlphaAnimation hide = Utils.createHideAnimation(mainVerifyLayout);
        mainVerifyLayout.startAnimation(hide);
    }

    @OnItemClick(R.id.drawer_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (position == 1) {
            Intent i = new Intent(this, MyReviewsActivity.class);
            startActivity(i);
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

    @OnItemClick(R.id.feed_list_view)
    public void onReviewClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
        ReviewBundle review = mainFeedAdapter.getItem(position);
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

                isLoading = true;

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

                isLoading = true;

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(Utils.getSchoolId(this), Utils.getUserId(this))
                        .enqueue(new GetCacheDataCallback());
            }
        }
    }
}
