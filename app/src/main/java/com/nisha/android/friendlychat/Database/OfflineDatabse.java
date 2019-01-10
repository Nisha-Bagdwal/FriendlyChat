package com.nisha.android.friendlychat.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisha.android.friendlychat.Objects.FriendlyContact;

import java.util.ArrayList;

public class OfflineDatabse {

    public static final String KEY_CONTACT="contact";
    public static final String KEY_NAME="name";
    public static final String KEY_MESSAGE="name";
    public static final String KEY_TIME="time";

    private static final String DATABASE_NAME="OfflineDB";
    private static final int DATABASE_VERSION=1;

    private static String MESSAGE_TABLE_NAME = "Messages";
    private static String USER_TABLE_NAME = "Users";

    private DbHelper ourHelper;
    private Context ourContext;
    private SQLiteDatabase ourDatabase;

    public OfflineDatabse(Context c){
        ourContext=c;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+USER_TABLE_NAME +" ( "+KEY_CONTACT+" TEXT , "+
                    KEY_NAME+" TEXT );"
            );

            db.execSQL("CREATE TABLE "+MESSAGE_TABLE_NAME +" ( "+KEY_TIME+" TEXT , "+
                    KEY_MESSAGE+" TEXT );"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TABLE_NAME);
            onCreate(db);
        }
    }

    public OfflineDatabse open(String tableName) throws SQLException {
        ourHelper=new DbHelper(ourContext);
        ourDatabase=ourHelper.getWritableDatabase();
        MESSAGE_TABLE_NAME=tableName;
        return this;
    }

    public long createEntryInUserTable(String contact, String name) {
        ContentValues cv=new ContentValues();
        cv.put(KEY_CONTACT,contact);
        cv.put(KEY_NAME,name);
        return ourDatabase.insert(USER_TABLE_NAME,null,cv);
    }

    public ArrayList<FriendlyContact> getUserData(){
        String table=USER_TABLE_NAME;
        String[] columns=new String[]{KEY_CONTACT,KEY_NAME};
        Cursor c=ourDatabase.query(true,table,columns,null,null,null,null,null,null);

        int icontact=c.getColumnIndex(KEY_CONTACT);
        int iname=c.getColumnIndex(KEY_NAME);

        ArrayList<FriendlyContact> totalContactsinDB = new ArrayList<FriendlyContact>();
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            FriendlyContact aContact=new FriendlyContact(c.getString(iname),c.getString(icontact));
            totalContactsinDB.add(aContact);
        }
        return totalContactsinDB;
    }

    public void close(){
        ourHelper.close();
    }

}
