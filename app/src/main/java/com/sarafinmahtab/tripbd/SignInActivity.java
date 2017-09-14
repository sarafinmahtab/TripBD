package com.sarafinmahtab.tripbd;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    String guideLoginUrl = ServerAddress.getMyServerAddress().concat("login.php");
    String guideRegUrl = ServerAddress.getMyServerAddress().concat("register.php");

    static boolean loggedIn = false;

    EditText Username, Password;

    Button signInBtn, registerBtn;

    RadioGroup radioGroup;
    RadioButton radioButton;

    private AlertDialog.Builder error_builder;
    static int radio_key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        Username = (EditText) findViewById(R.id.username_entry);
        Password = (EditText) findViewById(R.id.password_entry);

        signInBtn = (Button) findViewById(R.id.sign_in_btn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_builder = new AlertDialog.Builder(SignInActivity.this);

                final String username = Username.getText().toString();
                final String password = Password.getText().toString();

                if(username.equals("") || password.equals("")) {
                    builder_create("Invalid Login!!", "Please fill up the all the fields.");
                } else {
                    if (radio_key == 1 || radio_key == 2) {
                        StringRequest loginStringRequest = new StringRequest(Request.Method.POST, guideLoginUrl, new Response.Listener<String>() {
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
                                            loggedIn = true;

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
                    } else {
                        builder_create("Invalid Login!!", "Please select an alias!!");
                    }
                }
            }
        });
    }

    private void onRegisterButtonClick() {

        registerBtn = (Button) findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_builder = new AlertDialog.Builder(SignInActivity.this);

                if(radio_key == 1 || radio_key == 2) {
                    final View customView = LayoutInflater.from(SignInActivity.this).inflate(R.layout.reg_dialog_layout, null);

                    final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    final Dialog bottomSheetDialog = new Dialog(SignInActivity.this, R.style.MaterialDialogSheet);
                    bottomSheetDialog.setContentView(customView);
                    bottomSheetDialog.setCancelable(false);

                    if(bottomSheetDialog.getWindow() != null) {
                        bottomSheetDialog.getWindow().setLayout(width, height);
                        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        bottomSheetDialog.show ();
                    }

                    final EditText user_name = customView.findViewById(R.id.user_name_reg);
                    final EditText nick_name = customView.findViewById(R.id.nick_name_reg);
                    final EditText email = customView.findViewById(R.id.email_reg);
                    final EditText contact_num = customView.findViewById(R.id.phone_reg);
                    final EditText pass_word = customView.findViewById(R.id.password_entry_reg);
                    final EditText confirm_pass = customView.findViewById(R.id.confirm_pass_reg);
                    final Button registerFrst = customView.findViewById(R.id.reg_dialog_btn);
                    final ImageButton closeRegBtn = customView.findViewById(R.id.close_reg_sheet);

                    registerFrst.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if(user_name.getText().toString().equals("") || nick_name.getText().toString().equals("") || email.getText().toString().equals("")) {
                                                                builder_create("Invalid Login!!", "Blank Data can't be updated!!");
                                                            } else if(!pass_word.getText().toString().equals(confirm_pass.getText().toString())) {
                                                                builder_create("Invalid Login", "Password didn't matched!!");
                                                            } else {
                                                                StringRequest regStringRequest = new StringRequest(Request.Method.POST, guideRegUrl, new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONArray jsonArray = new JSONArray(response);
                                                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                            String code = jsonObject.getString("code");

                                                                            switch(code) {
                                                                                case "reg_failed":
                                                                                    builder_create("Registration Failed", jsonObject.getString("message"));
                                                                                    break;
                                                                                case "reg_success":
                                                                                    builder_create("Registration Success", jsonObject.getString("message"));
                                                                                    bottomSheetDialog.cancel();
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

                    closeRegBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.cancel();
                        }
                    });
                } else {
                    builder_create("Registration is not possible", "You have to select an alias to create your account!!");
                }
            }
        });
    }

    private void builder_create(String title, String message) {
        error_builder.setTitle(title);

        if(message.equals("forget")) {
            error_builder.setMessage("Login failed!! Did you forget your password?");

            error_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            error_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        } else {
            error_builder.setMessage(message);
            error_builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog alertDialog = error_builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static int getRadio_key() {
        return radio_key;
    }
}
