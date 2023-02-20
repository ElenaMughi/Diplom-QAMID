package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.WaitId;

public class NewsEditPageFragment {

    PrintText res = new PrintText();

    public void goToMainPage() {
        WaitId.waitId(R.id.main_menu_image_button, 5000);
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.main_swipe_refresh, 5000);
    }

    public NewsInfo.NewInfo createSimpleNews(NewsInfo.NewInfo newsInfo, boolean saveCancel) {
        onView(withId(R.id.add_news_image_view)).perform(click());

        WaitId.waitMyIdWithCheck(R.id.custom_app_bar_sub_title_text_view, 10000);
        NewsFragment news = new NewsFragment();
        return news.createSimpleNews(newsInfo, saveCancel);
    }

    public void checkEmptyFieldsWhenCreatingNews(NewsInfo.NewInfo newsInfo) {
        onView(withId(R.id.add_news_image_view)).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.checkEmptyFieldsWhenCreatingNews(newsInfo);
    }

    public void toFoundNews(NewsInfo.NewInfo newsInfo) {
        WaitId.waitId(R.id.news_list_recycler_view, 10000);
        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        WaitId.waitId(R.id.news_item_description_text_view, 15000);
    }

    public void setUpFilterActive(boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(withId(R.id.filter_news_active_material_check_box))
                .perform(CustomSetChecked.setChecked(active));
        onView(withId(R.id.filter_news_inactive_material_check_box))
                .perform(CustomSetChecked.setChecked(notActive));
        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void setUpFilterCategory(String category) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(allOf(withId(R.id.text_input_end_icon), withContentDescription("Show dropdown menu")))
                .perform(click());
        res.getItemFromList(category);
        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void setUpFilterCategoryAndActive(String category, boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(allOf(withId(R.id.text_input_end_icon), withContentDescription("Show dropdown menu")))
                .perform(click());
        res.getItemFromList(category);
        onView(withId(R.id.filter_news_active_material_check_box))
                .perform(CustomSetChecked.setChecked(active));
        onView(withId(R.id.filter_news_inactive_material_check_box))
                .perform(CustomSetChecked.setChecked(notActive));
        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 15000);
    }

    public void deleteNews(NewsInfo.NewInfo newsInfo, boolean okNo) {
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

    public NewsInfo.NewInfo changeActive(NewsInfo.NewInfo newsInfo, boolean getActive) {
        setUpFilterCategory(newsInfo.getCategory());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        return news.changeActive(newsInfo, getActive);
    }

    public NewsInfo.NewInfo editNews(NewsInfo.NewInfo newsInfo, boolean saveCancel) {
        setUpFilterCategory(newsInfo.getCategory());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        return news.editNews(newsInfo, saveCancel);
    }

    public void checkNewsActive(NewsInfo.NewInfo newsInfo, boolean active, boolean notActive) {
        setUpFilterActive(active, notActive);
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.checkNews(newsInfo);
    }

    public void checkNewsCategoryAndActive(NewsInfo.NewInfo newsInfo, boolean active, boolean notActive) {
        setUpFilterCategoryAndActive(newsInfo.getCategory(), active, notActive);
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.checkNews(newsInfo);
    }

    public void checkDataTimeInClaim(NewsInfo.NewInfo newsInfo) {
        setUpFilterCategory(newsInfo.getCategory());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(newsInfo.getTitle())))))))
                .perform(click());
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.checkNews(newsInfo);
    }

    public NewsInfo.NewInfo editNewsDataAndTime(NewsInfo.NewInfo news, String data, String time) {
        setUpFilterCategory(news.getCategory());
        toFoundNews(news);
        onView(allOf(withId(R.id.edit_news_item_image_view),
                withParent(withParent((hasDescendant(withText(news.getTitle())))))))
                .perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        news.setDateNews(data);
        news.setTimeNews(time);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.editNewsDataAndTime(news);
        return news;
    }
}

