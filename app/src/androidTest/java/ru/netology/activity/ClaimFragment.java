package ru.netology.activity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import static io.qameta.allure.kotlin.Allure.step;
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

    public void closeClaim() {
        step("Закрыть заявку.");
        onView(withId(R.id.close_image_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 10000);
    }

    public ClaimsInfo.ClaimInfo fillingInClaimFields(ClaimsInfo.ClaimInfo claimInfo) {
        step("Заполнение полей заявки.");
        onView(isRoot()).perform(waitId(R.id.title_edit_text, 10000));
        res.typingText(R.id.title_edit_text, claimInfo.getTitle()); //Заголовок

        if (claimInfo.getExecutor().length() > 0) {
            claimInfo.setStatus(HospiceData.claimsStatus.WORK.getTitle()); // сохраняем статус
            ViewInteraction executorClaim =  // Исполняющий
                    onView(withId(R.id.executor_drop_menu_auto_complete_text_view));
            executorClaim.perform(click());
            if (claimInfo.getExecutor() == claimInfo.getAuthor()) {
                res.getItemFromList(claimInfo.getExecutor());
            } else {
                //TODO на случай возможности вставки другого исполнителя - в текущей версии не используется
                res.typingText(R.id.executor_drop_menu_auto_complete_text_view, claimInfo.getExecutor());
            }
            executorClaim.check(matches(withText(claimInfo.getExecutor()))); //проверка вставки
        } else {
            claimInfo.setStatus(HospiceData.claimsStatus.OPEN.getTitle()); // сохраняем статус
        }

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, claimInfo.getPlanDate());
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, claimInfo.getPlanTime());

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo createClaim(ClaimsInfo.ClaimInfo claimInfo) {
        step("Создать заявку.");
        claimInfo = fillingInClaimFields(claimInfo);

        claimInfo.setCreationDate( // сохраняем дату перед сохранением заявки
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // сохраняем дату создания
        claimInfo.setCreationTime(
                LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"))); // сохраняем время создания

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.claim_list_swipe_refresh, 10000);

        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo cancellationCreateClaim(ClaimsInfo.ClaimInfo claimInfo, boolean saveCancel) {
        step("Проверка отмены создания заявки.");
        fillingInClaimFields(claimInfo); // заполнение полей

        onView(withId(R.id.cancel_button)).perform(click());
        onView(withText("The changes won't be saved, do you really want to log out?"))
                .check(matches(isEnabled()));

        if (saveCancel) {
            onView(withText("CANCEL")).perform(click());
            claimInfo.setCreationDate( // сохраняем дату перед сохранением заявки
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // сохраняем дату создания
            claimInfo.setCreationTime(
                    LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"))); // сохраняем время создания
            onView(withId(R.id.save_button)).perform(click());
            WaitId.waitId(R.id.claim_list_swipe_refresh, 5000);
        } else { // подтверждаем отмену
            onView(withText("OK")).perform(click());
            onView(withId(R.id.our_mission_image_button)).check(matches(isDisplayed()));
        }
        return claimInfo;
    }

    private void checkFillEmptyFieldsMessage() {

        onView(withId(R.id.save_button)).perform(click());
        onView(withText("Fill empty fields")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
    }

    public ClaimsInfo.ClaimInfo checkingEmptyFieldsWhenCreatingClaim(ClaimsInfo.ClaimInfo claimInfo) {
        step("Проверка заполения полей заявки.");
        WaitId.waitId(R.id.title_edit_text, 5000);

        checkFillEmptyFieldsMessage();

        res.typingText(R.id.title_edit_text, claimInfo.getTitle());
        checkFillEmptyFieldsMessage();

        //без исполнителя

        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата из календаря
        onView(withId(android.R.id.button1)).perform(click());
        checkFillEmptyFieldsMessage();

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время из календаря
        onView(withId(android.R.id.button1)).perform(click());
        checkFillEmptyFieldsMessage();

        res.typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание
        res.typingTextWithClear(R.id.title_edit_text, "");
        checkFillEmptyFieldsMessage();

        res.typingTextWithClear(R.id.title_edit_text, claimInfo.getTitle());
        onView(withId(R.id.save_button)).perform(click());

        WaitId.waitId(R.id.claim_list_swipe_refresh, 10000);

        claimInfo.setStatus(HospiceData.claimsStatus.OPEN.getTitle()); // сохраняем статус
        claimInfo.setCreationDate( // сохраняем дату перед сохранением заявки
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // сохраняем дату создания
        claimInfo.setCreationTime(
                LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"))); // сохраняем время создания

        return claimInfo;
    }

    public void checkClaimFields(ClaimsInfo.ClaimInfo claimInfo) {
        step("Проверка полей заявки.");
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

        onView(allOf(withId(R.id.create_time_text_view),   //время создания
                withText(claimInfo.getCreationTime()))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.plane_date_text_view),   //плановая дата
                withText(claimInfo.getPlanDate()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.plan_time_text_view),   //плановое время
                withText(claimInfo.getPlanTime()))).check(matches(isDisplayed()));

        if (claimInfo.getStatus() == HospiceData.claimsStatus.OPEN.getTitle()) { // доступность смены статуса
            onView(withId(R.id.status_processing_image_button)).check(matches(isClickable()));
            onView(withId(R.id.edit_processing_image_button)).check(matches(isClickable()));
        } else {
            if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) {
                if (claimInfo.getExecutor() == claimInfo.getAuthor()) {
                    onView(withId(R.id.status_processing_image_button)).check(matches(isClickable()));
                    onView(withId(R.id.edit_processing_image_button)).check(matches(isClickable())); //
                } else {
                    onView(withId(R.id.status_processing_image_button)).check(matches(isClickable())); //
                    onView(withId(R.id.edit_processing_image_button)).check(matches(isClickable())); //
                }
            } else {
                onView(withId(R.id.status_processing_image_button)).check(matches(isClickable())); //
                onView(withId(R.id.edit_processing_image_button)).check(matches(isClickable())); //
            }
            onView(withId(R.id.edit_processing_image_button)).check(matches(isClickable())); //
        }
        closeClaim();
    }

    public String defineRequiredMenu(ClaimsInfo.ClaimInfo claimInfo, String newStatus) {
        step("Определение необходимого пункта меню смены статуса.");
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
        // переход из  "В работе" на другой статус с комментарием. Для остальных параметр comment не используется и значение не важно.
        step("Смена статуса заявки.");
        WaitId.waitId(R.id.status_processing_image_button, 10000);
        String statusMenu = defineRequiredMenu(claimInfo, newStatus); //определяем необходимый пункт меню

        onView(withId(R.id.status_processing_image_button)).perform(click());
        res.getItemFromList(statusMenu);

        if (claimInfo.getStatus() == HospiceData.claimsStatus.WORK.getTitle()) {
            if (comment) { // если пишем комментарий
                res.typingText(R.id.editText, faker.bothify("Boniface ???#??#??#??#"));
                claimInfo.setStatus(newStatus); // переопределяем статус у заявки.
                if (newStatus == HospiceData.claimsStatus.OPEN.getTitle()){
                    claimInfo.setExecutor(""); //обнуляем исполнителя
                }
                onView(withId(android.R.id.button1)).perform(click());
                WaitId.waitId(R.id.status_icon_image_view, 5000);
            } else { // если не пишем комметарий. статус не меняется.
                onView(withId(android.R.id.button2)).perform(click());
                WaitId.waitId(R.id.status_icon_image_view, 5000);
            }
        } else {
            claimInfo.setStatus(newStatus); // переопределяем статус у заявки, если нет комментария
        }
        closeClaim();
        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo writeComment(ClaimsInfo.ClaimInfo claimInfo, String comment,
                                             boolean okCancel) {
        step("Создание комментария к заявке.");
        onView(withId(R.id.add_comment_image_button)).perform(click());
        res.typingTextWithParent(R.id.comment_text_input_layout, comment);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
            claimInfo.setNumberOfComments(claimInfo.getNumberOfComments() + 1); //счетчик комментариев
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
        }

        return claimInfo;
    }

    public void editComment(String comment, String comment2, boolean okCancel) {
        step("Редактирование комментария к заявке.");
        onView(allOf(withId(R.id.edit_comment_image_button),   //плановая дата
                hasSibling(withText(comment)))).perform(click());

        res.typingTextWithParentWithClear(R.id.comment_text_input_layout, comment2);
        if (okCancel) {
            onView(withId(R.id.save_button)).perform(click());
        } else {
            onView(withId(R.id.cancel_button)).perform(click());
        }
    }

    public void checkComment(ClaimsInfo.ClaimInfo claimInfo, String comment) {
        step("Проверка комментария к заявке.");
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

    public ClaimsInfo.ClaimInfo editTitleAndDescriptionInClaim(ClaimsInfo.ClaimInfo claimInfo,
                                                               boolean saveCancel) {
        step("Редактирования названия и описания заявки.");
        onView(withId(R.id.edit_processing_image_button)).perform(click());
        WaitId.waitId(R.id.title_text_input_layout, 3000);

        String title = faker.bothify("Tatiana ???#??#??#??#??#???");
        res.typingTextWithClear(R.id.title_edit_text, title); //Заголовок

        String descript = faker.bothify("Tatiana ???#??#??#??#??#???");
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

        closeClaim();

        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo changeExecutor(ClaimsInfo.ClaimInfo claimInfo, String executor) {
        step("Смена исполнителя у заявки.");
        WaitId.waitId(R.id.executor_name_text_view, 5000);

        onView(withId(R.id.edit_processing_image_button))
                .check(matches(isEnabled())).perform(click());
        WaitId.waitId(R.id.executor_drop_menu_auto_complete_text_view, 5000);

        onView(withId(R.id.executor_drop_menu_auto_complete_text_view)).perform(click());
        res.getItemFromList(executor);
        closeSoftKeyboard();

        onView(withId(R.id.save_button)).perform(click());
        WaitId.waitId(R.id.status_label_text_view, 10000);

        closeClaim();

        claimInfo.setExecutor(claimInfo.getAuthor());
        claimInfo.setStatus(HospiceData.claimsStatus.WORK.getTitle());
        return claimInfo;
    }

    public ClaimsInfo.ClaimInfo editDataTime(ClaimsInfo.ClaimInfo claimInfo, String data, String time) {
        step("Изменение даты и времени заявки.");
        onView(withId(R.id.edit_processing_image_button)).perform(click());
        WaitId.waitId(R.id.title_text_input_layout, 3000);

        SetDataTime.setDate(R.id.date_in_plan_text_input_edit_text, data);
        SetDataTime.setTime(R.id.time_in_plan_text_input_edit_text, time);

        onView(withId(R.id.save_button)).perform(click());
        claimInfo.setPlanDate(data);
        claimInfo.setPlanTime(time);

        closeClaim();
        return  claimInfo;
    }
}
