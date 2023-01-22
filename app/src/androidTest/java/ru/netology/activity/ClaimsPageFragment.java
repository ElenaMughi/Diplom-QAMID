package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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

import org.hamcrest.core.AllOf;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.EspressoIdlingResources;
import ru.netology.resourses.ForAllResourses;

public class ClaimsPageFragment {

    ForAllResourses res = new ForAllResourses();

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

    public MainPageFragment goToMainPage() throws Exception { // только вызов создания заявки
        //TODO
//        onView(withId(R.id.main_menu_image_button)).perform(click());
//        res.getItemFromList("Main");
//        Thread.sleep(2000);
//        onView(withId(R.id.all_claims_text_view))
//                .check(matches(isDisplayed()));
        return new MainPageFragment();
    }

    public ClaimFragment toFoundClaim(HospiceInfo.ClaimInfo claimInfo) throws Exception {
        ViewInteraction recyclerView = onView(withId(R.id.claim_list_recycler_view));
        recyclerView.check(CustomViewAssertions.isRecyclerView());
        recyclerView.perform(actionOnItem(hasDescendant(withText(claimInfo.getTitle())), click()));
        Thread.sleep(6000);
        return new ClaimFragment();
    }

    public ViewInteraction toFoundClaimWithFilter(HospiceInfo.ClaimInfo claimInfo) throws Exception {
//TODO
        ViewInteraction claim = null;
        return claim;
    }

    public void toCheckStatusClaim(HospiceInfo.ClaimInfo claimInfo, String status) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        claimFragment.toCheckClaim(claimInfo, status);
    }

    public HospiceInfo.ClaimInfo toChangeStatusClaim(HospiceInfo.ClaimInfo claimInfo, String oldStatus, String newStatus) throws Exception {
        ClaimFragment claimFragment = toFoundClaim(claimInfo);
        Thread.sleep(2000);
        return claimFragment.toChangeStatusClaim(claimInfo, oldStatus, newStatus);
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
}
