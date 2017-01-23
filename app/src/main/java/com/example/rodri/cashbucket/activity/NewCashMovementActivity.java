package com.example.rodri.cashbucket.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.Login;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by rodri on 1/4/2017.
 */

public class NewCashMovementActivity extends AppCompatActivity {

    private EditText etValue;
    private TextView txtDate;
    private Button btCalendar;
    private EditText etDesc;
    private Button btConfirm;
    private Button btCancel;
    private Spinner spinnerTypes;
    private MyDataSource dataSource;
    private long userId = 0;
    private int day = 0, month = 0, year = 0;
    private String date;
    private List<String> spinValues;
    private ArrayAdapter<String> adapterTypes;
    private int selectedType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cash_movement);

        iniViews();
        userId = Login.getInstance().getUserId();
        dataSource = new MyDataSource(NewCashMovementActivity.this);

        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear + 1;
                        year = _year;

                        String sDay = String.valueOf(day);
                        String sMonth = String.valueOf(month);
                        String sYear = String.valueOf(year);
                        if (month < 10) {
                            sMonth = "0" + sMonth;
                        }
                        if (day < 10) {
                            sDay = "0" + sDay;
                        }
                        date = sDay+"/"+sMonth+"/"+sYear;
                        txtDate.setText(date);
                        Toast.makeText(NewCashMovementActivity.this, date, Toast.LENGTH_SHORT).show();
                    }
                };

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewCashMovementActivity.this, R.style.AppTheme2,
                        dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        spinValues = Arrays.asList(getResources().getStringArray(R.array.list_cash_movement_types));
        adapterTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinValues);
        spinnerTypes.setAdapter(adapterTypes);

        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double value = 0.0;
                try {
                    value = Double.parseDouble(etValue.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    value = 0.00;
                }

                String desc = etDesc.getText().toString();
                selectedType += 1;

                if (value == 0) {
                    Toast.makeText(NewCashMovementActivity.this, R.string.toast_value_field_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else if (day == 0) {
                    Toast.makeText(NewCashMovementActivity.this, R.string.toast_date_field_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    try {
                        dataSource.open();

                        long id = dataSource.createCashMovement(value, selectedType, day, month, year, desc, userId);

                        if (id != 0) {
                            // change bank account or wallet
                            switch (selectedType) {
                                case 1: {
                                    dataSource.withdrawal(userId, value);
                                    break;
                                }
                                case 2: {
                                    dataSource.normalExpense(userId, value);
                                    break;
                                }
                                case 3: {
                                    dataSource.debit(userId, value);
                                    break;
                                }
                                case 4: {
                                    dataSource.deposit(userId, value);
                                    break;
                                }
                            }
                            dataSource.close();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(NewCashMovementActivity.this, R.string.toast_something_went_wrong,
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        dataSource.close();
                    }
                }

            }
        });

    }

    private void iniViews() {
        etValue = (EditText) findViewById(R.id.activityNewCM_etValue);
        txtDate = (TextView) findViewById(R.id.activityNewCM_txtDate);
        btCalendar = (Button) findViewById(R.id.activityNewCM_btCalendar);
        etDesc = (EditText) findViewById(R.id.activityNewCM_etDesc);
        btConfirm = (Button) findViewById(R.id.activityNewCM_btConfirm);
        btCancel = (Button) findViewById(R.id.activityNewCM_btCancel);
        spinnerTypes = (Spinner) findViewById(R.id.activityNewCM_spinType);
    }
}
