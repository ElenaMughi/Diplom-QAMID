package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static io.qameta.allure.kotlin.Allure.step;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.SetDataTime;
import ru.netology.resourses.WaitId;

public class NewsEditPageFragment {

    PrintText res = new PrintText();

    public void goToMainPage() {
        step("Переход на главную страницу.");
        WaitId.waitId(R.id.main_menu_image_button, 5000);
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.main_swipe_refresh, 5000);
    }

    public NewsPageFragment goToNewsPage() {
        step("Переход на страницу новостей.");
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("News");
        onView(withId(R.id.news_list_swipe_refresh))
                .check(matches(isDisplayed()));
        return new NewsPageFragment();
    }

    public void toFoundNews(NewsInfo.NewInfo newsInfo) {
        step("Поиск элемента на странице редактирования новостей.");
        WaitId.waitId(R.id.news_list_recycler_view, 10000);
        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        WaitId.waitId(R.id.news_item_description_text_view, 15000);
    }

    public void setUpFilter(NewsInfo.NewInfo newsInfo,
                            boolean category, boolean active, boolean notActive) {
        step("Установка фильтра на странице редактирования новостей.");

        onView(withId(R.id.filter_news_material_button)).perform(click());
        if (category) {
            onView(allOf(withId(R.id.text_input_end_icon),
                    withContentDescription("Show dropdown menu")))
                    .perform(click());
            res.getItemFromList(newsInfo.getCategory());
        }
        onView(withId(R.id.filter_news_active_material_check_box))
                .perform(CustomSetChecked.setChecked(active));
        onView(withId(R.id.filter_news_inactive_material_check_box))
                .perform(CustomSetChecked.setChecked(notActive));

        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void setUpFilterWithData(String data) {
        step("Установка фильтра по дате на странице редактирования новостей.");

        onView(withId(R.id.filter_news_material_button)).perform(click());

        SetDataTime.setDate(R.id.news_item_publish_date_start_text_input_edit_text, data);
        SetDataTime.setDate(R.id.news_item_publish_date_end_text_input_edit_text, data);

        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public NewsFragment goToCreateNews() {
        step("Открытие окна создания новости.");
        onView(withId(R.id.add_news_image_view)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.custom_app_bar_sub_title_text_view, 15000);
        return new NewsFragment();
    }

    public NewsFragment goToEditNews(NewsInfo.NewInfo newsInfo) {
        step("Открытие окна редактирования новости.");
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        WaitId.waitMyIdWithCheck(R.id.custom_app_bar_sub_title_text_view, 15000);
        return new NewsFragment();
    }

    public void deleteNews(NewsInfo.NewInfo newsInfo, boolean okNo) {
        step("Удаление новости.");
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.delete_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        if (okNo) {
            onView(withText("OK")).perform(click());
        } else {
            onView(withText("CANCEL")).perform(click());
        }
    }

    public void checkNewsActive(NewsInfo.NewInfo newsInfo) {
        step("Проверка активности новости.");
        if (newsInfo.isActive()) {
            onView(allOf(withId(R.id.news_item_published_text_view),
                    hasSibling(withText(newsInfo.getTitle()))))
                    .check(matches(withText("ACTIVE")));
        } else {
            onView(allOf(withId(R.id.news_item_published_text_view),
                    hasSibling(withText(newsInfo.getTitle()))))
                    .check(matches(withText("NOT ACTIVE")));
        }
    }

    public void checkNews(NewsInfo.NewInfo news) {
        step("Проверка новости на странице редактирования новости.");
        toFoundNews(news);

        onView(allOf(withId(R.id.news_item_title_text_view), withText(news.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.news_item_publication_date_text_view),
                hasSibling(withText(news.getDescription()))))
                .check(matches(withText(news.getDateNews())));

        onView(allOf(withId(R.id.news_item_create_date_text_view),
                hasSibling(withText(news.getDescription()))))
                .check(matches(withText(news.getCreationDate())));

        onView(allOf(withId(R.id.news_item_author_name_text_view),
                hasSibling(withText(news.getDescription()))))
                .check(matches(withText(news.getAuthor())));

        onView(allOf(withId(R.id.news_item_description_text_view),
                hasSibling(withText(news.getTitle()))))
                .check(matches(withText(news.getDescription())));
    }

}

