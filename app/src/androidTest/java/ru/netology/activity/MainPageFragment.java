package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.ForAllFunk;

public class MainPageFragment {

    ForAllFunk res = new ForAllFunk();

    public ClaimsPageFragment goToClaimsPageFromMenu() throws Exception { // только вызов создания заявки
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Claims");
        Thread.sleep(2000);
        onView(withId(R.id.all_claims_cards_block_constraint_layout))
                .check(matches(isDisplayed()));
        return new ClaimsPageFragment();
    }

    public ClaimsPageFragment goToClaimsPageFromClaimBox() throws Exception { // только вызов создания заявки
        onView(withId(R.id.all_claims_text_view)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.all_claims_cards_block_constraint_layout))
                .check(matches(isDisplayed()));
        return new ClaimsPageFragment();
    }

    public ClaimFragment callCreateNewClaimFromMainPage() { // только вызов создания заявки
        onView(withId(R.id.add_new_claim_material_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        onView(withId(R.id.container_custom_app_bar_include_on_fragment_create_edit_claim))
                .check(matches(isEnabled()));
        return new ClaimFragment();
    }

    public NewsPageFragment goToNewsPage() throws Exception {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("News");
        Thread.sleep(2000);
        onView(withId(R.id.news_list_swipe_refresh))
                .check(matches(isDisplayed()));
        return new NewsPageFragment();
    }

    public NewsPageFragment goToNewsPageFromNewsBox() throws Exception {
        onView(withId(R.id.all_news_text_view)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.news_list_swipe_refresh))
                .check(matches(isDisplayed()));
        return new NewsPageFragment();
    }

    public ClaimsPageFragment goToAboutPage() { // только вызов создания заявки
        onView(withId(R.id.main_menu_image_button)).perform(click());
        // TODO getMenuFromList("About");
        return new ClaimsPageFragment();
    }

    public void checkNews(HospiceInfo.NewsInfo newsInfo, boolean visible) {
        if (visible) {
            onView(allOf(withId(R.id.view_news_item_image_view),
                    withParent(withParent((hasDescendant(withText(newsInfo.getTitle()))))))
            ).perform(click());
            onView(allOf(withId(R.id.news_item_description_text_view),
                    withText(newsInfo.getDescription()))
            ).check(matches(isDisplayed()));
        } else {
            // TODO Проверка что на экране нет.
        }
    }

}
