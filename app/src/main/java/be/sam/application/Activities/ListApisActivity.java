package be.sam.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import be.sam.application.Database.Api;
import be.sam.application.Database.DbManager;
import be.sam.application.Database.User;
import be.sam.application.R;

public class ListApisActivity extends Activity {

    private Button btnBack;
    private SharedPreferences prefs;
    private TextView tvConnectedUser;
    private List<Api> apis;
    private DbManager dbm;
    private LinearLayout ll_apislist;
    private Activity activity;
    private User connectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apis);

        btnBack = (Button) findViewById(R.id.btn_apis_back);
        tvConnectedUser = (TextView) findViewById(R.id.tv_apis_connectedUser);
        ll_apislist = (LinearLayout) findViewById(R.id.ll_apis);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);



        this.activity = this;

        dbm = new DbManager(this);

        connectedUser = dbm.getUserByEmail(prefs.getString("email", "NULL"));

        if(!prefs.getAll().isEmpty()){
            tvConnectedUser.setText("Connected user : "+prefs.getString("email","NULL"));
        }

        apis = dbm.getApis();

        for(Api api : apis){
            final TextView tv = new TextView(this);

            final EditText inputName = new EditText(this);
            final EditText inputIp = new EditText(this);
            final EditText inputRack = new EditText(this);
            final EditText inputSlot = new EditText(this);
            final EditText inputType = new EditText(this);
            final EditText inputDatabloc = new EditText(this);

            final LinearLayout ll_popup = new LinearLayout(this);

            String type;

            if(api.getType() == 0) type = "Condtionnement de comprimés";
            else type = "Asservissement de liquides";

            String rack = Integer.toString(api.getRack());
            String slot = Integer.toString(api.getSlot());


            inputName.setText(api.getName());
            inputIp.setText(api.getIp());
            inputRack.setText(rack);
            inputSlot.setText(slot);
            inputType.setText(type);
            inputDatabloc.setText(api.getDatabloc());

            ll_popup.addView(inputName);
            ll_popup.addView(inputIp);
            ll_popup.addView(inputRack);
            ll_popup.addView(inputSlot);
            ll_popup.addView(inputType);
            ll_popup.addView(inputDatabloc);

            ll_popup.setVerticalGravity(1);
            ll_popup.setOrientation(LinearLayout.VERTICAL);

            tv.setText(api.toString());
            tv.setId(api.getId());
            tv.setBackgroundResource(R.color.white);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,0,0,20);
            tv.setLayoutParams(lp);


            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(connectedUser.getRights() == 0) {
                        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                        popup.setTitle("Warning");
                        popup.setMessage("You don't have the rights to edit the automaton datas.");
                        popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        popup.show();
                    }
                    else{
                        AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                        popup.setTitle("API Modification");
                        popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("MYSQLITE","ok");
                                int type;
                                if(inputType.getText().toString().equals("Conditionnement de comprimés")) type = 0;
                                else type = 1;

                                Api a = new Api(inputName.getText().toString(), inputIp.getText().toString(), Integer.parseInt(inputRack.getText().toString()), Integer.parseInt(inputSlot.getText().toString()), type, inputDatabloc.getText().toString());
                                a.setId(tv.getId());
                                dbm.updateApi(a.getId(), a);
                                Intent listActivity = new Intent(getApplicationContext(), ListApisActivity.class);
                                startActivity(listActivity);
                                finish();
                            }
                        });
                        popup.setView(ll_popup);
                        popup.show();
                    }
                }
            });
            ll_apislist.addView(tv);
        }
        dbm.close();


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
