package ru.netology.test;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.LoginFragment;
import ru.netology.data.HospiceData;
import ru.netology.resourses.EspressoIdlingResources;


@RunWith(AllureAndroidJUnit4.class)
//@RunWith(AndroidJUnit4.class)
public class SimpleHospiceTest {

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);
//    public IntentsTestRule intentsTestRule
//            = new IntentsTestRule(AppActivity.class);

    @Before
    public void registerIdlingResources() { //Подключаемся к “счетчику”
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResources() { //Отключаемся от “счетчика”
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    @Test
    public void SimpleLogInOutTest() throws Exception {

        HospiceData.LogInfo loginfo = new HospiceData.LogInfo();
        LoginFragment loginFragment = new LoginFragment();

        loginFragment.toComeIn(loginfo.getLogin(), loginfo.getPassword());
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));

        loginFragment.toComeOut();
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization")))
                .check(matches(isDisplayed()));
    }

//        Intents.init();
//        intended(hasComponent("http:\..");
//        Intents.release();

}

