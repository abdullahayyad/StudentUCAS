package ps.edu.ucas.portal.ui.fragment.studnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.ExamAdapter;
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.model.Exam;
import ps.edu.ucas.portal.ui.fragment.dialog.Dialog;

public class ListExamOfDay extends Fragment implements ExamAdapter.OnItemClicked,Dialog.dialogStatus{

    static final String EXAM_NAME = "exam";
    static final String LIST_EXAM_NAME = "list_exam";

    RecyclerView myRecycle;
    ExamAdapter adapter;
    ArrayList<Exam> myExams;


    public static ListExamOfDay getInstance( ArrayList<Exam> exams) {
        ListExamOfDay myListExam = new ListExamOfDay();
        Bundle bundle = new Bundle();

        bundle.putSerializable(EXAM_NAME, exams);
        myListExam.setArguments(bundle);

        return myListExam;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_event_of_day, container, false);
        myRecycle = (RecyclerView) v.findViewById(R.id.myEventList);
        //adapter = new ExamAdapter(getActivity(),myExams);

       // myRecycle.setAdapter(adapter);
        //adapter.setOnItemClicked(this);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
      //  Toast.makeText(getActivity(), myExams.size()+"", Toast.LENGTH_LONG).show();
        return v;
    }




    @Override
    public void getItemObject(View view, Exam exam) {
        showLoginDialog(
                getActivity().getResources().getString(R.string.adding_calender_title),
                getActivity().getResources().getString(R.string.adding_calender_massage), exam);
    }

    public void showLoginDialog(String title,String msg, final Exam event){
        //UtilityUCAS.showLoginDialog(ListExamOfDay.this, event, title, msg, false);
        final Dialog mDialog = Dialog.getDialogFragment(event,title, msg, false);
        mDialog.setTargetFragment(ListExamOfDay.this, 0);
        mDialog.show(getActivity().getSupportFragmentManager(), "tag");
    }

    @Override
    public void isConfirm(boolean isConfirm, Object object) {
        if(isConfirm){
            openCalendar((EventAcademicCalendar.Event) object);
        }
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
