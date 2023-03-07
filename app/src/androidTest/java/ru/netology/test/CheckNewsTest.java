package ru.netology.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.PerformException;
import androidx.test.rule.ActivityTestRule;

import net.datafaker.Faker;

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
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.activity.NewsEditPageFragment;
import ru.netology.activity.NewsFragment;
import ru.netology.activity.NewsPageFragment;
import ru.netology.data.HospiceData;
import ru.netology.data.LoginInfo;
import ru.netology.data.NewsInfo;

@RunWith(AllureAndroidJUnit4.class)
public class CheckNewsTest {

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
    @DisplayName("7.1 Создание/отмена создания/удаление/отмена удаления новости.")
    public void createAndDeleteNewsFromMainPageTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Salary.getTitle(), true); //заявка
        NewsFragment news = newsEdit.goToCreateNews();
        news.createSimpleNews(newsInfo, false); //отмена
        news = newsEdit.goToCreateNews();
        newsInfo = news.createSimpleNews(newsInfo, true); //создание
        newsEdit.checkNews(newsInfo);

        newsEdit.deleteNews(newsInfo, false); //отмена
        newsEdit.deleteNews(newsInfo, true);  //удаление
    }

    @Test
    @DisplayName("7.2 Создание новости с проверкой пустых полей.")
    public void createNewsWithCheckEmptyFieldsTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        NewsFragment news = newsEdit.goToCreateNews();
        newsInfo = news.checkEmptyFieldsWhenCreatingNews(newsInfo); //проверка пустых полей
        newsEdit.checkNews(newsInfo);
    }

    @Test
    @DisplayName("8. Отображение активной/неактивной новости на главное странице.")
    public void viewNewsInMainPageWithCheckActiveTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка

        NewsFragment news = newsEdit.goToCreateNews();
        newsInfo = news.createSimpleNews(newsInfo, true); //создание

        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo);

        mainPage.goToNewsPage();
        newsPage.goToEditNewsPage();
        newsEdit.toFoundNews(newsInfo);
        news = newsEdit.goToEditNews(newsInfo);
        news.changeActive(newsInfo, false);
        newsEdit.goToMainPage();
