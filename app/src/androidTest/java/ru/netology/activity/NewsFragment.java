package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import static io.qameta.allure.kotlin.Allure.step;

import androidx.test.espresso.ViewInteraction;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.SetDataTime;
import ru.netology.resourses.WaitId;

public class NewsFragment {

    PrintText res = new PrintText();
    static Faker faker = new Faker();

    private void clickSaveCancelButton(boolean saveCancel) {
        if (saveCancel) {
            onView(withId(R.id.save_button)).perform(click());
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
            onView(withText("CANCEL")).perform(click());
            onView(withText("CANCEL")).perform(click());
            onView(withText("OK")).perform(click());
        }
    }

    public NewsInfo.NewInfo createSimpleNews(NewsInfo.NewInfo newsInfo, boolean saveCancel) {
        step("Заполнение полей и создание новости.");

        onView(allOf(withClassName(endsWith("ImageButton")),
                withParent(withParent(withParent(withParent(withId(
                        R.id.news_item_category_text_input_layout)))))))
                .perform(click());
        res.getItemFromList(newsInfo.getCategory());

        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, newsInfo.getTitle()); //Заголовок

        SetDataTime.setDate(R.id.news_item_publish_date_text_input_edit_text, newsInfo.getDateNews());
        newsInfo.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        SetDataTime.setTime(R.id.news_item_publish_time_text_input_edit_text, newsInfo.getTimeNews());

        res.typingText(R.id.news_item_description_text_input_edit_text, newsInfo.getDescription()); // Описание

        clickSaveCancelButton(saveCancel); //сохранение/отмена
        WaitId.waitId(R.id.news_list_recycler_view, 10000);

        return newsInfo;
    }

    public NewsInfo.NewInfo checkEmptyFieldsWhenCreatingNews(NewsInfo.NewInfo newsInfo) {
        step("Проверка заполнения полей в создании новости.");

        clickSaveCancelButton(true); //сохранение/отмена
        // TODO проверка toast-сообщения - в текущей версии не работает

        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        res.getItemFromList(newsInfo.getCategory());
        onView(allOf(withId(R.id.news_item_title_text_input_edit_text), withText(newsInfo.getCategory())))
                .check(matches(isDisplayed()));              //Проверяем что категория перенеслась в заголовок
        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, "");         //Чистим заголовок
        clickSaveCancelButton(true); //сохранение/отмена
        // проверка toast-сообщения - не реализовано

        res.typingText(R.id.news_item_title_text_input_edit_text, newsInfo.getTitle()); //Заголовок
        clickSaveCancelButton(true); //сохранение/отмена
        // проверка toast-сообщения - не реализовано

        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click()); // Дата
        ViewInteraction date = onView(withId(android.R.id.button1));
        date.perform(click());
        clickSaveCancelButton(true); //сохранение/отмена
        // проверка toast-сообщения - не реализовано

        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click()); // Время
        ViewInteraction time = onView(withId(android.R.id.button1));
        time.perform(click());
        clickSaveCancelButton(true); //сохранение/отмена
        // проверка toast-сообщения - не реализовано

        res.typingText(R.id.news_item_description_text_input_edit_text, newsInfo.getDescription()); // Описание
        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, "");         //Чистим заголовок
        clickSaveCancelButton(true); //сохранение/отмена
        // проверка toast-сообщения - не реализовано

        res.typingText(R.id.news_item_title_text_input_edit_text, newsInfo.getTitle()); //Заголовок
        clickSaveCancelButton(true); //выходим без сохранения

        newsInfo.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        WaitId.waitId(R.id.news_list_recycler_view, 10000);

        return newsInfo;
    }

    public NewsInfo.NewInfo changeActive(NewsInfo.NewInfo newsInfo, boolean getActive) {
        step("Изменение активности новости.");
        if (getActive) {
            onView(withId(R.id.switcher)).perform(click());
        } else {
            onView(withId(R.id.switcher)).perform(click());
        }
        newsInfo.setActive(getActive); //сохраняем активность
        clickSaveCancelButton(true); //сохранение/отмена
        return newsInfo;
    }

    public NewsInfo.NewInfo editNewsCategoryTitleAndDescrpt(
            NewsInfo.NewInfo newsInfo, String category, boolean saveCancel) {
        step("Изменение категории, названия и/или описания новости.");

        WaitId.waitId(R.id.news_item_category_text_auto_complete_text_view, 5000);

        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(clearText());
        onView(allOf(withId(R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu")))
                .perform(click());
        res.getItemFromList(category);

        String title = faker.bothify("Bunny???#??#??#??#");
        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, title); //Заголовок

        String descript = faker.bothify("Fox???#??#??#??#");
        res.typingTextWithClear(R.id.news_item_description_text_input_edit_text, descript); // Описание

        if (saveCancel) {
            newsInfo.setCategory(category);
            newsInfo.setTitle(title);
            newsInfo.setDescription(descript);
        }
        clickSaveCancelButton(saveCancel); //сохранение/отмена
        WaitId.waitId(R.id.trademark_image_view, 5000);
        return newsInfo;
    }

    public NewsInfo.NewInfo editNewsDataAndTime(NewsInfo.NewInfo news, String data, String time) {
        step("Изменение даты и времени новости.");
        SetDataTime.setDate(R.id.news_item_publish_date_text_input_edit_text, data);
        SetDataTime.setTime(R.id.news_item_publish_time_text_input_edit_text, time);
        news.setDateNews(data);
        news.setTimeNews(time);
        clickSaveCancelButton(true);
        return news;
    }

    public void checkNews(NewsInfo.NewInfo news) {
        step("Проверка полей в просмотре деталей новости.");
        onView(allOf(withId(R.id.news_item_category_text_auto_complete_text_view), withText(news.getCategory())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.news_item_title_text_input_edit_text), withText(news.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.news_item_publish_date_text_input_edit_text), withText(news.getDateNews())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.news_item_publish_time_text_input_edit_text), withText(news.getTimeNews())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.news_item_description_text_input_edit_text), withText(news.getDescription())))
                .check(matches(isDisplayed()));

        clickSaveCancelButton(false);
    }
}
