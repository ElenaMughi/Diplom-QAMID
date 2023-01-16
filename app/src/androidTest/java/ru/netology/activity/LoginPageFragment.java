package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class LoginPageFragment {

    private void typingText(int parentId, String text) {
        onView(withId(parentId)).perform(click());
        ViewInteraction element = onView(
                allOf(
                        withClassName(endsWith("EditText")),
                        withParent(withParent(withId(parentId)))
                )
        );
        element.perform(typeText(text), closeSoftKeyboard());
    }

    public MainPageFragment toComeIn (String login, String password) throws Exception {
        Thread.sleep(4000);
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));

        typingText(R.id.login_text_input_layout, login);
        typingText(R.id.password_text_input_layout, password);
        onView(withId(R.id.enter_button)).perform(click());

        Thread.sleep(2000);
        return new MainPageFragment();
    }

    public void toComeOut () {
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.authorization_image_button)).perform(click());
        onView(withText("Log out")).perform(click());
    }
}
