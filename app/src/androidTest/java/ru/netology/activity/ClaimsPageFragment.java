package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.resourses.CustomSetChecked;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.EspressoIdlingResources;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.WaitId;

public class ClaimsPageFragment {

    PrintText res = new PrintText();
    HospiceData hospiceData = new HospiceData();

    public void setFilter(boolean filter[]) {
        WaitId.waitMyIdWithCheck(R.id.filters_material_button, 5000);
        onView(withId(R.id.filters_material_button)).perform(click());
        // ставим галочки согласно фильтру
        onView(withId(R.id.item_filter_open)).perform(scrollTo(), CustomSetChecked.setChecked(filter[0]));
        onView(withId(R.id.item_filter_in_progress)).perform(scrollTo(), CustomSetChecked.setChecked(filter[1]));
        onView(withId(R.id.item_filter_executed)).perform(scrollTo(), CustomSetChecked.setChecked(filter[2]));
        onView(withId(R.id.item_filter_cancelled)).perform(scrollTo(), CustomSetChecked.setChecked(filter[3]));

        onView(withId(R.id.claim_list_filter_ok_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.claim_list_recycler_view, 5000);
    }

    public void getFilterByStatus(String status) {
        boolean filter[] = {false, false, false, false};
        for (int i = 0; i < 4; i++) {
            if (status == hospiceData.claimStatus[i]) {
                filter[i] = true;
            }
        }
        setFilter(filter);
    }

    public ClaimFragment foundClaim(int id, String text) {
        EspressoIdlingResources.increment();
        ViewInteraction recyclerView = onView(withId(id));
        EspressoIdlingResources.decrement();

        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(text)), click()));

        WaitId.waitMyIdWithCheck(R.id.close_image_button, 15000);
        return new ClaimFragment();
    }

    public ClaimFragment toFoundClaimWithWholeFilter(ClaimsInfo.ClaimInfo claimInfo) {
        boolean filter[] = {true, true, true, true};
        setFilter(filter); //ставим галочки
        return foundClaim(R.id.claim_list_recycler_view, claimInfo.getTitle());
    }

    public ClaimFragment toFoundClaimWithFilter(ClaimsInfo.ClaimInfo claimInfo, String status) {
        //TODO Привести Canceled (статус)-Cancelled (статус в фильтре) к одному знаменателю.
        getFilterByStatus(status);
        return foundClaim(R.id.claim_list_recycler_view, claimInfo.getTitle());
    }

    public ClaimsInfo.ClaimInfo createClaim(ClaimsInfo.ClaimInfo claimInfo) {
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.save_button, 5000);
        ClaimFragment claim = new ClaimFragment();
        return claim.createClaim(claimInfo);
    }

    public void toCheckStatusClaim(ClaimsInfo.ClaimInfo claimInfo, String status) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo, status);
        claimFragment.toCheckClaim(claimInfo, status);
    }

    public void toChangeStatusClaim(ClaimsInfo.ClaimInfo claimInfo, String oldStatus, String newStatus, boolean comment) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo, oldStatus);
        claimFragment.toChangeStatusClaim(claimInfo, oldStatus, newStatus, comment);
    }

    public void addCommentToClaim(ClaimsInfo.ClaimInfo claimInfo, String comment, boolean okCancel, int numberOfComments) {
        ClaimFragment claimFragment = toFoundClaimWithWholeFilter(claimInfo);
        claimFragment.writeComment(claimInfo, comment, okCancel, numberOfComments);
    }

    public void editCommentToClaim(ClaimsInfo.ClaimInfo claimInfo, String comment, String oldComment, boolean okCancel, int numberOfComments) {
        ClaimFragment claimFragment = toFoundClaimWithWholeFilter(claimInfo);
        claimFragment.editComment(claimInfo, comment, oldComment, okCancel, numberOfComments);
    }

    public void editClaim(ClaimsInfo.ClaimInfo claimInfo, ClaimsInfo.ClaimInfo claimInfo2) {
        ClaimFragment claimFragment = toFoundClaimWithWholeFilter(claimInfo);
        claimFragment.editClaimTitleAndDescription(claimInfo, claimInfo2);
    }

    public void editClaimNot(ClaimsInfo.ClaimInfo claim, String status) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim, status);
        claimFragment.editClaimNot(claim);
    }

    public void changeExecutor(ClaimsInfo.ClaimInfo claim, String executor, String status) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim, status);
        claimFragment.changeExecutor(claim, executor);
    }

    public void goToMainPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.main_swipe_refresh, 3000);
    }

    public void checkClaim(ClaimsInfo.ClaimInfo claimInfo, String status) {
        ClaimFragment claimFragment = toFoundClaimWithWholeFilter(claimInfo);
        claimFragment.toCheckClaim(claimInfo, status);
    }

    public void checkClaimWithFiler(ClaimsInfo.ClaimInfo claim, String status) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim, status);
        claimFragment.toCheckClaim(claim, status);
    }

    public void checkClaimWithMultipleFiler(ClaimsInfo.ClaimInfo[] claims, boolean filter[]) {
        setFilter(filter);
        for (int i = 0; i < 3; i++) {
            if (filter[i]) {
                ClaimFragment claimFragment = foundClaim(R.id.claim_list_recycler_view, claims[i].getTitle());
                claimFragment.toCheckClaim(claims[i], hospiceData.claimStatus[i]);
            }
        }
    }

    public void checkDataTimeInClaim(ClaimsInfo.ClaimInfo claim) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim, hospiceData.claimStatus[1]);
        claimFragment.checkDataTime(claim);
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void editData(ClaimsInfo.ClaimInfo claim, String data, String time) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim, hospiceData.claimStatus[1]);
        claimFragment.editDataTime(data, time);
    }
}
