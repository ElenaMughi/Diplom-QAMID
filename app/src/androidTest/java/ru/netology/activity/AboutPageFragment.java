package ru.netology.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import ru.iteco.fmhandroid.R;
import ru.netology.data.HospiceData;

public class AboutPageFragment {

    public void checkPage() {
        onView(allOf(withId(R.id.about_version_value_text_view),
                withText(HospiceData.aboutPageData.Version.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.about_privacy_policy_value_text_view),
                withText(HospiceData.aboutPageData.PrivacyPolicy.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.about_terms_of_use_value_text_view),
                withText(HospiceData.aboutPageData.TermsOfUse.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.about_company_info_label_text_view),
                withText(HospiceData.aboutPageData.ITeco.getTitle())))
                .check(matches(isDisplayed()));

        onView(withId(R.id.about_back_image_button)).perform(click());
    }
}
