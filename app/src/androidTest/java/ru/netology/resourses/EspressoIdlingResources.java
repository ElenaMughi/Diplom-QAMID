package ru.netology.resourses;

import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResources {
    //EspressoIdlingResources.increment();// Увеличили счетчик...
//// Сложные операции требующие время ;)
// EspressoIdlingResources.decrement();// Уменьшили счетчик
    private static final String RESOURCE = "GLOBAL";
    public static CountingIdlingResource idlingResource = new CountingIdlingResource(RESOURCE);

    public static void increment() {
        idlingResource.increment();
    }

    public static void decrement() {
        if (!idlingResource.isIdleNow()) {
            idlingResource.decrement();
        }
    }
}
