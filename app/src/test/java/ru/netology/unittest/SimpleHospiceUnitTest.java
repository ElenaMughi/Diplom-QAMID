package ru.netology.unittest;

import org.junit.After;
import org.junit.Before;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleHospiceUnitTest {

    @Before
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @After
    static void forClose() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
