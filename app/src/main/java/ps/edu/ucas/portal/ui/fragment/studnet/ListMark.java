package ps.edu.ucas.portal.ui.fragment.studnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.MarksAdapter;
import ps.edu.ucas.portal.model.Transcript;


public class ListMark extends Fragment {

    static final String EVENT_NAME = "event";
    static final String LIST_MARK_NAME = "list_academic_calendar";
    static final String SHOW_HINT = "show_hint";

    RecyclerView myRecycle;
    MarksAdapter adapter;
    Transcript transcript;

    boolean showHint;
    public static ListMark getInstance(Transcript transcript,boolean showHint) {
        ListMark myListEvent = new ListMark();
        Bundle bundle = new Bundle();

        bundle.putSerializable(EVENT_NAME, transcript);
        bundle.putBoolean(SHOW_HINT, showHint);
        myListEvent.setArguments(bundle);

        return myListEvent;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            transcript =new  Transcript();
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                transcript = (Transcript) bundle.getSerializable(EVENT_NAME);
            }



        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_academic_calendar, container, false);
        myRecycle = (RecyclerView) v.findViewById(R.id.academic_calender_list);
        adapter = new MarksAdapter(getActivity(), transcript,showHint);
        myRecycle.setAdapter(adapter);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }


}
