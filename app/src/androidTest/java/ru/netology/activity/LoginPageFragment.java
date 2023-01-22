package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.ForAllResourses;

public class LoginPageFragment {

    private ForAllResourses res = new ForAllResourses();

    public void toComeIn(HospiceInfo.LogInfo logInfo) throws Exception {
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));

        res.typingTextWithParentWithClear(R.id.login_text_input_layout, logInfo.getLogin());
        res.typingTextWithParentWithClear(R.id.password_text_input_layout, logInfo.getPassword());
        onView(withId(R.id.enter_button)).perform(click());

        Thread.sleep(2000);
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));
    }

    public void toWrongComeIn(HospiceInfo.LogInfo logInfo) throws Exception {

        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));

        res.typingTextWithParentWithClear(R.id.login_text_input_layout, logInfo.getLogin());
        res.typingTextWithParentWithClear(R.id.password_text_input_layout, logInfo.getPassword());
        onView(withId(R.id.enter_button)).perform(click());

        //для проверки всплывающего сообщения
//        onView(withText(R.string.wrong_login_or_password)) //"Wrong login or password"
//        onView(withText(R.string.empty_login_or_password)) //"Login and password cannot be empty"
//                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
//                .check(matches(isDisplayed()));

        Thread.sleep(2000);
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));
    }

    public void toComeOut() throws Exception {
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));

        onView(withId(R.id.authorization_image_button)).perform(click());
        onView(withText("Log out")).perform(click());

        Thread.sleep(2000);
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));
    }
}
