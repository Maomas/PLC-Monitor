package be.sam.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.sam.application.Database.DbManager;
import be.sam.application.R;
import be.sam.application.Database.User;

public class ListUsersActivity extends Activity {

    private DbManager dbm;
    private List<User> users;
    private Button btnEmptyTable;
    private Button btnBack;
    private LinearLayout ll;
    private ListUsersActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        dbm = new DbManager(this);
        activity = this;


        btnEmptyTable = (Button) findViewById(R.id.btn_userslist_emptytable);
        btnBack = (Button) findViewById(R.id.btn_userslist_back);
        ll = (LinearLayout) findViewById(R.id.ll_userslist);

        users = dbm.getUsers();

        for(User u : users){
            final TextView tv = new TextView(this);

            final EditText inputFirstname = new EditText(this);
            final EditText inputLastname = new EditText(this);
            final EditText inputEmail = new EditText(this);
            final EditText inputPassword = new EditText(this);
            final EditText inputRights = new EditText(this);

            final LinearLayout ll_popup = new LinearLayout(this);

            String rights;
            if(u.getRights() == 0) rights = "R";
            else if(u.getRights() == 1) rights = "RW";
            else rights = "SU";

            inputFirstname.setText(u.getFirstName());
            inputLastname.setText(u.getLastName());
            inputEmail.setText(u.getEmail());
            inputPassword.setText(u.getPassword());
            inputRights.setText(rights);

            ll_popup.addView(inputFirstname);
            ll_popup.addView(inputLastname);
            ll_popup.addView(inputEmail);
            ll_popup.addView(inputPassword);
            ll_popup.addView(inputRights);

            ll_popup.setVerticalGravity(1);
            ll_popup.setOrientation(LinearLayout.VERTICAL);

            tv.setBackgroundResource(R.color.white);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,0,0,20);
            tv.setLayoutParams(lp);
            tv.setText(u.toString());
            tv.setId(u.getId());

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder popup = new AlertDialog.Builder(activity);
                    popup.setTitle("User Modification");

                    popup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int rights;
                            if(inputRights.getText().toString().equals("SU")) rights = 2;
                            else if(inputRights.getText().toString().equals("RW")) rights = 1;
                            else rights = 0;
                            User u = new User(inputFirstname.getText().toString(), inputLastname.getText().toString(), inputEmail.getText().toString(), inputPassword.getText().toString(), rights);
                            u.setId(tv.getId());
                            dbm.updateUser(u.getId(), u);
                            Intent listActivity = new Intent(getApplicationContext(), ListUsersActivity.class);
                            startActivity(listActivity);
                            finish();
                        }
                    });
                    popup.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int rights;
                            if(inputRights.getText().toString().equals("SU")) rights = 2;
                            else if(inputRights.getText().toString().equals("RW")) rights = 1;
                            else rights = 0;
                            User u = new User(inputFirstname.getText().toString(), inputLastname.getText().toString(), inputEmail.getText().toString(), inputPassword.getText().toString(), rights);
                            u.setId(tv.getId());
                            if(u.getRights() == 2) Toast.makeText(getApplicationContext(), "The super user cannot be deleted.", Toast.LENGTH_LONG).show();
                            else{
                                dbm.deleteUser(u.getId(), u);
                                Intent listActivity = new Intent(getApplicationContext(), ListUsersActivity.class);
                                startActivity(listActivity);
                                finish();
                            }

                        }
                    });

                    //ERREUR LIGNE 95 Lors du 2e clic sur un tv d'un user
                    popup.setView(ll_popup);
                    popup.show();



                }
            });
            ll.addView(tv);
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

        btnEmptyTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbm.emptyTable();
                Intent listActivity = new Intent(getApplicationContext(), ListUsersActivity.class);
                startActivity(listActivity);
                finish();
            }
        });
    }
}
