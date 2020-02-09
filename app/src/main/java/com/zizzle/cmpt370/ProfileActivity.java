package com.zizzle.cmpt370;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.profile_layout);
        NavigationView navigationView = findViewById(R.id.profile_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button




    }

    //When item is selected in the menu, open the respective element (fragment or activity)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(this, HomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_leagues:
                Intent intent2 = new Intent(this, LeagueActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(this, ProfileActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_aboutUs:
                Intent intent4 = new Intent(this, AboutUsActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                Intent tolog = new Intent(this, SigninActivity.class);
                startActivity(tolog);
        }
        //close drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //Button to open menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //allows menu button to show menu on click
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
