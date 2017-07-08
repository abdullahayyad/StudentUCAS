package ps.edu.ucas.portal.ui.fragment.studnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.AcademicCalendarAdapter;
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.utils.UtilityUCAS;

public class ListAcademicCalendar extends Fragment implements AcademicCalendarAdapter.OnItemClicked{


    static final String ACADEMIC_CALENDAR_NAME = "academic_calendar";
    static final String SHOW_HINT = "show_hint";
    static final String LIST_ACADEMIC_CALENDAR_NAME = "list_academic_calendar";

    RecyclerView myRecycle;
    AcademicCalendarAdapter adapter;
     EventAcademicCalendar academicCalendars;
    EventAcademicCalendar.Event event;

    boolean showHint;


    public static ListAcademicCalendar getInstance(EventAcademicCalendar academicCalendars, boolean showHint) {
        ListAcademicCalendar myListEvent = new ListAcademicCalendar();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ACADEMIC_CALENDAR_NAME, academicCalendars);
        bundle.putBoolean(SHOW_HINT, showHint);
        myListEvent.setArguments(bundle);

        return myListEvent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            Bundle bundle = this.getArguments();
            if (bundle != null) {
                academicCalendars = (EventAcademicCalendar) bundle.getSerializable(ACADEMIC_CALENDAR_NAME);
                showHint = bundle.getBoolean(SHOW_HINT);

            }

        }

     //   }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_academic_calendar, container, false);
         myRecycle = (RecyclerView) v.findViewById(R.id.academic_calender_list);
         adapter = new AcademicCalendarAdapter(getActivity(), academicCalendars,showHint);
          myRecycle.setAdapter(adapter);
           adapter.setOnItemClicked(this);
           myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }




    @Override
    public void getItemObject(View view, final EventAcademicCalendar.Event event) {
        this.event = event;
//        showLoginDialog(
//                getActivity().getResources().getString(R.string.adding_calender_title),
//                getActivity().getResources().getString(R.string.adding_calender_massage), event);
        UtilityUCAS.AppDialog(getContext(), R.string.adding_calender_title, R.string.adding_calender_massage, new UtilityUCAS.DialogEvent() {
            @Override
            public void onBack() {
                openCalendar(event);
            }
        });
    }


    public void showLoginDialog(String title,String msg, final EventAcademicCalendar.Event event) {
        UtilityUCAS.showLoginDialog(this, event, title, msg, false);

    }



    public void openCalendar(EventAcademicCalendar.Event object){
        EventAcademicCalendar.Event event = object;
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getEventDate());

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 00);


        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        //  intent.putExtra("allDay", true);
        //   intent.putExtra("rrule", "FREQ=YEARLY");
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 00);
        intent.putExtra("endTime", cal.getTimeInMillis());
        intent.putExtra("title", event.getEventTitle());
        getActivity().startActivity(intent);

    }



}
