package ps.edu.ucas.portal.ui.fragment.studnet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Day;
import ps.edu.ucas.portal.model.EventCourse;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.ui.NewsActivity;
import ps.edu.ucas.portal.ui.fragment.FragmentSwitcher;
import ps.edu.ucas.portal.ui.fragment.PagesFragment;
import ps.edu.ucas.portal.utils.UtilityUCAS;
import ps.edu.ucas.portal.view.MyLinearLayout;
import ps.edu.ucas.portal.view.WeekView;

import static ps.edu.ucas.portal.service.WebService.RESULT;

/**
 * Created by Ayyad on 8/13/2015.
 */
public class DashboardStudent extends Fragment implements View.OnClickListener, WebService.OnResponding {

    public static final String TAG = "DASHBOARD_STUDENT";


    private CountDownTimer countDownTimerEvent;
    private CountDownTimer countDownTimerExam;


    private RelativeLayout descriptionLayout;


    GradientDrawable bgShape;

    private TextView hall;
    private TextView event_name;
    private TextView event_time;
    private TextView myHour;

    private CardView cardView;

    FragmentSwitcher fragmentSwitcher;


    boolean isShowingDetail = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_dashboard, container, false);
        Button week_view = (Button) v.findViewById(R.id.week_view);
        Button event_day = (Button) v.findViewById(R.id.event_day);
        Button academic_calender = (Button) v.findViewById(R.id.academic_calender);
        Button mark_student = (Button) v.findViewById(R.id.mark_student);
        Button exam_student = (Button) v.findViewById(R.id.exam_student);
        Button financial_view = (Button) v.findViewById(R.id.finanical_view);

        Button collage_news = (Button) v.findViewById(R.id.collage_news);

        cardView = (CardView) v.findViewById(R.id.present_event);
        cardView.setVisibility(View.GONE);
        week_view.setOnClickListener(this);
        event_day.setOnClickListener(this);
        academic_calender.setOnClickListener(this);
        mark_student.setOnClickListener(this);
        exam_student.setOnClickListener(this);
        financial_view.setOnClickListener(this);
        collage_news.setOnClickListener(this);

        descriptionLayout = (RelativeLayout) v.findViewById(R.id.student_detail_layout);
        final ImageButton showerDescription = (ImageButton) v.findViewById(R.id.show_description_btn);
        showerDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowingDetail) {
                    descriptionLayout.setVisibility(View.GONE);
                    showerDescription.setImageResource(R.drawable.ic_more);
                    isShowingDetail = false;
                } else {
                    descriptionLayout.setVisibility(View.VISIBLE);
                    showerDescription.setImageResource(R.drawable.ic_top);
                    isShowingDetail = true;
                }
            }
        });


        TextView currentDate = (TextView) v.findViewById(R.id.currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMM yyyy");
        String currentDateandTime = sdf.format(new Date());
        currentDate.setText(currentDateandTime);

        //////new

        hall = (TextView) v.findViewById(R.id.event_hall);
        event_name = (TextView) v.findViewById(R.id.event_name);
        event_time = (TextView) v.findViewById(R.id.event_time);
        myHour = (TextView) v.findViewById(R.id.myHour);
        bgShape = (GradientDrawable) myHour.getBackground();
        MyLinearLayout mlinear = (MyLinearLayout) v.findViewById(R.id.myLinear);
        bgShape.setColor(getActivity().getResources().getColor(R.color.colorPrimary));


        initUserData(v);

        new WebService().startRequest(WebService.RequestAPI.EVENT_COURSE_LAST,null,this);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (getActivity() != null)
            switch (v.getId()) {

                case R.id.week_view:
                    fragmentSwitcher.switchFragment(PagesFragment.EVENT_WEEK_VIEW, null);

                    break;

                case R.id.event_day:
                    fragmentSwitcher.switchFragment(PagesFragment.EVENT_DAY, null);

                    break;

                case R.id.academic_calender:
                    fragmentSwitcher.switchFragment(PagesFragment.ACADEMIC_CALENDAR, null);

//                    myAcademicCalendar = new MainAcademicCalendar();
//                    fragmentSwitcher.switcher(R.id.my_fragment, myAcademicCalendar,2,true);
                    break;


                case R.id.mark_student:
                    fragmentSwitcher.switchFragment(PagesFragment.TRANSCRIPT, null);


//                    myMark = MainMark.getInstance(EventData.getDataMarkData());
//                    fragmentSwitcher.switcher(R.id.my_fragment, myMark,3,true);
                    break;


                case R.id.exam_student:
                    fragmentSwitcher.switchFragment(PagesFragment.EXAM, null);

                    //myExamDay = ExamDay.getInstance(EventData.getDataExamData());
//                    myExamDay = new ExamDay();
//                    fragmentSwitcher.switcher(R.id.my_fragment, myExamDay,4,true);
                    break;

                case R.id.finanical_view:
                    fragmentSwitcher.switchFragment(PagesFragment.FINANCE, null);
//                    mainFinancial = new MainFinancial();
//                    if(getActivity()!= null)
//                    fragmentSwitcher.switcher(R.id.my_fragment, mainFinancial,5,true);
                    break;


                case R.id.collage_news:
                    Intent intent3 = new Intent(getActivity(), NewsActivity.class);
                    startActivity(intent3);

                    break;


            }
    }


    public void initUserData(View v) {
        User u = UtilityUCAS.getUserData();
        TextView studentName = (TextView) v.findViewById(R.id.student_name);
        TextView student_major = (TextView) v.findViewById(R.id.student_major);
        TextView department_name = (TextView) v.findViewById(R.id.student_department);
        TextView success_hr = (TextView) v.findViewById(R.id.student_successHour);
        TextView study_hr = (TextView) v.findViewById(R.id.student_studyHour);
        TextView remain_hr = (TextView) v.findViewById(R.id.student_romaiHour);
        TextView std_avg = (TextView) v.findViewById(R.id.student_std_avg);
        TextView std_avg_grad = (TextView) v.findViewById(R.id.student_avg_grad);

        studentName.setText(u.getFullName().toString());
        student_major.setText(u.getSectionName());


        department_name.setText(u.getDepartment_name());
        success_hr.setText(u.getSuccessHR());
        study_hr.setText(u.getStudyHR());
        remain_hr.setText(u.getRemainHR());
        std_avg.setText(u.getStdAVG().equals("") ? "0%" : u.getStdAVG() + "%");
        std_avg_grad.setText(u.getStdAvgGrad() + "%");


    }


    public void initTimer(EventCourse eventCourse, EventCourse.CourseTime courseTime) {
        cardView.setVisibility(View.VISIBLE);
        hall.setText(courseTime.getHallName().toString());
        event_name.setText(eventCourse.getCourseName().toString());
        event_time.setText(courseTime.getCourseTime());
        myHour.setText(courseTime.getStartHour() + "");
        bgShape.setColor(eventCourse.getColor());

        Calendar calNow = Calendar.getInstance();
        Calendar calEvent = Calendar.getInstance();
        calEvent.set(Calendar.HOUR_OF_DAY, courseTime.getStartHour());
        calEvent.set(Calendar.MINUTE, courseTime.getStartMinute());
        calEvent.set(Calendar.SECOND, 0);

        long millisecond = calEvent.getTimeInMillis() - calNow.getTimeInMillis();
        final String hitEvent = getActivity().getResources().getString(R.string.time_event_romain);
        countDownTimerEvent = new CountDownTimer(millisecond, 1000) {

            public void onTick(long millisUntilFinished) {

                SimpleDateFormat time = new SimpleDateFormat("H:mm:ss");
                time.setTimeZone(TimeZone.getTimeZone("GMT"));
                ;
                event_time.setText(hitEvent + "  " + time.format(new Date(millisUntilFinished)));
            }


            public void onFinish() {
                cardView.setVisibility(View.GONE);
            }
        }.start();
    }


    private void getLifeEvent(ArrayList<EventCourse> eventCourses) {
        Calendar calNow = Calendar.getInstance();
        Calendar calEvent = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        int day = WeekView.getDayToday(cal.get(Calendar.DAY_OF_WEEK)) + 1;
        for (EventCourse course : eventCourses) {
            for (EventCourse.CourseTime courseTime : course.getCourseTimes()) {

                calEvent.set(Calendar.HOUR_OF_DAY, courseTime.getStartHour());
                calEvent.set(Calendar.MINUTE, courseTime.getStartMinute());


                if (courseTime.getDayOfWeekNumber() == day) {
                    if (calNow.getTimeInMillis() < calEvent.getTimeInMillis()) {

                        int hours = calEvent.get(Calendar.HOUR_OF_DAY) - calNow.get(Calendar.HOUR_OF_DAY);
                        if (hours <= 3) {
                            initTimer(course, courseTime);
                            return;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        switch (statusConnection) {
            case SUCCESS:
                try {
                    if (requestAPI == WebService.RequestAPI.EVENT_COURSE_LAST) {
                        Type collectionType = new TypeToken<ArrayList<EventCourse>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                        ArrayList<EventCourse> eventCourses = new Gson().fromJson(responseWrapper, collectionType);
                        for (int i = 0; i < eventCourses.size(); i++) {
                            eventCourses.get(i).getColor(i + 1);
                        }
                        getLifeEvent(eventCourses);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }

    }




    @Override
    public void onDetach() {
        super.onDetach();

        try {
            if(countDownTimerEvent != null)
                countDownTimerEvent.cancel();
            if(countDownTimerExam != null)
                countDownTimerExam.cancel();

        } catch (Exception e) {
           // throw new RuntimeException(e);
        }
    }

}
