package ru.netology.data;

import java.time.LocalDate;

public class HospiceData {
    // fio[0] - должно быть связано с логином login2, fio[1] - пустое/без исполнителя/.
    public static final String fio[] =
            {"Ivanov Ivan Ivanovich",
                    "",
                    "Petrov Petr Petrovich"};

    public static final String comment[] =
            {"So Long, and Thanks for All the Fish",
                    "Let's fun!",
                    "Simple comment",
                    "Try again",
                    "We will win!"};

    public static final String SymbolsAndLetters[] =
            {"nmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP",
                    "zxcvb1234567890<>?,./{}[]!@#$%^&*()_+=-;:'|",
                    "",
                    "1234567890123456789012345678901234567890123456789",
                    "ячсмитьбюфывапролджэйцукенгшщзхъё",
                    "ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ"};

    public static final String[] titles = {
            "Ok",
            "J. R. R. Tolkien ",
            "Terry Pratchett "};

    public static final String[] descriptions = {
            "No",
            "The lord of The ring ",
            "Discworld "};

    public static final String testingTime[] = {"08:00", "14:36", "23:59", "00:00", "05:07"};
    public static final String claimStatus[] = {"Open", "In progress", "Executed", "Canceled"};
    public static final String claimStatusListMenu[] = {"take to work", "Cancel", "Throw off", "To execute", ""};
    public static final String newsCategory[] = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};



    public static String setData(int day, int month, int year) {
        //дает дату относительно текущей
//            String dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); // для поверки даты создания
//            String timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")); // для поверки времени создания
        String mm = Integer.toString(LocalDate.now().getMonthValue() + month);
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        return (LocalDate.now().getDayOfMonth() + day) + "."
                + mm + "."
                + (LocalDate.now().getYear() + year);
    }
}
