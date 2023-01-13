package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import ru.iteco.fmhandroid.R;

public class LoginFragment {

        public MainPageFragment getLogIn(String login, String password) {
        onView(withId(R.id.login_text_input_layout))
                .perform(typeText(login), closeSoftKeyboard());
//                .perform(ViewActions.typeText("login2"), closeSoftKeyboard());
//                .perform(typeTextIntoFocusedView("login2"));
        onView(withId(R.id.password_text_input_layout))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.enter_button))
//                .check(matches(isDisplayed()));
                .perform(click());
        return new MainPageFragment();
    }

    public void getLogOut() {
      //TODO
    }
}
