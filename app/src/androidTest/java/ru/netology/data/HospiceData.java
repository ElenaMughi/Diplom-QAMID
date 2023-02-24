package ru.netology.data;

import java.time.LocalDate;

public class HospiceData {

    // fio.IVANOV - должно быть связано с логином login2
    public enum fio {
        IVANOV("Ivanov Ivan Ivanovich"),
        EMPTY(""),
        PETROV("Petrov Petr Petrovich");

        private String title;

        fio(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum authorNews {
        IVANOV("Ivanov I.I.");

        private String title;

        authorNews(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum claimsStatus {
        OPEN("Open"),
        WORK("In progress"),
        EXEC("Executed"),
        CANCEL("Canceled");

        private String title;

        claimsStatus(String title) {
            this.title = title;
        }

        public String getTitle() { return title; }
    }

    public enum claimStatusPopUpMenu {
        TOWORK("take to work"),
        CANCEL("Cancel"),
        THROWOFF("Throw off"),
        EXECUTE("To execute"),
        EMPTY("");

        private String title;

        claimStatusPopUpMenu(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum newsCategory {
        Announcement("Объявление"),
        Birthday("День рождения"),
        Salary("Зарплата"),
        Union("Профсоюз"),
        Holiday("Праздник"),
        Massage("Массаж"),
        Gratitude("Благодарность"),
        Help("Нужна помощь");

        private String title;

        newsCategory(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum SymbolsAndLetters {
        EnglishAndNumAndSymbol("nmasdfghjklqwertyuiopzxcvb1234567890{}[]()"),
        EnglishUpAndSymbol("ZXCVBNMASDFGHJKLQWERTYUIOP<>?,./!@#$%^&*_+=-;:'|"),
        OneSymbol("A"),
        Letters50("12345678901234567890123456789012345678901234567890"),
        Russian("ячсмитьбюфывапролджэйцукенгшщзхъё"),
        RussianUpper("ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ");

        private String title;

        SymbolsAndLetters(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

}
