package com.sarafinmahtab.tripbd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    static boolean logged_in = false;

    EditText username, password;
    Button sign_in_btn, register_btn;

    EditText user_name, nick_name, email, pass_word, confirm_pass;
    Button register_frst;

    RadioGroup radioGroup;
    RadioButton radioButton;

    public static int radio_key;

    String guide_login_url = "http://192.168.0.63/TripBD/login.php";
    String guide_reg_url = "http://192.168.0.63/TripBD/register.php";

    private AlertDialog.Builder alert_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        radioGroup = (RadioGroup) findViewById(R.id.radio_area);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_id);

                if(radioButton.getId() == R.id.traveller) {
                    radio_key = 1;
                } else if(radioButton.getId() == R.id.tour_guide) {
                    radio_key = 2;
                } else {
                    radio_key = 0;
                }
            }
        });

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password_d);

        sign_in_btn = (Button) findViewById(R.id.sign_in);
        register_btn = (Button) findViewById(R.id.register);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_key == 0) {
                    builder_create("Login not possible", "Please select an alias!!");
                } else {

                    StringRequest loginStringRequest = new StringRequest(Request.Method.POST, guide_login_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");

                                switch (code) {
                                    case "login_failed":
                                        builder_create(code, jsonObject.getString("message"));
                                        break;
                                    case "login_success":
                                        logged_in = true;

                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_id", jsonObject.getString("user_id"));
                                        bundle.putString("user_name", jsonObject.getString("user_name"));
                                        bundle.putString("nick_name", jsonObject.getString("nick_name"));
                                        bundle.putString("email", jsonObject.getString("email"));
                                        bundle.putString("password", jsonObject.getString("password"));
                                        bundle.putString("user_type_id", jsonObject.getString("user_type_id"));

                                        Toast.makeText(SignInActivity.this, code, Toast.LENGTH_LONG).show();

                                        SignInActivity.super.onBackPressed();
                                        break;
                                }
                            } catch (JSONException e) {
                                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("username", username.getText().toString());
                            params.put("password", password.getText().toString());

                            return params;
                        }
                    };

                    MySingleton.getMyInstance(SignInActivity.this).addToRequestQueue(loginStringRequest);
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);

//                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
                final View customView;

                customView = inflater.inflate(R.layout.reg_dialog_layout, null);

                user_name = customView.findViewById(R.id.user_name_d);
                nick_name = customView.findViewById(R.id.nick_name_d);
                email = customView.findViewById(R.id.email_d);
                pass_word = customView.findViewById(R.id.password_d);
                confirm_pass = customView.findViewById(R.id.confirm_pass_d);

                register_frst = customView.findViewById(R.id.register_first);

                builder.setView(customView)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Complete Registration");
                alert.show();

                register_frst.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             alert_builder = new AlertDialog.Builder(SignInActivity.this);

                             if(user_name.getText().toString().equals("") || nick_name.getText().toString().equals("") || email.getText().toString().equals("")) {
                                 builder_create("Invalid Login", "Blank Data can't be updated!!");
                             } else if(!pass_word.getText().toString().equals(confirm_pass.getText().toString())) {
                                 builder_create("Invalid Login", "Password didn't matched!!");
                             } else {
                                 StringRequest regStringRequest = new StringRequest(Request.Method.POST, guide_reg_url, new Response.Listener<String>() {
                                     @Override
                                     public void onResponse(String response) {
                                         try {
                                             JSONArray jsonArray = new JSONArray(response);
                                             JSONObject jsonObject = jsonArray.getJSONObject(0);
                                             String code = jsonObject.getString("code");

                                             switch(code) {
                                                 case "reg_failed":
                                                     builder_create("Registration Failed", jsonObject.getString("message"));
                                                 case "reg_success":
                                                     builder_create("Registration Success", jsonObject.getString("message"));
                                             }
                                         } catch (JSONException e) {
                                             Toast.makeText(SignInActivity.this, response + '\n' + e.getMessage(), Toast.LENGTH_LONG).show();
                                             e.printStackTrace();
                                         }
                                     }
                                 }, new Response.ErrorListener() {
                                     @Override
                                     public void onErrorResponse(VolleyError error) {
                                         Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                         error.printStackTrace();
                                     }
                                 }) {
                                     @Override
                                     protected Map<String, String> getParams() throws AuthFailureError {

                                         Map<String, String> params = new HashMap<>();

                                         params.put("radio_key", String.valueOf(radio_key+1));
                                         params.put("user_name", user_name.getText().toString());
                                         params.put("nick_name", nick_name.getText().toString());
                                         params.put("email", email.getText().toString());
                                         params.put("pass_word", pass_word.getText().toString());

                                         return params;
                                     }
                                 };

                                 MySingleton.getMyInstance(SignInActivity.this).addToRequestQueue(regStringRequest);
                             }
                         }
                    }
                );
            }
        });
    }

    private void builder_create(String title, String message) {
        alert_builder.setTitle(title);
        alert_builder.setMessage(message);
        alert_builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert_builder.create();
        alertDialog.show();
    }

    public static boolean isLogged_in() {
        return logged_in;
    }
}
