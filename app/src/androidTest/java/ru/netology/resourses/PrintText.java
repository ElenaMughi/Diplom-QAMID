package ru.netology.resourses;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;

public class PrintText {

    public static void typingText(int id, String text) {
        ViewInteraction textClaim =  //Заголовок
                onView(withId(id)).perform(click());
        textClaim.perform(typeText(text), closeSoftKeyboard());
    }

    public static void typingTextWithClear(int id, String text) {
        ViewInteraction textClaim =  //Заголовок
                onView(withId(id)).perform(click());
        textClaim.perform(clearText());
        textClaim.perform(typeText(text), closeSoftKeyboard());
    }

    public static void typingTextWithParent(int parentId, String text) {
        onView(withId(parentId)).perform(click());
        ViewInteraction element = onView(
                allOf(
                        withClassName(endsWith("EditText")),
                        withParent(withParent(withId(parentId)))
                )
        );
        element.perform(typeText(text), closeSoftKeyboard());
    }

    public static void typingTextWithParentWithClear(int parentId, String text) {
        onView(withId(parentId)).perform(click());
        ViewInteraction element = onView(
                allOf(
                        withClassName(endsWith("EditText")),
                        withParent(withParent(withId(parentId)))
                )
        );
        element.perform(clearText());
        element.perform(typeText(text), closeSoftKeyboard());
    }

    public static void getItemFromList(String text) {
        onView(withText(text))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

}
