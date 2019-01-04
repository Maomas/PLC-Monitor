package be.sam.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.sam.application.Database.DbManager;
import be.sam.application.R;
import be.sam.application.Database.User;

public class SignupActivity extends Activity {

    private EditText etFirstname;
    private EditText etLastname;
    private EditText etEmail;
    private EditText etPassword;

    private SharedPreferences prefs;

    private Button btnSubmit;
    private Button btnBack;

    private DbManager dbm;

    private SignupActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSubmit = (Button) findViewById(R.id.btn_signup_submit);
        btnBack = (Button) findViewById(R.id.btn_signup_back);
        etFirstname = (EditText) findViewById(R.id.et_signup_firstname);
        etLastname = (EditText) findViewById(R.id.et_signup_lastname);
        etEmail = (EditText) findViewById(R.id.et_signup_email);
        etPassword = (EditText) findViewById(R.id.et_signup_password);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dbm = new DbManager(this);

        this.activity = this;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etFirstname.getText().toString().equals("") || etLastname.getText().toString().equals("") || etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("Please complete the empty fields.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }

                else if(etPassword.getText().toString().length() <= 4)
                {
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("Your password must have minimum 4 characters.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }

                else {
                    User userCheck = dbm.getUserByEmail(etEmail.getText().toString());
                    dbm.close();

                    if(userCheck != null) {
                        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                        popup.setTitle("Warning");
                        popup.setMessage("The user already exists.  Choose another email.");
                        popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        popup.show();
                    }
                    else {
                        String firstname = "\"" + etFirstname.getText().toString() + "\"";
                        String lastname = "\"" + etLastname.getText().toString() + "\"";
                        String email = "\"" + etEmail.getText().toString() + "\"";
                        String password = "\"" + etPassword.getText().toString() + "\"";

                        User u = new User(firstname, lastname, email, password, 0);
                        dbm.insertUser(u);

                        dbm.close();

                        SharedPreferences.Editor edit_datas = prefs.edit();
                        edit_datas.putString("firstName", firstname.replace("\"",""));
                        edit_datas.putString("lastName", lastname.replace("\"",""));
                        edit_datas.putString("email", email.replace("\"",""));
                        edit_datas.putString("password", password.replace("\"",""));
                        edit_datas.putInt("rights", u.getRights());

                        edit_datas.commit();

                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }

        });

    }
}
