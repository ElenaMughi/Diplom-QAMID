package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.matcher.RootMatchers;

import ru.iteco.fmhandroid.R;
import ru.netology.resourses.ForAllResourses;

public class MainPageFragment {

    ForAllResourses res = new ForAllResourses();

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

    public ClaimsPageFragment goToNewsPage() throws Exception { // только вызов создания заявки
//   TODO     onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("News");
        return new ClaimsPageFragment();
    }

    public ClaimsPageFragment callCreateNewNewsFromMainPage() { // только вызов создания заявки
//     TODO   onView(withId(R.id.add_new_claim_material_button)).perform(click());
        return new ClaimsPageFragment();
    }

    public ClaimsPageFragment goToAboutPage() { // только вызов создания заявки
        onView(withId(R.id.main_menu_image_button)).perform(click());
//       TODO getMenuFromList("About");
        return new ClaimsPageFragment();
    }

}
