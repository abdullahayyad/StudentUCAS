package ps.edu.ucas.portal.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Transcript;

/**
 * Created by Ayyad on 7/14/2015.
 */
public class MarksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private LayoutInflater inflater;
    private Transcript transcript;
    private Context myContext;
    boolean showHint;

    public MarksAdapter(Context myContext, Transcript transcript, boolean showHint){
        inflater = LayoutInflater.from(myContext);
       this.myContext = myContext;
        this.transcript = transcript;
        this.showHint = showHint;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.row_mark,parent,false);
            return  new  MyViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.header_row_mark,parent,false);
            return new VHHeader(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            int myPosition = position-1;
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.cours_detail.setText(transcript.getCourseMarks().get(myPosition).getCourseName().toString());
            myHolder.total_mark.setText(transcript.getCourseMarks().get(myPosition).getStudentMark() + "");
            myHolder.midterm_mark_mark.setText(transcript.getCourseMarks().get(myPosition).getMidtermMark() + "");
            myHolder.final_mark.setText(transcript.getCourseMarks().get(myPosition).getFinalMark() + "");
            myHolder.numberOfHour.setText(transcript.getCourseMarks().get(myPosition).getCourseHour() + "");

            int mark = 0;
            try {
                 mark = Integer.parseInt(transcript.getCourseMarks().get(myPosition).getStudentMark());
            }
            catch(NumberFormatException nfe)
            {
                mark = -1;
            }

            if(transcript.getCourseMarks().get(myPosition).getIsPassed() && mark != -1) {
                myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.pass));
            }else
            if (!transcript.getCourseMarks().get(myPosition).getIsPassed() && mark>0) {
                myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.failed));
            }else
            if(mark == 0) {
                myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.exception));
            }else
                myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.exception));

            if (transcript.getCourseMarks().get(myPosition).getStatusMessage() != 0) {
                myHolder.statusMassage.setVisibility(View.VISIBLE);
                myHolder.statusMassage.setText(myContext.getResources().getString(transcript.getCourseMarks().get(myPosition).getStatusMessage()));
                myHolder.statusMassage.setBackgroundColor(myContext.getResources().getColor(transcript.getCourseMarks().get(myPosition).getStatusColor()));


            }


            if (transcript.getCourseMarks().get(myPosition).getSecoundMessage() != 0) {
                myHolder.secondStatusMassage.setVisibility(View.VISIBLE);
                myHolder.secondStatusMassage.setText(myContext.getResources().getString(transcript.getCourseMarks().get(myPosition).getSecoundMessage()));

                //holder.bgShape.setColor(transcript.get(position).getCourseColor());

            }


            if (!transcript.getCourseMarks().get(myPosition).isPaid()) {
                myHolder.statusMassage.setVisibility(View.VISIBLE);
                myHolder.statusMassage.setText(myContext.getResources().getString(R.string.payment_of_fees));
                myHolder.markLayout.setVisibility(View.GONE);
                myHolder.secondStatusMassage.setVisibility(View.GONE);
                myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.exception));
            }
        }else
        if (holder instanceof VHHeader) {
            VHHeader vhHeader = (VHHeader) holder;
            if(transcript.getAvgGPA().equals(""))
            vhHeader.student_gpa.setText(transcript.getAvgGPA());
            else vhHeader.student_gpa.setText(transcript.getAvgGPA()+"%");


            if(transcript.getSemesterGPA().equals(""))
                vhHeader.semestar_gpa.setText(transcript.getSemesterGPA());
            else vhHeader.semestar_gpa.setText(transcript.getSemesterGPA()+"%");


            vhHeader.semester_hours.setText(transcript.getSemesterHours());
            vhHeader.semester_succeed.setText(transcript.getSemesterSucceedHour());



            if(showHint){
                vhHeader.linearLayout.setVisibility(View.VISIBLE);
                vhHeader.bgShapeGreen.setColor(myContext.getResources().getColor(R.color.pass));
                vhHeader.bgShapeYellow.setColor(myContext.getResources().getColor(R.color.exception));
                vhHeader.bgShapeRed.setColor(myContext.getResources().getColor(R.color.failed));

            }



        }

    }

    @Override
    public int getItemCount() {
            return transcript.getCourseMarks().size()+1;
    }


     class MyViewHolder extends RecyclerView.ViewHolder{
        TextView total_mark;
        TextView midterm_mark_mark;
        TextView final_mark;
        TextView cours_detail;

        LinearLayout markLayout;
        TextView statusMassage;
        TextView secondStatusMassage;

        TextView numberOfHour;
        GradientDrawable bgShape;
        View mycircleView;
        public MyViewHolder(View itemView) {
            super(itemView);
            total_mark = (TextView) itemView.findViewById(R.id.total_mark);
            midterm_mark_mark = (TextView) itemView.findViewById(R.id.midterm_mark);
            final_mark = (TextView) itemView.findViewById(R.id.final_mark);
            cours_detail = (TextView) itemView.findViewById(R.id.cours_detail);

            mycircleView =  itemView.findViewById(R.id.mycircleView);
            bgShape = (GradientDrawable)mycircleView.getBackground();
            markLayout = (LinearLayout) itemView.findViewById(R.id.markLayout);
              statusMassage = (TextView) itemView.findViewById(R.id.statusMassageText);
            secondStatusMassage = (TextView) itemView.findViewById(R.id.secondStatusMassageText);
            numberOfHour = (TextView) itemView.findViewById(R.id.numberHour);
        }
    }


    class VHHeader extends RecyclerView.ViewHolder {
        TextView semestar_gpa;
        TextView student_gpa;

        TextView semester_hours;
        TextView semester_succeed;


        LinearLayout linearLayout;
        View greenCircle;
        GradientDrawable bgShapeGreen;

        View yellowCircle;
        GradientDrawable bgShapeYellow;

        View redCircle;
        GradientDrawable bgShapeRed;






        public VHHeader(View itemView) {
            super(itemView);
            semestar_gpa = (TextView) itemView.findViewById(R.id.semestar_gpa);
            student_gpa = (TextView) itemView.findViewById(R.id.student_gpa);

            semester_hours = (TextView) itemView.findViewById(R.id.semester_hours);
            semester_succeed = (TextView) itemView.findViewById(R.id.semester_succeed);


            linearLayout = (LinearLayout) itemView.findViewById(R.id.hintKey);
            greenCircle = itemView.findViewById(R.id.myGreenCircle);
            bgShapeGreen = (GradientDrawable)greenCircle.getBackground();

            yellowCircle = itemView.findViewById(R.id.myYellowCircle);
            bgShapeYellow = (GradientDrawable)yellowCircle.getBackground();


            redCircle = itemView.findViewById(R.id.myRedCircle);
            bgShapeRed = (GradientDrawable)redCircle.getBackground();



        }
    }



    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
