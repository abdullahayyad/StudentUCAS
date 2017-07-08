package ps.edu.ucas.portal.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.ui.fragment.other.ListNews;

import static ps.edu.ucas.portal.service.WebService.RequestAPI.ADVER_RSS;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.LATEST_RSS;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.NEWS_RSS;


/**
 * Created by Ayyad on 11/27/2015.
 */
public class NewsActivity extends AppCompatActivity {



    WebService.RequestAPI[] url_new = {LATEST_RSS,NEWS_RSS,ADVER_RSS};

        String[] tabs_name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news_layout);

            tabs_name = getResources().getStringArray(R.array.collage_news_type);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
              TabLayout tabLayout = (TabLayout)  findViewById(R.id.tab_layout);
            getSupportActionBar().setTitle(getResources().getString(R.string.collage_news));

            ViewPager pagerNews = (ViewPager) findViewById(R.id.viewpager);
              pagerNews.setAdapter(new newsAdapter(getSupportFragmentManager()));
            tabLayout.setupWithViewPager(pagerNews);



        }


        public class newsAdapter extends FragmentStatePagerAdapter {

            public newsAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int i) {
                return ListNews.getInstance(url_new[i]);
            }

            @Override
            public int getCount() {
                return url_new.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return  tabs_name[position];
            }

        }


}



