package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.matcher.RootMatchers;

import ru.iteco.fmhandroid.R;

public class MainPageFragment {

    protected void getMenuFromList(String menu) {
        onView(withText(menu))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

    public ClaimsPageFragment callCreateNewClaimFromMainPage() { // только вызов создания заявки
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        return new ClaimsPageFragment();
    }

    public ClaimsPageFragment goToClaimPage() { // только вызов создания заявки
        onView(withId(R.id.main_menu_image_button)).perform(click());
        getMenuFromList("Claims");
        return new ClaimsPageFragment();
    }

    //TODO


}
