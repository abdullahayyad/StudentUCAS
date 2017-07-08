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

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.StudentEventAdapter;
import ps.edu.ucas.portal.model.Day;
import ps.edu.ucas.portal.model.EventCourse;
import ps.edu.ucas.portal.ui.CourseDetail;

public class ListEventOfDay extends Fragment implements StudentEventAdapter.OnItemClicked{

    static final String EVENT_NAME = "event";
    static final String LIST_EVENT_NAME = "list_event";

    RecyclerView myRecycle;
    StudentEventAdapter adapter;
    ArrayList<EventCourse> events;

    public static ListEventOfDay getInstance( ArrayList<EventCourse> events) {
        ListEventOfDay myListEvent = new ListEventOfDay();
        Bundle bundle = new Bundle();

        bundle.putSerializable(EVENT_NAME, events);
        myListEvent.setArguments(bundle);

        return myListEvent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            events = new ArrayList<EventCourse>();
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                events = (ArrayList<EventCourse>) bundle.getSerializable(EVENT_NAME);


            }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_event_of_day, container, false);
        myRecycle = (RecyclerView) v.findViewById(R.id.myEventList);

        adapter = new StudentEventAdapter(getActivity(),  sortTimeEven(events) );
        myRecycle.setAdapter(adapter);
        adapter.setOnItemClicked(this);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

       // Toast.makeText(getActivity(), events.size()+"", Toast.LENGTH_LONG).show();
        return v;
    }




    @Override
    public void getItemObject(View view, EventCourse event) {
        Intent intent = new Intent(getActivity(), CourseDetail.class);
        intent.putExtra("course_detail",event);
        getActivity().startActivity(intent);

    }

    private  ArrayList<EventCourse> sortTimeEven( ArrayList<EventCourse> events){
        EventCourse myEvent[] =  events.toArray(new EventCourse[events.size()]);
        EventCourse temp;
        int position = 0;
        for(int i=0 ;i<events.size();i++) {
            for (int j = i + 1; j < events.size(); j++) {

                if (events.get(i).getCourseTimes().get(0).getStartHour() > events.get(j).getCourseTimes().get(0).getStartHour()) {
                    temp = myEvent[i];
                    myEvent[i] = myEvent[j];
                    myEvent[j] = temp ;
                }


            }
        }
        ArrayList<EventCourse> mEvent = new ArrayList<>();
        for(EventCourse event :myEvent ){
            mEvent.add(event);
        }

        return mEvent;
    }
}
