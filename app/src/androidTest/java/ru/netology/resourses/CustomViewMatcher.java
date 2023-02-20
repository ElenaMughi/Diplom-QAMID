package ru.netology.resourses;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomViewMatcher {

    public static Matcher<View> recyclerViewSizeMatcher(final int matcherSize) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
//    ViewInteraction recycleView =
//            onView(CustomViewMatcher.recyclerViewSizeMatcher(10));// Ожидаемое кол-во элементов
//    ViewInteraction recycleView = onView(withId(R.id.recycler_view));
//    recycleView.check(
//    matches(CustomViewMatcher.recyclerViewSizeMatcher(10)) // Проверяем ожидаемое кол-во элементов
//    recyclerView.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(claimInfo.getTitle()))));

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) { // Проверка
                return matcherSize == recyclerView.getAdapter().getItemCount();
            }

            @Override
            public void describeTo(Description description) { // Доп. описание ошибки
                description.appendText("Item count: " + matcherSize);
            }

        };
    }
}
