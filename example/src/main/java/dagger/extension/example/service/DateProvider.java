package dagger.extension.example.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.Replaceable;

public class DateProvider {

    @Inject @Replaceable
    public DateProvider() { }

    public Calendar getCurrentDate(){
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        instance.setTimeInMillis(System.currentTimeMillis());
        return instance;
    }

    public Calendar parse(String strDate) throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

        Calendar parsedDate = Calendar.getInstance(Locale.GERMANY);
        parsedDate.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        parsedDate.setTime(dateFormat.parse(strDate));

        return parsedDate;
    }

}
