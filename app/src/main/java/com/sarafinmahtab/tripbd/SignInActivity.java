package com.sarafinmahtab.tripbd;

import android.content.DialogInterface;
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

public class SignInActivity extends AppCompatActivity {

    EditText username, password;
    Button sign_in_btn, register_btn;

    EditText user_name, nick_name, email, pass_word, confirm_pass;
    Button register_frst;

    RadioGroup radioGroup;
    RadioButton radioButton;

    public static int radio_key;

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
                Toast.makeText(SignInActivity.this, username.getText().toString() + '\n'
                        + password.getText().toString() + '\n'
                        + String.valueOf(radio_key), Toast.LENGTH_LONG).show();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);

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
                                 Toast.makeText(SignInActivity.this, "Hi", Toast.LENGTH_LONG).show();
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

            }
        });
        AlertDialog alertDialog = alert_builder.create();
        alertDialog.show();
    }
}
