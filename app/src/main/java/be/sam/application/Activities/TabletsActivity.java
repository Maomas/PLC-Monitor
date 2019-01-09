package be.sam.application.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import be.sam.application.Api_Monitoring.ReadTabletsS7;
import be.sam.application.Database.Api;
import be.sam.application.Database.DbManager;
import be.sam.application.Database.User;
import be.sam.application.R;

public class TabletsActivity extends Activity {

    private TextView tvConnectedUser;
    private Button btnBack;
    private EditText inputIp;
    private EditText inputRack;
    private EditText inputSlot;
    private Button btnSave;
    private Button btnConnection;
    private TextView tvPlc;
    private TextView tvBottles;
    private TextView tvPills;
    private TextView tvConnectionType;
    private Button btnManagement;

    private ReadTabletsS7 readS7;

    private SharedPreferences prefs;

    private ColorStateList baseColor;

    //Permet d'avoir accès aux infos du réseau
    private NetworkInfo network;

    //Permet d'avoir accès au réseau
    private ConnectivityManager connectionStatus;

    private ProgressBar pbProgressionS7;

    private DbManager dbm;
    private User connectedUser;
    private Api a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablets);

        tvConnectedUser = (TextView) findViewById(R.id.tv_tablets_connectedUser);
        btnBack = (Button) findViewById(R.id.btn_tablets_back);
        btnConnection = (Button) findViewById(R.id.btn_tablets_connectionS7);
        btnManagement = (Button) findViewById(R.id.btn_tablets_modify);
        btnSave = (Button) findViewById(R.id.btn_tablets_save);
        inputIp = (EditText) findViewById(R.id.ev_tablets_ip);
        inputRack = (EditText) findViewById(R.id.ev_tablets_rack);
        inputSlot = (EditText) findViewById(R.id.ev_tablets_slot);
        pbProgressionS7 = (ProgressBar) findViewById(R.id.pb_tablets_progressionS7);
        tvPlc = (TextView) findViewById(R.id.tv_tablets_plc);
        tvBottles = (TextView) findViewById(R.id.tv_tablets_bottles);
        tvPills = (TextView) findViewById(R.id.tv_tablets_pills);
        tvConnectionType = (TextView) findViewById(R.id.tv_tablets_connectionType);

        baseColor = tvPlc.getTextColors();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dbm = new DbManager(this);

        connectionStatus = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connectionStatus.getActiveNetworkInfo();

        connectedUser = dbm.getUserByEmail(prefs.getString("email", "NULL"));

        String name = "Comprimés";

        a = dbm.getApiByName(name);
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
                Intent tabletsActivity = new Intent(getApplicationContext(), TabletsActivity.class);
                startActivity(tabletsActivity);
                finish();
            }
        });

        btnManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent managementActivity = new Intent(getApplicationContext(), TabletsManagementActivity.class);
                startActivity(managementActivity);
                finish();

            }
        });

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(network != null && network.isConnectedOrConnecting()){

                    if(btnConnection.getText().equals("Connection")) {
                        tvConnectionType.setText("Connection type : "+network.getTypeName());
                        tvConnectionType.setTextColor(Color.rgb(9,171,31));
                        btnConnection.setText("Disconnection");
                        Toast.makeText(getApplicationContext(),"Connection started.",Toast.LENGTH_SHORT).show();

                        readS7 = new ReadTabletsS7(btnConnection, pbProgressionS7, tvPlc, tvBottles, tvPills, tvConnectionType);
                        readS7.Start(a.getIp(), Integer.toString(a.getRack()), Integer.toString(a.getSlot()));
                    }
                    else{
                        readS7.Stop();

                        btnConnection.setText(("Connection"));
                        Toast.makeText(getApplicationContext(), "Connection stopped.", Toast.LENGTH_SHORT).show();
                        tvConnectionType.setText("Connection type : disconnected");
                        tvConnectionType.setTextColor(baseColor);
                    }

                }
                else Toast.makeText(getApplicationContext(), "Network connection failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
