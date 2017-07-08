package ps.edu.ucas.portal.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Exam;
import ps.edu.ucas.portal.view.MyLinearLayout;


/**
 * Created by Ayyad on 7/14/2015.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Exam> marks;
    private Context myContext;
    private OnItemClicked onItemClicked;



    public ExamAdapter(Context myContext, ArrayList<Exam> marks){
        inflater = LayoutInflater.from(myContext);
       this.myContext = myContext;
        this.marks = marks;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_exam_day,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.myHour.setText(marks.get(position).getStartHour()+"");
//        holder.exam_time.setText(marks.get(position).getTime() + "");
//        holder.exam_name.setText(marks.get(position).getCourseName()+"");
//        holder.exam_hall.setText(marks.get(position).getHallName()+"");
//
//        holder.day_number.setText(marks.get(position).getDayOfMounth()+"");
//        holder.month.setText(marks.get(position).getMonthName()+"");
//
//         //holder.bgShape.setColor(myContext.getResources().getColor(marks.get(position).getCourseColor()));
//
//        holder.bgShape.setColor(myContext.getResources().getColor(marks.get(position).getmColor()));
    }

    @Override
    public int getItemCount() {
        return marks.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements   View.OnClickListener{
        TextView myHour;
        TextView exam_time;
        TextView exam_name;
        TextView exam_hall;


        TextView day_number;
        TextView month;

        GradientDrawable bgShape;
        View mycircleView;


        MyLinearLayout mlinear;



        public MyViewHolder(View itemView) {
            super(itemView);
            myHour = (TextView) itemView.findViewById(R.id.myHour);
            exam_time = (TextView) itemView.findViewById(R.id.exam_time);
            exam_name = (TextView) itemView.findViewById(R.id.exam_name);
            exam_hall = (TextView) itemView.findViewById(R.id.exam_hall);

            day_number = (TextView) itemView.findViewById(R.id.day_number);
            month = (TextView) itemView.findViewById(R.id.month);

            bgShape = (GradientDrawable)myHour.getBackground();


            mlinear = (MyLinearLayout) itemView.findViewById(R.id.myLinear);
            mlinear.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(onItemClicked !=null){
                onItemClicked.getItemObject(v, marks.get(getPosition()));
            }
        }
    }


    public interface OnItemClicked{
        void getItemObject(View view, Exam exam);

    }



    public void setOnItemClicked(OnItemClicked onItemClicked){
        this.onItemClicked = onItemClicked;
    }

}
