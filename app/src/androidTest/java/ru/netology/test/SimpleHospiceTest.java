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

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.ClaimsPageFragment;
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.EspressoIdlingResources;


//@RunWith(AllureAndroidJUnit4.class)
@RunWith(AndroidJUnit4.class)
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

        HospiceInfo.LogInfo loginfo = HospiceInfo.getLogInfo();
        LoginPageFragment loginFragment = new LoginPageFragment();

        loginFragment.toComeIn(loginfo.getLogin(), loginfo.getPassword());
        onView(withId(R.id.authorization_image_button))
                .check(matches(isDisplayed()));

        loginFragment.toComeOut();
        onView(allOf(
                withParent(withParent(withId(R.id.nav_host_fragment))),
                withText("Authorization ")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void createClaimTest() throws Exception {
//        HospiceData.LogInfo loginfo = new HospiceData.LogInfo();
//        loginPageFragment loginPageFragment = new loginPageFragment();
//
//        loginPageFragment.toComeIn(loginfo.getLogin(), loginfo.getPassword());
//        onView(withId(R.id.authorization_image_button))
//                .check(matches(isDisplayed()));

        Thread.sleep(4000);
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.callCreateNewClaimFromMainPage();
        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo();
        claimPage.createClaim(claimInfo);
        ClaimsPageFragment claimPage2 = mainPage.goToClaimPage();
//        claimPage2.addCommentToClaim(claimInfo, HospiceInfo.comment[0]);

//        loginPageFragment.toComeOut();
//        onView(allOf(
//                withParent(withParent(withId(R.id.nav_host_fragment))),
//                withText("Authorization ")))
//                .check(matches(isDisplayed()));
    }

//    проверять элементы в списке
//    ViewInteraction recycleView =
//            onView(CustomViewMatcher.recyclerViewSizeMatcher(10));// Ожидаемое кол-во элементов
//    ViewInteraction recycleView =
//            onView(withId(R.id.recycler_view));
//    recycleView.check(
//    matches(CustomViewMatcher.recyclerViewSizeMatcher(10)) // Проверяем ожидаемое кол-во элементов

//EspressoIdlingResources.increment();// Увеличили счетчик...
//// Сложные операции требующие время ;)
// EspressoIdlingResources.decrement();// Уменьшили счетчик

    //проверить что список
//    ViewInteraction recyclerView = onView(withId(R.id.recycler_view));
//    recyclerView.check(CustomViewAssertions.isRecyclerView());
}

