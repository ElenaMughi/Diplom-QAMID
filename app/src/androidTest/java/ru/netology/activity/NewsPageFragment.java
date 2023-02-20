package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.NewsInfo;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.WaitId;

public class NewsPageFragment {

    PrintText res = new PrintText();

    public NewsEditPageFragment goToEditNewsPage() {
        onView(withId(R.id.edit_news_material_button)).perform(click());
        onView(withId(R.id.news_control_panel_swipe_to_refresh))
                .check(matches(isDisplayed()));
        return new NewsEditPageFragment();
    }

    public void checkNewsWithCategoryFilter(NewsInfo.NewInfo newsInfo) {
        setUpCategoryFilter(newsInfo.getCategory());
        toFoundNews(newsInfo);
        onView(allOf(withId(R.id.news_item_title_text_view), withText(newsInfo.getTitle())))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.news_item_description_text_view), withText(newsInfo.getDescription())))
                .check(matches(isDisplayed()));
    }

    public void setUpCategoryFilter(String category) {
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu")))
                .perform(click());
        res.getItemFromList(category);
        onView(withId(R.id.filter_button)).perform(click());
        WaitId.waitId(R.id.news_list_recycler_view, 5000);
    }

    public void toFoundNews(NewsInfo.NewInfo newsInfo) {
        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        recyclerView.perform(actionOnItem(hasDescendant(withText(newsInfo.getTitle())), click()));
        WaitId.waitId(R.id.news_item_description_text_view, 10000);
    }
}
