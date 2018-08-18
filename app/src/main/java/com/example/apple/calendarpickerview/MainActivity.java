package com.example.apple.calendarpickerview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CalendarPickerView calendar;
    TextView mTvShowing;
    int mRangeStartYear, mRangeStartMonth, mRangeStartDate, mRangeEndMonth, mRangeEndYear, mRangeEndDate, i, mHavingDays;
    ArrayList<String> mDatesInString = new ArrayList<>();
    int mHavingMonths = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvShowing = findViewById(R.id.tv_showing);

        // Date is coming from today(min) onwards to next year same date(max)
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 2);
        Calendar back = Calendar.getInstance();
        back.add(Calendar.YEAR, -2);

        calendar = findViewById(R.id.calendar_view);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        // Ranging date selected
        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        today.add(Calendar.DATE, 2);
        dates.add(today.getTime());
        today.add(Calendar.DATE, 4);
        dates.add(today.getTime());
        //
        calendar.init(back.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDates(dates);
//        calendar.highlightDates(getHoliday()); //Picking single Date from the Calendar
//        mTvShowing.setText(String.valueOf(getHoliday())); //Picking single Date from the Calendar

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String[] dateInString = {"17-08-2018", "29-08-2018", "24-09-2018", "15-09-2018", "05-10-2018", "23-11-2018", "01-12-2018"};
        for (String aDate : dateInString) {
            try {
                Date data = sdf.parse(String.valueOf(aDate));
                ArrayList<Date> holidays = new ArrayList<>();
                holidays.add(data);
                calendar.highlightDates(holidays);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // implementing the for loop based on dates also validating the range date, range month(Month days) and range year(Leap year/Non-leap year)
        String splitFromDate = "2018-12-27"; // 2018-08-18
        mRangeStartYear = Integer.parseInt(splitFromDate.split("-")[0]); // 2018
        mRangeStartMonth = Integer.parseInt(splitFromDate.split("-")[1]); // 08
        mRangeStartDate = Integer.parseInt(splitFromDate.split("-")[2]); // 18

        String splitToDate = "2019-01-27"; // 2018-08-19
        mRangeEndYear = Integer.parseInt(splitToDate.split("-")[0]);
        mRangeEndMonth = Integer.parseInt(splitToDate.split("-")[1]);
        mRangeEndDate = Integer.parseInt(splitToDate.split("-")[2]);

        // CalendarPickerView multiple date select
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (mRangeStartDate != 0) {
            for (; mRangeStartYear <= mRangeEndYear; mRangeStartYear++) {
                if (mRangeStartYear == mRangeEndYear) {
                    mRangeStartMonth = 1;
                    mHavingMonths = mRangeEndMonth;
                }
                Log.d("info>>", "mRangeStartYear: " + mRangeStartYear);
                for (; mRangeStartMonth <= mHavingMonths; mRangeStartMonth++) {
                    Log.d("info>>", "mRangeStartMonth: " + mRangeStartMonth);
                    if ((mRangeStartYear % 4 == 0) && (mRangeStartYear % 100 != 0) || (mRangeStartYear % 400 == 0)) {
                        if (mRangeStartMonth == 2) {
                            mHavingDays = 29; // Is leap year feb month
                        } else if ((mRangeStartMonth == 7) || ((mRangeStartMonth % 2) == 0)) {
                            mHavingDays = 31; // Is july & even months
                        } else {
                            mHavingDays = 30; // Is odd months
                        }
                    } else {
                        if (mRangeStartMonth == 2) {
                            mHavingDays = 28; // Is leap year feb month
                        } else if ((mRangeStartMonth == 7) || ((mRangeStartMonth % 2) == 0)) {
                            mHavingDays = 31; // Is july & even months
                        } else {
                            mHavingDays = 30; // Is odd months
                        }
                    }
                    if (mRangeStartMonth == mRangeEndMonth) {
                        mRangeStartDate = 1;
                        mHavingDays = mRangeEndDate;
                    }
                    for (; mRangeStartDate <= mHavingDays; mRangeStartDate++) {
                        Log.d("info>>", "mRangeStartDate: " + mRangeStartDate);
                        mDatesInString.add(String.valueOf(mRangeStartYear) + "-" + String.valueOf(mRangeStartMonth) + "-" + mRangeStartDate);
                        Log.d("info>>", "simpleDateFormat: " + String.valueOf(mRangeStartYear) + "-" + String.valueOf(mRangeStartMonth) + "-" + mRangeStartDate);

                        for (String aDate : mDatesInString) {
                            try {
                                Date data = simpleDateFormat.parse(String.valueOf(aDate));
                                ArrayList<Date> holidays = new ArrayList<>();
                                holidays.add(data);
                                calendar.highlightDates(holidays);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        initCustomSpinner();
    }

    // Collection = ArrayList methods.
    // https://www.truiton.com/2015/04/android-date-range-picker/
    // Picking single Date from the Calendar
    private Collection<Date> getHoliday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = "30-08-2018";
        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Date> holidays = new ArrayList<>();
        holidays.add(date);
        return holidays;
    }

    private void initCustomSpinner() {

        Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerCustom);
        // Spinner Drop down elements
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("Andorid");
        languages.add("IOS");
        languages.add("PHP");
        languages.add("Java");
        languages.add(".Net");
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, languages);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
