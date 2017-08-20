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
import com.sarafinmahtab.tripbd.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    static boolean logged_in = false;

    EditText Username, Password;
    EditText user_name, nick_name, email, contact_num, pass_word, confirm_pass;

    Button sign_in_btn, register_btn;
    Button register_frst;

    RadioGroup radioGroup;
    RadioButton radioButton;

    private AlertDialog.Builder error_builder;
    private int radio_key = 0;

//    String guide_login_url = "http://192.168.0.63/TripBD/login.php";
//    String guide_reg_url = "http://192.168.0.63/TripBD/register.php";

    String guide_login_url = "http://192.168.43.65/TripBD/login.php";
    String guide_reg_url = "http://192.168.43.65/TripBD/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        onRadioButtonClick();
        OnSignInButtonClick();
        onRegisterButtonClick();
    }

    private void onRadioButtonClick() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_area);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_id);

                if(radioButton.getId() == R.id.traveller_radio) {
                    radio_key = 1;
                } else if(radioButton.getId() == R.id.tour_guide_radio) {
                    radio_key = 2;
                }
            }
        });
    }

    private void OnSignInButtonClick() {
        sign_in_btn = (Button) findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = (EditText) findViewById(R.id.username_entry);
                Password = (EditText) findViewById(R.id.password_entry);

                error_builder = new AlertDialog.Builder(SignInActivity.this);

                final String username = Username.getText().toString();
                final String password = Password.getText().toString();

                if(username.equals("") || password.equals("")) {
                    builder_create("Invalid Login!!", "Please fill up the all the fields.");
                } else {
                    if (radio_key == 1) {
                        builder_create("Travller's Profile", "Still working on it!!");
                    } else if (radio_key == 2) {

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

                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            intent.putExtras(bundle);

                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

                                params.put("username", Username.getText().toString());
                                params.put("password", Password.getText().toString());

                                return params;
                            }
                        };

                        MySingleton.getMyInstance(SignInActivity.this).addToRequestQueue(loginStringRequest);
                    } else if(radio_key == 0) {
                        builder_create("Invalid Login!!", "Please select an alias!!");
                    }
                }
            }
        });
    }

    private void onRegisterButtonClick() {
        register_btn = (Button) findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder reg_dialog_builder = new AlertDialog.Builder(SignInActivity.this);

//                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
                final View customView;

                customView = inflater.inflate(R.layout.reg_dialog_layout, null);

                user_name = customView.findViewById(R.id.user_name_reg);
                nick_name = customView.findViewById(R.id.nick_name_reg);
                email = customView.findViewById(R.id.email_reg);
                contact_num = customView.findViewById(R.id.phone_reg);
                pass_word = customView.findViewById(R.id.password_entry_reg);
                confirm_pass = customView.findViewById(R.id.confirm_pass_reg);
                register_frst = customView.findViewById(R.id.reg_dialog_btn);

                reg_dialog_builder.setView(customView)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = reg_dialog_builder.create();
                alert.setTitle("Complete Registration");
                alert.show();

                register_frst.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         error_builder = new AlertDialog.Builder(SignInActivity.this);

                                                         if(user_name.getText().toString().equals("") || nick_name.getText().toString().equals("") || email.getText().toString().equals("")) {
                                                             builder_create("Invalid Login!!", "Blank Data can't be updated!!");
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
                                                                                 alert.cancel();
                                                                             case "reg_success":
                                                                                 builder_create("Registration Success", jsonObject.getString("message"));
                                                                                 alert.cancel();
                                                                         }
                                                                     } catch (JSONException e) {
                                                                         Toast.makeText(SignInActivity.this, response, Toast.LENGTH_LONG).show();
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
                                                                     params.put("contact_num", contact_num.getText().toString());
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
        error_builder.setTitle(title);
        error_builder.setMessage(message);
        error_builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = error_builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static boolean isLogged_in() {
        return logged_in;
    }
}
