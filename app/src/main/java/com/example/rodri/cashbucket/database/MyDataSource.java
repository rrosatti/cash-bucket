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

import java.util.ArrayList;
import java.util.List;

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
            MySQLiteHelper.COLUMN_DAY,
            MySQLiteHelper.COLUMN_ACTIVE
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
            MySQLiteHelper.COLUMN_DESC,
            MySQLiteHelper.COLUMN_USER_ID
    };

    private String[] autoLoginColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_ACTIVE
    };

    private String[] autoDepositLogColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_AUTO_DEPOSIT_ID,
            MySQLiteHelper.COLUMN_VALUE,
            MySQLiteHelper.COLUMN_DATE_IN_MILLIS
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
        values.put(MySQLiteHelper.COLUMN_ACTIVE, 1);

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

    public long createCashMovement(double price, int type, int day, int month, int year, String desc, long userId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRICE, price);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_MONTH, month);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        values.put(MySQLiteHelper.COLUMN_DESC, desc);
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

    public boolean createAutoLogin(long userId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
        values.put(MySQLiteHelper.COLUMN_ACTIVE, 0);

        long insertedId = db.insert(MySQLiteHelper.TABLE_AUTO_LOGIN, null, values);

        if (insertedId != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createAutoDepositLog(long autoDepositId, double value, long dateInMillis) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AUTO_DEPOSIT_ID, autoDepositId);
        values.put(MySQLiteHelper.COLUMN_VALUE, value);
        values.put(MySQLiteHelper.COLUMN_DATE_IN_MILLIS, dateInMillis);

        long insertedId = db.insert(MySQLiteHelper.TABLE_AUTO_DEPOSIT_LOG, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT_LOG, autoDepositLogColumns,
                MySQLiteHelper.KEY_ID + " = " + insertedId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return false;
        }

        return true;
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
        if (cursor.getInt(4) == 1) {
            autoDeposit.setActive(true);
        } else {
            autoDeposit.setActive(false);
        }
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
        cashMovement.setValue(cursor.getDouble(1));
        cashMovement.setType(cursor.getInt(2));
        cashMovement.setDay(cursor.getInt(3));
        cashMovement.setMonth(cursor.getInt(4));
        cashMovement.setYear(cursor.getInt(5));
        cashMovement.setDesc(cursor.getString(6));
        cashMovement.setUserId(cursor.getLong(7));
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

    public User getUser(long userId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER, userColumns,
                MySQLiteHelper.KEY_ID + " = " + userId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        User user = cursorToUser(cursor);
        cursor.close();

        return user;
    }

    public long getBankAccountId(long userId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER_BANK_ACCOUNT, userBankAccountColumns,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId, null, null, null, null, null);
        if (isCursorEmpty(cursor)) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();

        long bankAccountId = cursor.getLong(1);
        cursor.close();

        return bankAccountId;

    }

    public long getWalletId(long userId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER_WALLET, userWalletColumns,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();

        long walletId = cursor.getLong(1);
        cursor.close();

        return walletId;
    }

    public BankAccount getBankAccount(long userId) {
        long bankAccountId = getBankAccountId(userId);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_BANK_ACCOUNT, bankAccountColumns,
                MySQLiteHelper.KEY_ID + " = " + bankAccountId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        BankAccount bankAccount = cursorToBankAccount(cursor);
        cursor.close();

        return bankAccount;
    }

    public Wallet getWallet(long userId) {
        long walletId = getWalletId(userId);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_WALLET, walletColumns,
                MySQLiteHelper.KEY_ID + " = " + walletId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        Wallet wallet = cursorToWallet(cursor);
        cursor.close();

        return wallet;

    }

    public List<CashMovement> getAllCashMovements(long userId) {
        List<CashMovement> cashMovements = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_CASH_MOVEMENT, cashMovementColumns,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            System.out.println("getAllCashMovements()");
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            cashMovements.add(cursorToCashMovement(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return cashMovements;
    }

    public AutoDeposit getAutoDeposit(long bankAccountId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT, autoDepositColumns,
                MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID + " = " + bankAccountId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        AutoDeposit autoDeposit = cursorToAutoDeposit(cursor);
        /**if (autoDeposit.isActive()) {
            cursor.close();
            return autoDeposit;
        }*/

        cursor.close();
        return autoDeposit;

    }

    public List<AutoDeposit> getAllActiveAutoDeposits() {
        List<AutoDeposit> autoDeposits = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT, autoDepositColumns,
                MySQLiteHelper.COLUMN_ACTIVE + " = " + 1, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            System.out.println("getAllActiveAutoDeposits()!");
            return null;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            autoDeposits.add(cursorToAutoDeposit(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return autoDeposits;
    }

    public long getUserId(long bankAccountId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER_BANK_ACCOUNT, userBankAccountColumns,
                MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID + " = " + bankAccountId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }

        long userId = cursor.getLong(0);
        cursor.close();

        return userId;
    }

    public long getDataOfLastDeposit(long autoDepositId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT_LOG, autoDepositLogColumns,
                MySQLiteHelper.COLUMN_AUTO_DEPOSIT_ID + " = " + autoDepositId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return 0;
        }
        // last or first??
        cursor.moveToLast();

        long dateInMillis = cursor.getLong(3);
        cursor.close();

        return dateInMillis;

    }

    public List<CashMovement> getCashMovements(long userId, int month, int year) {
        List<CashMovement> cashMovements = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_CASH_MOVEMENT, cashMovementColumns,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId + " AND " +
                        MySQLiteHelper.COLUMN_MONTH + " = " + month + " AND " +
                        MySQLiteHelper.COLUMN_YEAR + " = " + year, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            cashMovements.add(cursorToCashMovement(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return cashMovements;
    }

    /** ------------ UPDATE ---------------- */

    public boolean updateBankAccount(long bankAccountId, double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        int rowsAffected = db.update(MySQLiteHelper.TABLE_BANK_ACCOUNT, values,
                MySQLiteHelper.KEY_ID + " = " + bankAccountId, null);

        if (rowsAffected == 0) {
            System.out.println("Something went wrong!");
            return false;
        }

        return true;
    }

    public boolean updateWallet(long walletId, double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        int rowsAffected = db.update(MySQLiteHelper.TABLE_WALLET, values,
                MySQLiteHelper.KEY_ID + " = " + walletId, null);

        if (rowsAffected == 0) {
            System.out.println("Something went wrong!");
            return false;
        }

        return true;
    }
    public boolean updateAutoDeposit(AutoDeposit autoDeposit) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_VALUE, autoDeposit.getValue());
        values.put(MySQLiteHelper.COLUMN_DAY, autoDeposit.getDay());
        if (autoDeposit.isActive()) {
            values.put(MySQLiteHelper.COLUMN_ACTIVE, 1);
        } else {
            values.put(MySQLiteHelper.COLUMN_ACTIVE, 0);
        }

        int rowsAffected = db.update(MySQLiteHelper.TABLE_AUTO_DEPOSIT, values,
                MySQLiteHelper.KEY_ID + " = " + autoDeposit.getId(), null);

        if (rowsAffected == 0) {
            System.out.println("Something went wrong!");
            return false;
        }

        return true;

    }

    public boolean updateAutoLogin(long userId, int active) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACTIVE, active);

        int rowsAffected = db.update(MySQLiteHelper.TABLE_AUTO_LOGIN, values,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId, null);

        if (rowsAffected == 0) {
            System.out.println("Something went wrong!");
            return false;
        }

        return true;
    }

    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, user.getName());
        values.put(MySQLiteHelper.COLUMN_USERNAME, user.getUsername());
        values.put(MySQLiteHelper.COLUMN_PASSWORD, user.getPassword());

        int rowsAffected = db.update(MySQLiteHelper.TABLE_USER, values,
                MySQLiteHelper.KEY_ID + " = " + user.getId(), null);

        if (rowsAffected == 0) {
            System.out.println("Something went wrong");
            return false;
        }

        return true;
    }

    /** ------------ DELETE ---------------- */

    public boolean deleteCashMovement(long cashMovementId) {
        Cursor cursorCashMovement = db.query(MySQLiteHelper.TABLE_CASH_MOVEMENT, cashMovementColumns,
                MySQLiteHelper.KEY_ID + " = " + cashMovementId, null, null, null, null, null);

        if (isCursorEmpty(cursorCashMovement)) {
            cursorCashMovement.close();
            return false;
        }
        cursorCashMovement.moveToFirst();

        CashMovement cashMovement = cursorToCashMovement(cursorCashMovement);
        int type = cashMovement.getType();
        long userId = cashMovement.getUserId();

        /**
         *  1 - Withdrawal
         *  2 - Normal Expense
         *  3 - Debit
         *  4 - Deposit
         */
        boolean succeeded = false;
        switch (type) {
            case 1: {
                succeeded = withdrawal(userId, -cashMovement.getValue());
                break;
            }
            case 2: {
                succeeded = normalExpense(userId, -cashMovement.getValue());
                break;
            }
            case 3: {
                succeeded = debit(userId, -cashMovement.getValue());
                break;
            }
            case 4: {
                succeeded = deposit(userId, -cashMovement.getValue());
                break;
            }
        }

        if (succeeded) {
            int rowsAffected = db.delete(MySQLiteHelper.TABLE_CASH_MOVEMENT,
                    MySQLiteHelper.KEY_ID + " = " + cashMovementId, null);
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Something went wrong!");
            return false;
        }
    }

    /** ------------ CASH MOVEMENT ACTIONS ---------------- */

    public boolean withdrawal(long userId, double value) {
        // I need to build something like "SQL Transaction"
        BankAccount bankAccount = getBankAccount(userId);
        Wallet wallet = getWallet(userId);

        bankAccount.setBalance(bankAccount.getBalance()-value);
        wallet.setBalance(wallet.getBalance()+value);

        boolean updated1 = updateBankAccount(bankAccount.getId(), bankAccount.getBalance());
        boolean updated2 = updateWallet(wallet.getId(), wallet.getBalance());

        if (updated1 && updated2) {
            return true;
        } else {
            System.out.println("Something went wrong!");
            return false;
        }

    }

    public boolean normalExpense(long userId, double value) {
        // I need to build something like "SQL Transaction"
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance()-value);
        boolean updated = updateWallet(wallet.getId(), wallet.getBalance());

        if (updated) {
            return true;
        } else {
            System.out.println("Something went wrong!");
            return false;
        }
    }

    public boolean debit(long userId, double value) {
        // I need to build here a "SQL Transaction"
        BankAccount bankAccount = getBankAccount(userId);
        bankAccount.setBalance(bankAccount.getBalance()-value);
        boolean updated = updateBankAccount(bankAccount.getId(), bankAccount.getBalance());

        if (updated) {
            return true;
        } else {
            System.out.println("Something went wrong!");
            return false;
        }
    }

    public boolean deposit(long userId, double value) {
        // I need to build here a "SQL Transaction"
        BankAccount bankAccount = getBankAccount(userId);
        bankAccount.setBalance(bankAccount.getBalance()+value);
        boolean updated = updateBankAccount(bankAccount.getId(), bankAccount.getBalance());

        if (updated) {
            return true;
        } else {
            System.out.println("Something went wrong!");
            return false;
        }
    }

    /** ------------ EXTRA ---------------- */

    public boolean isCursorEmpty(Cursor cursor) {
        if (cursor.moveToFirst()) {
            return false;
        }else {
            System.out.println("The cursor is empty!!");
            return true;
        }
    }

    public boolean isAutoLoginActive(long userId) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_LOGIN, autoLoginColumns,
                MySQLiteHelper.COLUMN_USER_ID + " = " + userId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return false;
        }
        int active = cursor.getInt(2);

        if (active == 0) {
            return false;
        } else {
            return true;
        }
    }


}
