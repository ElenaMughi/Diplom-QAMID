package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.WaitId;

public class ClaimsPageFragment {

    PrintText res = new PrintText();
    HospiceData hospiceData = new HospiceData();

    public void setFilter(boolean filter[]) {
        WaitId.waitId(R.id.filters_material_button, 20000);
        onView(withId(R.id.filters_material_button)).perform(click());
        // ставим галочки согласно фильтру
        onView(withId(R.id.item_filter_open)).perform(scrollTo(), CustomSetChecked.setChecked(filter[0]));
        onView(withId(R.id.item_filter_in_progress)).perform(scrollTo(), CustomSetChecked.setChecked(filter[1]));
        onView(withId(R.id.item_filter_executed)).perform(scrollTo(), CustomSetChecked.setChecked(filter[2]));
        onView(withId(R.id.item_filter_cancelled)).perform(scrollTo(), CustomSetChecked.setChecked(filter[3]));

        onView(withId(R.id.claim_list_filter_ok_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.claim_list_recycler_view, 40000);
    }

    public ClaimFragment foundClaim(String text) {

        ViewInteraction recyclerView = onView(withId(R.id.claim_list_recycler_view));
        recyclerView.perform(actionOnItem(hasDescendant(withText(text)), click()));

        WaitId.waitMyIdWithCheck(R.id.title_text_view, 90000);
        return new ClaimFragment();
    }

    public ClaimFragment toFoundClaimWithRandomFilter(ClaimsInfo.ClaimInfo claimInfo, boolean filter[]) {
        setFilter(filter); //ставим галочки

        ClaimFragment claim = foundClaim(claimInfo.getTitle());
        WaitId.waitMyIdWithCheck(R.id.status_label_text_view, 20000);
        return claim;
    }

    public ClaimFragment toFoundClaimWithFilter(ClaimsInfo.ClaimInfo claimInfo) {
        boolean filter[] = {false, false, false, false}; //определяем галочки в фильтре
        if (claimInfo.getStatus() == HospiceData.claimsStatus.OPEN.getTitle()) { filter[0] = true; }
        if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) { filter[1] = true; }
        if (claimInfo.getStatus() == HospiceData.claimsStatus.EXEC.getTitle()) { filter[2] = true; }
        if (claimInfo.getStatus() == HospiceData.claimsStatus.CANCEL.getTitle()) { filter[3] = true; }
        setFilter(filter);

        ClaimFragment claim = foundClaim(claimInfo.getTitle());
        WaitId.waitMyIdWithCheck(R.id.status_label_text_view, 20000);
        return claim;
    }

    public ClaimFragment callCreateClaim() {
        WaitId.waitMyIdWithCheck(R.id.add_new_claim_material_button, 10000);
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.save_button, 10000);
        return new ClaimFragment();
    }

    public void goToMainPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.claim_list_recycler_view, 5000);
    }

}
