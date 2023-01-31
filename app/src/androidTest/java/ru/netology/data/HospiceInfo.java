package ru.netology.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HospiceInfo {

    // fio[0] - должно быть связано с логином login2, fio[1] - пустое/без исполнителя/.
    public static final String fio[] = {"Ivanov Ivan Ivanovich", "", "Petrov Petr Petrovich", "Sidorov Sidor Sidorovich"};
    public static final String comment[] = {"So Long, and Thanks for All the Fish", "Let's fun!", "Simple comment", "Try again", "We will win!"};
    public static final String claimStatus[] = {"Open", "In progress", "Executed", "Canceled"};
    //    public static final String claimStatusForFilter[] = {"Open", "In progress", "Executed", "Cancelled"};
    public static final String claimStatusListMenu[] = {"take to work", "Cancel", "Throw off", "To execute", ""};

    public static final String newsCategory[] = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};

    public static LogInfo getLogInfo() {
        return new LogInfo("login2", "password2");
    }

    public static LogInfo getWrongLogInfo() {
        return new LogInfo("login", "password");
    }

    public static LogInfo getLogInfoWithLoginEmpty() {
        return new LogInfo("", "password2");
    }

    public static LogInfo getLogInfoWithPasswordEmpty() {
        return new LogInfo("login2", "");
    }

    public static ClaimInfo getClaimInfo(int i) {
        String title = "Adams Douglas " + Math.round(Math.random() * 1000);
        String discr = "The Hitchhiker's Guide to the Galaxy " + Math.round(Math.random() * 1000);
        String dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        return new ClaimInfo(title, fio[i - 1], dateText, timeText, discr, fio[0]);
    }

    public static NewsInfo getNewInfo(int numCategory, boolean NewsActive) {
        String title = "Terry Pratchett " + Math.round(Math.random() * 1000);
        String datePublish = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timePublish = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        String description = "Discworld " + Math.round(Math.random() * 1000);;
        boolean active = NewsActive;
        return new NewsInfo(newsCategory[numCategory-1], title, datePublish, timePublish, description, active);
    }

    public static class LogInfo {
        private String login;
        private String password;

        public LogInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class ClaimInfo {
        private String title;

        private String executor;
        private String dateClaim;
        private String timeClaim;
        private String description;
        private String author;

        public ClaimInfo(String title, String executor, String dateClaim, String timeClaim, String description, String author) {
            this.title = title;
            this.executor = executor;
            this.dateClaim = dateClaim;
            this.timeClaim = timeClaim;
            this.description = description;
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getExecutor() {
            return executor;
        }

        public String getDateClaim() {
            return dateClaim;
        }

        public String getTimeClaim() {
            return timeClaim;
        }

        public String getDescription() {
            return description;
        }

        public void setExecutor(String executor) {
            this.executor = executor;
        }
    }

    public static class NewsInfo {
        private String category;

        private String title;
        private String dateNews;
        private String timeNews;
        private String description;
        private boolean active;

        public NewsInfo(String category, String title, String dateNews, String timeNews, String description, boolean active) {
            this.category = category;
            this.title = title;
            this.dateNews = dateNews;
            this.timeNews = timeNews;
            this.description = description;
            this.active = active;
        }

        public String getCategory() { return category; }

        public String getTitle() { return title; }

        public String getDateNews() { return dateNews; }

        public String getTimeNews() { return timeNews; }

        public String getDescription() { return description; }

        public boolean isActive() { return active; }

        public void setActive(boolean active) { this.active = active; }
    }
}
