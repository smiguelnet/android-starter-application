package net.smiguel.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import net.smiguel.app.constants.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class DataUtils {

    private static final Locale sLocale = new Locale("pt", "PT");
    private static final DateFormat sSimpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", sLocale);
    private static final DateFormat sSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", sLocale);

    public static Date getCurrentDate() {
        return Calendar.getInstance(sLocale).getTime();
    }

    public static String formatDatetime(Date date) {
        if (date == null) {
            return "";
        }
        return sSimpleDatetimeFormat.format(date);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return sSimpleDateFormat.format(date);
    }

    public static Date parseDate(String date) {
        if (date == null || date.toString().trim().length() == 0) {
            return null;
        }
        try {
            return sSimpleDateFormat.parse(date);

        } catch (ParseException e) {
            Timber.e(e);
            return null;
        }
    }

    public static boolean isValidDate(String date) {
        return parseDate(date) != null;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is not valid to get SharedPreferences reference");
        }
        return context.getSharedPreferences(AppConstants.SharedData.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setSharedPreferencesValue(SharedPreferences sharedPreferences, String key, String value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(key, value).commit();
        }
    }

    public static String getSharedPreferencesValue(SharedPreferences sharedPreferences, String key) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, null);
    }
}
