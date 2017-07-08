package ps.edu.ucas.portal.ui.fragment.studnet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Day;
import ps.edu.ucas.portal.model.Exam;


public class ExamDay extends Fragment {
    static final String EXAM_NAME = "exam";
    static final String LIST_EXAM_NAME = "list_exam";


    private ArrayList<Exam> myExam = new ArrayList<>();
    private ViewPager viewPager;

     private PagerTabStrip pagerTabStrip;

    public static ExamDay getInstance(ArrayList<Exam> exams) {
        ExamDay eventDay = new ExamDay();
        Bundle bundle = new Bundle();

        bundle.putSerializable(EXAM_NAME, exams);
        eventDay.setArguments(bundle);

        return eventDay;
    }




    public ExamDay() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_event_day_view, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.pager_event);
        pagerTabStrip =  (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

        viewPager.setAdapter(new myFragmentAdapter(getChildFragmentManager()));



        return v;
    }







    private class myFragmentAdapter extends FragmentStatePagerAdapter {

        String []days ;
        public myFragmentAdapter(FragmentManager fm) {
            super(fm);
            days = getActivity().getResources().getStringArray(R.array.days);
            //getItem(1);

        }

        @Override
        public Fragment getItem(int position) {


            ListExamOfDay list = ListExamOfDay.getInstance(sortingData(Day.getDay(6-position),myExam));
           //  list.setTargetFragment(ExamDay.this,0);
            return list;
        }

        @Override
        public int getCount() {
            return days.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return days[days.length-position-1];
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }


    }




    private ArrayList<Exam> sortingData(Day day,ArrayList<Exam> myExam){
        ArrayList<Exam> exams = new ArrayList<Exam>();
//        for (Exam exam : myExam ){
//
//            if(Day.getDay(day) == exam.getDayOfWeek()){
//                Log.e("title",exam.getCourseName());
//                exams.add(exam);
//            }
//
//        }

        return exams;
    }




}
