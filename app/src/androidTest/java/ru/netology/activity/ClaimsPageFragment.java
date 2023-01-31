package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.ForAllFunk;

public class ClaimsPageFragment {

    ForAllFunk res = new ForAllFunk();

    public void createClaim(HospiceInfo.ClaimInfo claimInfo) throws Exception {
        onView(withId(R.id.add_new_claim_material_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.add_new_claim_material_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.container_custom_app_bar_include_on_fragment_create_edit_claim))
                .check(matches(isEnabled()));
        ClaimFragment claim = new ClaimFragment();
        claim.createClaim(claimInfo);
    }

    public ClaimFragment toFoundClaim(HospiceInfo.ClaimInfo claimInfo) throws Exception {

        res.setUpFilter(true); //ставим галочки
        Thread.sleep(1000);
        ViewInteraction recyclerView = onView(withId(R.id.claim_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        Thread.sleep(1000);
        recyclerView.perform(actionOnItem(hasDescendant(withText(claimInfo.getTitle())), click()));
        Thread.sleep(10000);
        return new ClaimFragment();
    }

    public ClaimFragment toFoundClaimWithFilter(HospiceInfo.ClaimInfo claimInfo, String status) throws Exception {

        //TODO Привести Canceled (статус)-Cancelled (статус в фильтре) к одному знаменателю.
        if (status == "Canceled") {
            status = "Cancelled";
        }
        res.setUpFilter(false); //чистим галочки

        onView(withId(R.id.filters_material_button)).perform(click());
        // ставим нужную галочку
        onView(withText(status)).check(matches(isNotChecked())).perform(scrollTo(), click());

        onView(withId(R.id.claim_list_filter_ok_material_button)).perform(click());
        Thread.sleep(6000);

        ViewInteraction recyclerView = onView(withId(R.id.claim_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(claimInfo.getTitle())), click()));

        return new ClaimFragment();
    }

    public void toCheckStatusClaim(HospiceInfo.ClaimInfo claimInfo, String status) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        claimFragment.toCheckClaim(claimInfo, status);
    }

    public HospiceInfo.ClaimInfo toChangeStatusClaim(HospiceInfo.ClaimInfo claimInfo, String oldStatus, String newStatus, boolean comment) throws Exception {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo, oldStatus);
        Thread.sleep(2000);
        return claimFragment.toChangeStatusClaim(claimInfo, oldStatus, newStatus, comment);
    }

    public void addCommentToClaim(HospiceInfo.ClaimInfo claimInfo, String comment, boolean okCancel, int numberOfComments) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        claimFragment.writeComment(claimInfo, comment, okCancel, numberOfComments);
    }

    public void editCommentToClaim(HospiceInfo.ClaimInfo claimInfo, String comment, String oldComment, boolean okCancel, int numberOfComments) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        claimFragment.editComment(claimInfo, comment, oldComment, okCancel, numberOfComments);
    }

    public void editClaim(HospiceInfo.ClaimInfo claimInfo, HospiceInfo.ClaimInfo claimInfo2) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        claimFragment.editClaimTitleAndDescription(claimInfo, claimInfo2);
    }

    public void editClaimNot(HospiceInfo.ClaimInfo claimInfo, String status) throws Exception {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo, status);
        Thread.sleep(2000);
        claimFragment.editClaimNot(claimInfo);
    }

    public void changeExecutor(HospiceInfo.ClaimInfo claimInfo, String executor, String claimStatus) throws Exception {
        ClaimFragment claimFragment = toFoundClaimWithFilter(claimInfo, claimStatus);
        Thread.sleep(2000);
        claimFragment.changeExecutor(claimInfo, executor);
    }

    public void goToMainPage() throws Exception {
        onView(withId(R.id.main_menu_image_button)).perform(click());
        res.getItemFromList("Main");
        Thread.sleep(2000);
        onView(withId(R.id.main_swipe_refresh))
                .check(matches(isDisplayed()));
    }
}
