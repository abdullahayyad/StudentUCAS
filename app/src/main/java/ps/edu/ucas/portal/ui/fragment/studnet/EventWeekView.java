package ps.edu.ucas.portal.ui.fragment.studnet;


import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.EventCourse;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.ui.CourseDetail;
import ps.edu.ucas.portal.ui.MainContainerActivity;
import ps.edu.ucas.portal.utils.UtilityUCAS;
import ps.edu.ucas.portal.view.WeekView;

import static ps.edu.ucas.portal.service.WebService.RESULT;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.EVENT_COURSE_LAST;

public class EventWeekView extends Fragment implements WeekView.EventClickListener, WeekView.OnDayClickListener, WebService.OnResponding {

    public static final String TAG = "WEEK_VIEW";


    private WeekView weekView;

    private ViewGroup viewGroup;

    public EventWeekView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_week_view, container, false);
        weekView = (WeekView) view.findViewById(R.id.timeWeek);
        weekView.setNumberOfVisibleDays(6);
        weekView.setFirstDayOfWeek(Calendar.SATURDAY);
        viewGroup = (ViewGroup) view;



        Button retry = (Button) view.findViewById(R.id.btn_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();

            }

            
        });


        weekView.setOnEventClickListener(this);
        weekView.setOnDayClickListenerClickListener(this);
        startRequest();
        return view;

    }

    private void startRequest() {
        new WebService().startRequest(EVENT_COURSE_LAST, null, this);
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.WAITING);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onEventClick(EventCourse event, RectF eventRect, int position) {
        Intent intent = new Intent(getActivity(), CourseDetail.class);
        intent.putExtra("course_detail", event);
        intent.putExtra("position", position);
        getActivity().startActivity(intent);
        Log.e("event", event.getCourseTimes().get(position).toString());

    }

    @Override
    public void onDayClick(int position) {
        //EventDay eventDay = EventDay.getInstance(position,eventCourses);
        //getChildFragmentManager().beginTransaction().addToBackStack(null).add(R.id.weekViewLayout,eventDay,"myChosinDay").commit();

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
                        ArrayList<EventCourse> eventCourses = new Gson().fromJson(responseWrapper, collectionType);
                        weekView.setEvents(eventCourses);

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
