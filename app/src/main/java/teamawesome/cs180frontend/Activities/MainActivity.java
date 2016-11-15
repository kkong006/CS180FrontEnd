package teamawesome.cs180frontend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    private NavDrawerAdapter mAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";
    private int schoolId;

    private MainFeedAdapter mFeedAdapter;

    ProgressDialog mProgressDialog;
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
        setButtons();

        schoolId = SPSingleton.getInstance(this).getSp().getInt(Constants.SCHOOL_ID, -1);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        RetrofitSingleton.getInstance()
                .getMatchingService()
                .getData(schoolId)
                .enqueue(new GetCacheDataCallback());

//        RetrofitSingleton.getInstance()
//                .getMatchingService()
//                .reviews(schoolId)
//                .enqueue(new GetReviewsCallback());
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
        DataSingleton.getInstance().cacheDataBundle(data);
        System.out.println("SCHOOL ID " + schoolId);
        System.out.println("SCHOOL SIZE " + DataSingleton.getInstance().getSchoolCache().size());
        System.out.println("PROFESSOR SIZE " + DataSingleton.getInstance().getProfessorCache().size());
        System.out.println("CLASS SIZE " + DataSingleton.getInstance().getClassCache().size());
        System.out.println("SUBJECT SIZE " + DataSingleton.getInstance().getSubjectCache().size());
        mProgressDialog.dismiss();

    }

    @Subscribe
    public void reviewsResp(List<ReviewRespBundle> reviewList) {
        mProgressDialog.dismiss();

        if(reviewList != null) {
            final int[] reviewIds = new int[reviewList.size()];
            int[] classIds = new int[reviewList.size()];
            int[] profIds = new int[reviewList.size()];
            int[] ratings = new int[reviewList.size()];
            final int[] userRatings = new int[reviewList.size()];
            final String[] reviewDates = new String[reviewList.size()];
            final String[] reviews = new String[reviewList.size()];
            final String[] classes = new String[reviewList.size()];
            final String[] professors = new String[reviewList.size()];

            for(int i = 0; i < reviewList.size(); i++) {
                reviewIds[i] = reviewList.get(i).getReviewId();
                classIds[i] = reviewList.get(i).getClassId();
                profIds[i] = reviewList.get(i).getProfId();
                ratings[i] = reviewList.get(i).getRating();
                userRatings[i] = reviewList.get(i).getReviewRating();
                reviewDates[i] = reviewList.get(i).getReviewDate();
                reviews[i] = reviewList.get(i).getMessage();
                classes[i] = DataSingleton.getInstance().getClassName(classIds[i]);
                professors[i] = DataSingleton.getInstance().getProfessorName(profIds[i]);
            }

            mFeedAdapter = new MainFeedAdapter(this, professors, ratings, classes, reviewDates, reviews, reviewIds);
            mFeedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mFeedList.setAdapter(mFeedAdapter);
            mFeedList.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int[] r = mFeedAdapter.getItem(position);
                    if(r.length == 2) {
                        int j = 0;
                        for(; j < reviewIds.length; j++) {
                            if(reviewIds[j] == r[0]) {
                                break;
                            }
                        }
                        if(j < reviewIds.length) {
                            Intent intent = new Intent(getBaseContext(), ReadReviewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(getString(R.string.REVIEW_RATING), r[1]);
                            bundle.putString(getString(R.string.REVIEW_CONTENT), reviews[j]);
                            bundle.putString(getString(R.string.REVIEW_CLASS_NAME), classes[j]);
                            bundle.putString(getString(R.string.REVIEW_DATE), reviewDates[j]);
                            bundle.putInt(getString(R.string.REVIEW_USER_RATING), userRatings[j]);
                            bundle.putString(getString(R.string.PROFESSOR_NAME), professors[j]);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                }
            });

            if(reviewList.size() == 0) {
                Utils.showSnackbar(this, parent, getString(R.string.reviews_dne));
            }
        }
    }

    @Subscribe
    public void intResp(Integer i) {
        if (i.equals(0)) {
            Utils.showSnackbar(this, parent, getString(R.string.data_doesnt_exist));
        } else if (i.equals(-1)) {
            Utils.showSnackbar(this, parent, getString(R.string.error_getting_data));
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
        if(schoolId < 1) {
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

        } else if (position == 3) {
            //Show user reviews
//            Intent i = new Intent(getApplicationContext(), MyReviewsActivity.class);
//            startActivity(i);
            Intent i = new Intent(getApplicationContext(), SearchResultsActivity.class);
            startActivity(i);
        } else if (position == 4) {
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
                        .getData(schoolId)
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
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
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
