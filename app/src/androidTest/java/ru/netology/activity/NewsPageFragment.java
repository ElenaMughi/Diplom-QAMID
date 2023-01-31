package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
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
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.ForAllFunk;

public class NewsPageFragment {

    ForAllFunk res = new ForAllFunk();

    public NewsEditPageFragment goToEditNewsPage() throws Exception {
        onView(withId(R.id.edit_news_material_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.news_control_panel_swipe_to_refresh))
                .check(matches(isDisplayed()));
        return new NewsEditPageFragment();
    }

    public void checkNewsCategory(HospiceInfo.NewsInfo newsInfo) throws Exception {
        setUpFilter(newsInfo.getCategory());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.news_item_title_text_view),
                withText(newsInfo.getTitle()))
        ).perform(click());
        onView(allOf(withId(R.id.news_item_description_text_view),
                withText(newsInfo.getDescription()))
        ).check(matches(isDisplayed()));
    }

    public void setUpFilter(String category) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(
                allOf(
                        withClassName(endsWith("ImageButton")),
                        withParent(withParent(withParent(withParent(withId(R.id.news_item_category_text_auto_complete_text_view)))))
                )
        ).perform(click());
//        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(clearText());
//        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click()); //Категория
        res.getItemFromList(category);
        onView(withId(R.id.filter_button)).perform(click());
    }

    public void toFoundNews(HospiceInfo.NewsInfo newsInfo) throws Exception {

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        Thread.sleep(6000);
    }
}
