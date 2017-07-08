package ps.edu.ucas.portal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ps.edu.ucas.portal.R;


public class LeaderboardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_leaderboard);
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        Button about = (Button) findViewById(R.id.about_btn);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);

                startActivity(intent);
            }
        });



        Button academic = (Button) findViewById(R.id.academic_calender);
        academic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), MainActivity.class);
//                intent.putExtra("result", 0);
//                startActivity(intent);
            }
        });



        Button logIn = (Button) findViewById(R.id.log_in_btn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StudentLoginActivity.class);
                startActivity(intent);

            }
        });




        Button newsCollage  = (Button) findViewById(R.id.news_btn);
        newsCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsActivity.class);
                startActivity(intent);
             }
        });

    }

}
