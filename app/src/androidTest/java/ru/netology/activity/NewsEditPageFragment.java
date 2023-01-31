package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.ForAllFunk;

public class NewsEditPageFragment {

    ForAllFunk res = new ForAllFunk();

    public void createSimpleNews(HospiceInfo.NewsInfo newsInfo, boolean saveCancel) throws Exception {
        onView(withId(R.id.add_news_image_view)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.createSimpleNews(newsInfo, saveCancel);
    }

    public void checkFieldsFromNews(HospiceInfo.NewsInfo newsInfo) throws Exception {
        onView(withId(R.id.add_news_image_view)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.custom_app_bar_sub_title_text_view))
                .check(matches(isDisplayed()));
        NewsFragment news = new NewsFragment();
        news.checkFieldsFromNews(newsInfo);
    }

    public void toFoundNews(HospiceInfo.NewsInfo newsInfo) throws Exception {

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        Thread.sleep(6000);
    }

    public void deleteNews(HospiceInfo.NewsInfo newsInfo, boolean okNo) throws Exception {
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

    public void goToMainPage() throws Exception {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        Thread.sleep(2000);
        onView(withId(R.id.main_swipe_refresh))
                .check(matches(isDisplayed()));
    }

    public void changeActive(HospiceInfo.NewsInfo newsInfo, boolean getActive) throws Exception {
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

    public void editNews(HospiceInfo.NewsInfo newsInfo, HospiceInfo.NewsInfo newsInfo2, boolean saveCancel) throws Exception {
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

    public void checkNewsCategory(HospiceInfo.NewsInfo newsInfo, boolean active, boolean notActive) throws Exception {
        setUpFilterCategory(newsInfo.getCategory());
        setUpFilterActive(active, notActive);
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.news_item_title_text_view),
                withText(newsInfo.getTitle()))
        ).perform(click());
        onView(allOf(withId(R.id.news_item_description_text_view),
                withText(newsInfo.getDescription()))
        ).check(matches(isDisplayed()));
    }

    private void setUpFilterActive(boolean active, boolean notActive) {
        onView(withId(R.id.filter_news_active_material_check_box)).perform(scrollTo(), CustomSetChecked.setChecked(active));
        onView(withId(R.id.filter_news_inactive_material_check_box)).perform(scrollTo(), CustomSetChecked.setChecked(notActive));
    }

    public void setUpFilterCategory(String Category) {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(clearText());
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        ViewInteraction category =
                onView(withId(R.id.news_item_category_text_auto_complete_text_view)); //Категория
        category.perform(click());
        res.getItemFromList(Category);
        onView(withId(R.id.filter_button)).perform(click());
    }
}

