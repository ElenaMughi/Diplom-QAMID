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
        WaitId.waitId(R.id.filters_material_button, 15000);
        onView(withId(R.id.filters_material_button)).perform(click());
        // ставим галочки согласно фильтру
        onView(withId(R.id.item_filter_open)).perform(scrollTo(), CustomSetChecked.setChecked(filter[0]));
        onView(withId(R.id.item_filter_in_progress)).perform(scrollTo(), CustomSetChecked.setChecked(filter[1]));
        onView(withId(R.id.item_filter_executed)).perform(scrollTo(), CustomSetChecked.setChecked(filter[2]));
        onView(withId(R.id.item_filter_cancelled)).perform(scrollTo(), CustomSetChecked.setChecked(filter[3]));

        onView(withId(R.id.claim_list_filter_ok_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.claim_list_recycler_view, 15000);
    }

    public void getFilterByStatus(String status) {
        boolean filter[] = {false, false, false, false};

        if (status == HospiceData.claimsStatus.OPEN.getTitle()) { filter[0] = true; }
        if (status == HospiceData.claimsStatus.WORK.getTitle()) { filter[1] = true; }
        if (status == HospiceData.claimsStatus.EXEC.getTitle()) { filter[2] = true; }
        if (status == HospiceData.claimsStatus.CANCEL.getTitle()) { filter[3] = true; }

        setFilter(filter);
    }

    public ClaimFragment foundClaim(int id, String text) {
        ViewInteraction recyclerView = onView(withId(id));
        recyclerView.perform(actionOnItem(hasDescendant(withText(text)), click()));

        WaitId.waitMyIdWithCheck(R.id.title_text_view, 20000);
        return new ClaimFragment();
    }

    public ClaimFragment toFoundClaimWithWholeFilter(ClaimsInfo.ClaimInfo claimInfo) {
        boolean filter[] = {true, true, true, true};
        setFilter(filter); //ставим галочки
        return foundClaim(R.id.claim_list_recycler_view, claimInfo.getTitle());
    }

    public ClaimFragment toFoundClaimWithFilter(ClaimsInfo.ClaimInfo claimInfo) {
        //TODO Привести Canceled (статус)-Cancelled (статус в фильтре) к одному знаменателю.
        getFilterByStatus(claimInfo.getStatus());
        return foundClaim(R.id.claim_list_recycler_view, claimInfo.getTitle());
    }

    public ClaimsInfo.ClaimInfo createClaim(ClaimsInfo.ClaimInfo claimInfo) {
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        WaitId.waitMyIdWithCheck(R.id.save_button, 5000);
        ClaimFragment claim = new ClaimFragment();
        return claim.createClaim(claimInfo);
    }

    public ClaimsInfo.ClaimInfo changeStatusOfClaim(ClaimsInfo.ClaimInfo claimInfo, String newStatus, boolean comment) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo);
        WaitId.waitMyIdWithCheck(R.id.title_text_view, 5000);
        return claimFragment.toChangeStatusClaim(claimInfo, newStatus, comment);
    }

    public ClaimsInfo.ClaimInfo addCommentToClaim(ClaimsInfo.ClaimInfo claimInfo, boolean okCancel) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo);
        return claimFragment.writeComment(claimInfo, okCancel);
    }

    public void editCommentToClaim(ClaimsInfo.ClaimInfo claimInfo, boolean okCancel) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo);
        claimFragment.editComment(claimInfo, okCancel);
    }

    public ClaimsInfo.ClaimInfo editTitleAndDescriptionInClaim(ClaimsInfo.ClaimInfo claimInfo, boolean saveCancel) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo);
        return claimFragment.editTitleAndDescriptionInClaim(claimInfo, saveCancel);
    }

    public ClaimsInfo.ClaimInfo changeExecutor(ClaimsInfo.ClaimInfo claim, String executor) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim);
        return claimFragment.changeExecutor(claim, executor);
    }

    public void goToMainPage() {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        WaitId.waitId(R.id.main_swipe_refresh, 3000);
    }

    public void checkClaimWithWholeFilter(ClaimsInfo.ClaimInfo claimInfo) {
        ClaimFragment claimFragment = toFoundClaimWithWholeFilter(claimInfo);
        claimFragment.toCheckClaim(claimInfo);
    }

    public void checkClaimWithFilter(ClaimsInfo.ClaimInfo claim) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim);
        claimFragment.toCheckClaim(claim);
    }

    public void checkClaimWithMultipleFiler(ClaimsInfo.ClaimInfo[] claims, boolean filter[]) {
        setFilter(filter);
        for (int i = 0; i < 3; i++) {
            if (filter[i]) {
                ClaimFragment claimFragment = foundClaim(R.id.claim_list_recycler_view, claims[i].getTitle());
                claimFragment.toCheckClaim(claims[i]);
            }
        }
    }

    public void editData(ClaimsInfo.ClaimInfo claim, String data, String time) {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claim);
        claimFragment.editDataTime(data, time);
    }
}
