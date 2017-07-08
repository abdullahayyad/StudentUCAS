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
import ps.edu.ucas.portal.model.Financial;

/**
 * Created by Ayyad on 7/14/2015.
 */
public class FinancialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private LayoutInflater inflater;
    private Financial financial;
    private Context myContext;
    boolean showHint;

    public FinancialAdapter(Context myContext, Financial transcript, boolean showHint){
        inflater = LayoutInflater.from(myContext);
       this.myContext = myContext;
        this.financial = transcript;
        this.showHint = showHint;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.row_financial,parent,false);
            return  new  MyViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.header_row_financial,parent,false);
            return new VHHeader(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            int myPosition = position-1;
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.finanical_detail.setText(financial.getTransactionsFinancial().get(myPosition).getDescription().toString());
            myHolder.debit_financial.setText(financial.getTransactionsFinancial().get(myPosition).getDebit() + "");
            myHolder.credit_financial.setText(financial.getTransactionsFinancial().get(myPosition).getCredit() + "");
            myHolder.date_financial.setText(financial.getTransactionsFinancial().get(myPosition).getDate() + "");
            myHolder.record_financial.setText(financial.getTransactionsFinancial().get(myPosition).getRecordNumber() + "");
            myHolder.bgShape.setColor(myContext.getResources().getColor(R.color.colorPrimary));




        }else
        if (holder instanceof VHHeader) {
            VHHeader vhHeader = (VHHeader) holder;
           vhHeader.student_balance.setText(financial.getSemesterBalance()+"");
            vhHeader.total_balance_std.setText(financial.getTotalBalance()+"");
            vhHeader.season_number.setText(financial.getSemesterId()+"");

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
            return financial.getTransactionsFinancial().size()+1;
    }


     class MyViewHolder extends RecyclerView.ViewHolder{
        TextView finanical_detail;
        TextView debit_financial;
        TextView credit_financial;
        TextView date_financial;
         TextView record_financial;

        LinearLayout markLayout;



        GradientDrawable bgShape;
        View mycircleView;
        public MyViewHolder(View itemView) {
            super(itemView);
            finanical_detail = (TextView) itemView.findViewById(R.id.finanical_detail);
            debit_financial = (TextView) itemView.findViewById(R.id.debit_financial);
            credit_financial = (TextView) itemView.findViewById(R.id.credit_financial);
            date_financial = (TextView) itemView.findViewById(R.id.date_financial);
            record_financial = (TextView) itemView.findViewById(R.id.record_financial);

            mycircleView =  itemView.findViewById(R.id.mycircleView);
            bgShape = (GradientDrawable)mycircleView.getBackground();
            markLayout = (LinearLayout) itemView.findViewById(R.id.markLayout);


        }
    }


    class VHHeader extends RecyclerView.ViewHolder {
        TextView student_balance;
        TextView total_balance_std;

        TextView season_number;



        LinearLayout linearLayout;
        View greenCircle;
        GradientDrawable bgShapeGreen;

        View yellowCircle;
        GradientDrawable bgShapeYellow;

        View redCircle;
        GradientDrawable bgShapeRed;






        public VHHeader(View itemView) {
            super(itemView);
            student_balance = (TextView) itemView.findViewById(R.id.student_balance);
            total_balance_std = (TextView) itemView.findViewById(R.id.total_balance_std);

            season_number = (TextView) itemView.findViewById(R.id.season_number);


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