//TODO проверка на отсутствие на главной странице - в текущей версии не работает

        mainPage.goToNewsPage();
        newsPage.goToEditNewsPage();
        newsEdit.toFoundNews(newsInfo);
        news = newsEdit.goToEditNews(newsInfo);
        news.changeActive(newsInfo, true);
        newsEdit.goToMainPage();
        mainPage.checkNews(newsInfo);
    }

    @Test
    @DisplayName("8.2 Редактирование/отмена редактирования новости.")
    public void checkEditNewsTest() {

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsFragment news = newsEdit.goToCreateNews();
        newsInfo = news.createSimpleNews(newsInfo, true); //создание

        newsEdit.toFoundNews(newsInfo);
        news = newsEdit.goToEditNews(newsInfo);
        news.editNewsCategoryTitleAndDescrpt(
                newsInfo, HospiceData.newsCategory.Holiday.getTitle(), false); //отмена

        newsEdit.toFoundNews(newsInfo);
        news = newsEdit.goToEditNews(newsInfo);
        newsInfo = news.editNewsCategoryTitleAndDescrpt(
                newsInfo, HospiceData.newsCategory.Holiday.getTitle(), true);

        newsEdit.toFoundNews(newsInfo);
        news = newsEdit.goToEditNews(newsInfo);
        news.checkNews(newsInfo);
    }

    @Test
    @DisplayName("12. Проверка доступности символов при создании новости.")
    public void checkPrintAllSymbolsAndLettersInNewsTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        String[] text = {
                HospiceData.SymbolsAndLetters.EnglishAndSymbol40.getTitle() + faker.bothify("??#??"),
                HospiceData.SymbolsAndLetters.EnglishUpAndSymbol40.getTitle() + faker.bothify("??#??"),
                HospiceData.SymbolsAndLetters.Letters40.getTitle() + faker.bothify("??#????#??"),
                faker.bothify("?")
        };

        NewsInfo.NewInfo newInfo;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            newInfo = NewsInfo.getNewInfoWithTitleAndDescr(text[i], text[i]); //заявка
            NewsFragment newsFragment = newsEdit.goToCreateNews();
            newInfo = newsFragment.createSimpleNews(newInfo, true); //создание
        }
    }

    @Test
    @DisplayName("16. Проверка даты и времени в новости с проверкой фильтра по дате.")
    public void checkNewsWithDateAndTimeTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPage();
        NewsEditPageFragment newsEditPage = newsPage.goToEditNewsPage();

        String data1 = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        NewsInfo.NewInfo newsInfo = NewsInfo.getNewsInfoDateTimeChoice(data1,
                LocalTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("kk:mm")));

        NewsFragment newsFragment = newsEditPage.goToCreateNews();
        newsInfo = newsFragment.createSimpleNews(newsInfo, true);
        newsEditPage.setUpFilterWithData(data1); // фильтр по дате в редактировании новостей
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        newsFragment.checkNews(newsInfo); // проверяем даты в редактировании новости

        newsEditPage.setUpFilterWithData(data1);
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        String data2 = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        newsInfo = newsFragment.editNewsDataAndTime(newsInfo, data2,   //изменяем дату на более раннюю
                LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("kk:mm")));

        newsEditPage.setUpFilterWithData(data2);
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        newsFragment.checkNews(newsInfo); // проверяем дату в редактировании новости

        newsEditPage.goToNewsPage();
        newsPage.setUpDataFilter(data2); // фильтр по дате в новостях
        newsPage.toFoundNews(newsInfo);
        newsPage.checkNews(newsInfo); // проверяем даты в новостях

        newsEditPage = newsPage.goToEditNewsPage();
        newsEditPage.setUpFilterWithData(data2);
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        newsFragment.changeActive(newsInfo, false); //делаем новость неактивной

        newsEditPage.setUpFilterWithData(data2);
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        String data3 = LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        newsInfo = newsFragment.editNewsDataAndTime(newsInfo, data3, //изменяем дату на более позднюю
                LocalTime.now().minusMinutes(10).format(DateTimeFormatter.ofPattern("kk:mm")));

        newsEditPage.setUpFilterWithData(data3);
        newsEditPage.toFoundNews(newsInfo);
        newsFragment = newsEditPage.goToEditNews(newsInfo);
        newsFragment.checkNews(newsInfo); // проверяем даты в редактировании новости
    }

    @Test
    @DisplayName("9. Проверка фильтров новостей. Категории.")
    public void checkNewsFilterCategoryTest() {

        String category[] = {
                HospiceData.newsCategory.Announcement.getTitle(), //новость Объявление
                HospiceData.newsCategory.Birthday.getTitle(), //новость День рождения
                HospiceData.newsCategory.Salary.getTitle(), //новость Зарплата
                HospiceData.newsCategory.Union.getTitle(), //новость Профсоюз
                HospiceData.newsCategory.Holiday.getTitle(), //новость Праздник
                HospiceData.newsCategory.Massage.getTitle(), //новость Массаж
                HospiceData.newsCategory.Gratitude.getTitle(), //новость Благодарность
                HospiceData.newsCategory.Help.getTitle(), //новость Нужна помощь
                HospiceData.newsCategory.Announcement.getTitle() //нужно для последнего цикла
        };
        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();
        NewsFragment news = newsEdit.goToCreateNews();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(category[0], true); //заявка
        newsInfo = news.createSimpleNews(newsInfo, true);

        for (int i = 0; i < 8; i++) {
            newsPage = newsEdit.goToNewsPage();
            newsPage.setUpCategoryFilter(newsInfo.getCategory()); // устанавливаем фильтр в Новостях
            newsPage.toFoundNews(newsInfo);
            newsPage.checkNews(newsInfo);

            newsEdit = newsPage.goToEditNewsPage();
            // устанавливаем фильтр в редактировании новостей
            newsEdit.setUpFilter(newsInfo, true, true, true);
            newsEdit.toFoundNews(newsInfo);
            newsEdit.checkNews(newsInfo);

            news = newsEdit.goToEditNews(newsInfo); // меняем категорию
            newsInfo = news.editNewsCategoryTitleAndDescrpt(newsInfo, category[i + 1], true);
        }
    }

    @Test
    @DisplayName("9. Проверка фильтров новостей. Активность.")
    public void checkNewsFilterTest() {

        MainPageFragment mainPage = new MainPageFragment();
        NewsPageFragment newsPage = mainPage.goToNewsPageFromNewsBox();
        NewsEditPageFragment newsEdit = newsPage.goToEditNewsPage();

        NewsInfo.NewInfo newsInfo =
                NewsInfo.getNewInfo(HospiceData.newsCategory.Announcement.getTitle(), true); //заявка
        NewsFragment news = newsEdit.goToCreateNews();
        newsInfo = news.createSimpleNews(newsInfo, true); //создание

        newsEdit.setUpFilter(newsInfo, false, true, false);
        newsEdit.toFoundNews(newsInfo);
        newsEdit.checkNewsActive(newsInfo);

        news = newsEdit.goToEditNews(newsInfo);
        newsInfo = news.changeActive(newsInfo, false);
        newsEdit.setUpFilter(newsInfo, false, false, true);
        newsEdit.toFoundNews(newsInfo);
        newsEdit.checkNewsActive(newsInfo);
    }
}
