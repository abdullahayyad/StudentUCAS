package ps.edu.ucas.portal.ui;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rey.material.app.Dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Authentication;
import ps.edu.ucas.portal.model.StatusResult;
import ps.edu.ucas.portal.model.User;
import ps.edu.ucas.portal.service.WebService;
import ps.edu.ucas.portal.utils.UtilityUCAS;

import static ps.edu.ucas.portal.service.WebService.RESULT;

public class StudentLoginActivity extends FragmentActivity implements WebService.OnResponding {

    public final static String ERROR_INVALID_GRANT = "invalid_grant";
    public final static String ERROR_INVALID_CLIENT_SECRET = "invalid_clientSecret";
    public final static String ERROR_INVALID_CLIENT_ID = "invalid_clientId";


    ProgressDialog progressDialog;
    EditText numberId, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //checkAccount();
        setContentView(R.layout.activity_login_student);

        numberId = (EditText) findViewById(R.id.edit_user_id);

        password = (EditText) findViewById(R.id.edit_password);


        numberId.setText("120160046");
        password.setText("12345678@A");


        // numberId.setText("120150001");
        // password.setText("93123$186245");

        // numberId.setText("120117012");
        //password.setText("mjawi095521406630");


        //numberId.setText("120150001");
        //password.setText("12345678#A");

        final Button login = (Button) findViewById(R.id.btn_login);

        numberId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {

                    login.setEnabled(true);
                } else {
                    login.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] AllP = new String[]{Manifest.permission.WRITE_CALENDAR};


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(AllP, 123);

                        if (PackageManager.PERMISSION_DENIED == -1) {

                            Toast.makeText(StudentLoginActivity.this, "الرجاء الموافقة على الصلاحيات لتتمكن من تسجيل الدخول", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        //Toast.makeText(v.getContext(), UtilityUCAS.getUnique(v.getContext()), Toast.LENGTH_LONG).show();
                        startRequestLogin();
                    }
                } else {
                    //Toast.makeText(v.getContext(), UtilityUCAS.getUnique(v.getContext()), Toast.LENGTH_LONG).show();
                    startRequestLogin();

                }


            }

        });
    }


    public void startRequestLogin() {
        progressDialog = new ProgressDialog(StudentLoginActivity.this);
        progressDialog.setMessage("تسجيل الدخول");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("grant_type", "password");
        hashMap.put("username", numberId.getText().toString());
        hashMap.put("password", password.getText().toString());
        hashMap.put("device_id", UtilityUCAS.getUnique(this));
        hashMap.put("client_secret", WebService.CLIENT_SECRET);
        new WebService().startRequest(WebService.RequestAPI.AUTHENTICATION, hashMap, this);

    }


    @Override
    public void onResponding(WebService.RequestAPI requestAPI, WebService.StatusConnection statusConnection, HashMap<String, Object> objectResult) {
        progressDialog.dismiss();
        switch (statusConnection) {
            case SUCCESS:
                if (requestAPI == WebService.RequestAPI.AUTHENTICATION) {
                    try {
                        JSONObject ObJsonObject = new JSONObject(objectResult.get(RESULT).toString());
                        Authentication authentication = new Gson().fromJson(String.valueOf(ObJsonObject), Authentication.class);
                        UtilityUCAS.setAuthentication(authentication);
                        new WebService().startRequest(WebService.RequestAPI.PROFILE, null, this);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_clint_id_secret);
                    }
                } else if (requestAPI == WebService.RequestAPI.PROFILE) {

                    try {

                        JSONObject ObJsonObject = new JSONObject(objectResult.get(RESULT).toString());
                        User user = new Gson().fromJson(String.valueOf(ObJsonObject), User.class);
                        UtilityUCAS.setUserData(user);
                        Intent i = new Intent(this, MainContainerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_clint_id_secret);
                    }
                }
                break;
            case NO_CONNECTION:

                UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.no_internet_connection);
                break;
            case NO_PRIVILEGE:
                if (requestAPI == WebService.RequestAPI.AUTHENTICATION) {
                    try {
                        if (objectResult.get(RESULT) != null) {
                            JSONObject ObJsonObject = new JSONObject(objectResult.get(RESULT).toString());
                            StatusResult statusResult = new Gson().fromJson(String.valueOf(ObJsonObject), StatusResult.class);
                            if (statusResult != null) {
                                if (statusResult.getError().equals("invalid_grant")) {
                                    UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_grant);
                                } else {
                                    UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_grant);
                                }
                            } else
                                UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_grant);

                        } else
                            UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_clint_id_secret);

                    } catch (Exception e) {
                        UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.invalid_clint_id_secret);
                    }
                } else
                    UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.error_privilege_message);


                break;
            case OTHER:
                UtilityUCAS.AppDialog(this, R.string.error_login_title, R.string.no_internet_connection);
                break;
        }
    }


    public void showLoginDialog(String title, String msg) {
        final Dialog mDialog = new Dialog(StudentLoginActivity.this);

        if (!title.equals("")) {
            ((TextView) mDialog.findViewById(R.id.dialogTitle)).setText(title);
        }
        mDialog.setContentView(R.layout.dialog_message);
        ((TextView) mDialog.findViewById(R.id.message_text)).setText(msg);
        ((Button) mDialog.findViewById(R.id.btn_agree)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.setCancelable(false);

        mDialog.show();
    }

    public void showLoginDialog(String msg) {
        showLoginDialog("", msg);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRequestLogin();

        }
    }


}
