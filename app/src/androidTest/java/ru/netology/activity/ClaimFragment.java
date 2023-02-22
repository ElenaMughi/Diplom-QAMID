package ru.netology.activity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.ViewInteraction;

import net.datafaker.Faker;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;
import ru.netology.data.ClaimsInfo;
import ru.netology.resourses.CustomViewMatcher;
import ru.netology.resourses.PrintText;
import ru.netology.resourses.SetDataTime;
import ru.netology.resourses.WaitId;

public class ClaimFragment {

    PrintText res = new PrintText();
    static Faker faker = new Faker();

    public void goBackToClaimPage() {
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void fillingInClaimFields(ClaimsInfo.ClaimInfo claimInfo) {

        onView(isRoot()).perform(waitId(R.id.title_edit_text, 3000));
        res.typingText(R.id.title_edit_text, claimInfo.getTitle()); //Заголовок

        if (claimInfo.getExecutor().length() > 0) {
            claimInfo.setStatus(HospiceData.claimsStatus.WORK.getTitle()); // сохраняем статус
            ViewInteraction executorClaim =  // Исполняющий
                    onView(withId(R.id.executor_drop_menu_auto_complete_text_view));
            executorClaim.perform(click());
            if (claimInfo.getExecutor() == claimInfo.getAuthor()) {
                res.getItemFromList(claimInfo.getExecutor());
            } else {
                //TODO на случай возможности вставки другого исполнителя
                res.typingText(R.id.executor_drop_menu_auto_complete_text_view, claimInfo.getExecutor());
            }
            executorClaim.check(matches(withText(claimInfo.getExecutor()))); //проверка вставки
        } else {
            claimInfo.setStatus(HospiceData.claimsStatus.OPEN.getTitle()); // сохраняем статус
        }

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, claimInfo.getPlanDate());
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, claimInfo.getPlanTime());

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

    }

    public ClaimsInfo.ClaimInfo createClaim(ClaimsInfo.ClaimInfo claimInfo) {

        fillingInClaimFields(claimInfo);

        claimInfo.setCreationDate( // сохраняем дату перед сохранением заявки
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // сохраняем дату создания
        claimInfo.setCreationTime(
                LocalTime.now(Clock.system(ZoneId.of("Europe/Moscow"))).format(DateTimeFormatter.ofPattern("hh:mm"))); // сохраняем время создания

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 5000);

        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo cancellationCreateClaim(ClaimsInfo.ClaimInfo claimInfo, boolean OKCANCEL) {

        fillingInClaimFields(claimInfo);

        onView(withId(R.id.cancel_button)).perform(click());

        onView(withText("The changes won't be saved, do you really want to log out?"))
                .check(matches(isEnabled()));

        if (OKCANCEL) {
            onView(withText("OK")).perform(click());
            onView(withId(R.id.our_mission_image_button)).check(matches(isDisplayed()));
        } else {
            onView(withText("CANCEL")).perform(click());
            onView(withId(R.id.save_button)).check(matches(isDisplayed()));
            claimInfo.setCreationDate( // сохраняем дату перед сохранением заявки
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // сохраняем дату создания
            claimInfo.setCreationTime(
                    LocalTime.now(Clock.system(ZoneId.of("Europe/Moscow"))).format(DateTimeFormatter.ofPattern("hh:mm"))); // сохраняем время создания
            onView(withId(R.id.save_button)).perform(click());
            WaitId.waitId(R.id.claim_list_swipe_refresh, 5000);
        }
        return claimInfo;
    }

    private void checkFillEmptyFieldsMessage() {

        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    public void checkingEmptyFieldsWhenCreatingClaim(ClaimsInfo.ClaimInfo claimInfo) {

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

        onView(withId(R.id.cancel_button)).perform(click()); // отменяем создание
        onView(withText("OK")).perform(click());

        WaitId.waitId(R.id.main_swipe_refresh, 5000);
    }

    public void toCheckClaim(ClaimsInfo.ClaimInfo claimInfo) {
        WaitId.waitId(R.id.status_label_text_view, 10000);

        onView(allOf(withId(R.id.status_label_text_view),  //статус
                withText(claimInfo.getStatus()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title_text_view),         //заголовок
                withText(claimInfo.getTitle()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.description_text_view),   //описание
                withText(claimInfo.getDescription()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.author_name_text_view),   //автор
                withText(claimInfo.getAuthor()))).check(matches(isDisplayed()));

        if (claimInfo.getExecutor() == "") {              // исполнителя
            onView(allOf(withId(R.id.executor_name_text_view),
                    withText("NOT ASSIGNED"))).check(matches(isDisplayed()));
        } else {
            onView(allOf(withId(R.id.executor_name_text_view),
                    withText(claimInfo.getExecutor()))).check(matches(isDisplayed()));
        }

        onView(allOf(withId(R.id.create_data_text_view),   //дата создания
                withText(claimInfo.getCreationDate()))).check(matches(isDisplayed()));
        //TODO уточнить время создания и плановое
//        onView(allOf(withId(R.id.create_time_text_view),   //время создания
//                withText(claimInfo.getCreationTime()))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.plane_date_text_view),   //плановая дата
                withText(claimInfo.getCreationDate()))).check(matches(isDisplayed()));
//        onView(allOf(withId(R.id.plan_time_text_view),   //плановое время
//                withText(claimInfo.getCreationTime()))).check(matches(isDisplayed()));

        if (claimInfo.getStatus() == HospiceData.claimsStatus.OPEN.getTitle()) { // доступность смены статуса
            onView(withId(R.id.status_processing_image_button)).check(matches(isEnabled()));
            onView(withId(R.id.edit_processing_image_button)).check(matches(isEnabled()));
        } else {
            if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) {
                if (claimInfo.getExecutor() == claimInfo.getAuthor()) {
                    onView(withId(R.id.status_processing_image_button)).check(matches(isEnabled()));
                } else {
                    //TODO как проверить что кнопка отжата?
//                    onView(withId(R.id.status_processing_image_button)).check(matches(not(isEnabled())));
                }
            } else {
//                onView(withId(R.id.status_processing_image_button)).check(matches(not(isEnabled())));
            }
//            onView(withId(R.id.edit_processing_image_button)).check(matches(not(isEnabled())));
        }

        onView(withId(R.id.close_image_button)).perform(click());
    }

    public String defineRequiredMenu(ClaimsInfo.ClaimInfo claimInfo, String newStatus) {

        String statusMenu = HospiceData.claimStatusPopUpMenu.EMPTY.getTitle(); //устанавливаем недоступный вариант.
        if (claimInfo.getStatus() == HospiceData.claimsStatus.OPEN.getTitle()) {
            if (newStatus == HospiceData.claimsStatus.WORK.getTitle()) {
                statusMenu = HospiceData.claimStatusPopUpMenu.TOWORK.getTitle();
                claimInfo.setExecutor(claimInfo.getAuthor()); // при смене статуса на в работе меняется исполнитель. добавляем его.
            }
            if (newStatus == HospiceData.claimsStatus.CANCEL.getTitle()) {
                statusMenu = HospiceData.claimStatusPopUpMenu.CANCEL.getTitle();
            }
        }
        if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) {
            if (newStatus == HospiceData.claimsStatus.OPEN.getTitle()) {
                statusMenu = HospiceData.claimStatusPopUpMenu.THROWOFF.getTitle();
            }
            if (newStatus == HospiceData.claimsStatus.EXEC.getTitle()) {
                statusMenu = HospiceData.claimStatusPopUpMenu.EXECUTE.getTitle();
            }
        }
        return statusMenu;
    }

    public ClaimsInfo.ClaimInfo toChangeStatusClaim(ClaimsInfo.ClaimInfo claimInfo, String newStatus, boolean comment) {

        String statusMenu = defineRequiredMenu(claimInfo, newStatus); //определяем необходимый пункт меню
        if (statusMenu == HospiceData.claimStatusPopUpMenu.EMPTY.getTitle()) {
            // Проверяем что кнопка недоступна - можно вынести в отдельный метод.
            onView(withId(R.id.status_processing_image_button))
                    .check(matches(isEnabled())).perform(click());
            WaitId.waitId(R.id.status_icon_image_view, 3000);
        } else {
            onView(withId(R.id.status_processing_image_button))
                    .perform(click());
            res.getItemFromList(statusMenu);
            if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) {
                // переход из  "В работе" на другой статус с комментарием
                if (comment) { // если пишем комментарий
                    res.typingText(R.id.editText, faker.bothify("Boniface???#??#??#??#"));
                    claimInfo.setStatus(newStatus); // переопределяем статус у заявки.
                    onView(withId(android.R.id.button1)).perform(click());
                } else { // если не пишем комметарий. статус не меняется.
                    onView(withId(android.R.id.button2)).perform(click());
                }
            } else {
                claimInfo.setStatus(newStatus); // переопределяем статус у заявки, если нет комментария
            }
        }
        onView(withId(R.id.close_image_button)).perform(click());
        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo writeComment(ClaimsInfo.ClaimInfo claimInfo, boolean okCancel) {

        onView(withId(R.id.add_comment_image_button)).perform(click());
        String comment = faker.bothify("Elena???#??#??#??#??#???");
        res.typingTextWithParent(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            claimInfo.setNumberOfComments(claimInfo.getNumberOfComments() + 1); //счетчик комментариев
            checkComment(claimInfo, comment);
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
        }
        onView(withId(R.id.close_image_button)).perform(click());
        return claimInfo;
    }

    public void editComment(ClaimsInfo.ClaimInfo claimInfo, boolean okCancel) {

        onView(withId(R.id.edit_comment_image_button)).perform(click());
        String comment = faker.bothify("Elena???#??#??#??#??#???");
        res.typingTextWithParentWithClear(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            checkComment(claimInfo, comment);
        } else {
            // TODO как "вытащить" старый комментарий для проверки отказа?
            onView(withId(R.id.cancel_button)).perform(click());
        }
        onView(withId(R.id.close_image_button)).perform(click());
    }

    public void checkComment(ClaimsInfo.ClaimInfo claimInfo, String comment) {

        if (claimInfo.getNumberOfComments() > 1) {
            ViewInteraction recyclerView = onView(withId(R.id.claim_comments_list_recycler_view));
            recyclerView.perform(swipeUp());
            onView(withText(comment)).check(matches(isDisplayed()));
            recyclerView.check(
                    matches(CustomViewMatcher.recyclerViewSizeMatcher(claimInfo.getNumberOfComments())));
        } else {
            onView(withText(comment)).check(matches(isDisplayed()));
            onView(withId(R.id.commentator_name_text_view)).check(matches(withText(claimInfo.getAuthor())));
        }
    }

    public ClaimsInfo.ClaimInfo editTitleAndDescriptionInClaim(ClaimsInfo.ClaimInfo claimInfo, boolean saveCancel) {

        onView(withId(R.id.edit_processing_image_button)).perform(click());
        WaitId.waitId(R.id.title_text_input_layout, 3000);

        String title = faker.bothify("Tatiana???#??#??#??#??#???");
        res.typingTextWithClear(R.id.title_edit_text, title); //Заголовок

        String descript = faker.bothify("Tatiana???#??#??#??#??#???");
        res.typingTextWithClear(R.id.description_edit_text, descript); // Описание

        if (saveCancel) {
            onView(withId(R.id.save_button)).perform(click());
            claimInfo.setTitle(title);
            claimInfo.setDescription(descript);
        } else { // щелкаем по кнопкам отказа
            onView(withId(R.id.cancel_button)).perform(click());
            onView(withId(android.R.id.button2)).perform(click());
            onView(withId(R.id.cancel_button)).perform(click());
            onView(withId(android.R.id.button1)).perform(click());
        }

        WaitId.waitId(R.id.close_image_button, 3000);
        onView(withId(R.id.close_image_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);

        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo changeExecutor(ClaimsInfo.ClaimInfo claimInfo, String executor) {

        WaitId.waitId(R.id.executor_name_text_view, 5000);
//        onView(allOf(withId(R.id.executor_name_text_view), withText("NOT ASSIGNED")))
//                .check(matches(isDisplayed()));

        onView(withId(R.id.edit_processing_image_button))
                .check(matches(isEnabled())).perform(click());
        WaitId.waitId(R.id.executor_drop_menu_auto_complete_text_view, 5000);

        onView(withId(R.id.executor_drop_menu_auto_complete_text_view)).perform(click());
        res.getItemFromList(executor);
        closeSoftKeyboard();

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.status_label_text_view, 10000);

        //TODO не сработало
//        onView(allOf(withId(R.id.status_label_text_view), withText("In progress")))
//                .check(matches(isDisplayed()));

        onView(withId(R.id.close_image_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 5000);

        claimInfo.setExecutor(claimInfo.getAuthor());
        claimInfo.setStatus(HospiceData.claimsStatus.WORK.getTitle());
        return claimInfo;
    }

    public void editDataTime(String data, String time) {

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, data);
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, time);

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 3000);
    }
}
