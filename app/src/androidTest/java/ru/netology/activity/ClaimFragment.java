package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasDataString;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsNot.not;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.data.HospiceInfo;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.CustomViewMatcher;
import ru.netology.resourses.ForAllResourses;

public class ClaimFragment {

    ForAllResourses res = new ForAllResourses();

    public void goBackToClaimPage() {
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void toCheckClaim(HospiceInfo.ClaimInfo claimInfo, String status) {

        onView(allOf(withId(R.id.status_label_text_view), withText(status))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title_text_view), withText(claimInfo.getTitle()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.description_text_view), withText(claimInfo.getDescription()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.author_name_text_view), withText(claimInfo.getAuthor()))).check(matches(isDisplayed()));

        if (claimInfo.getExecutor() == "") { // проверка исполнителя
            onView(allOf(withId(R.id.executor_name_text_view), withText("NOT ASSIGNED"))).check(matches(isDisplayed()));
        } else {
            onView(allOf(withId(R.id.executor_name_text_view), withText(claimInfo.getExecutor()))).check(matches(isDisplayed()));
        }

        if (claimInfo.getExecutor() == claimInfo.getAuthor()) {  // проверка доступности кнопки смены статса
            onView(withId(R.id.status_processing_image_button)).check(matches(isEnabled()));
        } else {
            if (claimInfo.getExecutor() == "") {
                onView(withId(R.id.status_processing_image_button)).check(matches(isEnabled()));
            } else {
                onView(withId(R.id.status_processing_image_button)).check(matches(not(isEnabled())));
            }
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void changeExecutor(HospiceInfo.ClaimInfo claimInfo, String executor) {

    }

    public HospiceInfo.ClaimInfo toChangeStatusClaim(HospiceInfo.ClaimInfo claimInfo, String oldStatus, String
            newStatus) {

        onView(allOf(withId(R.id.status_label_text_view), withText(oldStatus))).check(matches(isDisplayed()));
        String status = newStatus;
        if (newStatus == HospiceInfo.claimStatus[0]) {
            status = HospiceInfo.claimStatusListMenu[0];
        } else {
            if (newStatus == HospiceInfo.claimStatus[1]) {
                status = HospiceInfo.claimStatusListMenu[1];
            } else {
                if (newStatus == HospiceInfo.claimStatus[2]) {
                    status = HospiceInfo.claimStatusListMenu[2];
                } else {
                    if (newStatus == HospiceInfo.claimStatus[3]) {
                        status = HospiceInfo.claimStatusListMenu[3];
                    }
                }
            }
        }
        onView(withId(R.id.status_processing_image_button)).perform(click());
        res.getItemFromList(status);
        onView(withId(R.id.close_image_button)).perform(click());
        claimInfo.setExecutor(claimInfo.getAuthor());
        return claimInfo;
    }

    public void createClaim(HospiceInfo.ClaimInfo claimInfo) throws Exception {

        res.typingText(R.id.title_edit_text, claimInfo.getTitle()); //Заголовок

        if (claimInfo.getExecutor().length() > 0) {
            ViewInteraction executorClaim =  // Исполняющий
                    onView(withId(R.id.executor_drop_menu_auto_complete_text_view));
            executorClaim.perform(click());
            if (claimInfo.getExecutor() == claimInfo.getAuthor()) {
                res.getItemFromList(claimInfo.getExecutor());
            } else {
                res.typingText(R.id.executor_drop_menu_auto_complete_text_view, claimInfo.getExecutor());
            }
            executorClaim.check(matches(withText(claimInfo.getExecutor()))); //проверка вставки
        }

        //TODO выбор даты-времени не реализован
        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата
        ViewInteraction date = onView(withId(android.R.id.button1));
        date.perform(click());

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время
        ViewInteraction time = onView(withId(android.R.id.button1));
        time.perform(click());

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

        onView(withId(R.id.save_button)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
    }

    public void cancelCreateClaim(HospiceInfo.ClaimInfo claimInfo, boolean OKCANCEL) throws
            Exception {

        res.typingText(R.id.title_edit_text, claimInfo.getTitle()); //Заголовок

        // без исполнителя

        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата из календаря
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время из календаря
        onView(withId(android.R.id.button1)).perform(click());

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

        onView(withId(R.id.cancel_button)).perform(click());

        onView(withText("The changes won't be saved, do you really want to log out?"))
                .check(matches(isEnabled()));

        if (OKCANCEL) {
            onView(withText("OK")).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.our_mission_image_button)).check(matches(isDisplayed()));
        } else {
            onView(withText("CANCEL")).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.save_button)).check(matches(isDisplayed()));
            onView(withId(R.id.save_button)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
        }
    }

    public void checkEmptyFieldsCreateClaim(HospiceInfo.ClaimInfo claimInfo) {

        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        res.typingText(R.id.title_edit_text, claimInfo.getTitle());
        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        // без исполнителя

        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата из календаря
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время из календаря
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание
        res.typingTextWithClear(R.id.title_edit_text, "");
        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.cancel_button)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.all_claims_text_view))
                .check(matches(isDisplayed()));
    }

    public void writeComment(HospiceInfo.ClaimInfo claimInfo, String comment, boolean okCancel, int numberOfComments) throws
            Exception {

        onView(withId(R.id.add_comment_image_button)).perform(click());
        res.typingTextWithParent(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            Thread.sleep(6000);
            checkComment(claimInfo, comment, numberOfComments);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
            Thread.sleep(6000);
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void editComment(HospiceInfo.ClaimInfo claimInfo, String comment, String oldComment, boolean okCancel, int numberOfComments) throws
            Exception {

        onView(withId(R.id.edit_comment_image_button)).perform(click());
        res.typingTextWithParentWithClear(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            Thread.sleep(6000);
            checkComment(claimInfo, comment, numberOfComments);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
            Thread.sleep(6000);
            checkComment(claimInfo, oldComment, numberOfComments);
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void checkComment(HospiceInfo.ClaimInfo claimInfo, String comment, int numberOfComments) {
        if (numberOfComments > 1) {

            onView(withText(comment)).check(matches(isDisplayed()));

            ViewInteraction recyclerView = onView(withId(R.id.claim_comments_list_recycler_view));
            recyclerView.check(CustomViewAssertions.isRecyclerView());
            recyclerView.perform(swipeUp());
            recyclerView.check(
                    matches(CustomViewMatcher.recyclerViewSizeMatcher(numberOfComments)));
        }else {
            onView(withText(comment)).check(matches(isDisplayed()));
            onView(withId(R.id.commentator_name_text_view)).check(matches(withText(claimInfo.getAuthor())));
        }
    }

}
