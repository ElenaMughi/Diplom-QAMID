package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;

public class LoveIsAllPageFragment {

    public void checkQuote() {

        onView(allOf(withId(R.id.our_mission_item_open_card_image_button),
                hasSibling(withText(HospiceData.OurMissionData.OneTitle.getTitle()))))
                .perform(click());

        onView(allOf(withId(R.id.our_mission_item_description_text_view),
                hasSibling(withText(HospiceData.OurMissionData.OneTitle.getTitle()))))
                .check(matches(withText(HospiceData.OurMissionData.OneDiscript.getTitle())));

        onView(allOf(withId(R.id.our_mission_item_open_card_image_button),
                hasSibling(withText(HospiceData.OurMissionData.TwoTitle.getTitle()))))
                .perform(click());

        onView(allOf(withId(R.id.our_mission_item_description_text_view),
                hasSibling(withText(HospiceData.OurMissionData.TwoTitle.getTitle()))))
                .check(matches(withText(HospiceData.OurMissionData.TwoDiscript.getTitle())));

    }
}
