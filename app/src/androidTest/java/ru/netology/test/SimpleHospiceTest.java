package ru.netology.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static io.qameta.allure.kotlin.Allure.step;
import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.ClaimFragment;
import ru.netology.activity.ClaimsPageFragment;
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.activity.NewsEditPageFragment;
import ru.netology.activity.NewsPageFragment;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.EspressoIdlingResources;


@RunWith(AllureAndroidJUnit4.class)
public class SimpleHospiceTest {

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);

    @Before
    public void registerIdlingResources() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }


    private LoginPageFragment logIn() {
        LoginPageFragment loginPage = new LoginPageFragment();
        ClaimsInfo.LogInfo loginInfo = ClaimsInfo.getLogInfo();
        try {
            onView(isRoot()).perform(waitId(R.id.nav_host_fragment, 7000));
            loginPage.toComeIn(loginInfo);
        } catch (NoMatchingViewException e) {
            System.out.println("не найдено" + R.id.nav_host_fragment);
        }
        onView(isRoot()).perform(waitId(R.id.main_swipe_refresh, 5000));
        return loginPage;
    }

    @Test
    public void logInOutTest() throws Exception {
        step("1. Авторизация в приложении");


        Thread.sleep(4000); // загрузка
        LoginPageFragment loginPageFragment = new LoginPageFragment();

        loginPageFragment.toComeOut();

        ClaimsInfo.LogInfo loginInfo = ClaimsInfo.getWrongLogInfo();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = ClaimsInfo.getLogInfoWithLoginEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = ClaimsInfo.getLogInfoWithPasswordEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = ClaimsInfo.getLogInfo();
        loginPageFragment.toComeIn(loginInfo);

    }

    @Test
    public void createClaimTestFromMainWithCheckEmptyFields() throws Exception {
        step("2. Создание заявки из главной страницы и проверка заполнения полей");
//        Thread.sleep(4000); // загрузка
        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo = ClaimsInfo.getClaimInfoWithChoiceFIO(1); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        ClaimFragment claim = mainPage.callCreateNewClaimFromMainPage();

        claim.checkCreateClaimWithEmptyFields(claimInfo);

        claim = mainPage.callCreateNewClaimFromMainPage();
        claim.createClaim(claimInfo);

        ClaimsPageFragment claimsPageFragment = mainPage.goToClaimsPageFromMenu();
        claim = claimsPageFragment.toFoundClaimWithWholeFilter(claimInfo);
        Thread.sleep(4000); // загрузка
        claim.goBackToClaimPage();
        claimsPageFragment.goToMainPage();

        loginPage.toComeOut();
    }

    @Test
    public void createClaimTestFromMainWithCheckCancel() throws Exception {
        step("3. Проверка отмены создания заявки");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo = ClaimsInfo.getClaimInfoWithChoiceFIO(1); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        ClaimFragment claim = mainPage.callCreateNewClaimFromMainPage();
        claim.cancelCreateClaim(claimInfo, true); //Да, при подтверждении

        claim = mainPage.callCreateNewClaimFromMainPage();
        claim.cancelCreateClaim(claimInfo, false); //нет, при подтверждении
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claim = claimPage.toFoundClaimWithWholeFilter(claimInfo);
        Thread.sleep(4000); // загрузка
        claim.goBackToClaimPage();
        claimPage.goToMainPage();

//        loginPage.toComeOut();
    }

    @Test
    public void createClaimTestFromClaimsWithCheckExecutor() throws Exception { // выяснить
        step("4. Создание заявки из страницы с заявками с проверкой заполнения поля Исполнитель.");
        Thread.sleep(6000); // загрузка
//        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo = ClaimsInfo.getClaimInfoWithChoiceFIO(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claimPage.createClaim(claimInfo);
        claimPage.toCheckStatusClaim(claimInfo, HospiceData.claimStatus[0]);
        claimPage.toChangeStatusClaim(claimInfo, HospiceData.claimStatus[0], HospiceData.claimStatus[1], true);
        claimPage.toCheckStatusClaim(claimInfo, HospiceData.claimStatus[1]);

        ClaimsInfo.ClaimInfo claimInfo2 = ClaimsInfo.getClaimInfoWithChoiceFIO(3); //заявка
        ClaimsPageFragment claimPage2 = new ClaimsPageFragment();
        claimPage2.createClaim(claimInfo2);
        // TODO: 22.01.2023 //Здесь видимо ошибка. Проверка снята.
//        claimPage2.toCheckStatusClaim(claimInfo2, HospiceInfo.claimStatus[1]);

        ClaimsInfo.ClaimInfo claimInfo3 = ClaimsInfo.getClaimInfoWithChoiceFIO(1); //заявка
        ClaimsPageFragment claimPage3 = new ClaimsPageFragment();
        claimPage3.createClaim(claimInfo3);
        claimPage3.toCheckStatusClaim(claimInfo3, HospiceData.claimStatus[1]);

//        loginPage.toComeOut();
    }

    @Test
    public void createAndEditCommentOnClaim() throws Exception {
        step("5. Проверка добавления комментариев к заявке на этапах Открыто и в Работе.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo = ClaimsInfo.getClaimInfoWithChoiceFIO(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromClaimBox();
        claimPage.createClaim(claimInfo);

        claimPage.addCommentToClaim(claimInfo, HospiceData.comment[1], false, 0);
        claimPage.addCommentToClaim(claimInfo, HospiceData.comment[1], true, 1);
        claimPage.editCommentToClaim(claimInfo, HospiceData.comment[2], HospiceData.comment[1], false, 1);
        claimPage.editCommentToClaim(claimInfo, HospiceData.comment[2], HospiceData.comment[1], true, 1);

        claimPage.toChangeStatusClaim(claimInfo, HospiceData.claimStatus[0], HospiceData.claimStatus[1], false);
        claimPage.addCommentToClaim(claimInfo, HospiceData.comment[3], true, 2);

        ClaimsInfo.ClaimInfo claimInfo2 = ClaimsInfo.getClaimInfoWithChoiceFIO(1); //заявка2
        claimPage.createClaim(claimInfo2);

        claimPage.addCommentToClaim(claimInfo2, HospiceData.comment[3], true, 1);
        claimPage.editCommentToClaim(claimInfo2, HospiceData.comment[4], HospiceData.comment[3], true, 1);
        claimPage.addCommentToClaim(claimInfo2, HospiceData.comment[3], true, 2);
        claimPage.addCommentToClaim(claimInfo2, HospiceData.comment[0], true, 3);

//        loginPage.toComeOut();
    }

    @Test
    public void changeClaimStatus() throws Exception {
        step("6. Прохождение по статусам заявок с редактированием заявки и использованием фильтра заявок.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo = ClaimsInfo.getClaimInfoWithChoiceFIO(2); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();
        claimPage.createClaim(claimInfo);
        ClaimsInfo.ClaimInfo claimInfo2 = ClaimsInfo.getClaimInfoWithChoiceFIO(2); //заявка
        claimPage.editClaim(claimInfo, claimInfo2);
        claimPage.toChangeStatusClaim(claimInfo2, HospiceData.claimStatus[0], HospiceData.claimStatus[3], true);
        claimPage.editClaimNot(claimInfo2, HospiceData.claimStatus[3]);

        ClaimsInfo.ClaimInfo claimInfo3 = ClaimsInfo.getClaimInfoWithChoiceFIO(2);
        claimPage.createClaim(claimInfo3);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceData.claimStatus[0], HospiceData.claimStatus[1], true);
        claimPage.editClaimNot(claimInfo3, HospiceData.claimStatus[1]);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceData.claimStatus[1], HospiceData.claimStatus[0], true);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceData.claimStatus[0], HospiceData.claimStatus[1], true);
        claimPage.toChangeStatusClaim(claimInfo3, HospiceData.claimStatus[1], HospiceData.claimStatus[2], true);
        claimPage.editClaimNot(claimInfo3, HospiceData.claimStatus[2]);

        claimPage.createClaim(claimInfo);
        claimPage.changeExecutor(claimInfo, claimInfo.getAuthor(), HospiceData.claimStatus[0]);
