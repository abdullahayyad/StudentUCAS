package ps.edu.ucas.portal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.EventCourse;
import ps.edu.ucas.portal.view.MyLinearLayout;

/**
 * Created by Ayyad on 7/14/2015.
 */
public class StudentEventAdapter extends RecyclerView.Adapter<StudentEventAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<EventCourse> myEvents;
    private Context myContext;
    private OnItemClicked onItemClicked;


    public StudentEventAdapter(Context myContext , ArrayList<EventCourse> myEvents){
        inflater = LayoutInflater.from(myContext);
        this.myContext = myContext;

        this.myEvents = myEvents;

    }

    public void setOnItemClicked(OnItemClicked onItemClicked){
        this.onItemClicked = onItemClicked;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_list_event_day,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.hall.setText(myEvents.get(position).getCourseTimes().get(0).getHallName().toString());
        holder.event_name.setText(myEvents.get(position).getCourseName().toString());
        holder.event_time.setText(myEvents.get(position).getCourseTimes().get(0).getCourseTime());
        holder.myHour.setText(myEvents.get(position).getCourseTimes().get(0).getStartHour()+"");
        int color = Color.parseColor(myContext.getString(myEvents.get(position).getColor()));
        holder.bgShape.setColor(color);
    }

    @Override
    public int getItemCount() {
        return myEvents.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hall;
        MyLinearLayout mlinear;
        TextView event_name;
        TextView event_time;
        GradientDrawable bgShape;
        TextView myHour;
        public MyViewHolder(View itemView) {
            super(itemView);
            hall = (TextView) itemView.findViewById(R.id.event_hall);
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_time = (TextView) itemView.findViewById(R.id.event_time);
            myHour = (TextView) itemView.findViewById(R.id.myHour);
            bgShape = (GradientDrawable)myHour.getBackground();
            mlinear = (MyLinearLayout) itemView.findViewById(R.id.myLinear);
            mlinear.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(onItemClicked !=null){
                onItemClicked.getItemObject(v, myEvents.get(getPosition()));
            }
        }
    }

    public interface OnItemClicked{
         void getItemObject(View view, EventCourse event);

    }



}
