package ru.netology.resourses;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;

public class ForAllFunk {

    public void typingText(int id, String text) {
        ViewInteraction textClaim =  //Заголовок
                onView(withId(id)).perform(click());
        textClaim.perform(typeText(text), closeSoftKeyboard());
    }

    ;

    public void typingTextWithClear(int id, String text) {
        ViewInteraction textClaim =  //Заголовок
                onView(withId(id)).perform(click());
        textClaim.perform(clearText());
        textClaim.perform(typeText(text), closeSoftKeyboard());
    }

    ;

    public void typingTextWithParent(int parentId, String text) {
        onView(withId(parentId)).perform(click());
        ViewInteraction element = onView(
                allOf(
                        withClassName(endsWith("EditText")),
                        withParent(withParent(withId(parentId)))
                )
        );
        element.perform(typeText(text), closeSoftKeyboard());
    }

    ;

    public void typingTextWithParentWithClear(int parentId, String text) {
        onView(withId(parentId)).perform(click());
        ViewInteraction element = onView(
                allOf(
                        withClassName(endsWith("EditText")),
                        withParent(withParent(withId(parentId)))
                )
        );
        element.perform(clearText());
        element.perform(typeText(text), closeSoftKeyboard());
    }

    public void getItemFromList(String text) {
        onView(withText(text))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

    public void setUpFilter(boolean check) {
        onView(withId(R.id.filters_material_button)).perform(click());
        // снимаем все галочки в фильтре
        onView(withId(R.id.item_filter_open)).perform(scrollTo(), CustomSetChecked.setChecked(check));
        onView(withId(R.id.item_filter_in_progress)).perform(scrollTo(), CustomSetChecked.setChecked(check));
        onView(withId(R.id.item_filter_executed)).perform(scrollTo(), CustomSetChecked.setChecked(check));
        onView(withId(R.id.item_filter_cancelled)).perform(scrollTo(), CustomSetChecked.setChecked(check));

        onView(withId(R.id.claim_list_filter_ok_material_button)).perform(click());

    }

    public String choiceStatus(HospiceInfo.ClaimInfo claimInfo, String oldStatus, String newStatus) {
        onView(allOf(withId(R.id.status_label_text_view), withText(oldStatus))).check(matches(isDisplayed()));

        String statusMenu = HospiceInfo.claimStatusListMenu[4]; //устанавливаем недоступный вариант.
        if (oldStatus == HospiceInfo.claimStatus[0]) {
            if (newStatus == HospiceInfo.claimStatus[1]) {
                statusMenu = HospiceInfo.claimStatusListMenu[0];
            }
            if (newStatus == HospiceInfo.claimStatus[3]) {
                statusMenu = HospiceInfo.claimStatusListMenu[1];
                claimInfo.setExecutor(claimInfo.getAuthor()); // при смене статуса на в работе меняется исполнитель. добавляем его.
            }
        }
        if (oldStatus == HospiceInfo.claimStatus[1]) {
            if (newStatus == HospiceInfo.claimStatus[0]) {
                statusMenu = HospiceInfo.claimStatusListMenu[2];
            }
            if (newStatus == HospiceInfo.claimStatus[2]) {
                statusMenu = HospiceInfo.claimStatusListMenu[3];
            }
        }
        return statusMenu;
    }
}
