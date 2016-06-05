package com.crackerjack.notificationcenter.utils;

import android.annotation.SuppressLint;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pratik 05/06/16.
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {


    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String Time = "HH:mm:ss";
    public static final String TIME_AM = "hh:mm a";
    private static SimpleDateFormat LocalOnlyTimeFormatter = new SimpleDateFormat(
            Time, Locale.US);

    private static SimpleDateFormat UTCOnlyTimeFormatter = new SimpleDateFormat(
            Time, Locale.US);

    public static String getLocalTimeOnlyAmFormat(String time,String outputPattern) {

        Logger.d("Utils ::::"+time);
        SimpleDateFormat localtimeSDF = new SimpleDateFormat("HH:mm:ss",
                Locale.US);
        SimpleDateFormat UTCtimeSDF = new SimpleDateFormat("HH:mm:ss",
                Locale.US);
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");

        SimpleDateFormat parseFormat = new SimpleDateFormat(outputPattern);

        String s = "";
        try {

            UTCtimeSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = UTCtimeSDF.parse(time);
            localtimeSDF.setTimeZone(TimeZone.getDefault());
            s = localtimeSDF.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            Date date = displayFormat.parse(s);
            s = parseFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return s;

    }



    public static String formatTime24to12(String str_time) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = displayFormat.parse(str_time);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        String parsedTime = null;
        try {
            parsedTime = parseFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedTime;
    }


    public static String longToDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        Date date = new Date(time);
        return sdf.format(date);
    }


    public static String getUTCOnlyTimeString(String strLocalTime) {

        String s = "";
        try {
            LocalOnlyTimeFormatter.setTimeZone(TimeZone.getDefault());
            Date d = LocalOnlyTimeFormatter.parse(strLocalTime);
            UTCOnlyTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            s = UTCOnlyTimeFormatter.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String utcToLocal(String utcTime) {
        String s = null;
        try {

            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(utcTime);
            df.setTimeZone(TimeZone.getDefault());
            s = df.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String convertAmFromat(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdfs.parse(time);
            time = sdf.format(dt);
        } catch (ParseException e) {
            Logger.v("ex", "" + e);
            e.printStackTrace();
        }
        return time;

    }

    public static String convertNonAMFromat(String time) {
        SimpleDateFormat sdfs = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf= new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdfs.parse(time);
            time = sdf.format(dt);
        } catch (ParseException e) {
            Logger.v("ex", "" + e);
            e.printStackTrace();
        }
        return time;

    }


    public static String getFormattedDate(String time, String outputPattern) {

        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        if (time == null) {

            return "";
        }

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

            //Log.v("parseDate", "Converted Date Today:" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

}
