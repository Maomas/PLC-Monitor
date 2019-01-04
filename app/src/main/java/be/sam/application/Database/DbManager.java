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
    private static final String TABLE_NAME = "User";
    private static final int DATABASE_VERSION = 1;

    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;

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

    private SQLiteDatabase db;



    public DbManager(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table = "create table "+TABLE_NAME+"("
                + COL_ID+" integer primary key autoincrement, "
                + COL_FIRSTNAME+" text not null, "
                +COL_LASTNAME+" text not null, "
                +COL_EMAIL+" text not null, "
                +COL_PASSWORD+" text not null, "
                +COL_RIGHTS+" integer not null)";
        db.execSQL(create_table);
        Log.i("MYSQLITE", "Database created.");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_table = "drop table "+TABLE_NAME;
        db.execSQL(drop_table);
        this.onCreate(db);
        Log.i("MYSQLITE", "Database upgraded.");
    }

    public void insertUser(User u) {
        String insert = "insert into "+TABLE_NAME+" ("+COL_FIRSTNAME+", "+COL_LASTNAME+", "+COL_EMAIL+", "+COL_PASSWORD+","+COL_RIGHTS+") values ("+u.getFirstName()+", "+u.getLastName()+", "+u.getEmail()+", "+u.getPassword()+", "+u.getRights()+")";
        Log.i("MYSQLITE", insert);
        this.getWritableDatabase().execSQL(insert);
        Log.i("MYSQLITE","User "+u.getFirstName().replace("\"","")+" "+u.getLastName().replace("\"","")+" inserted.");

    }

    public List<User> getUsers() {

        String select = "select * from "+TABLE_NAME+ " order by "+COL_ID;

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
        Cursor c = this.getReadableDatabase().query(TABLE_NAME, new String[] {COL_ID, COL_FIRSTNAME, COL_LASTNAME, COL_EMAIL, COL_PASSWORD, COL_RIGHTS},
                COL_EMAIL + " LIKE \""+ email + "\"", null, null, null, COL_EMAIL);
        return cursorToUser(c);
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

    public void updateUser(int id, User u){
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_FIRSTNAME, u.getFirstName());
        content.put(COL_LASTNAME, u.getLastName());
        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_PASSWORD, u.getPassword());
        content.put(COL_RIGHTS, u.getRights());
        Log.i("MYSQLITE", "User "+u.getFirstName()+" "+u.getLastName()+" modified.");
        db.update(TABLE_NAME, content, COL_ID + " = "+id, null);
        db.close();
    }

    public void emptyTable() {
        db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, null,null);
        db.execSQL("delete from "+TABLE_NAME+" where "+COL_RIGHTS+" <> 2");
        db.close();
        Log.i("MYSQLITE", "Table empty.");
    }

    public void deleteUser(int id, User u){
        db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" where "+COL_ID+ " = "+id);
        db.close();
        Log.i("MYSQLITE", "User "+u.getFirstName()+" "+u.getLastName()+"deleted.");

    }


}
