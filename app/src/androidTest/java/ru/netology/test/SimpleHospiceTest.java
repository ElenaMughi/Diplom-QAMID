package ru.netology.test;


import androidx.test.espresso.IdlingRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.LoginFragment;
import ru.netology.data.HospiceData;
import ru.netology.resourses.EspressoIdlingResources;


@RunWith(AllureAndroidJUnit4.class)
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
    public void testSimpleTask() throws Exception {
        Thread.sleep(1000);
        HospiceData.LogInfo loginfo = new HospiceData.LogInfo();
        LoginFragment loginFragment = new LoginFragment();
        EspressoIdlingResources.increment();
        loginFragment.getLogIn(loginfo.getLogin(), loginfo.getPassword());
        EspressoIdlingResources.decrement();

//        Intents.init();
//        intended(hasComponent("http:\..");
//        Intents.release();

    }

}

