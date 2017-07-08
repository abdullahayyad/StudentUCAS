package ps.edu.ucas.portal.ui.fragment.studnet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Day;
import ps.edu.ucas.portal.model.EventCourse;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.utils.UtilityUCAS;
import ps.edu.ucas.portal.view.WeekView;

import static ps.edu.ucas.portal.service.WebService.RESULT;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.EVENT_COURSE_LAST;

public class EventDay extends Fragment implements WebService.OnResponding{

    static final String EVENT_NAME = "event";

    static final String DAY_NUMBER = "day_number";
    public static final String TAG ="EventDay" ;

    private int dayNumber = -1;
    private ArrayList<EventCourse> eventCourses;
    private ViewPager viewPager;

    private PagerTabStrip pagerTabStrip;
    private ViewGroup viewGroup;


    public static EventDay getInstance(int myDayNumber, ArrayList<EventCourse> events) {
        EventDay eventDay = new EventDay();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_NAME, events);
        bundle.putInt(DAY_NUMBER, myDayNumber);
        eventDay.setArguments(bundle);

        return eventDay;
    }


    public EventDay() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            dayNumber = bundle.getInt(DAY_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_day_view, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.pager_event);
        pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        viewGroup = (ViewGroup) v;


        Button retry = (Button) v.findViewById(R.id.btn_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();

            }


        });


        startRequest();
//        getPagerDay();
        return v;
    }


    private void startRequest() {
        new WebService().startRequest(EVENT_COURSE_LAST, null, this);
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.WAITING);

    }



    public class myFragmentAdapter extends FragmentStatePagerAdapter {

        String[] days = getResources().getStringArray(R.array.days);

        public myFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("evenDay ",evenDay(Day.getDay(6 - position), eventCourses).toString());
            ListEventOfDay list = ListEventOfDay.getInstance(evenDay(Day.getDay(6 - position), eventCourses));
            return list;
        }

        @Override
        public int getCount() {
            return days.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return days[days.length - position - 1];
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }


    }

    private ArrayList<EventCourse> evenDay(Day day, ArrayList<EventCourse> events) {
        ArrayList<EventCourse> myEvent = new ArrayList<>();
         for (EventCourse eventCourse : events) {
             EventCourse e =null;
             try {
                  e = (EventCourse) eventCourse.clone();
             } catch (CloneNotSupportedException e1) {
                 e1.printStackTrace();
             }
             for (EventCourse.CourseTime courseTime : eventCourse.getCourseTimes()) {
                if (Day.getDay(courseTime.getDayOfWeekNumber()) == day) {
                    ArrayList<EventCourse.CourseTime> courseTimes = new ArrayList<>();
                    courseTimes.add(courseTime);
                    //eventCourse.setCourseTimes(courseTimes);
                    e.setCourseTimes(courseTimes);
                    myEvent.add(e);
                }
            }

        }

        return myEvent;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void getPagerDay() {
        Calendar cal = Calendar.getInstance();
        if (dayNumber == -1) {
            int day = WeekView.getDayToday(cal.get(Calendar.DAY_OF_WEEK));
            if (day < 6) {
                viewPager.setCurrentItem(6 - (day + 1));
            }
        } else {
            viewPager.setCurrentItem(6 - (dayNumber + 1));
        }

    }

    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.FINISHED);

        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == WebService.RequestAPI.EVENT_COURSE_LAST) {
                    try {
                        Type collectionType = new TypeToken<ArrayList<EventCourse>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                          eventCourses = new Gson().fromJson(responseWrapper, collectionType);
                        for(int i=0;i<eventCourses.size();i++){
                            eventCourses.get(i).getColor(i+1);
                        }

                        viewPager.setAdapter(new myFragmentAdapter(getChildFragmentManager()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }

                break;
            case NO_CONNECTION:
                UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.RETRY);
                break;

        }

    }
}
