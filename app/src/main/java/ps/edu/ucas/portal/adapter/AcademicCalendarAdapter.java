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
import ps.edu.ucas.portal.model.EventAcademicCalendar;
import ps.edu.ucas.portal.view.MyLinearLayout;

/**
 * Created by Ayyad on 7/14/2015.
 */
public class AcademicCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private LayoutInflater inflater;
    private EventAcademicCalendar academicCalendars;
    private Context myContext;

    private OnItemClicked onItemClicked;


    boolean showHint;

    public AcademicCalendarAdapter(Context myContext, EventAcademicCalendar academicCalendars, boolean showHint) {
        inflater = LayoutInflater.from(myContext);
        this.myContext = myContext;
        this.academicCalendars = academicCalendars;
        this.showHint = showHint;


    }


    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.row_acadmic_calender, parent, false);
            EventViewHolder myViewHolder = new EventViewHolder(view);
            return myViewHolder;
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.header_row_academic_calendar, parent, false);

            return new VHHeader(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
            int myPosition = position - 1;
            EventViewHolder myHolder = (EventViewHolder) holder;
            myHolder.day_number.setText(academicCalendars.getEvents().get(myPosition).getEventDate().getDate() + "");
            myHolder.event_detail.setText(academicCalendars.getEvents().get(myPosition).getEventTitle().toString());
            myHolder.month.setText(EventAcademicCalendar.Event.getMonthName(academicCalendars.getEvents().get(myPosition).getEventDate().getMonth()) + "");
            myHolder.bgShape.setColor(myContext.getResources().getColor(EventAcademicCalendar.Event.getColor(academicCalendars.getEvents().get(myPosition))));
        } else if (holder instanceof VHHeader) {
            VHHeader vhHeader = (VHHeader) holder;
            int myPosition = position - 1;
              vhHeader.seasonLbl.setText( academicCalendars.getSemesterName());


            if (showHint) {
                vhHeader.linearLayout.setVisibility(View.VISIBLE);
                vhHeader.bgShapeGreen.setColor(myContext.getResources().getColor(R.color.pass));
                vhHeader.bgShapeRed.setColor(myContext.getResources().getColor(R.color.failed));

            }


        }
    }

    @Override
    public int getItemCount() {
        return academicCalendars.getEvents().size() + 1;
    }


    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView day_number;
        TextView event_detail;
        TextView month;
        MyLinearLayout mlinear;
        GradientDrawable bgShape;
        View mycircleView;

        public EventViewHolder(View itemView) {
            super(itemView);
            day_number = (TextView) itemView.findViewById(R.id.day_number);
            event_detail = (TextView) itemView.findViewById(R.id.event_detail);
            month = (TextView) itemView.findViewById(R.id.month);
            mycircleView = itemView.findViewById(R.id.mycircleView);
            bgShape = (GradientDrawable) mycircleView.getBackground();
            mlinear = (MyLinearLayout) itemView.findViewById(R.id.myLinear);
            mlinear.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (onItemClicked != null) {
               onItemClicked.getItemObject(v, academicCalendars.getEvents().get(getAdapterPosition() - 1));
            }
        }


    }


    public interface OnItemClicked {
        void getItemObject(View view, EventAcademicCalendar.Event event);

    }


    class VHHeader extends RecyclerView.ViewHolder {
        TextView seasonLbl;

        LinearLayout linearLayout;
        View greenCircle;
        GradientDrawable bgShapeGreen;

        View redCircle;
        GradientDrawable bgShapeRed;

        public VHHeader(View itemView) {
            super(itemView);
            seasonLbl = (TextView) itemView.findViewById(R.id.season_lbl);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.hintKey);
            greenCircle = itemView.findViewById(R.id.myGreenCircle);
            bgShapeGreen = (GradientDrawable) greenCircle.getBackground();
            redCircle = itemView.findViewById(R.id.myRedCircle);
            bgShapeRed = (GradientDrawable) redCircle.getBackground();
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
