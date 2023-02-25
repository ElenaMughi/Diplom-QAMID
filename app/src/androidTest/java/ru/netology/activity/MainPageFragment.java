package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static ru.netology.resourses.WaitId.waitId;

import ru.iteco.fmhandroid.R;
import ru.netology.data.NewsInfo;

import ru.netology.resourses.PrintText;
import ru.netology.resourses.WaitId;

public class MainPageFragment {

    PrintText res = new PrintText();

    public ClaimsPageFragment goToClaimsPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Claims");
        onView(isRoot())
                .perform(waitId(R.id.all_claims_cards_block_constraint_layout, 2000));
        WaitId.waitId(R.id.all_claims_cards_block_constraint_layout, 3000);
        return new ClaimsPageFragment();
    }

    public ClaimsPageFragment goToClaimsPageFromClaimBox() { // только вызов создания заявки
        onView(withId(R.id.all_claims_text_view)).perform(click());
        WaitId.waitId(R.id.all_claims_cards_block_constraint_layout, 3000);
        return new ClaimsPageFragment();
    }

    public ClaimFragment callCreationClaimFromMainPage() { // только вызов создания заявки
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.save_button, 10000);
        return new ClaimFragment();
    }

    public NewsPageFragment goToNewsPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("News");
        WaitId.waitMyIdWithCheck(R.id.news_list_swipe_refresh, 10000);
        return new NewsPageFragment();
    }

    public NewsPageFragment goToNewsPageFromNewsBox() {
        onView(withId(R.id.all_news_text_view)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.news_list_swipe_refresh, 10000);
        return new NewsPageFragment();
    }

    public AboutPageFragment goToAboutPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("About");
        return new AboutPageFragment();
    }

    public LoveIsAllPageFragment goToLoveIsAllPage() {
        onView(withId(R.id.our_mission_image_button)).perform(click());
        return new LoveIsAllPageFragment();
    }

    public void checkNews(NewsInfo.NewInfo newsInfo, boolean visible) {
        if (visible) {
            WaitId.waitId(R.id.view_news_item_image_view, 5000);
            onView(allOf(withId(R.id.view_news_item_image_view),
                    withParent(withParent((hasDescendant(withText(newsInfo.getTitle()))))))
            ).perform(click());

            onView(allOf(withId(R.id.news_item_description_text_view),
                    withText(newsInfo.getDescription()))
            ).check(matches(isDisplayed()));
        } else {
// TODO Проверка что на экране нет - не реализовано
        }
    }

}
