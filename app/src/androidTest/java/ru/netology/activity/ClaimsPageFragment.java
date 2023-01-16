package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceInfo;

public class ClaimsPageFragment {

    protected void getExecutorFromList(String text) {
        onView(withText(text))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

    private void typingText(int id, String text) {
        ViewInteraction textClaim =  //Заголовок
                onView(withId(id)).perform(click());
        textClaim.perform(typeText(text), closeSoftKeyboard());
    }

    public void createClaim(HospiceInfo.ClaimInfo claimInfo) {

        typingText(R.id.title_edit_text, claimInfo.getTitle()); //Заголовок

        ViewInteraction executorClaim =  // Исполняющий
                onView(withId(R.id.executor_drop_menu_auto_complete_text_view));
        executorClaim.perform(click());
        getExecutorFromList(claimInfo.getExecutor());
        executorClaim.check(matches(withText(claimInfo.getExecutor())));

        //TODO Calendar выбор даты-времени не реализован
        onView(withId(R.id.date_in_plan_text_input_edit_text)).perform(click()); // Дата
        Intents.init();
        onView(withId(android.R.id.button1)).perform(click());
        Intents.release();

        onView(withId(R.id.time_in_plan_text_input_edit_text)).perform(click()); // Время
        Intents.init();
        onView(withId(android.R.id.button1)).perform(click());
        Intents.release();

        typingText(R.id.description_edit_text, claimInfo.getDescription()); // Описание

        onView(withId(R.id.save_button)).perform(click());
    }

    public void toFoundClaim(HospiceInfo.ClaimInfo claimInfo) throws Exception {
//TODO
    }

    public void addCommentToClaim(HospiceInfo.ClaimInfo claimInfo, String comment) throws Exception {
        toFoundClaim(claimInfo);

//TODO
    }
}
