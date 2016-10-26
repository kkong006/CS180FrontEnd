package teamawesome.cs180frontend.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import teamawesome.cs180frontend.Adapters.NavDrawerAdapter;
import teamawesome.cs180frontend.Misc.Constants;
import teamawesome.cs180frontend.R;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind (R.id.nav_drawer) ListView mDrawerList;
    @Bind(R.id.main_layout) CoordinatorLayout mMainLayout;
    @Bind(R.id.fab) FloatingActionButton mFab;

    private NavDrawerAdapter mAdapter;
    private String[] mNavTitles;
    private String[] mIconTitles;
    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mNavTitles = getResources().getStringArray(R.array.nav_drawer_array);
        mIconTitles = getResources().getStringArray(R.array.nav_drawer_icon_array);

        mAdapter = new NavDrawerAdapter(mIconTitles, mNavTitles, this);
        
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//                @Override
//                public void onDrawerStateChanged(int state) {
//                    Utils.hideKeyboard(mainLayout, getApplicationContext());
//                }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Write review activity
                Intent i = new Intent(getApplicationContext(), WriteReviewActivity.class);
                startActivity(i);
            }
        });

        getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE)
            .edit().putInt(Constants.USER_ID, 2);

        getSharedPreferences(Constants.PASSWORD, Context.MODE_PRIVATE)
                .edit().putString(Constants.PASSWORD, "hello123");

        getSharedPreferences(Constants.SCHOOL_ID, Context.MODE_PRIVATE)
                .edit().putInt(Constants.SCHOOL_ID, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        } else {
            //Logout
            logOut();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0) {
                //Home
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else if(position == 1) {
                //Search
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            } else if(position == 2) {
                //My Reviews
                Intent i = new Intent(getApplicationContext(), MyReviewsActivity.class);
                startActivity(i);
            } else if(position == 3) {
                //logout
                logOut();
            }
        }
    }

    private void logOut() {
        Toast.makeText(getBaseContext(), "Logging out", Toast.LENGTH_SHORT).show();
    }
}
