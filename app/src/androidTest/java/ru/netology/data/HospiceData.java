package ru.netology.data;

import java.time.LocalDate;

public class HospiceData {

    // fio.IVANOV - должно быть связано с логином login2
    public enum fio {
        IVANOV("Ivanov Ivan Ivanovich"),
        EMPTY("");
//        PETROV("Petrov Petr Petrovich");

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

        public String getTitle() {
            return title;
        }
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
        EnglishAndSymbol40("nmasdfghjklqwertyuiopzxcvb{}[]();:'|+=-_"),
        EnglishUpAndSymbol40("ZXCVBNMASDFGHJKLQWERTYUIOP<>?,./!@#$%^&*"),
        OneSymbol("A"),
        Letters40("1234567890123456789012345678901234567890"),
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

    public enum aboutPageData {
        Version("1.0.0"),
        PrivacyPolicy("https://vhospice.org/#/privacy-policy/"),
        TermsOfUse("https://vhospice.org/#/terms-of-use"),
        ITeco("© I-Teco, 2022");

        private String title;

        aboutPageData(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
        }

    public enum OurMissionData {
        OneTitle("«Хоспис для меня - это то, каким должен быть мир.\""),
        OneDiscript("\"Ну, идеальное устройство мира в моих глазах. Где никто " +
                "не оценивает, никто не осудит, где говоришь, и тебя слышат, где, " +
                "если страшно, тебя обнимут и возьмут за руку, а если холодно тебя " +
                "согреют.” Юля Капис, волонтер"),
        TwoTitle("Хоспис в своем истинном понимании - это творчество"),
        TwoDiscript("Нет шаблона и стандарта, есть только дух, который живет в " +
                "разных домах по-разному. Но всегда он добрый, любящий и помогающий.");

        private String title;

        OurMissionData(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
