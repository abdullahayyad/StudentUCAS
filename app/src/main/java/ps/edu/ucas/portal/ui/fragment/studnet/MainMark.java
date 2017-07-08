package ps.edu.ucas.portal.ui.fragment.studnet;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.HashMap;


import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.model.Transcript;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static android.icu.lang.UScript.UCAS;
import static ps.edu.ucas.portal.service.WebService.RESULT;
import static ps.edu.ucas.portal.service.WebService.RequestAPI.EVENT_COURSE_LAST;

public class MainMark extends Fragment implements WebService.OnResponding{

     public static final String TAG = "main_mark";

    private ArrayList<Transcript> transcripts;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewGroup viewGroup;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_academic_calendar, container, false);
          tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
          viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewGroup = (ViewGroup) v;

        tabLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);

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
        new WebService().startRequest(WebService.RequestAPI.TRANSCRIPT,null,this);
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.WAITING);

    }





    public class TranscriptsPagerAdapter extends FragmentStatePagerAdapter {

        boolean showHint;
        public TranscriptsPagerAdapter(FragmentManager fm, boolean showHint) {
            super(fm);
            this.showHint=showHint;


        }
        @Override
        public Fragment getItem(int position) {

                 return ListMark.getInstance(transcripts.get(position),showHint);

        }

        @Override
        public int getCount() {
            return transcripts.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return transcripts.get(position).getSemesterName();
        }
    }





    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.FINISHED);

        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == WebService.RequestAPI.TRANSCRIPT) {
                    try {
                        Type collectionType = new TypeToken<ArrayList<Transcript>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                        transcripts = new Gson().fromJson(responseWrapper, collectionType);
                        viewPager.setAdapter(new TranscriptsPagerAdapter(getChildFragmentManager(),false));

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
