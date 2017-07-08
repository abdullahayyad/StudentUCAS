package ps.edu.ucas.portal.ui.fragment.studnet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static ps.edu.ucas.portal.service.WebService.RESULT;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.EVENT_COURSE_LAST;


public class AcademicCalendar extends Fragment implements WebService.OnResponding {

     public static final String TAG =  "academic_calendar";


    ArrayList<EventAcademicCalendar> academicCalendars;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ViewGroup viewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_academic_calendar, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewGroup = (ViewGroup) v;

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        Button retry = (Button) v.findViewById(R.id.btn_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();

            }


        });


        startRequest();


        return v;
    }


    private void startRequest() {
        new WebService().startRequest(WebService.RequestAPI.ACADEMIC_CALENDER, null, this);
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.WAITING);

    }

    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.FINISHED);

        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == WebService.RequestAPI.ACADEMIC_CALENDER) {
                    try {
                        Type collectionType = new TypeToken<ArrayList<EventAcademicCalendar>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                        academicCalendars = new Gson().fromJson(responseWrapper, collectionType);
                        viewPager.setAdapter(new SectionPagerAdapter(getChildFragmentManager(), false));

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

    private class SectionPagerAdapter extends FragmentStatePagerAdapter {

        boolean showHint;

        public SectionPagerAdapter(FragmentManager fm, boolean showHint) {
            super(fm);
            this.showHint = showHint;
            getItem(1);

        }

        @Override
        public Fragment getItem(int position) {
            ListAcademicCalendar listAcademicCalendar;
            switch (position) {
                case 0:
                    listAcademicCalendar = ListAcademicCalendar.getInstance(sortingData(EventAcademicCalendar.AcademicCalendarPages.PREVIOUS, academicCalendars), showHint);
                    listAcademicCalendar.setTargetFragment(AcademicCalendar.this, 0);

                    return listAcademicCalendar;
                case 1:
                    listAcademicCalendar = ListAcademicCalendar.getInstance(sortingData(EventAcademicCalendar.AcademicCalendarPages.CURRENT, academicCalendars), showHint);
                    listAcademicCalendar.setTargetFragment(AcademicCalendar.this, 0);

                    return listAcademicCalendar;

                case 2:
                    listAcademicCalendar = ListAcademicCalendar.getInstance(sortingData(EventAcademicCalendar.AcademicCalendarPages.NEXT, academicCalendars), showHint);
                    listAcademicCalendar.setTargetFragment(AcademicCalendar.this, 0);
                    return listAcademicCalendar;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return academicCalendars.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "الفصل السابق";
                case 1:
                    return "الفصل الحالي";


                default:
                    return "الفصل القادم";

            }
        }
    }


    private EventAcademicCalendar sortingData(EventAcademicCalendar.AcademicCalendarPages type, ArrayList<EventAcademicCalendar> academicCalendars) {
        for (EventAcademicCalendar academicCalendar : academicCalendars) {

            if (academicCalendar.getSemester() == type) {
                return academicCalendar;
            }

        }

        return null;
    }
}





