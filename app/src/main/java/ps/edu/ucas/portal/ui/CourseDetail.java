package ps.edu.ucas.portal.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.EventCourse;


public class CourseDetail extends AppCompatActivity {


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        EventCourse eventCourse = (EventCourse) intent.getExtras().getSerializable("course_detail");
        int color = Color.parseColor(getString(eventCourse.getColor()));
        int position =  intent.getExtras().getInt("position");
        Log.e("eventCourse",eventCourse.getColor()+"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(color);
        getSupportActionBar().setTitle(eventCourse.getCourseName());

        //toolbar.setSubtitle(event.getCourseName());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView techName = (TextView) findViewById(R.id.tech_name);
        TextView hallName = (TextView) findViewById(R.id.hall_name);
        TextView eventTime = (TextView) findViewById(R.id.event_time);
        TextView branchNumb = (TextView) findViewById(R.id.branch_no);

        techName.setText(eventCourse.getTeacherName());
        hallName.setText(eventCourse.getCourseTimes().get(position).getHallName());
        eventTime.setText(eventCourse.getCourseTimes().get(position).getCourseTime());
        branchNumb.setText(eventCourse.getBranchNo());



        ImageView circleImage1 = (ImageView) findViewById(R.id.shape_circle1);
        ImageView circleImage2 = (ImageView) findViewById(R.id.shape_circle2);
        ImageView circleImage3 = (ImageView) findViewById(R.id.shape_circle3);
        ImageView circleImage4 = (ImageView) findViewById(R.id.shape_circle4);

        GradientDrawable bgShape1 = (GradientDrawable)circleImage1.getBackground();
        bgShape1.setColor(color);

        GradientDrawable bgShape2 = (GradientDrawable)circleImage2.getBackground();
        bgShape2.setColor(color);

        GradientDrawable bgShape3 = (GradientDrawable)circleImage3.getBackground();
        bgShape3.setColor(color);

        GradientDrawable bgShape4 = (GradientDrawable)circleImage4.getBackground();
        bgShape4.setColor(color);




    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
           this.finish();
        }
        return true;
    }
}
