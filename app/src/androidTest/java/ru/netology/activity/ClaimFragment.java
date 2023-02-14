package ru.netology.activity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

import static ru.netology.resourses.WaitId.waitId;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;

import org.hamcrest.Matchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.resourses.CustomViewAssertions;
import ru.netology.resourses.CustomViewMatcher;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.SetDataTime;
import ru.netology.resourses.WaitId;

public class ClaimFragment {

    PrintText res = new PrintText();
    HospiceData hospiceData = new HospiceData();

    public void goBackToClaimPage() {
        onView(withId(R.id.close_image_button)).perform(click());
    }


    public ClaimsInfo.ClaimInfo createClaim(ClaimsInfo.ClaimInfo claimInfo) {

        onView(isRoot()).perform(waitId(R.id.title_edit_text, 3000));
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

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, claimInfo.getPlanDate());
        claimInfo.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, claimInfo.getPlanTime());
        claimInfo.setCreationTime(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")));

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
        return claimInfo;
    }

    public String choiceStatus(ClaimsInfo.ClaimInfo claimInfo, String oldStatus, String newStatus) {
        onView(allOf(withId(R.id.status_label_text_view), withText(oldStatus))).check(matches(isDisplayed()));

        String statusMenu = hospiceData.claimStatusListMenu[4]; //устанавливаем недоступный вариант.
        if (oldStatus == hospiceData.claimStatus[0]) {
            if (newStatus == hospiceData.claimStatus[1]) {
                statusMenu = hospiceData.claimStatusListMenu[0];
                claimInfo.setExecutor(claimInfo.getAuthor()); // при смене статуса на в работе меняется исполнитель. добавляем его.
            }
            if (newStatus == hospiceData.claimStatus[3]) {
                statusMenu = hospiceData.claimStatusListMenu[1];
            }
        }
        if (oldStatus == hospiceData.claimStatus[1]) {
            if (newStatus == hospiceData.claimStatus[0]) {
                statusMenu = hospiceData.claimStatusListMenu[2];
            }
            if (newStatus == hospiceData.claimStatus[2]) {
                statusMenu = hospiceData.claimStatusListMenu[3];
            }
        }
        return statusMenu;
    }

    public void toCheckClaim(ClaimsInfo.ClaimInfo claimInfo, String status) {

        onView(allOf(withId(R.id.status_label_text_view), withText(status))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title_text_view), withText(claimInfo.getTitle()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.description_text_view), withText(claimInfo.getDescription()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.author_name_text_view), withText(claimInfo.getAuthor()))).check(matches(isDisplayed()));

        if (claimInfo.getExecutor() == "") { // проверка исполнителя
            onView(allOf(withId(R.id.executor_name_text_view), withText("NOT ASSIGNED"))).check(matches(isDisplayed()));
        } else {
            String s = claimInfo.getExecutor();
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

    public void toChangeStatusClaim(ClaimsInfo.ClaimInfo claimInfo, String oldStatus, String
            newStatus, boolean comment) {

        String statusMenu = choiceStatus(claimInfo, oldStatus, newStatus); //определяем пункт меню у статуса
        if (statusMenu == hospiceData.claimStatusListMenu[4]) {
            // Проверяем что кнопка недоступна - можно вынести в отдельный метод.
            onView(withId(R.id.status_processing_image_button))
                    .check(matches(isEnabled())).perform(click());
            onView(withId(R.id.status_icon_image_view))
                    .check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.status_processing_image_button))
                    .perform(click());
            res.getItemFromList(statusMenu);
            if (oldStatus == hospiceData.claimStatus[1]) {  // переход из  "В работе" на другой статус с комментарием
                if (comment) { // если пишем комментарий
                    res.typingText(R.id.editText, hospiceData.comment[0]);
                    onView(withId(android.R.id.button1)).perform(click());
                } else { // если не пишем комметарий. статус не меняется.
                    onView(withId(android.R.id.button2)).perform(click());
                }
            }
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void cancelCreateClaim(ClaimsInfo.ClaimInfo claimInfo, boolean OKCANCEL) {

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
            onView(withId(R.id.our_mission_image_button)).check(matches(isDisplayed()));
        } else {
            onView(withText("CANCEL")).perform(click());
            onView(withId(R.id.save_button)).check(matches(isDisplayed()));
            onView(withId(R.id.save_button)).perform(click());
            WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
        }
    }

    private void checkFillEmptyFieldsMessage() {
        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    public void checkCreateClaimWithEmptyFields(ClaimsInfo.ClaimInfo claimInfo) {

        checkFillEmptyFieldsMessage();

        res.typingText(R.id.title_edit_text, claimInfo.getTitle());
        checkFillEmptyFieldsMessage();

        // без исполнителя можно

        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата из календаря
        onView(withId(android.R.id.button1)).perform(click());
        checkFillEmptyFieldsMessage();

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время из календаря
        onView(withId(android.R.id.button1)).perform(click());
        checkFillEmptyFieldsMessage();

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание
        res.typingTextWithClear(R.id.title_edit_text, "");
        checkFillEmptyFieldsMessage();

        onView(withId(R.id.cancel_button)).perform(click());
        onView(withText("OK")).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
    }

    public void writeComment(ClaimsInfo.ClaimInfo claimInfo, String comment, boolean okCancel, int numberOfComments) {

        onView(withId(R.id.add_comment_image_button)).perform(click());
        comment = comment + Math.round(Math.random() * 10);
        res.typingTextWithParent(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            checkComment(claimInfo, comment, numberOfComments);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void editComment(ClaimsInfo.ClaimInfo claimInfo, String comment, String oldComment, boolean okCancel, int numberOfComments) {

        onView(withId(R.id.edit_comment_image_button)).perform(click());
        res.typingTextWithParentWithClear(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            checkComment(claimInfo, comment, numberOfComments);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
            checkComment(claimInfo, oldComment, numberOfComments);
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void checkComment(ClaimsInfo.ClaimInfo claimInfo, String comment, int numberOfComments) {
        if (numberOfComments > 1) {

            onView(withText(comment)).check(matches(isDisplayed()));

            ViewInteraction recyclerView = onView(withId(R.id.claim_comments_list_recycler_view));
            recyclerView.check(CustomViewAssertions.isRecyclerView());
            recyclerView.perform(swipeUp());
            recyclerView.check(
                    matches(CustomViewMatcher.recyclerViewSizeMatcher(numberOfComments)));
        } else {
            onView(withText(comment)).check(matches(isDisplayed()));
            onView(withId(R.id.commentator_name_text_view)).check(matches(withText(claimInfo.getAuthor())));
        }
    }

    public void editClaimTitleAndDescription(ClaimsInfo.ClaimInfo claimInfo, ClaimsInfo.ClaimInfo claimInfo2) {
        onView(withId(R.id.edit_processing_image_button)).perform(click());

        onView(allOf(withId(R.id.title_edit_text), withText(claimInfo.getTitle())))
                .check(matches(isDisplayed()));
        res.typingTextWithClear(R.id.title_edit_text, claimInfo2.getTitle()); //Заголовок

        onView(allOf(withId(R.id.description_edit_text), withText(claimInfo.getDescription())))
                .check(matches(isDisplayed()));
        res.typingTextWithClear(R.id.description_edit_text, claimInfo2.getDescription()); // Описание

        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.close_image_button)).perform(click());
        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.create_data_text_view)) // проверка времени
                .check(matches(withText(claimInfo2.getPlanDate())));
        onView(withId(R.id.create_time_text_view))
                .check(matches(withText(claimInfo2.getPlanTime())));
    }

    public void changeExecutor(ClaimsInfo.ClaimInfo claimInfo, String executor) {
        onView(allOf(withId(R.id.executor_name_text_view), withText("NOT ASSIGNED")))
                .check(matches(isDisplayed()));

        onView(withId(R.id.edit_processing_image_button))
                .check(matches(isEnabled())).perform(click());

        ViewInteraction executorClaim =  // Исполняющий
                onView(withId(R.id.executor_drop_menu_auto_complete_text_view));
        executorClaim.perform(click());
        res.getItemFromList(executor);
        executorClaim.check(matches(withText(executor))); //проверка вставки
        closeSoftKeyboard();
        onView(withId(R.id.save_button)).perform(click());
        onView(allOf(withId(R.id.status_label_text_view), withText("In progress")))
                .check(matches(isDisplayed()));

        onView(withId(R.id.close_image_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
    }

    public void editClaimNot(ClaimsInfo.ClaimInfo claimInfo) {
        // TODO должна быть проверка всплывающего сообщения
        onView(withId(R.id.edit_processing_image_button))
                .check(matches(isEnabled())).perform(click());
        onView(withId(R.id.status_icon_image_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.close_image_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
    }

    public void checkDataTime(ClaimsInfo.ClaimInfo claim) {
        onView(withId(R.id.plane_date_text_view))
                .check(matches(withText(claim.getPlanDate())));
        onView(withId(R.id.plan_time_text_view))
                .check(matches(withText(claim.getPlanTime())));
    }

    public void editDataTime(String data, String time) {

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, data);
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, time);

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
    }
}
