package be.sam.application.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "User.db";
    private static final String TABLE_NAME_USER = "User";
    private static final String TABLE_NAME_API = "Api";
    private static final int DATABASE_VERSION = 1;

    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;

    //Noms des colonnes de la table User
    private static final String COL_FIRSTNAME = "firstName";
    private static final int NUM_COL_FIRSTNAME = 1;
    private static final String COL_LASTNAME = "lastName";
    private static final int NUM_COL_LASTNAME = 2;
    private static final String COL_EMAIL = "email";
    private static final int NUM_COL_EMAIL = 3;
    private static final String COL_PASSWORD = "password";
    private static final int NUM_COL_PASSWORD = 4;
    private static final String COL_RIGHTS = "rights";
    private static final int NUM_COL_RIGHTS = 5;

    //Noms des colonnes de la table Api
    private static final String COL_NAME = "name";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_IP = "ip";
    private static final int NUM_COL_IP = 2;
    private static final String COL_RACK= "rack";
    private static final int NUM_COL_RACK = 3;
    private static final String COL_SLOT = "slot";
    private static final int NUM_COL_SLOT = 4;
    private static final String COL_TYPE= "type";
    private static final int NUM_COL_TYPE = 5;
    private static final String COL_DATABLOC = "databloc";
    private static final int NUM_COL_DATABLOC = 6;

    private SQLiteDatabase db;



    public DbManager(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_user = "create table "+TABLE_NAME_USER+"("
                + COL_ID+" integer primary key autoincrement, "
                + COL_FIRSTNAME+" text not null, "
                +COL_LASTNAME+" text not null, "
                +COL_EMAIL+" text not null, "
                +COL_PASSWORD+" text not null, "
                +COL_RIGHTS+" integer not null)";

        String create_table_api = "create table "+TABLE_NAME_API+"("
                +COL_ID+" integer primary key autoincrement,"
                + COL_NAME+" text not null, "
                +COL_IP +" text not null, "
                +COL_RACK +" integer not null, "
                +COL_SLOT+" integer not null, "
                +COL_TYPE +" integer not null, "
                +COL_DATABLOC+" text not null)";

        String insert_tablets_api = "insert into "+TABLE_NAME_API+"("+COL_NAME+", "+COL_IP+", "+COL_RACK+", "+COL_SLOT+", "+COL_TYPE+", "+COL_DATABLOC+") values ('Comprimés', '192.168.10.136', 0, 2, 0, 25)";
        String insert_liquids_api = "insert into "+TABLE_NAME_API+"("+COL_NAME+", "+COL_IP+", "+COL_RACK+", "+COL_SLOT+", "+COL_TYPE+", "+COL_DATABLOC+") values ('Liquides', '192.168.10.136', 0, 2, 1, 25)";


        db.execSQL(create_table_user);
        db.execSQL(create_table_api);

        Log.i("MYSQLITE", "Database created.");

        db.execSQL(insert_tablets_api);
        db.execSQL(insert_liquids_api);

        Log.i("MYSQLITE", "APIs inserted in database.");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_table_user = "drop table "+TABLE_NAME_USER;
        String drop_table_api = "drop table "+TABLE_NAME_API;
        db.execSQL(drop_table_user);
        db.execSQL(drop_table_api);
        this.onCreate(db);
        Log.i("MYSQLITE", "Database upgraded.");
    }

    public void insertUser(User u) {
        String insert = "insert into "+TABLE_NAME_USER+" ("+COL_FIRSTNAME+", "+COL_LASTNAME+", "+COL_EMAIL+", "+COL_PASSWORD+","+COL_RIGHTS+") values ("+u.getFirstName()+", "+u.getLastName()+", "+u.getEmail()+", "+u.getPassword()+", "+u.getRights()+")";
        Log.i("MYSQLITE", insert);
        this.getWritableDatabase().execSQL(insert);
        Log.i("MYSQLITE","User "+u.getFirstName().replace("\"","")+" "+u.getLastName().replace("\"","")+" inserted.");

    }


    public List<User> getUsers() {

        String select = "select * from "+TABLE_NAME_USER+ " order by "+COL_ID;

        ArrayList<User> listUsers = new ArrayList<User>();

        Cursor c = this.getReadableDatabase().rawQuery(select, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            User u = new User(c.getString(NUM_COL_FIRSTNAME), c.getString(NUM_COL_LASTNAME), c.getString(NUM_COL_EMAIL), c.getString(NUM_COL_PASSWORD), c.getInt(NUM_COL_RIGHTS));
            u.setId(c.getInt(NUM_COL_ID));
            listUsers.add(u);
            c.moveToNext();
        }
        c.close();

        Log.i("MYSQLITE", "Users selected.");
        return listUsers;
    }

    public User getUserByEmail(String email) {
        Cursor c = this.getReadableDatabase().query(TABLE_NAME_USER, new String[] {COL_ID, COL_FIRSTNAME, COL_LASTNAME, COL_EMAIL, COL_PASSWORD, COL_RIGHTS},
                COL_EMAIL + " LIKE \""+ email + "\"", null, null, null, COL_EMAIL);
        return cursorToUser(c);
    }

    public Api getApiByName(String name){
        Cursor c = this.getReadableDatabase().query(TABLE_NAME_API, new String[] {COL_ID, COL_NAME, COL_IP, COL_RACK, COL_SLOT, COL_TYPE, COL_DATABLOC},
                COL_NAME + " LIKE \""+ name +"\"", null, null, null, COL_NAME);
        return cursorToApi(c);
    }

    public List<Api> getApis() {
        String select = "select * from "+TABLE_NAME_API+" order by "+COL_ID;

        ArrayList<Api> listApis = new ArrayList<Api>();

        Cursor c = this.getReadableDatabase().rawQuery(select, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            Api a = new Api(c.getString(NUM_COL_NAME), c.getString(NUM_COL_IP), c.getInt(NUM_COL_RACK), c.getInt(NUM_COL_SLOT), c.getInt(NUM_COL_TYPE), c.getString(NUM_COL_DATABLOC));
            a.setId(c.getInt(NUM_COL_ID));
            listApis.add(a);
            c.moveToNext();
        }
        Log.i("MYSQLITE","APIs selected.");
        return listApis;
    }


    public User cursorToUser(Cursor c) {
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();

        User u = new User();
        u.setId(c.getInt(NUM_COL_ID));
        u.setFirstName(c.getString(NUM_COL_FIRSTNAME));
        u.setLastName(c.getString(NUM_COL_LASTNAME));
        u.setEmail(c.getString(NUM_COL_EMAIL));
        u.setPassword(c.getString(NUM_COL_PASSWORD));
        u.setRights(c.getInt(NUM_COL_RIGHTS));
        c.close();

        Log.i("MYSQLITE","User "+u.getFirstName()+" "+u.getLastName()+" selected.");
        return u;

    }

    public Api cursorToApi(Cursor c){
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        c.moveToFirst();

        Api a = new Api();
        a.setId(c.getInt(NUM_COL_ID));
        a.setName(c.getString(NUM_COL_NAME));
        a.setIp(c.getString(NUM_COL_IP));
        a.setRack(c.getInt(NUM_COL_RACK));
        a.setSlot(c.getInt(NUM_COL_SLOT));
        a.setType(c.getInt(NUM_COL_TYPE));
        a.setDatabloc(c.getString(NUM_COL_DATABLOC));

        c.close();

        Log.i("MYSQLITE", "Api "+a.getName()+" selected.");
        return a;
    }

    public void updateUser(int id, User u){
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_FIRSTNAME, u.getFirstName());
        content.put(COL_LASTNAME, u.getLastName());
        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_PASSWORD, u.getPassword());
        content.put(COL_RIGHTS, u.getRights());
        Log.i("MYSQLITE", "User "+u.getFirstName()+" "+u.getLastName()+" modified.");
        db.update(TABLE_NAME_USER, content, COL_ID + " = "+id, null);
        db.close();
    }

    public void updateApi(int id, Api a){
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_NAME, a.getName());
        content.put(COL_IP, a.getIp());
        content.put(COL_RACK, a.getRack());
        content.put(COL_SLOT, a.getSlot());
        content.put(COL_TYPE, a.getType());
        content.put(COL_DATABLOC, a.getDatabloc());
        Log.i("MYSQLITE", "API "+a.getName()+" modified.");
        db.update(TABLE_NAME_API, content, COL_ID + " = "+id, null);
        db.close();

    }

    public void emptyTable() {
        db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, null,null);
        db.execSQL("delete from "+TABLE_NAME_USER+" where "+COL_RIGHTS+" <> 2");
        db.close();
        Log.i("MYSQLITE", "Table empty.");
    }

    public void deleteUser(int id, User u){
        db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME_USER+" where "+COL_ID+ " = "+id);
        db.close();
        Log.i("MYSQLITE", "User "+u.getFirstName()+" "+u.getLastName()+"deleted.");

    }


}
