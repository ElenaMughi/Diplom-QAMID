package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static ru.netology.resourses.WaitId.waitId;

import ru.iteco.fmhandroid.R;
import ru.netology.data.LoginInfo;
import ru.netology.resourses.PrintText;

public class LoginPageFragment {

    private PrintText res = new PrintText();

    public void toComeIn(LoginInfo.LogInfo logInfo) {
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));

        res.typingTextWithParentWithClear(R.id.login_text_input_layout, logInfo.getLogin());
        res.typingTextWithParentWithClear(R.id.password_text_input_layout, logInfo.getPassword());
        onView(withId(R.id.enter_button)).perform(click());
        onView(isRoot()).perform(waitId(R.id.main_swipe_refresh, 20000));
    }

    public void toWrongComeIn(LoginInfo.LogInfo logInfo) {

        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));

        res.typingTextWithParentWithClear(R.id.login_text_input_layout, logInfo.getLogin());
        res.typingTextWithParentWithClear(R.id.password_text_input_layout, logInfo.getPassword());
        onView(withId(R.id.enter_button)).perform(click());

// TODO для проверки всплывающего сообщения - проверка снята

//        onView(withText(R.string.wrong_login_or_password)) //"Wrong login or password"
//        onView(withText(R.string.empty_login_or_password)) //"Login and password cannot be empty"
//                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
//                .check(matches(isDisplayed()));

        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));
    }

    public void toComeOut() {
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));

        onView(withId(R.id.authorization_image_button)).perform(click());
        onView(withText("Log out")).perform(click());

        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));
    }
}
