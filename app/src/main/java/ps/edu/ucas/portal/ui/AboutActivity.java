package ps.edu.ucas.portal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.view.ViewPagerParallax;


public class AboutActivity extends ActionBarActivity {
    private static final int MAX_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_about);

        final ViewPagerParallax pager = (ViewPagerParallax) findViewById(R.id.pager);
        pager.set_max_pages(MAX_PAGES);
        pager.setBackgroundAsset(R.raw.gray_background);
     //   CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        pager.setAdapter(new my_adapter());
       // defaultIndicator.setViewPager(pager);

        CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);


        pager.setCurrentItem(MAX_PAGES-1);
    }


    private class my_adapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MAX_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=null;
            LayoutInflater inflater = getLayoutInflater();
            switch (position){
            case 2:

             view = inflater.inflate(R.layout.about_first_page, null);
             ImageButton url_btn = (ImageButton) view.findViewById(R.id.url_btn_web_site);
             //   ImageButton facebook_btn = (ImageButton) view.findViewById(R.id.facebook_btn);

                url_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl("https://www.ucas.edu.ps/");
                    }
                });

                ImageButton url_btn_service = (ImageButton) view.findViewById(R.id.url_btn_std_service);

                url_btn_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl("https://my.ucas.edu.ps/login?backurl=/home");
                    }
                });

                ImageButton url_btn_moodle = (ImageButton) view.findViewById(R.id.url_btn_moodle);

                url_btn_moodle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl("http://moodle.ucas.edu.ps/moodle/login/ucaslogin.php");
                    }
                });




               /* facebook_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUrl("https://www.facebook.com/ucasGaza");
                    }
                });*/
            container.addView(view);

            return view;

                case 1:


                    view = inflater.inflate(R.layout.about_second_page, null);
                    container.addView(view);

                    return view;

                case 0:

                    view = inflater.inflate(R.layout.about_third_page, null);
                    ImageView facebook = (ImageView) view.findViewById(R.id.facebook);
                    ImageView youtube = (ImageView) view.findViewById(R.id.youtube);
                    ImageView twitter = (ImageView) view.findViewById(R.id.twitter);
                    ImageView flickr = (ImageView) view.findViewById(R.id.flickr);
                    ImageView issuu = (ImageView) view.findViewById(R.id.issuu);
                    ImageView google = (ImageView) view.findViewById(R.id.google);
                    ImageView soundcloud = (ImageView) view.findViewById(R.id.soundcloud);
                    ImageView instagram = (ImageView) view.findViewById(R.id.instagram);


                    facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://www.facebook.com/ucasGaza");
                        }
                    });


                    youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://www.youtube.com/user/TheUniversityCollege?sub_confirmation=1");
                        }
                    });

                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://twitter.com/UCASGaza");
                        }
                    });

                    flickr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://www.flickr.com/photos/ucasgaza");
                        }
                    });


                    issuu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("http://issuu.com/ucasgaza");
                        }
                    });


                    google.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://plus.google.com/+TheUniversityCollege");
                        }
                    });


                    soundcloud.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("https://soundcloud.com/ucasgaza");
                        }
                    });


                    instagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToUrl("http://instagram.com/UCASGAZA");
                        }
                    });







                    container.addView(view);

                    return view;


            }

            return view;
        }

    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}
