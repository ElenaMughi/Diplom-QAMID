package ru.netology.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static io.qameta.allure.kotlin.Allure.step;
import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.PerformException;
import androidx.test.rule.ActivityTestRule;

import net.datafaker.Faker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.AboutPageFragment;
import ru.netology.activity.ClaimFragment;
import ru.netology.activity.ClaimsPageFragment;
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.LoveIsAllPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.activity.NewsEditPageFragment;
import ru.netology.activity.NewsFragment;
import ru.netology.activity.NewsPageFragment;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.data.LoginInfo;
import ru.netology.data.NewsInfo;

@RunWith(AllureAndroidJUnit4.class)
public class SimpleHospiceTest {

    Faker faker = new Faker();

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);

    @Before
    public void enterToApp() {
        LoginPageFragment loginPage = new LoginPageFragment();
        LoginInfo.LogInfo loginInfo = LoginInfo.getLogInfo();
        try {
            onView(isRoot()).perform(waitId(R.id.enter_button, 10000));
            loginPage.toComeIn(loginInfo);
        } catch (PerformException e) {
            System.out.println("не найдено" + R.id.enter_button);
        }
    }

    @Test
    @DisplayName("1. Авторизация в приложении")
    public void logInOutTest() {

        LoginPageFragment loginPage = new LoginPageFragment();

        loginPage.toComeOut();

        LoginInfo.LogInfo loginInfo = LoginInfo.getWrongLogInfo();
        loginPage.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfoWithLoginEmpty();
        loginPage.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfoWithPasswordEmpty();
        loginPage.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfo();
        loginPage.toComeIn(loginInfo);
    }

     @Test
    @DisplayName("16.1 Пробег по кнопкам на главной странице.")
    public void checkMainPageButtonTest() {

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsInfo.ClaimInfo claim = ClaimsInfo.getClaimInfoWithOutFIO();
        ClaimFragment claimFragment = mainPage.callCreationClaimFromMainPage();
        claim = claimFragment.createClaim(claim);

        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        claimFragment = claimsPage.toFoundClaimWithFilter(claim);
        claimFragment.checkClaimFields(claim);
        claimsPage.goToMainPage();

        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        newsPage.goToMainPage();
    }

    @Test
    @DisplayName("16.2 Пробег по кнопкам. Страница About.")
    public void checkAboutPageButtonTest() {

        MainPageFragment mainPage = new MainPageFragment();
        AboutPageFragment aboutPage = mainPage.goToAboutPage();
        aboutPage.checkPage();
    }

    @Test
    @DisplayName("16.3 Пробег по кнопкам. Страница Love Is All.")
    public void checkLoveIsAllPageButtonTest() {

        MainPageFragment mainPage = new MainPageFragment();
        LoveIsAllPageFragment loveIsAllPage = mainPage.goToLoveIsAllPage();
        loveIsAllPage.checkQuote();
    }
}

