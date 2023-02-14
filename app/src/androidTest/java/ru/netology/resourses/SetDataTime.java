package ru.netology.resourses;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;

import org.hamcrest.Matchers;

public class SetDataTime {

    public static void setDate(int id, String date) {

        int year = Integer.parseInt(date.substring(6));
        int month = Integer.parseInt(date.substring(3,5));
        int day = Integer.parseInt(date.substring(0,2));

        onView(withId(id)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void setTime(int id, String time) {

        int hours = Integer.parseInt(time.substring(0,2));
        int month = Integer.parseInt(time.substring(3));

        onView(withId(id)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hours, month));
        onView(withId(android.R.id.button1)).perform(click());
    }
}
