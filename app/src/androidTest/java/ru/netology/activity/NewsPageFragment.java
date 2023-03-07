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
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static io.qameta.allure.kotlin.Allure.step;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.SetDataTime;
import ru.netology.resourses.WaitId;

public class NewsPageFragment {

    PrintText res = new PrintText();

    public void goToMainPage() {
        step("Переход на главную страницу.");
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.main_swipe_refresh, 3000);
    }

    public NewsEditPageFragment goToEditNewsPage() {
        step("Переход на страницу редактирования новостей.");
        onView(withId(R.id.edit_news_material_button)).perform(click());
        onView(withId(R.id.news_control_panel_swipe_to_refresh))
                .check(matches(isDisplayed()));
        return new NewsEditPageFragment();
    }

    public void setUpCategoryFilter(String category) {
        step("Установка фильтра.");
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu")))
                .perform(click());
        res.getItemFromList(category);
        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void toFoundNews(NewsInfo.NewInfo newsInfo) {
        step("Поиск элемента.");
        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        WaitId.waitId(R.id.news_item_description_text_view, 10000);
    }

    public void setUpDataFilter(String data) {
        step("Установка фильтра по дате.");
        onView(withId(R.id.filter_news_material_button)).perform(click());

        SetDataTime.setDate(R.id.news_item_publish_date_start_text_input_edit_text, data);
        SetDataTime.setDate(R.id.news_item_publish_date_end_text_input_edit_text, data);

        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void checkNews(NewsInfo.NewInfo newsInfo) {
        step("Проверка новости на странице новостей.");
        onView(allOf(withId(R.id.news_item_title_text_view),
                hasSibling(withText(newsInfo.getDescription()))))
                .check(matches(withText(newsInfo.getTitle())));

        onView(allOf(withId(R.id.news_item_description_text_view),
                hasSibling(withText(newsInfo.getTitle()))))
                .check(matches(withText(newsInfo.getDescription())));

        onView(allOf(withId(R.id.news_item_date_text_view),
                hasSibling(withText(newsInfo.getDescription()))))
                .check(matches(withText(newsInfo.getDateNews())));
    }
}
