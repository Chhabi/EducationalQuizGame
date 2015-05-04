package tu_sofia.ivanmishev.bg.educationalgamequiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Admin on 18.4.2015 г..
 */
public class MyData extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "questionDB";
    private static final int DATABASE_VERSION = 1;

    public MyData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

    public Cursor getQuestions() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Question", "cAnswer", "wAnswer1", "wAnswer2", "wAnswer3", "difficulty"};
        String sqlTables = "Questions";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getGameQuestions() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Question", "cAnswer", "wAnswer1", "wAnswer2", "wAnswer3", "difficulty"};
        String sqlTables = "Questions";
        String limit = "50";
        String orderBy = "RANDOM()";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, orderBy, limit);

        c.moveToFirst();
        return c;
    }

}