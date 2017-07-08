package ps.edu.ucas.portal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ps.edu.ucas.portal.App;
import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Authentication;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.ui.fragment.dialog.Dialog;

import static ps.edu.ucas.portal.utils.AppSharedPreferences.AUTHENTICATION;
import static ps.edu.ucas.portal.utils.AppSharedPreferences.HAS_SEND_FCM_TOKEN;
import static ps.edu.ucas.portal.utils.AppSharedPreferences.USER;


public class UtilityUCAS {


    public static String getUnique(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public static String getOSVersion() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;


        return manufacturer
                + " - " + model
                + " - " + version
                + " ( " + versionRelease + " ) ";
    }


    public static boolean isLogin() {
        String userToken = App.getInstance().getSharedPreferences().readString(AUTHENTICATION);
        if (!userToken.equals("")) {
            return true;
        }
        return false;
    }


    public static void logOut() {
        App.getInstance().getSharedPreferences().writeString(AUTHENTICATION, "");
        App.getInstance().getSharedPreferences().clear();
    }


    public static void setHasUpdateToken(boolean result) {
        App.getInstance().getSharedPreferences().writeBoolean(HAS_SEND_FCM_TOKEN, result);
    }

    public static boolean isHasUpdateToken() {
        boolean result = App.getInstance().getSharedPreferences().readBoolean(HAS_SEND_FCM_TOKEN);
        return result;
    }



    public static void setCashData(String name, String value) {
        App.getInstance().getSharedPreferences().writeString(name, value);
    }

    public static String getCashData(String name) {
        String result = App.getInstance().getSharedPreferences().readString(name);
        return result;
    }


    public static void setUserData(User user) {
        String userData = new Gson().toJson(user);
        Log.d("prference", userData);
        App.getInstance().getSharedPreferences().writeString(USER, userData);
    }

    public static User getUserData() {
        String userJsonData = App.getInstance().getSharedPreferences().readString(USER);
        User user = new Gson().fromJson(userJsonData, User.class);
        return user;
    }


    public static void setAuthentication(Authentication authentication) {
        String userData = new Gson().toJson(authentication);
        Log.d("prference", userData);
        App.getInstance().getSharedPreferences().writeString(AUTHENTICATION, userData);
    }

    public static Authentication getAuthentication() {
        String userJsonData = App.getInstance().getSharedPreferences().readString(AUTHENTICATION);
        Authentication authentication = new Gson().fromJson(userJsonData, Authentication.class);
        return authentication;
    }


    public static void initLanguage(Context context) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = Locale.ENGLISH;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    public static void initLanguage(Context context, String local) {
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    public static void AppDialog(Context context, int title, int message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(title));
        dialog.setMessage(context.getResources().getString(message));
        dialog.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void AppDialog(Context context, int title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(title));
        dialog.setMessage(message);
        dialog.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public static void AppDialog(Context context, int title, int message, final DialogEvent dialogEvent) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(title));
        dialog.setMessage(context.getResources().getString(message));
        dialog.setCancelable(false);
        //dialog.setNegativeButton(android.R.string.cancel,null);
        dialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogEvent.onBack();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("لا",null);
        dialog.show();

    }




    public static void progressState(View view,ProgressStatus progressStatus){
        LinearLayout waitingLayout = (LinearLayout) view.findViewById(R.id.waiting_layout);
        LinearLayout retryLayout = (LinearLayout) view.findViewById(R.id.retry_layout);
        LinearLayout includeLayout = (LinearLayout) view.findViewById(R.id.inc_retry_layout);

        switch (progressStatus){
            case WAITING:
                waitingLayout.setVisibility(View.VISIBLE);
                retryLayout.setVisibility(View.GONE);
                includeLayout.setVisibility(View.VISIBLE);
             break;
            case FINISHED:
                waitingLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.GONE);
                includeLayout.setVisibility(View.GONE);
                break;
            case RETRY:
                waitingLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.VISIBLE);
                includeLayout.setVisibility(View.VISIBLE);
                break;

            case PULL_TO_REFRESH:
                waitingLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.GONE);
                includeLayout.setVisibility(View.GONE);
                break;

        }




    }


    public enum ProgressStatus {
        WAITING, FINISHED, RETRY, PULL_TO_REFRESH
    }

    public interface DialogEvent {
        void onBack();
    }


    public static void setupUI(final Activity activity) {
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (activity.getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        });
    }


    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }


    public static void showLoginDialog(Fragment fragment, Object object, String title, String msg, boolean isConfirm) {
        final Dialog mDialog = Dialog.getDialogFragment(object,title, msg, isConfirm);
        mDialog.setTargetFragment(fragment.getTargetFragment(), 0);
        mDialog.show(fragment.getActivity().getSupportFragmentManager(),"tag");

    }



}
