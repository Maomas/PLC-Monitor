package be.sam.application.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.sam.application.Database.DbManager;
import be.sam.application.R;

public class MainActivity extends AppCompatActivity {

    private Button btnSignup;
    private Button btnList;
    private Button btnLogin;
    private TextView tvConnectedUser;

    private SharedPreferences prefs;

    private DbManager dbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignup = (Button) findViewById(R.id.btn_main_signup);
        btnList = (Button) findViewById(R.id.btn_main_list);
        btnLogin = (Button) findViewById(R.id.btn_main_login);
        tvConnectedUser = (TextView) findViewById(R.id.tv_main_connectedUser);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(!prefs.getAll().isEmpty()){
            Toast.makeText(this, "Welcome, "+prefs.getString("firstName", "NULL")+" "+prefs.getString("lastName","NULL")+".", Toast.LENGTH_SHORT).show();
            tvConnectedUser.setText("Connected user : "+prefs.getString("email","NULL"));
        }
        if(prefs.getInt("rights",2) != 2){
            btnList.setVisibility(View.GONE);
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signupActivity);
                finish();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listActivity = new Intent(getApplicationContext(), ListUsersActivity.class);
                startActivity(listActivity);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    }
}
