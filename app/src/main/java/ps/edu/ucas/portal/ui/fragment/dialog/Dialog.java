package ps.edu.ucas.portal.ui.fragment.dialog;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rey.material.app.DialogFragment;

import java.util.HashMap;

import ps.edu.ucas.portal.R;


/**
 * Created by Ayyad on 9/26/2015.
 */
public class Dialog extends DialogFragment {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IS_CONFIRM = "is_confirm";
    private static final String OBJECT = "object";

    private String title ;
    private String  msg;
    private Object object;
    private Boolean isConfirm;
    private dialogStatus mDialogStatus;
    HashMap<String,Object> hashMap;

    public static Dialog getDialogFragment(Object object,String title,String msg,boolean isConfirm){
        Dialog myDialogFragment = new Dialog();
        Bundle bundle = new Bundle();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put(OBJECT,object);
        bundle.putSerializable(OBJECT,  hashMap);
        bundle.putString(TITLE,title);
        bundle.putString(MESSAGE, msg);
        bundle.putBoolean(IS_CONFIRM,isConfirm);
        myDialogFragment.setArguments(bundle);
        return myDialogFragment;
    }

    public Dialog(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


       // try {

       /* } catch (ClassCastException e) {
            //throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mDialogStatus = (dialogStatus) getTargetFragment();
            } catch (ClassCastException e) {
            //throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }



        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            msg = getArguments().getString(MESSAGE);
            hashMap = (HashMap<String, Object>) getArguments().getSerializable(OBJECT);
            isConfirm = getArguments().getBoolean(IS_CONFIRM);
        }
    }





    @NonNull
    @Override
    public com.rey.material.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final com.rey.material.app.Dialog  mDialog = new  com.rey.material.app.Dialog (getActivity());

       // mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_message_confirm);



        ((TextView) mDialog.findViewById(R.id.dialogTitle)).setText(title);
        ((TextView) mDialog.findViewById(R.id.message_text)).setText(msg);


        ((Button) mDialog.findViewById(R.id.btn_agree)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                if(mDialogStatus !=null)
                mDialogStatus.isConfirm(true,hashMap.get(OBJECT));
            }
        });

        if(isConfirm){
            ((Button) mDialog.findViewById(R.id.btn_cancle)).setVisibility(View.GONE);
        }else{
            ((Button) mDialog.findViewById(R.id.btn_cancle)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();

                    if(mDialogStatus !=null)
                    mDialogStatus.isConfirm(false,hashMap.get(OBJECT));
                }
            });
        }

        return mDialog;
    }

    public void setDialogStatus(dialogStatus mDialogStatus){
        this.mDialogStatus = mDialogStatus;
    }


    public interface dialogStatus{
        void isConfirm(boolean isConfirm, Object object);
    }
}
