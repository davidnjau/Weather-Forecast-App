package com.centafrique.weather.helper_class;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Calculator {

    public String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("EEE, dd-MM-yyyy", cal).toString();


        return date;

    }

    public Double getDegreeCelcius(Double Temp){

        return Temp - 273.15;

    }





}
