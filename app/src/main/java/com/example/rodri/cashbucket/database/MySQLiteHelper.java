package com.example.rodri.cashbucket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rodri on 12/13/2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "cash_bucket.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USER = "user";
    public static final String TABLE_BANK_ACCOUNT = "bank_account";
    public static final String TABLE_USER_BANK_ACCOUNT = "user_bank_account";
    public static final String TABLE_AUTO_DEPOSIT = "auto_deposit";
    public static final String TABLE_WALLET = "wallet";
    public static final String TABLE_USER_WALLET = "user_wallet";
    public static final String TABLE_TRANSACTION = "transaction";

    // Common column name
    public static final String KEY_ID = "id";

    // User columns name
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Bank Account columns name
    public static final String COLUMN_BALANCE = "balance";

    // User Bank Account columns name
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_BANK_ACCOUNT_ID = "bank_account_id";

    // Auto Deposit columns name { id, bank_account_id }
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_DAY = "day";

    // Wallet columns name {id, balance }

    // User Wallet columns name { user_id }
    public static final String COLUMN_WALLET_ID = "wallet_id";

    // Transaction columns name { id, user_id }
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type";


    /**  ----------- CREATING TABLES ----------- */

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " ("
            + KEY_ID + " INTEGER AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_USERNAME + " TEXT NOT NULL, "
            + COLUMN_PASSWORD + " TEXT NOT NULL, "
            + " PRIMARY KEY (" + KEY_ID + "));";

    private static final String CREATE_TABLE_BANK_ACCOUNT =
            "CREATE TABLE " + TABLE_BANK_ACCOUNT + " ("
            + KEY_ID + " INTEGER AUTOINCREMENT, "
            + COLUMN_BALANCE + " FLOAT NOT NULL, "
            + "PRIMARY KEY (" + KEY_ID + "));";

    private static final String CREATE_TABLE_USER_BANK_ACCOUNT =
            "CREATE TABLE " + TABLE_USER_BANK_ACCOUNT + " ("
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_BANK_ACCOUNT_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_BANK_ACCOUNT_ID + "), "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + " (" + KEY_ID + "), "
            + "FOREIGN KEY (" + COLUMN_BANK_ACCOUNT_ID + ") REFERENCES " + TABLE_BANK_ACCOUNT + " (" + KEY_ID + "));";

    private static final String CREATE_TABLE_AUTO_DEPOSIT =
            "CREATE TABLE " + TABLE_AUTO_DEPOSIT + " ("
            + KEY_ID + " INTEGER AUTOINCREMENT, "
            + COLUMN_BANK_ACCOUNT_ID + " INTEGER NOT NULL, "
            + COLUMN_VALUE + " FLOAT NOT NULL, "
            + COLUMN_DAY + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + KEY_ID + "), "
            + "FOREIGN KEY (" + COLUMN_BANK_ACCOUNT_ID + ") REFERENCES " + TABLE_BANK_ACCOUNT + " (" + KEY_ID + "));";

    private static final String CREATE_TABLE_WALLET =
            "CREATE TABLE " + TABLE_WALLET + " ("
            + KEY_ID + " INTEGER AUTOINCREMENT, "
            + COLUMN_BALANCE + " FLOAT NOT NULL, "
            + "PRIMARY KEY (" + KEY_ID + "));";

    private static final String CREATE_TABLE_USER_WALLET =
            "CREATE TABLE " + TABLE_USER_WALLET + " ("
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_WALLET_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_WALLET_ID + "), "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + " (" + KEY_ID + "), "
            + "FOREIGN KEY (" + COLUMN_WALLET_ID + ") REFERENCES " + TABLE_WALLET + " (" + KEY_ID + "));";

    private static final String CREATE_TABLE_TRANSACTION =
            "CREATE TABLE " + TABLE_TRANSACTION + " ("
            + KEY_ID + " INTEGER AUTOINCREMENT, "
            + COLUMN_PRICE + " FLOAT NOT NULL, "
            + COLUMN_DATE + " INTEGER NOT NULL, "
            + COLUMN_TYPE + " SMALLINT NOT NULL, "
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + KEY_ID + "), "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + " (" + KEY_ID + "));";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BANK_ACCOUNT);
        db.execSQL(CREATE_TABLE_USER_BANK_ACCOUNT);
        db.execSQL(CREATE_TABLE_AUTO_DEPOSIT);
        db.execSQL(CREATE_TABLE_WALLET);
        db.execSQL(CREATE_TABLE_USER_WALLET);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(), "Upgrading from version " + oldVersion + " to " + newVersion
        + ". The old data will be deleted.");

        if (newVersion > oldVersion) {
            // do something
        }
    }
}
