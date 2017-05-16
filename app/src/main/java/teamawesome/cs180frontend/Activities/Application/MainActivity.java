package teamawesome.cs180frontend.Activities.Application;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import teamawesome.cs180frontend.API.Models.ReviewModel.ReviewRatingResp;
import teamawesome.cs180frontend.API.Models.StatusModel.CacheReqStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewFetchStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.ReviewRatingStatus;
import teamawesome.cs180frontend.API.Models.StatusModel.VerifyStatus;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyBundle;
import teamawesome.cs180frontend.API.Models.UserModel.VerifyResp;
import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetCacheDataCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.API.Services.Callbacks.VerifyCallback;
import teamawesome.cs180frontend.Activities.Onboarding.LoginActivity;
import teamawesome.cs180frontend.Activities.Settings.SettingsActivity;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
import teamawesome.cs180frontend.Adapters.NavDrawerAdapter;
import teamawesome.cs180frontend.Misc.Constants;
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
    @Bind(R.id.verify_body) LinearLayout verifyBody;
    @Bind(R.id.verify_snippet_text) TextView verifySnippetText;
    @Bind(R.id.pin_edittext) EditText pinEditText;
    @Bind(R.id.verify) Button verify;
    @Bind(R.id.later) Button later;

    private NavDrawerAdapter drawerAdapter;
    private MainFeedAdapter mainFeedAdapter;

    AbsListView.OnScrollListener mainLVScroll = null;

    ProgressDialog progressDialog;

    private DataSingleton data;
    private VerifyBundle bundle;
    private VerifyCallback verifyCallback;

    int offset = 0;
    int lastSelected = 0;

    private boolean isLoading = false;
    private boolean isRefreshing = false;
    private boolean initialLoad = true;
    private boolean verifiedDisabled = false; //to stop people from spamming verification stuff
    //using boolean vs hiding view because too many methods interact with the verification layout
    private boolean verifiedHidden = false;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.getString(Constants.MESSAGE, "").isEmpty()) {
            Toast.makeText(this, extras.getString(Constants.MESSAGE, ""), Toast.LENGTH_SHORT).show();
        }

        verifyCallback = new VerifyCallback(this);

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

        setUpAdapter();
        getData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.please_sign_in));
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
                progressDialog.setMessage(getString(R.string.loading));
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
                mainSWL.setRefreshing(false);
                errorTV.setVisibility(View.GONE);

                offset = 0;
                lastSelected = 0;
                isLoading = true;

                System.out.println(Utils.getSchoolId(this));
                mainFeedAdapter.clear();
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .getData(Utils.getSchoolId(this), Utils.getUserId(this))
                        .enqueue(new GetCacheDataCallback());
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String msg = intent.getExtras().getString(Constants.MESSAGE, null);
                if (msg != null) {
                    Utils.showSnackBar(this, parent, R.color.colorPrimary,
                            msg);
                }
            }
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
    public void onFabClick() {
        Intent intent = new Intent(this, WriteReviewActivity.class);
        startActivityForResult(intent, 3);
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

    @OnClick(R.id.verify)
    public void verify() {
        if (!verifiedDisabled) {
            String pin = pinEditText.getText().toString();
            if (pin.length() < 4) {
                Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.pin_too_short));
                return;
            }

            verifiedDisabled = true;
            Utils.hideKeyboard(parent, this);
            progressDialog.setMessage(getString(R.string.verifying));
            progressDialog.show();

            if (bundle == null) {
                bundle = new VerifyBundle(Utils.getUserId(this),
                        Utils.getPassword(this), pin);
            } else {
                bundle.changeValues(Utils.getUserId(this),
                        Utils.getPassword(this), pin);
            }

            RetrofitSingleton.getInstance()
                    .getUserService()
                    .verifyUser(bundle)
                    .enqueue(verifyCallback);
        }
    }

    @OnClick(R.id.later)
    public void onLater() {
        Utils.hideKeyboard(parent, this);
        if (verifyBody.getVisibility() == View.VISIBLE) {
            shrinkVerifyLayout();
        }
    }

    @OnClick(R.id.close_verify_view)
    public void hideVerifyLayout() {
        if (!verifiedDisabled) {
            verifiedDisabled = true; //probably don't need this, just in case though
            verifiedHidden = true;
            AlphaAnimation hide = Utils.createHideAnimation(mainVerifyLayout);
            mainVerifyLayout.startAnimation(hide);
        }
    }

    @OnItemClick(R.id.drawer_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (position == 1) {
            Intent intent = new Intent(this, ReviewsActivity.class);
            intent.putExtra(Constants.ID_TYPE, Constants.USER_ID);
            intent.putExtra(Constants.USER_ID, Utils.getUserId(this));
            startActivity(intent);
        } else if (position == 2) {
            //Search for professor stats
            Intent intent = new Intent(this, FindProfActivity.class);
            startActivity(intent);
        } else if (position == 3) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (position == 4) {
            //Logout
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
        ReviewBundle review = mainFeedAdapter.getItem(position);
        if (review != null) {
            Intent intent = new Intent(this, ReadReviewActivity.class);
            intent.putExtra(Constants.REVIEW, (Parcelable) review);
            intent.putExtra(Constants.USER_RATING, Utils.getReviewRating(review.getReviewId()));
            startActivity(intent);
        }
    }

    @Subscribe
    public void failedDataResp(CacheReqStatus resp) {
        progressDialog.dismiss();
        if (resp.getStatus() != -1) {
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.data_doesnt_exist));
        } else {
            Utils.showSnackBar(this, parent, R.color.colorPrimary,
                    getString(R.string.error_getting_data));
        }

        mainFeedList.setVisibility(View.GONE);
        errorTV.setText(getString(R.string.fetch_data_again));
        errorTV.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void failedPageFetch(ReviewFetchStatus failedFetch) {
        if (failedFetch.getContext().equals(this)) {
            initialLoad = false;
            progressDialog.dismiss();
            if (failedFetch.getStatus() != -1) {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.invalid_review_request));
            } else {
                Utils.showSnackBar(this, parent, R.color.colorPrimary,
                        getString(R.string.failed_review_request));
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
            Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
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

    @Subscribe
    public void onVerifyResp(VerifyResp resp) {
        mainVerifyLayout.setVisibility(View.GONE);
        Utils.setVerified(this, true);
        progressDialog.dismiss();
        Utils.showSnackBar(this, parent, R.color.colorPrimary, getString(R.string.verify_success));
    }

    @Subscribe
    public void onVerifyFailed(VerifyStatus status) {
        if (status.getContext().equals(this)) {
            verifiedDisabled = false;
            Utils.failedVerifySnackBar(progressDialog, parent, R.color.colorPrimary, status.getStatus(), this);
        }
    }

    @Subscribe
    public void onDataFetched(CacheDataBundle data) {
        DataSingleton.getInstance().cacheDataBundle(this, data);
        System.out.println("Liked: " + data.getReviewRatings().getLiked().size());
        System.out.println("Disliked: " + data.getReviewRatings().getDisliked().size());
        getFeed(offset);
    }

    @Subscribe
    public void reviewsResp(final ReviewPageBundle page) {
        if (page.getContext().equals(this)) {
            System.out.println("REVIEW COUNT " + page.getReviews().size());
            offset += page.getReviews().size();

            final Handler handler = new Handler();
            if (isRefreshing) {
                mainFeedList.setEnabled(false);
                mainFeedList.setSelection(0);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainFeedList.setVisibility(View.GONE);
                        refreshFeed(handler, page);
                    }
                }, 250);
                return;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addToFeed(page);
                }
            }, 150);
        }
    }

    private void logout() {
        Utils.nukeUserData(this);
        //drawerAdapter.changeLoginElem();
        //setButtons();
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
        mainFeedAdapter = new MainFeedAdapter(this, new ArrayList<ReviewBundle>());
        mainFeedList.setAdapter(mainFeedAdapter);
    }

    private void getData() {
        progressDialog.setMessage(getString(R.string.loading));
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

    private void setOnScrollListener() {
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

    private void initOnRefreshListener() {

    }

    public void addToFeed(ReviewPageBundle page) {
        List<ReviewBundle> reviews = page.getReviews();

        if (initialLoad) {
            initialLoad = false;
            if (!verifiedHidden) {
                if (Utils.isVerified(this)) {
                    mainVerifyLayout.setVisibility(View.GONE);
                } else {
                    mainVerifyLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        if (mainFeedAdapter.getCount() == 0 && offset == 0) {
            progressDialog.dismiss();
            mainFeedList.setVisibility(View.GONE);
            errorTV.setText(getString(R.string.fetch_feed_again));
            errorTV.setVisibility(View.VISIBLE);
            isLoading = false;
            isRefreshing = false;
            return;
        }

        errorTV.setVisibility(View.GONE);

        if (reviews.size() > 2) {
            reviews.add(2, null);
        } else if ((mainFeedAdapter.getCount() == 0) && (reviews.size() > 0)) {
            reviews.add(null);
        }

        if (offset >= 10) {
            setOnScrollListener();
        }

        mainFeedAdapter.append(reviews);
        mainFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        progressDialog.dismiss();

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

    private void refreshFeed(Handler handler, final ReviewPageBundle page) {
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


}
