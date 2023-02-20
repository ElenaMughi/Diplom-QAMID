package ru.netology.resourses;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

public class CustomViewAssertions {
    public static ViewAssertion isRecyclerView() {
        return new ViewAssertion() {
//    OnView(withId(R.id.recycler_view)).check(CustomViewAssertions.isRecyclerView()); //    проверить что список

            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                try {
                    RecyclerView recyclerView = (RecyclerView) view;
                } catch (ClassCastException cce) {
                    throw new IllegalStateException("This is not a RecyclerView");
                }
            }
        };
    }
}