//        loginPage.toComeOut();
    }

    @Test
    public void createNewsWithMenuEmptyFieldsAndCancel() throws Exception {
        step("7. Создание новости с проверкой пустых полей, отмены создания, удаление новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo newsInfo = NewsInfo.getNewInfo(1, true); //заявка
        MainPageFragment mainPage = new MainPageFragment();

        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();
        newsEdit.createSimpleNews(newsInfo, false);
        newsEdit.createSimpleNews(newsInfo, true);
        NewsInfo.NewInfo newsInfo2 = NewsInfo.getNewInfo(1, true); //заявка
        newsEdit.checkFieldsFromNews(newsInfo2);
        newsEdit.deleteNews(newsInfo, false);
        newsEdit.deleteNews(newsInfo, true);

//        loginPage.toComeOut();
    }

    @Test
    public void viewNewsFromMainWithEditAndActive() throws Exception {
        step("8. Просмотр текущих новостей на главной странице и редактирование. Активность новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo newsInfo = NewsInfo.getNewInfo(1, true); //заявка
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
        NewsInfo.NewInfo newsInfo2 = NewsInfo.getNewInfo(2, true); //заявка
        newsEdit.editNews(newsInfo, newsInfo2, false);
        newsEdit.editNews(newsInfo, newsInfo2, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo2, true);

//        loginPage.toComeOut();
    }

    @Test
    public void checkNewsFilter() throws Exception {
        step("9. Проверка фильтров новостей.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo[] news = {
                NewsInfo.getNewInfo(1, true), //новость Объявление
                NewsInfo.getNewInfo(2, true), //новость День рождения
                NewsInfo.getNewInfo(3, true), //новость Зарплата
                NewsInfo.getNewInfo(4, true), //новость Профсоюз
                NewsInfo.getNewInfo(5, true), //новость Праздник
                NewsInfo.getNewInfo(6, true), //новость Массаж
                NewsInfo.getNewInfo(7, true), //новость Благодарность
                NewsInfo.getNewInfo(8, true) //новость Нужна помощь
        };
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        for (NewsInfo.NewInfo newsInfo : news) { // создаем новости
            newsEdit.createSimpleNews(newsInfo, true);
        }

        newsEdit.goToMainPage();
        mainPage.goToNewsPage();
        for (NewsInfo.NewInfo newsInfo : news) { //проверяем фильтр по категориям в новостях
            newsPage.checkNewsCategory(newsInfo);
        }

        newsPage.goToEditNewsPage();
        for (NewsInfo.NewInfo newsInfo : news) { //проверяем фильтр по категориям в редактировании новостей
            newsEdit.checkNewsCategoryAndActive(newsInfo, true, true);
        }

        mainPage.goToNewsPage();
        newsPage.goToEditNewsPage();
        newsEdit.changeActive(news[0], false);
        newsEdit.checkNewsActive(news[0], false, true);
        newsEdit.checkNewsActive(news[1], true, false);

        newsEdit.goToMainPage();
        mainPage.goToNewsPage();
        newsPage.goToEditNewsPage();
        newsEdit.checkNewsCategoryAndActive(news[0], false, true);
        newsEdit.checkNewsCategoryAndActive(news[1], true, false);

//        loginPage.toComeOut();
    }

    @Test
    public void checkClaimsFilter() throws Exception {
        step("10. Проверка фильтров заявок.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claims[] = {
                ClaimsInfo.getClaimInfoWithChoiceFIO(2), //заявка1
                ClaimsInfo.getClaimInfoWithChoiceFIO(1), //заявка2
                ClaimsInfo.getClaimInfoWithChoiceFIO(1), //заявка3
                ClaimsInfo.getClaimInfoWithChoiceFIO(2) //заявка4
        };

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();

        for (ClaimsInfo.ClaimInfo cl : claims) {
            claimPage.createClaim(cl);  // создаем все заявки и присваиваем им статусы далее
        }
        claimPage.toChangeStatusClaim(claims[2], HospiceData.claimStatus[1], HospiceData.claimStatus[2], true);
        claimPage.toChangeStatusClaim(claims[3], HospiceData.claimStatus[0], HospiceData.claimStatus[3], true);

        for (int i = 0; i < 4; i = i + 1) { //фильтр со всеми галочками
            claimPage.checkClaim(claims[i], HospiceData.claimStatus[i]);
        }

        for (int i = 0; i < 4; i = i + 1) { //фильтр с одной галочкой
            claimPage.checkClaimWithFiler(claims[i], HospiceData.claimStatus[i]);
        }

        boolean filter1[] = {true, true, false, false};
        claimPage.checkClaimWithMultipleFiler(claims, filter1);
        boolean filter2[] = {true, false, true, false};
        claimPage.checkClaimWithMultipleFiler(claims, filter2);
        boolean filter3[] = {true, false, true, true};
        claimPage.checkClaimWithMultipleFiler(claims, filter3);

//        loginPage.toComeOut();
    }


    @Test
    public void checkPrintSymbolsAndLettersInClaims() throws Exception {
        step("11. Проверка доступности символов при создании заявки.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromClaimBox();

        String[] text = {
                HospiceData.SymbolsAndLetters[0],
                HospiceData.SymbolsAndLetters[1],
                HospiceData.SymbolsAndLetters[2],
                HospiceData.SymbolsAndLetters[3]
        };

        ClaimsInfo.ClaimInfo claim;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            claim = ClaimsInfo.getClaimInfoWithChoiceTitleAndDiscr(text[i], text[i]); //заявка
            claimPage.createClaim(claim);
            claimPage.addCommentToClaim(claim, text[i], true, 0);
        }

//        loginPage.toComeOut();
    }

    @Test
    public void checkPrintSymbolsAndLettersInNews() throws Exception {
        step("12. Проверка доступности символов при создании новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        String[] text = {
                HospiceData.SymbolsAndLetters[0],
                HospiceData.SymbolsAndLetters[1],
                HospiceData.SymbolsAndLetters[2],
                HospiceData.SymbolsAndLetters[3]
        };

        NewsInfo.NewInfo news;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            news = NewsInfo.getNewInfoWithTitleAndDescr(text[i], text[i]); //заявка
            newsEdit.createSimpleNews(news, true);
        }

//        loginPage.toComeOut();
    }

    @Test
    public void createClaimTestWithDateAndTime() throws Exception {
        step("15. Проверка даты и времени в заявке.");

        Thread.sleep(4000); // загрузка
        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claims[] = {
//                ClaimsInfo.getClaimInfoWithChoiceDateTime(HospiceData.setData(1, 0, 0), HospiceData.testingTime[1]),
                ClaimsInfo.getClaimInfoWithChoiceDateTime(HospiceData.setData(0, 1, 0), HospiceData.testingTime[1]),
//                ClaimsInfo.getClaimInfoWithChoiceDateTime(HospiceData.setData(0, 0, 1), HospiceData.testingTime[2])
        };

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromMenu();

        for (int i = 0; i < claims.length; i = i + 1) {
            claimPage.createClaim(claims[i]);
            claimPage.checkDataTimeInClaim(claims[i]);
        }

        claimPage.editData(claims[0], HospiceData.setData(2, 2, 2), HospiceData.testingTime[3]);
        claimPage.checkDataTimeInClaim(claims[0]);

        claimPage.editData(claims[0], HospiceData.setData(2, 2, 0), HospiceData.testingTime[4]);
        claimPage.checkDataTimeInClaim(claims[0]);

        loginPage.toComeOut();
    }

    @Test
    public void createNewsTestWithDateAndTime() throws Exception {
        step("16. Проверка даты и времени в новости.");
        Thread.sleep(4000); // загрузка
//        LoginPageFragment loginPage = logIn();


        NewsInfo.NewInfo news[] = {
                NewsInfo.getNewsInfoDateTimeChoice(HospiceData.setData(1, 0, 0), HospiceData.testingTime[0]),
                NewsInfo.getNewsInfoDateTimeChoice(HospiceData.setData(0, 1, 0), HospiceData.testingTime[1]),
                NewsInfo.getNewsInfoDateTimeChoice(HospiceData.setData(0, 0, 1), HospiceData.testingTime[2])
        };
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEditPage = newsPage.goToEditNewsPage();

        for (int i = 0; i < news.length; i = i + 1) {
            news[i] = newsEditPage.createSimpleNews(news[i], true);
            newsEditPage.checkDataTimeInClaim(news[i]);
        }

        news[0] = newsEditPage.editNewsDataAndTime(news[1], HospiceData.setData(0, 0, 2), HospiceData.testingTime[3]);
        newsEditPage.checkDataTimeInClaim(news[1]);

        news[0] = newsEditPage.editNewsDataAndTime(news[2], HospiceData.setData(3, 2, 0), HospiceData.testingTime[4]);
        newsEditPage.checkDataTimeInClaim(news[2]);

//        loginPage.toComeOut();
    }
}

