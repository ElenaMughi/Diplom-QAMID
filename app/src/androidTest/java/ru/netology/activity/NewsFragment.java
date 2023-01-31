package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.ForAllFunk;

public class NewsFragment {

    ForAllFunk res = new ForAllFunk();

    private void clickSaveCancelButton(boolean saveCancel) throws Exception {
        if (saveCancel) {
            onView(withId(R.id.save_button)).perform(click());
            Thread.sleep(2000);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
            Thread.sleep(2000);
            onView(withText("CANCEL")).perform(click());
            Thread.sleep(2000);
            onView(withText("CANCEL")).perform(click());
            Thread.sleep(2000);
            onView(withText("OK")).perform(click());
        }
    }

    public void createSimpleNews(HospiceInfo.NewsInfo newsInfo, boolean saveCancel) throws Exception {

//                onView(withId(R.id.news_item_category_text_auto_complete_text_view)); //Категория
        onView(
                allOf(
                        withClassName(endsWith("ImageButton")),
                        withParent(withParent(withParent(withParent(withId(R.id.news_item_category_text_input_layout)))))
                )
        ).perform(click());
//        onView(withId(R.id.text_input_end_icon)).perform(click());
        res.getItemFromList(newsInfo.getCategory());

        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, newsInfo.getTitle()); //Заголовок

        // TODO выбор даты-времени не реализован
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click()); // Дата
        ViewInteraction date = onView(withId(android.R.id.button1));
        date.perform(click());

        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click()); // Время
        ViewInteraction time = onView(withId(android.R.id.button1));
        time.perform(click());

        res.typingText(R.id.news_item_description_text_input_edit_text, newsInfo.getDescription()); // Описание

        clickSaveCancelButton(saveCancel); //сохранение/отмена

        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
    }

    public void checkFieldsFromNews(HospiceInfo.NewsInfo newsInfo) throws Exception {

        clickSaveCancelButton(true); //сохранение/отмена
        // TODO проверка toast-сообщения - не реализовано

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

        clickSaveCancelButton(false); //сохранение/отмена

    }

    public void changeActive(boolean getActive) throws Exception {
        if (getActive) {
            onView(allOf(withId(R.id.switcher), withText("Not active")))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.switcher)).perform(click());
            onView(allOf(withId(R.id.switcher), withText("Active")))
                    .check(matches(isDisplayed()));
        } else {
            onView(allOf(withId(R.id.switcher), withText("Active")))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.switcher)).perform(click());
            onView(allOf(withId(R.id.switcher), withText("Not active")))
                    .check(matches(isDisplayed()));
        }
        clickSaveCancelButton(true); //сохранение/отмена
    }

    public void editNews(HospiceInfo.NewsInfo newsInfo, HospiceInfo.NewsInfo newsInfo2, boolean saveCancel) throws Exception {

        onView(allOf(withId(R.id.news_item_category_text_auto_complete_text_view), withText(newsInfo.getCategory())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(clearText());
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click()); //Категория
        res.getItemFromList(newsInfo2.getCategory());

        onView(allOf(withId(R.id.news_item_title_text_input_edit_text), withText(newsInfo.getTitle())))
                .check(matches(isDisplayed()));
        res.typingTextWithClear(R.id.news_item_title_text_input_edit_text, newsInfo2.getTitle()); //Заголовок

        onView(allOf(withId(R.id.news_item_description_text_input_edit_text), withText(newsInfo.getDescription())))
                .check(matches(isDisplayed()));
        res.typingTextWithClear(R.id.news_item_description_text_input_edit_text, newsInfo2.getDescription()); // Описание

        clickSaveCancelButton(saveCancel); //сохранение/отмена

        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
    }
}
