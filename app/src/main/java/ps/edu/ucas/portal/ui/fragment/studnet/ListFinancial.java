package ps.edu.ucas.portal.ui.fragment.studnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.adapter.FinancialAdapter;
import ps.edu.ucas.portal.model.Financial;


public class ListFinancial extends Fragment {

    static final String FINANCIAL_NAME = "student_financial";
    static final String LIST_FINANCIAL_NAME = "list_student_financial";
    static final String SHOW_HINT = "show_hint";

    RecyclerView myRecycle;
    FinancialAdapter adapter;
    Financial financial;


    boolean showHint;
    public static ListFinancial getInstance(Financial transcript,boolean showHint) {
        ListFinancial myListEvent = new ListFinancial();
        Bundle bundle = new Bundle();

        bundle.putSerializable(FINANCIAL_NAME, transcript);
        bundle.putBoolean(SHOW_HINT, showHint);
        myListEvent.setArguments(bundle);

        return myListEvent;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            financial =new Financial();
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                financial = (Financial) bundle.getSerializable(FINANCIAL_NAME);
            }



        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_academic_calendar, container, false);
        myRecycle = (RecyclerView) v.findViewById(R.id.academic_calender_list);
        adapter = new FinancialAdapter(getActivity(), financial,showHint);
          myRecycle.setAdapter(adapter);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }


}
