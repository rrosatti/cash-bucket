package com.example.rodri.cashbucket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.rodri.cashbucket.model.AutoDeposit;
import com.example.rodri.cashbucket.model.BankAccount;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.User;
import com.example.rodri.cashbucket.model.Wallet;

/**
 * Created by rodri on 12/14/2016.
 */

public class MyDataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper helper;
    private String[] userColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_USERNAME,
            MySQLiteHelper.COLUMN_PASSWORD
    };
    private String[] bankAccountColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BALANCE,
    };
    private String[] userBankAccountColumns = {
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID
    };
    private String[] autoDepositColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID,
            MySQLiteHelper.COLUMN_VALUE,
            MySQLiteHelper.COLUMN_DAY
    };
    private String[] walletColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BALANCE
    };
    private String[] userWalletColumns = {
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_WALLET_ID
    };
    private String[] cashMovementColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_PRICE,
            MySQLiteHelper.COLUMN_TYPE,
            MySQLiteHelper.COLUMN_DAY,
            MySQLiteHelper.COLUMN_MONTH,
            MySQLiteHelper.COLUMN_YEAR,
            MySQLiteHelper.COLUMN_USER_ID
    };

    public MyDataSource(Context context) {
        helper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    /** ------------ CREATE ---------------- */

    public long createUser(String name, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, password);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER, userColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        return insertId;
    }

    public long createBankAccount(double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        long insertId = db.insert(MySQLiteHelper.TABLE_BANK_ACCOUNT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_BANK_ACCOUNT, bankAccountColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        return insertId;
    }

    public long createAutoDeposit(long bankAccountId, double value, int day) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID, bankAccountId);
        values.put(MySQLiteHelper.COLUMN_VALUE, value);
        values.put(MySQLiteHelper.COLUMN_DAY, day);

        long insertId = db.insert(MySQLiteHelper.TABLE_AUTO_DEPOSIT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT, autoDepositColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        return insertId;
    }

    public long createWallet(double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        long insertId = db.insert(MySQLiteHelper.TABLE_WALLET, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_WALLET, walletColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        return insertId;

    }

    public long createCashMovement(double price, int type, int day, int month, int year, long userId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRICE, price);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_MONTH, month);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);

        long insertId = db.insert(MySQLiteHelper.TABLE_CASH_MOVEMENT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_CASH_MOVEMENT, cashMovementColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        return insertId;
    }

    public boolean createUserBankAccount(long userId, long bankAccountId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
        values.put(MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID, bankAccountId);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER_BANK_ACCOUNT, null, values);

        if (insertId != 0) return true;
        else return false;

    }

    public boolean createUserWallet(long userId, long walletId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
        values.put(MySQLiteHelper.COLUMN_WALLET_ID, walletId);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER_WALLET, null, values);

        if (insertId != 0) return true;
        else return false;

    }


    /** ------------ CURSOR TO ---------------- */

    public User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setUsername(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        return user;
    }

    public BankAccount cursorToBankAccount(Cursor cursor) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(cursor.getLong(0));
        bankAccount.setBalance(cursor.getDouble(1));
        return bankAccount;
    }

    public AutoDeposit cursorToAutoDeposit(Cursor cursor) {
        AutoDeposit autoDeposit = new AutoDeposit();
        autoDeposit.setId(cursor.getLong(0));
        autoDeposit.setBankAccountId(cursor.getLong(1));
        autoDeposit.setValue(cursor.getDouble(2));
        autoDeposit.setDay(cursor.getInt(3));
        return autoDeposit;
    }

    public Wallet cursorToWallet(Cursor cursor) {
        Wallet wallet = new Wallet();
        wallet.setId(cursor.getLong(0));
        wallet.setBalance(cursor.getDouble(1));
        return wallet;
    }

    public CashMovement cursorToCashMovement(Cursor cursor) {
        CashMovement cashMovement = new CashMovement();
        cashMovement.setId(cursor.getLong(0));
        cashMovement.setPrice(cursor.getDouble(1));
        cashMovement.setType(cursor.getInt(2));
        cashMovement.setDay(cursor.getInt(3));
        cashMovement.setMonth(cursor.getInt(4));
        cashMovement.setYear(cursor.getInt(5));
        cashMovement.setUserId(cursor.getLong(6));
        return cashMovement;
    }

    /** ------------ GET DATA ---------------- */

    public User getUser(String username, String password) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER, userColumns,
                MySQLiteHelper.COLUMN_USERNAME + " = '" + username + "' AND " +
                MySQLiteHelper.COLUMN_PASSWORD + " = '" + password + "'", null, null, null, null, null);
        Cursor cursor2 = db.query(MySQLiteHelper.TABLE_USER, userColumns,
                null, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        User user = cursorToUser(cursor);
        return user;
    }

    /** ------------ EXTRA ---------------- */

    public boolean isCursorEmpty(Cursor cursor) {
        if (cursor.moveToFirst()) {
            System.out.println("The cursor is empty!!");
            return false;
        }else {
            return true;
        }
    }
}
