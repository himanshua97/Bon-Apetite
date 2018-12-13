package resturant.example.com.comexampleresturant;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class SignedInActivity extends AppCompatActivity {
  String name,email,phone,userid,dob,aadhar_no,password;

    EditText txtPhone,txtDob,txtVehicleNo,txtPassword,txtEmail,txtName;
    SharedPreferences sharedPreferences;

    FloatingActionButton fab;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        context=this;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


txtDob=(EditText)findViewById(R.id.txtDob) ;
txtPassword=(EditText)findViewById(R.id.txtPassword) ;
txtPhone=(EditText)findViewById(R.id.txtPhone) ;
txtVehicleNo=(EditText)findViewById(R.id.txtVehicleNo) ;
txtEmail=(EditText)findViewById(R.id.txtEmail) ;
txtName=(EditText)findViewById(R.id.txtName) ;


//        toolbar.setTitle(name);
//        setSupportActionBar(toolbar);
//        Log.i("SA", "onCreate: phone from prev"+phone);
//        if(TextUtils.isEmpty(phone)){
//            Log.i("SA", "onCreate: phone is"+phone);
//            phone=sharedPreferences.getString("phone",null);
//
//        }
//        txtPhone.setText(phone);
//        if(aadhar_no==null){
//            aadhar_no=sharedPreferences.getString("aadhar_no",null);
//        }
//        txtVehicleNo.setText(aadhar_no);
//        if(dob==null){
//            dob=sharedPreferences.getString("dob",null);
//        }
//        txtDob.setText(dob);
//        if(password==null){
//            password=sharedPreferences.getString("password",null);
//        }
//        txtPassword.setText(password);


         fab =  findViewById(R.id.fab);



        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                                                                                 // Do whatever you want when the date is selected.
                                                                                String dd=Integer.toString(i2);
                                                                                 if(Integer.toString(i2).length()<2){
                                                                                     dd="0"+i2;
                                                                                 }
                                                                                 String mm=Integer.toString(i1);
                                                                                 if(Integer.toString(i1).length()<2){
                                                                                     mm="0"+i1;
                                                                                 }
                                                                                 txtDob.setText(i+"-"+mm+"-"+dd);

                                                                             }
                                                                         },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.setYearRange(1900, 2009); // You can add your value for YEARS_IN_THE_FUTURE.
txtDob.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       datePickerDialog.show(getFragmentManager(),"hello");
    }
});


    }
    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w("SA", "storage permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,       Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, permissions, 5);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        5);
            }
        };



    }
    ProgressDialog dialog;
public void save(View v){

   phone=txtPhone.getText().toString();
    dob=txtDob.getText().toString();
    aadhar_no=txtVehicleNo.getText().toString();
    password=txtPassword.getText().toString();
    name=txtName.getText().toString();
    email=txtEmail.getText().toString();
    if(!TextUtils.isEmpty(aadhar_no)){
        
        if(password.length()>=6){
            if(!TextUtils.isEmpty(phone)){
                if(!TextUtils.isEmpty(dob)){
if(name.length()>0){
    if(email.contains("@")&&email.contains(".")){
        //String name, String userid, String url, String phone, String email, String vehicle_id, String dob, String password)
        // User user=new User(name,userid,url_image,phone,email,vehicle_no,dob,password);
        dialog = ProgressDialog.show(context,
                "Please Wait ...",
                "Saving Details", true, false);
        RequestParams params = new RequestParams();
        params.add("userid", userid);
        params.add("name", name);
        params.add("dob", dob);
        params.add("email", email);
        params.add("password", password);
        params.add("aadhar_no", aadhar_no);
        params.add("phone", phone);
        AsyncHttpClient client = new AsyncHttpClient();
// Don't forget to change the IP address to
// your LAN address. Port no as
// well.

        client.post(Constant.url_save_user, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    dialog.dismiss();
                    String str = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("success")) {
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("userid",userid)
                                .putString("name",name)
                                .putString("dob",dob)
                                .putString("email",email)
                                .putString("password",password)
                                .putString("aadhar_no",aadhar_no)
                                .putString("phone",phone)
                                .apply();

                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    dialog.dismiss();
                    String str = new String(responseBody, "UTF-8");


                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }else{
        Toast.makeText(context, "Please check name.", Toast.LENGTH_SHORT).show();
    }
}else{
    Toast.makeText(context, "Please check name", Toast.LENGTH_SHORT).show();
}

                  


                }else {
                    Toast.makeText(context, "Please check dob", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "Please check phone no.", Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(this, "Password requries minimum six characters!", Toast.LENGTH_SHORT).show();
        }
    }else{
        Toast.makeText(this, "Aadhar No. cannot be blank!", Toast.LENGTH_SHORT).show();
    }

}

}
