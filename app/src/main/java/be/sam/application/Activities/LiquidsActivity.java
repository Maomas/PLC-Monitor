package be.sam.application.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import be.sam.application.Database.Api;
import be.sam.application.Database.DbManager;
import be.sam.application.Database.User;
import be.sam.application.R;

public class LiquidsActivity extends Activity {

    private TextView tvConnectedUser;
    private Button btnBack;
    private EditText inputIp;
    private  EditText inputRack;
    private EditText inputSlot;
    private Button btnSave;

    private SharedPreferences prefs;

    private DbManager dbm;
    private User connectedUser;
    private Api a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquids);

        tvConnectedUser = (TextView) findViewById(R.id.tv_liquids_connectedUser);
        btnBack = (Button) findViewById(R.id.btn_liquids_back);
        btnSave = (Button) findViewById(R.id.btn_liquids_save);
        inputIp = (EditText) findViewById(R.id.ev_liquids_ip);
        inputRack = (EditText) findViewById(R.id.ev_liquids_rack);
        inputSlot = (EditText) findViewById(R.id.ev_liquids_slot);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dbm = new DbManager(this);

        connectedUser = dbm.getUserByEmail(prefs.getString("email", "NULL"));

        String name = "Liquides";

        //a = dbm.getApiByName(name);
        inputIp.setText(a.getIp());
        inputRack.setText(Integer.toString(a.getRack()));
        inputSlot.setText(Integer.toString(a.getSlot()));

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setIp(inputIp.getText().toString());
                a.setSlot(Integer.parseInt(inputSlot.getText().toString()));
                a.setRack(Integer.parseInt(inputRack.getText().toString()));
                dbm.updateApi(a.getId(), a);
                Intent liquidsActivity = new Intent(getApplicationContext(), LiquidsActivity.class);
                startActivity(liquidsActivity);
                finish();
            }
        });


    }
}
