package be.sam.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.sam.application.Database.DbManager;
import be.sam.application.R;
import be.sam.application.Database.User;

public class LoginActivity extends Activity {

    private Button btnSubmit;
    private Button btnBack;

    private EditText etEmail;
    private EditText etPassword;

    private LoginActivity activity;

    private DbManager dbm;

    private SharedPreferences prefs;

    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSubmit = (Button) findViewById(R.id.btn_login_submit);
        btnBack = (Button) findViewById(R.id.btn_login_back);

        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.activity = this;

        dbm = new DbManager(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
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

                else{
                    u = dbm.getUserByEmail(etEmail.getText().toString());
                    dbm.close();
                    if(u == null)
                    {
                        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                        popup.setTitle("Warning");
                        popup.setMessage("The user "+ etEmail.getText().toString() +" doesn't exist. Please try again.");
                        popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        popup.show();
                    }
                    else if(!u.getPassword().equals(etPassword.getText().toString()))
                    {
                        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                        popup.setTitle("Warning");
                        popup.setMessage("Password incorrect. Please try again.");
                        popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        popup.show();
                    }
                    else{
                        SharedPreferences.Editor edit_datas = prefs.edit();
                        edit_datas.putString("firstName", u.getFirstName());
                        edit_datas.putString("lastName", u.getLastName());
                        edit_datas.putString("email", u.getEmail());
                        edit_datas.putString("password", u.getPassword());
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
