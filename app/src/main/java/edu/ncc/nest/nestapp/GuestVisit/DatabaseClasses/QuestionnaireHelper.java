package edu.ncc.nest.nestapp.GuestVisit.DatabaseClasses;

/**
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * QuestionnaireHelper: The helper file of the three database files. This file creates the table
 * itself for the database to have items stored in.
 */
public class QuestionnaireHelper extends SQLiteOpenHelper {

    public static final String TAG = QuestionnaireHelper.class.getSimpleName();

    // Database name and version
    public static final String DATABASE_NAME = "QuestionnaireSubmissions.db";
    public static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_NAME = "questionnaire_submissions";

    // Columns in the table
    public static final String ROW_ID = "row_id";
    public static final String GUEST_ID = "guest_id"; //Reference to the id num of the guest
    public static final String ADULT_COUNT = "adult_count"; //Reference to the number of non senior adults in the household
    public static final String SENIOR_COUNT = "senior_count"; //Reference to the number of seniors in the household
    public static final String CHILD_COUNT = "child_count"; //Reference to the number of children in the household
    public static final String FIRST_VISIT = "first_visit"; //Reference to whether or not it is the user's first visit to the NEST
    public static final String DATE = "date"; //Date
    public static final String VISIT_COUTNER = "visit_counter"; //Reference to how many times the user has visited the NEST post-registration

    public QuestionnaireHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    ////////////// Lifecycle Methods Start //////////////

    /**
     * onCreate method
     * Method starts on the creation of the class, creating the SQLiteDatabase.
     * @param db the SQLiteDatabase
     */
    @Override
    //@SuppressLint("DefaultLocale")
    public void onCreate(SQLiteDatabase db) { // Creates the database table\
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GUEST_ID + " TEXT, " +
                ADULT_COUNT + " TEXT, " +
                SENIOR_COUNT + " TEXT, " +
                CHILD_COUNT + " TEXT, " +
                FIRST_VISIT + " TEXT, " +
                DATE + " TEXT, " +
                VISIT_COUTNER + " TEXT);");
    }

    /**
     * onUpgrade method
     * When a new version of the app is created, drop the old table and create a new one
     * @param db SQLiteDatabase
     * @param oldVersion reference to the old app version
     * @param newVersion reference to the new app version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // If we are upgrading to a new version drop the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Re-create the table
        onCreate(db);

    }


    ////////////// Custom Methods Start //////////////

    /**
     * validateColumnCount method
     * Confirms that the number of columns corresponds to the number of questions, if not, create
     * a new table with the correct number of columns.
     */
    private void validateColumnCount() {

        //Create a new database
        SQLiteDatabase db = this.getReadableDatabase();

        if (getColumnCount(db) != (8)) {

            // Print a warning, stating why we're dropping the table
            Log.w(TAG, "Dropping table due to question count change.");

            // Drop the table since the column count needs to change
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Recreate the table
            onCreate(db);

        }

        // Done reading from database so close the reference
        db.close();

    }

    /**
     * getColumnCount method
     * Gets and returns the number of columns in the database.
     * @param db SQLiteDatabase
     * @return columnCount integer
     */
    private int getColumnCount(SQLiteDatabase db) {

        // Create a query to get table info
        Cursor c = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);

        //Create a primitive and set it equal to the getColumnCount of c
        int columnCount = c.getCount();

        // Done reading from cursor so make sure we close it
        c.close();

        //Return columnCount integer
        return columnCount;

    }

}