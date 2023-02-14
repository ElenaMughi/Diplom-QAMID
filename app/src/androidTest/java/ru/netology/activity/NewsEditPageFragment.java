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

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.ClaimsInfo;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.EspressoIdlingResources;
import ru.netology.resourses.PrintText;

public class NewsEditPageFragment {

    PrintText res = new PrintText();

    public NewsInfo.NewInfo createSimpleNews(NewsInfo.NewInfo newsInfo, boolean saveCancel) {
        onView(withId(R.id.add_news_image_view)).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        return news.createSimpleNews(newsInfo, saveCancel);
    }

    public void checkFieldsFromNews(NewsInfo.NewInfo newsInfo) {
        onView(withId(R.id.add_news_image_view)).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.checkFieldsFromNews(newsInfo);
    }

    public void toFoundNews(NewsInfo.NewInfo newsInfo) {
        EspressoIdlingResources.increment();
        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        EspressoIdlingResources.decrement();
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
    }

    public void deleteNews(NewsInfo.NewInfo newsInfo, boolean okNo) {
        toFoundNews(newsInfo);
        onView(
                allOf(
                        withId(R.id.delete_news_item_image_view),
                        withParent(withParent((hasDescendant(withText(newsInfo.getTitle()))))))
        ).perform(click());
        if (okNo) {
            onView(withText("OK")).perform(click());
        } else {
            onView(withText("CANCEL")).perform(click());
        }
    }

    public void goToMainPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        onView(withId(R.id.main_swipe_refresh))
                .check(matches(isDisplayed()));
    }

    public void changeActive(NewsInfo.NewInfo newsInfo, boolean getActive) {
        toFoundNews(newsInfo);
        onView(
                allOf(
                        withId(R.id.edit_news_item_image_view),
                        withParent(withParent((hasDescendant(withText(newsInfo.getTitle()))))))
        ).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.changeActive(getActive);

    }

    public void editNews(NewsInfo.NewInfo newsInfo, NewsInfo.NewInfo newsInfo2, boolean saveCancel) {
        toFoundNews(newsInfo);
        onView(
                allOf(
                        withId(R.id.edit_news_item_image_view),
                        withParent(withParent((hasDescendant(withText(newsInfo.getTitle()))))))
        ).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.editNews(newsInfo, newsInfo2, saveCancel);
    }

    public void checkNewsActive(NewsInfo.NewInfo newsInfo, boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        setChekBox(active, notActive);
        onView(withId(R.id.filter_button)).perform(click());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.news_item_title_text_view),
                withText(newsInfo.getTitle()))
        ).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.news_item_description_text_view),
                withText(newsInfo.getDescription()))
        ).check(matches(isDisplayed()));
    }

    public void setChekBox(boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_active_material_check_box))
                .perform(CustomSetChecked.setChecked(active));
        onView(withId(R.id.filter_news_inactive_material_check_box))
                .perform(CustomSetChecked.setChecked(notActive));
    }

    public void setUpFilterCategory(String category, boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(
                allOf(
                        withId(R.id.text_input_end_icon),
                        withContentDescription("Show dropdown menu")
                )
        ).perform(click());
        res.getItemFromList(category);
        setChekBox(active, notActive);
        onView(withId(R.id.filter_button)).perform(click());
    }

    public void checkNewsCategoryAndActive(NewsInfo.NewInfo newsInfo, boolean active, boolean notActive) {
        setUpFilterCategory(newsInfo.getCategory(), active, notActive);
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.news_item_title_text_view),
                withText(newsInfo.getTitle()))
        ).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.news_item_description_text_view),
                withText(newsInfo.getDescription()))
        ).check(matches(isDisplayed()));
    }

    public void checkDataTimeInClaim(NewsInfo.NewInfo news) {
        toFoundNews(news);
        onView(allOf(hasSibling(withText(news.getTitle())),
                withId(R.id.news_item_publication_date_text_view))
        ).check(matches(withText(news.getDateNews())));
        onView(allOf(hasSibling(withText(news.getTitle())),
                withId(R.id.news_item_create_date_text_view))
        ).check(matches(withText(news.getCreationDate())));
    }

    public NewsInfo.NewInfo editNewsDataAndTime(NewsInfo.NewInfo news, String data, String time) {
        toFoundNews(news);
        onView(
                allOf(
                        withId(R.id.edit_news_item_image_view),
                        withParent(withParent((hasDescendant(withText(news.getTitle()))))))
        ).perform(click());
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        news.setDateNews(data);
        news.setTimeNews(time);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.editNewsDataAndTime(news);
        return news;
    }
}

