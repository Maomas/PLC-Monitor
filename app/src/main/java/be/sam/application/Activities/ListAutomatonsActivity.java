package be.sam.application.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import be.sam.application.R;

public class ListAutomatonsActivity extends Activity {

    private Button btnBack;
    private SharedPreferences prefs;
    private TextView tvConnectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_automatons);

        btnBack = (Button) findViewById(R.id.btn_automatons_back);
        tvConnectedUser = (TextView) findViewById(R.id.tv_automatons_connectedUser);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(!prefs.getAll().isEmpty()){
            tvConnectedUser.setText("Connected user : "+prefs.getString("email","NULL"));
        }

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
