package ps.edu.ucas.portal.ui;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.ui.fragment.FragmentSwitcher;
import ps.edu.ucas.portal.ui.fragment.PagesFragment;
import ps.edu.ucas.portal.ui.fragment.studnet.AcademicCalendar;
import ps.edu.ucas.portal.ui.fragment.studnet.DashboardStudent;
import ps.edu.ucas.portal.ui.fragment.studnet.EventDay;
import ps.edu.ucas.portal.ui.fragment.studnet.EventWeekView;
import ps.edu.ucas.portal.ui.fragment.studnet.ExamDay;
import ps.edu.ucas.portal.ui.fragment.studnet.MainFinancial;
import ps.edu.ucas.portal.ui.fragment.studnet.MainMark;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static android.R.id.toggle;
import static ps.edu.ucas.portal.ui.fragment.PagesFragment.DASHBOARD_STUDENT;
import static ps.edu.ucas.portal.ui.fragment.PagesFragment.EVENT_WEEK_VIEW;


public class MainContainerActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, FragmentSwitcher {

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.ucas_portal));
        toolbar.setNavigationIcon(R.drawable.ic_logo);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.student_drawer);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.setDrawerIndicatorEnabled(false);


        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView username  = (TextView) headerView.findViewById(R.id.user_name);
        username.setText(UtilityUCAS.getUserData().getFullName());
        TextView userMajor = (TextView) headerView.findViewById(R.id.user_major);
        userMajor.setText(UtilityUCAS.getUserData().getSectionName());
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,new DashboardStudent(),DashboardStudent.TAG).commit();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        int title = 0;
        switch (id){
            case R.id.nav_event_day:
                switchFragment(PagesFragment.EVENT_DAY,null);

                break;
            case R.id.nav_week_view:
                switchFragment(EVENT_WEEK_VIEW,null);


                break;
            case R.id.nav_academic_calender:
                switchFragment(PagesFragment.ACADEMIC_CALENDAR,null);


                break;
            case R.id.nav_marks:
                switchFragment(PagesFragment.TRANSCRIPT,null);


                break;

            case R.id.nav_exam_day:
                switchFragment(PagesFragment.EXAM,null);

                break;

            case R.id.nav_financial:

                switchFragment(PagesFragment.FINANCE,null);


                break;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getSupportFragmentManager().getBackStackEntryCount() == 0 ){
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setSubtitle(null);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new DashboardStudent(),DashboardStudent.TAG).commit();
        }

    }

    @Override
    public void switchFragment(PagesFragment pagesFragment, Object object) {
        Fragment fragment = null;
        int title = 0;

        switch (pagesFragment){
            case DASHBOARD_STUDENT :
                getSupportFragmentManager().beginTransaction().add(R.id.frame_container,new DashboardStudent(),DashboardStudent.TAG).commit();
                break;
            case EVENT_WEEK_VIEW :
                fragment = new EventWeekView();
                title = R.string.week_view;
                break;

            case EVENT_DAY :
                fragment = new EventDay();
                title = R.string.event_day;
                break;

            case ACADEMIC_CALENDAR :
                fragment = new AcademicCalendar();
                title = R.string.academic_calender;
                break;

            case TRANSCRIPT:
                fragment = new MainMark();
                title = R.string.mark_student;
                break;

            case FINANCE:
                fragment = new MainFinancial();
                title = R.string.financial_student;
                break;

            case EXAM:
                fragment = new ExamDay();
                title = R.string.exam_student;
                break;
        }
        toolbar.setSubtitle(title);
        if(getSupportFragmentManager().getBackStackEntryCount() == 0 ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, EventWeekView.TAG).addToBackStack(null).commit();
        }else
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, EventWeekView.TAG).commit();

        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

//        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
//        }

        if(DASHBOARD_STUDENT != pagesFragment){
            toggle.setDrawerIndicatorEnabled(true);
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                }
                else {
                    drawer.openDrawer(Gravity.START);
                }



                return true;

            case R.id.logout:

                UtilityUCAS.logOut();
                startActivity(new Intent(this, LeaderboardActivity.class));


                finish();
                return true;

            case R.id.setting:
                Intent intent = new Intent(this,
                        SettingsActivity.class);
                startActivity(intent);
                return true;


            case R.id.about:
                Intent intent2 = new Intent(this, AboutActivity.class);

                startActivity(intent2);
                return true;




            default:
                return super.onOptionsItemSelected(item);



        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(1 == 1)
            getMenuInflater().inflate(R.menu.menu_main, menu);
        else
            return false;

        return true;
    }
}
