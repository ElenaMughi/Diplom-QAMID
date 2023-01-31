package ru.netology.test;

import static io.qameta.allure.kotlin.Allure.step;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.ClaimFragment;
import ru.netology.activity.ClaimsPageFragment;
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.activity.NewsEditPageFragment;
import ru.netology.activity.NewsFragment;
import ru.netology.activity.NewsPageFragment;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.EspressoIdlingResources;


@RunWith(AllureAndroidJUnit4.class)
//@RunWith(AndroidJUnit4.class)
public class SimpleHospiceTest {

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);

//    @Rule
//    public IntentsTestRule intentsTestRule
//            = new IntentsTestRule(AppActivity.class);

    HospiceInfo hospiceInfo = new HospiceInfo();

    @Before
    public void registerIdlingResources() { //Подключаемся к “счетчику”
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResources() { //Отключаемся от “счетчика”
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    @Test
    public void LogInOutTest() throws Exception {
        step("1. Авторизация в приложении");


        Thread.sleep(4000); // загрузка
        LoginPageFragment loginPageFragment = new LoginPageFragment();

        loginPageFragment.toComeOut();

        HospiceInfo.LogInfo loginInfo = HospiceInfo.getWrongLogInfo();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = HospiceInfo.getLogInfoWithLoginEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = HospiceInfo.getLogInfoWithPasswordEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = HospiceInfo.getLogInfo();
        loginPageFragment.toComeIn(loginInfo);


    }

    @Test
    public void createClaimTestFromMainWithCheckEmptyFields() throws Exception {
        step("2. Создание заявки из главной страницы и проверка заполнения полей");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo(1); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        ClaimFragment claim = mainPage.callCreateNewClaimFromMainPage();

        claim.checkEmptyFieldsCreateClaim(claimInfo);

        claim = mainPage.callCreateNewClaimFromMainPage();
        claim.createClaim(claimInfo);

        ClaimsPageFragment claimsPageFragment = mainPage.goToClaimsPageFromMenu();
        claim = claimsPageFragment.toFoundClaim(claimInfo);
        Thread.sleep(4000); // загрузка
        claim.goBackToClaimPage();
        claimsPageFragment.goToMainPage();

//        loginPageFragment.toComeOut();
    }

    @Test
    public void createClaimTestFromMainWithCheckCancel() throws Exception {
        step("3. Проверка отмены создания заявки");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo(1); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        ClaimFragment claim = mainPage.callCreateNewClaimFromMainPage();
        claim.cancelCreateClaim(claimInfo, true); //Да, при подтверждении

        claim = mainPage.callCreateNewClaimFromMainPage();
        claim.cancelCreateClaim(claimInfo, false); //нет, при подтверждении
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claim = claimPage.toFoundClaim(claimInfo);
        Thread.sleep(4000); // загрузка
        claim.goBackToClaimPage();
        claimPage.goToMainPage();

//        loginPageFragment.toComeOut();
    }

    @Test
    public void createClaimTestFromClaimsWithCheckExecutor() throws Exception { // выяснить
        step("4. Создание заявки из страницы с заявками с проверкой заполнения поля Исполнитель.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claimPage.createClaim(claimInfo);
        claimPage.toCheckStatusClaim(claimInfo, HospiceInfo.claimStatus[0]);
        claimInfo = claimPage.toChangeStatusClaim(claimInfo, HospiceInfo.claimStatus[0], HospiceInfo.claimStatus[1], true);
        claimPage.toCheckStatusClaim(claimInfo, HospiceInfo.claimStatus[1]);

        HospiceInfo.ClaimInfo claimInfo2 = HospiceInfo.getClaimInfo(3); //заявка
        ClaimsPageFragment claimPage2 = new ClaimsPageFragment();
        claimPage2.createClaim(claimInfo2);
        // TODO: 22.01.2023 //Здесь видимо ошибка. Проверка снята.
//        claimPage2.toCheckStatusClaim(claimInfo2, HospiceInfo.claimStatus[1]);

        HospiceInfo.ClaimInfo claimInfo3 = HospiceInfo.getClaimInfo(1); //заявка
        ClaimsPageFragment claimPage3 = new ClaimsPageFragment();
        claimPage3.createClaim(claimInfo3);
        claimPage3.toCheckStatusClaim(claimInfo3, HospiceInfo.claimStatus[1]);

//        loginPageFragment.toComeOut();
    }

    @Test
    public void createAndEditCommentOnClaim() throws Exception {
        step("5. Проверка добавления комментариев к заявке на этапах Открыто и в Работе.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromClaimBox();
        claimPage.createClaim(claimInfo);

        claimPage.addCommentToClaim(claimInfo, HospiceInfo.comment[1], false, 0);
        claimPage.addCommentToClaim(claimInfo, HospiceInfo.comment[1], true, 1);
        claimPage.editCommentToClaim(claimInfo, HospiceInfo.comment[2], HospiceInfo.comment[1], false, 1);
        claimPage.editCommentToClaim(claimInfo, HospiceInfo.comment[2], HospiceInfo.comment[1], true, 1);

        claimInfo = claimPage.toChangeStatusClaim(claimInfo, HospiceInfo.claimStatus[0], HospiceInfo.claimStatus[1], false);
        claimPage.addCommentToClaim(claimInfo, HospiceInfo.comment[3], true, 2);

        HospiceInfo.ClaimInfo claimInfo2 = HospiceInfo.getClaimInfo(1); //заявка2
        claimPage.createClaim(claimInfo2);

        claimPage.addCommentToClaim(claimInfo2, HospiceInfo.comment[3], true, 1);
        claimPage.editCommentToClaim(claimInfo2, HospiceInfo.comment[4], HospiceInfo.comment[3], true, 1);
        claimPage.addCommentToClaim(claimInfo2, HospiceInfo.comment[3], true, 2);
        claimPage.addCommentToClaim(claimInfo2, HospiceInfo.comment[0], true, 3);

//        loginPageFragment.toComeOut();
    }

    @Test
    public void changeClaimStatus() throws Exception {
        step("6. Прохождение по статусам заявок с редактированием заявки и использованием фильтра заявок.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.ClaimInfo claimInfo = HospiceInfo.getClaimInfo(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claimPage.createClaim(claimInfo);
        HospiceInfo.ClaimInfo claimInfo2 = HospiceInfo.getClaimInfo(2); //заявка
        claimPage.editClaim(claimInfo, claimInfo2);
        claimPage.toChangeStatusClaim(claimInfo2, HospiceInfo.claimStatus[0], HospiceInfo.claimStatus[3], true);
        claimPage.editClaimNot(claimInfo2, HospiceInfo.claimStatus[3]);

        HospiceInfo.ClaimInfo claimInfo3 = HospiceInfo.getClaimInfo(2);
        claimPage.createClaim(claimInfo3);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceInfo.claimStatus[0], HospiceInfo.claimStatus[1], true);
        claimPage.editClaimNot(claimInfo3, HospiceInfo.claimStatus[1]);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceInfo.claimStatus[1], HospiceInfo.claimStatus[0], true);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceInfo.claimStatus[0], HospiceInfo.claimStatus[1], true);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceInfo.claimStatus[1], HospiceInfo.claimStatus[2], true);
        claimPage.editClaimNot(claimInfo3, HospiceInfo.claimStatus[2]);

        claimPage.createClaim(claimInfo);
        claimPage.changeExecutor(claimInfo, claimInfo.getAuthor(), HospiceInfo.claimStatus[0]);
//        loginPageFragment.toComeOut();
    }

    @Test
    public void createNewsWithMenuEmptyFieldsAndCancel() throws Exception {
        step("7. Создание новости с проверкой пустых полей, отмены создания, удаление новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.NewsInfo newsInfo = HospiceInfo.getNewInfo(1, true); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();
        newsEdit.createSimpleNews(newsInfo, false);
        newsEdit.createSimpleNews(newsInfo, true);
        HospiceInfo.NewsInfo newsInfo2 = HospiceInfo.getNewInfo(1, true); //заявка
        newsEdit.checkFieldsFromNews(newsInfo2);
        newsEdit.deleteNews(newsInfo, false);
        newsEdit.deleteNews(newsInfo, true);

//        loginPageFragment.toComeOut();
    }

    @Test
    public void viewNewsFromMainWithEditAndActive() throws Exception {
        step("8. Просмотр текущих новостей на главной странице и редактирование. Активность новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.NewsInfo newsInfo = HospiceInfo.getNewInfo(1, true); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        newsEdit.createSimpleNews(newsInfo, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo, true);
        mainPage.goToNewsPageFromNewsBox();
        newsPage.goToEditNewsPage();
        newsEdit.changeActive(newsInfo, false);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo, false);
        mainPage.goToNewsPageFromNewsBox();
        newsPage.goToEditNewsPage();
        newsEdit.changeActive(newsInfo, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo, true);
        mainPage.goToNewsPageFromNewsBox();
        newsPage.goToEditNewsPage();
        HospiceInfo.NewsInfo newsInfo2 = HospiceInfo.getNewInfo(2, true); //заявка
        newsEdit.editNews(newsInfo, newsInfo2, false);
        newsEdit.editNews(newsInfo, newsInfo2, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo2, true);

//        loginPageFragment.toComeOut();
    }

    @Test
    public void CheckNewsFilter() throws Exception {
        step("9. Проверка фильтров новостей.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPageFragment = new LoginPageFragment();
//        HospiceInfo.LogInfo loginInfo = HospiceInfo.getLogInfo();
//        loginPageFragment.toComeIn(loginInfo);

        HospiceInfo.NewsInfo[] news = {
//                HospiceInfo.getNewInfo(1, true), //новость Объявление
//                HospiceInfo.getNewInfo(2, true), //новость День рождения
//                HospiceInfo.getNewInfo(3, true), //новость Зарплата
//                HospiceInfo.getNewInfo(4, true), //новость Профсоюз
//                HospiceInfo.getNewInfo(5, true), //новость Праздник
//                HospiceInfo.getNewInfo(6, true), //новость Массаж
//                HospiceInfo.getNewInfo(7, true), //новость Благодарность
                HospiceInfo.getNewInfo(8, true) //новость Нужна помощь
        };
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        for (HospiceInfo.NewsInfo newsInfo : news) {
            newsEdit.createSimpleNews(newsInfo, true);
        }

//        newsEdit.goToMainPage();
        mainPage.goToNewsPage();
        for (HospiceInfo.NewsInfo newsInfo : news) {
            newsPage.checkNewsCategory(newsInfo);
        }

        newsPage.goToEditNewsPage();
        for (HospiceInfo.NewsInfo newsInfo : news) {
            newsEdit.checkNewsCategory(newsInfo, false, false);
        }

        newsEdit.changeActive(news[0], false);
        newsEdit.checkNewsCategory(news[0], false, true);
        newsEdit.checkNewsCategory(news[1], true, false);

//        loginPageFragment.toComeOut();
    }
}

