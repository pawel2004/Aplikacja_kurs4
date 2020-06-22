package com.example.grybos.aplikacjakurs4.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper{

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE tabela1 (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'Title' TEXT, 'Ticolor' TEXT,'Text' TEXT, 'Tecolor' TEXT, 'File' TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tabela1");
        onCreate(sqLiteDatabase);

    }

    public boolean insert(String Title, String Text, String Ticolor, String Tecolor, String File){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Title", Title);
        contentValues.put("Ticolor", Ticolor);
        contentValues.put("Text", Text);
        contentValues.put("Tecolor", Tecolor);
        contentValues.put("File", File);

        db.insertOrThrow("tabela1", null, contentValues); // gdy insert się nie powiedzie, będzie błąd
        db.close();
        return true;
    }

    public ArrayList<Note> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes= new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM tabela1" , null);
        while(result.moveToNext()){
            notes.add( new Note(

                    result.getString(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("Title")),
                    result.getString(result.getColumnIndex("Ticolor")),
                    result.getString(result.getColumnIndex("Text")),
                    result.getString(result.getColumnIndex("Tecolor")),
                    result.getString(result.getColumnIndex("File"))

            ));

        }
        return notes;
    }

    public int delete (String id){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("tabela1",
                "_id = ? ",
                new String[]{id}); // chodzi o id w tej linii

    }

    public void edit (String id, String Title, String Text, String Ticolor, String Tecolor){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Title", Title);
        contentValues.put("Ticolor", Ticolor);
        contentValues.put("Text", Text);
        contentValues.put("Tecolor", Tecolor);

        db.update("tabela1",
                contentValues,
                "_id = ? ",
                new String[]{id}); // chodzi o id w tej linii
        db.close();

    }

}
