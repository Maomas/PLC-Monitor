package be.sam.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.sam.application.R;

public class TabletsManagementActivity extends Activity {

    private Button btnSave;
    private Button btnBack;
    private EditText evBottles;
    private EditText evPills;
    private int pills;
    private int bottles;
    private TabletsManagementActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablets_management);

        activity = this;

        btnSave = (Button) findViewById(R.id.btn_tabletsManagement_save);
        btnBack = (Button) findViewById(R.id.btn_tabletsManagement_back);
        evBottles = (EditText) findViewById(R.id.ev_tabletsManagement_bottles);
        evPills = (EditText) findViewById(R.id.ev_tabletsManagement_pills);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNumeric(evBottles.getText().toString())){
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("Please choose a correct bottles number.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }
                else if(Integer.parseInt(evBottles.getText().toString()) > 999 || Integer.parseInt(evBottles.getText().toString()) < 0){
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("The bottles number is too high.  Choose between 0 and 999.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }

                else if(!isNumeric(evPills.getText().toString()) ){
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("Please choose a correct pills number.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }

                else if(Integer.parseInt(evPills.getText().toString()) > 15 || Integer.parseInt(evPills.getText().toString()) < 0){
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("Warning");
                    popup.setMessage("The pills number is too high.  Choose between 0 and 15.");
                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popup.show();
                }


                else{
                    bottles = Integer.parseInt(evBottles.getText().toString());
                    pills = Integer.parseInt(evPills.getText().toString());
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabletsActivity = new Intent(getApplicationContext(), TabletsActivity.class);
                startActivity(tabletsActivity);
                finish();
            }
        });
    }

    private static boolean isNumeric(String str){
        try{
            int i = Integer.parseInt(str);
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }


}
