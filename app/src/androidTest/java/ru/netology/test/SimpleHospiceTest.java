package ru.netology.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static io.qameta.allure.kotlin.Allure.step;
import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.PerformException;
import androidx.test.rule.ActivityTestRule;

import net.datafaker.Faker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
import ru.netology.data.LoginInfo;
import ru.netology.data.NewsInfo;

@RunWith(AllureAndroidJUnit4.class)
public class SimpleHospiceTest {

    Faker faker = new Faker();

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);

    private LoginPageFragment logIn() {
        LoginPageFragment loginPage = new LoginPageFragment();
        LoginInfo.LogInfo loginInfo = LoginInfo.getLogInfo();
        try {
            onView(isRoot()).perform(waitId(R.id.enter_button, 10000));
            loginPage.toComeIn(loginInfo);
        } catch (PerformException e) {
            System.out.println("не найдено" + R.id.enter_button);
        }
        return loginPage;
    }

    @Test
    public void logInOutTest() {
        step("1. Авторизация в приложении");

        LoginPageFragment loginPageFragment = logIn();
        loginPageFragment.toComeOut();

        LoginInfo.LogInfo loginInfo = LoginInfo.getWrongLogInfo();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfoWithLoginEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfoWithPasswordEmpty();
        loginPageFragment.toWrongComeIn(loginInfo);

        loginInfo = LoginInfo.getLogInfo();
        loginPageFragment.toComeIn(loginInfo);

        loginPageFragment.toComeOut();
    }

    @Test
    public void createClaimFromMainPageWithCheckEmptyFieldsTest() {
        step("2. Создание заявки и проверка заполнения полей(без исполнителя)");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();
        ClaimFragment claim = claimsPage.callCreateClaim(); //вызов создания заявки
        claimInfo = claim.checkingEmptyFieldsWhenCreatingClaim(claimInfo); // проверка пустых полей

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
        claimsPage.goToMainPage();

        loginPage.toComeOut();
    }

    @Test
    public void createClaimFromMainPageWithCheckCancelTest() {
        step("3. Проверка отмены создания заявки");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claim.cancellationCreateClaim(claimInfo, false); //не сохранять при подтверждении

        claim = claimsPage.callCreateClaim();
        claimInfo = claim.cancellationCreateClaim(claimInfo, true); //сохранить при подтверждении

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
        claimsPage.goToMainPage();

        loginPage.toComeOut();
    }

    @Test
    public void createClaimFromClaimsPageWithCheckExecutorTest() {
        step("4. Создание заявки с Исполнителем и проверкой статуса.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();

        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

// TODO: 22.01.2023 //Нельзя ввести несуществующего исполнителя. Проверка снята.

//        ClaimsInfo.ClaimInfo claimInfo2 =
//                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.PETROV.getTitle()); //заявка
//        claim = claimsPage.callCreateClaim();
//        claimInfo2 = claim.createClaim(claimInfo2);
//        claim = claimsPage.toFoundClaimWithFilter(claimInfo2);
//        claim.checkClaimFields(claimInfo2);

        ClaimsInfo.ClaimInfo claimInfo3 =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()); //заявка
        claim = claimsPage.callCreateClaim();
        claimInfo3 = claim.createClaim(claimInfo3);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo3);
        claim.checkClaimFields(claimInfo3);

        loginPage.toComeOut();
    }

    @Test
    public void createAndEditCommentsOnOpenClaimTest() {
        step("5.1 Добавление/редактирование (с отменой) комментариев к заявке в статусе Открыто.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();

        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        String comment = faker.bothify("Comment #??#??#??#??#???");
        claimInfo = claim.writeComment(claimInfo, comment, false); // не сохранять коммент
        claimInfo = claim.writeComment(claimInfo, comment, true); // сохранять коммент
        claim.checkComment(claimInfo, comment);

        String comment2 = faker.bothify("Comment #??#??#??#??#???");
        claim.editComment(comment, comment2, false); // не сохранять исправления
        claim.checkComment(claimInfo, comment);
        claim.editComment(comment, comment2, true); // сохранять исправления
        claim.checkComment(claimInfo, comment2);

        claimInfo = claim.writeComment(claimInfo, comment, true); // сохранять коммент
        claim.checkComment(claimInfo, comment);

        loginPage.toComeOut();
    }

    @Test
    public void createAndEditCommentsOnWorkClaimTest() {
        step("5.2 Добавление/редактирование нескольких комментариев к заявке в статусе в Работе.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        String comment = faker.bothify("Comment #??#??#??#??#???");
        String comment2 = faker.bothify("Comment2 #??#??#??#??#???");

        claimInfo = claim.writeComment(claimInfo, comment, true);
        claim.editComment(comment, comment2, true); // не сохранять исправления
        claim.checkComment(claimInfo, comment2);
        claimInfo = claim.writeComment(claimInfo, comment, true);
        claim.checkComment(claimInfo, comment);

        String comment3 = faker.bothify("Comment2 #??#??#??#??#???");
        claimInfo = claim.writeComment(claimInfo, comment3, true);
        claim.checkComment(claimInfo, comment3);

        loginPage.toComeOut();
    }

    @Test
    public void editOpenStatusClaimTest() {
        step("6.1. Редактирование названия и описания заявки в статусе Открыто.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.editTitleAndDescriptionInClaim(claimInfo, false); //не сохраняем
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.editTitleAndDescriptionInClaim(claimInfo, true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        loginPage.toComeOut();
    }

    @Test
    public void editExecutorInOpenStatusClaimTest() {
        step("6.2. Изменение Исполнителя в заявке в статусе Открыто.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.changeExecutor(claimInfo, claimInfo.getAuthor()); //сохраняем новый статус

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo); // проверка в том числе статуса

        loginPage.toComeOut();
    }

    @Test
    public void changeClaimStatusFromOpenTest() {
        step("6.3. Прохождение по статусам заявки начиная со статуса Открыто.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка без исполнителя

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.WORK.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.toChangeStatusClaim // статус без изменений
                (claimInfo, HospiceData.claimsStatus.EXEC.getTitle(), false);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.EXEC.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        loginPage.toComeOut();
    }

    @Test
    public void changeClaimStatusFromWorkTest() {
        step("6.4. Прохождение по статусам отмены заявки начиная со статуса В работе.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle());

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.OPEN.getTitle(), false);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.OPEN.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.CANCEL.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        loginPage.toComeOut();
    }

    @Test
    public void createAndDeleteNewsFromMainPageTest() {
        step("7. Создание/отмена создания/удаление новости с проверкой пустых полей.");

        LoginPageFragment loginPage = logIn();

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        newsEdit.checkEmptyFieldsWhenCreatingNews(newsInfo); //проверка пустых полей

        NewsInfo.NewInfo newsInfo2 =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        newsEdit.createSimpleNews(newsInfo2, false); //отмена
        newsEdit.createSimpleNews(newsInfo2, true);  //создание
        newsEdit.checkEmptyFieldsWhenCreatingNews(newsInfo2);
        newsEdit.deleteNews(newsInfo2, false);
        newsEdit.deleteNews(newsInfo2, true);

        loginPage.toComeOut();
    }

    @Test
    public void viewNewsInMainPageWithEditAndActiveTest() {
        step("8. Просмотр текущих новостей на главной странице и редактирование. Активность новости.");

        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        newsEdit.createSimpleNews(newsInfo, true);
        newsEdit.goToMainPage();
        // TODO На главной странице может не быть, если за текущий день больше 3 новостей
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
        newsInfo = newsEdit.editNews(newsInfo, false);
        newsInfo = newsEdit.editNews(newsInfo, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo, true);

        loginPage.toComeOut();
    }

    @Test
    public void checkNewsFilterTest() {
        step("9. Проверка фильтров новостей.");

        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo[] news = {
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true), //новость Объявление
                NewsInfo.getNewInfo(HospiceData.newsCategory.Birthday.getTitle(), true), //новость День рождения
                NewsInfo.getNewInfo(HospiceData.newsCategory.Salary.getTitle(), true), //новость Зарплата
                NewsInfo.getNewInfo(HospiceData.newsCategory.Union.getTitle(), true), //новость Профсоюз
                NewsInfo.getNewInfo(HospiceData.newsCategory.Holiday.getTitle(), true), //новость Праздник
                NewsInfo.getNewInfo(HospiceData.newsCategory.Massage.getTitle(), true), //новость Массаж
                NewsInfo.getNewInfo(HospiceData.newsCategory.Gratitude.getTitle(), true), //новость Благодарность
                NewsInfo.getNewInfo(HospiceData.newsCategory.Help.getTitle(), true) //новость Нужна помощь
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
            newsPage.checkNewsWithCategoryFilter(newsInfo);
        }

        newsPage.goToEditNewsPage();
        for (NewsInfo.NewInfo newsInfo : news) { //проверяем фильтр по категориям в редактировании новостей
            newsEdit.checkNewsCategoryAndActive(newsInfo, true, true);
        }

        newsEdit.changeActive(news[0], false);
        newsEdit.checkNewsActive(news[0], false, true);
        newsEdit.checkNewsActive(news[1], true, false);

        newsEdit.goToMainPage();
        mainPage.goToNewsPage();
        newsPage.goToEditNewsPage();
        newsEdit.checkNewsCategoryAndActive(news[0], false, true);
        newsEdit.checkNewsCategoryAndActive(news[1], true, false);

        loginPage.toComeOut();
    }

    @Test
    public void checkClaimsFilterTest() {
        step("10. Проверка фильтров заявок.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claims[] = {
                ClaimsInfo.getClaimInfoWithOutFIO(), //заявка1 Open
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()), //заявка2 Work
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()), //заявка3 Exec
                ClaimsInfo.getClaimInfoWithOutFIO() //заявка4 Cancel
        };

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPage();

        for (ClaimsInfo.ClaimInfo cl : claims) {
            claimPage.createClaim(cl);  // создаем все заявки и присваиваем им статусы далее
        }
        claimPage.changeStatusOfClaim(claims[2], HospiceData.claimsStatus.EXEC.getTitle(), true);
        claimPage.changeStatusOfClaim(claims[3], HospiceData.claimsStatus.CANCEL.getTitle(), true);

        for (int i = 0; i < 4; i = i + 1) { //фильтр со всеми галочками
            claimPage.checkClaimWithWholeFilter(claims[i]);
        }

        for (int i = 0; i < 4; i = i + 1) { //фильтр с одной галочкой
            claimPage.checkClaimWithFilter(claims[i]);
        }

        boolean filter1[] = {true, true, false, false};
        claimPage.checkClaimWithMultipleFiler(claims, filter1);
        boolean filter2[] = {true, false, true, false};
        claimPage.checkClaimWithMultipleFiler(claims, filter2);
        boolean filter3[] = {true, false, true, true};
        claimPage.checkClaimWithMultipleFiler(claims, filter3);

        loginPage.toComeOut();
    }


    @Test
    public void checkPrintAllSymbolsAndLettersInClaimsTest() {
        step("11. Проверка доступности символов при создании заявки.");

        LoginPageFragment loginPage = logIn();

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromClaimBox();

        String[] text = {
                HospiceData.SymbolsAndLetters.EnglishAndNumAndSymbol.getTitle(),
                HospiceData.SymbolsAndLetters.EnglishUpAndSymbol.getTitle(),
                HospiceData.SymbolsAndLetters.Letters50.getTitle(),
                HospiceData.SymbolsAndLetters.OneSymbol.getTitle()
        };

        ClaimsInfo.ClaimInfo claim;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            claim = ClaimsInfo.getClaimInfoWithChoiceTitleAndDiscr(text[i], text[i]); //заявка
            claimPage.createClaim(claim);
//            claimPage.addCommentToClaim(claim, true);
        }
        loginPage.toComeOut();
    }

    @Test
    public void checkPrintAllSymbolsAndLettersInNewsTest() {
        step("12. Проверка доступности символов при создании новости.");

        LoginPageFragment loginPage = logIn();

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        String[] text = {
                HospiceData.SymbolsAndLetters.EnglishAndNumAndSymbol.getTitle(),
                HospiceData.SymbolsAndLetters.EnglishUpAndSymbol.getTitle(),
                HospiceData.SymbolsAndLetters.Letters50.getTitle(),
                HospiceData.SymbolsAndLetters.OneSymbol.getTitle()
        };

        NewsInfo.NewInfo news;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            news = NewsInfo.getNewInfoWithTitleAndDescr(text[i], text[i]); //заявка
            newsEdit.createSimpleNews(news, true);
        }

        loginPage.toComeOut();
    }

    @Test
    public void createClaimTestWithDateAndTime() {
        step("15. Проверка даты и времени в заявке.");

        LoginPageFragment loginPage = logIn();

        ClaimsInfo.ClaimInfo claims[] = {
                ClaimsInfo.getClaimInfoWithChoiceDateTime(
                        LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("hh:mm"))),
                ClaimsInfo.getClaimInfoWithChoiceDateTime(
                        LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().plusMinutes(1).format(DateTimeFormatter.ofPattern("hh:mm"))),
                ClaimsInfo.getClaimInfoWithChoiceDateTime(
                        LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh:mm")))
        };

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPage();

        for (int i = 0; i < claims.length; i = i + 1) {
            claimPage.createClaim(claims[i]);
            claimPage.checkClaimWithFilter(claims[i]);
        }

        claimPage.editData(claims[0],
                LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh:mm")));
        claimPage.checkClaimWithFilter(claims[0]);

        claimPage.editData(claims[0],
                LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().minusMinutes(10).format(DateTimeFormatter.ofPattern("hh:mm")));
        claimPage.checkClaimWithFilter(claims[0]);

        loginPage.toComeOut();
    }

    @Test
    public void checkNewsWithDateAndTimeTest() {
        step("16. Проверка даты и времени в новости.");

        LoginPageFragment loginPage = logIn();

        NewsInfo.NewInfo news[] = {
                NewsInfo.getNewsInfoDateTimeChoice(LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("hh:mm"))),
                NewsInfo.getNewsInfoDateTimeChoice(
                        LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().plusMinutes(1).format(DateTimeFormatter.ofPattern("hh:mm"))),
                NewsInfo.getNewsInfoDateTimeChoice(
                        LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh:mm")))
        };
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEditPage = newsPage.goToEditNewsPage();

        for (int i = 0; i < news.length; i = i + 1) {
            news[i] = newsEditPage.createSimpleNews(news[i], true);
            newsEditPage.checkDataTimeInClaim(news[i]);
        }

        news[0] = newsEditPage.editNewsDataAndTime(news[1],
                LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh:mm")));
        newsEditPage.checkDataTimeInClaim(news[1]);

        news[0] = newsEditPage.editNewsDataAndTime(news[2],
                LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().minusMinutes(10).format(DateTimeFormatter.ofPattern("hh:mm")));
        newsEditPage.checkDataTimeInClaim(news[2]);

        loginPage.toComeOut();
    }

    //    {
//      2. Создание заявки из главной страницы(все варианты)
//    }
}

