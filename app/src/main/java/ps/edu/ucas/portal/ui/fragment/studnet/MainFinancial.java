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
import ps.edu.ucas.portal.model.Financial;
import ps.edu.ucas.portal.model.Transcript;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static ps.edu.ucas.portal.service.WebService.RESULT;


/**
 * Created by Ayyad on 11/24/2015.
 */
public class MainFinancial extends Fragment implements WebService.OnResponding {

      public static final String TAG = "main_financial" ;

    private ArrayList<Financial> financials;
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
        new WebService().startRequest(WebService.RequestAPI.FINANCE_FILE,null,this);
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.WAITING);

    }


    public class FinancePagerAdapter extends FragmentStatePagerAdapter {

        boolean showHint;

        public FinancePagerAdapter(FragmentManager fm, boolean showHint) {
            super(fm);
            this.showHint = showHint;


        }

        @Override
        public Fragment getItem(int position) {
            return ListFinancial.getInstance(financials.get(position), false);
        }

        @Override
        public int getCount() {
            return financials.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return financials.get(position).getSemesterName();
        }
    }


    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        UtilityUCAS.progressState(viewGroup, UtilityUCAS.ProgressStatus.FINISHED);

        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == WebService.RequestAPI.FINANCE_FILE) {
                    try {
                        Type collectionType = new TypeToken<ArrayList<Financial>>() {
                        }.getType();
                        JsonParser parser = new JsonParser();
                        JsonArray element = (JsonArray) parser.parse(objectResult.get(RESULT).toString());
                        JsonElement responseWrapper = element.getAsJsonArray();
                        financials = new Gson().fromJson(responseWrapper, collectionType);
                        viewPager.setAdapter(new FinancePagerAdapter(getChildFragmentManager(),false));

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
