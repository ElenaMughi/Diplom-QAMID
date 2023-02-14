package ru.netology.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NewsInfo {

    public static NewInfo getNewInfo(int numCategory, boolean NewsActive) {
        String title = HospiceData.titles[1] + Math.round(Math.random() * 1000);
        String datePublish = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timePublish = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        String descript = HospiceData.descriptions[1] + Math.round(Math.random() * 1000);
        boolean active = NewsActive;
        return new NewInfo(HospiceData.newsCategory[numCategory - 1], title, datePublish, timePublish, descript, active);
    }

    public static NewInfo getNewInfoWithTitleAndDescr(String title, String descript) {
        title = title + Math.round(Math.random() * 10);
        String datePublish = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timePublish = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        descript = descript + Math.round(Math.random() * 10);
        return new NewInfo(HospiceData.newsCategory[4], title, datePublish, timePublish, descript, true);
    }

    public static NewInfo getNewsInfoDateTimeChoice(String date, String time) {
        String title = HospiceData.titles[1] + Math.round(Math.random() * 1000);
        String descript = HospiceData.descriptions[1] + Math.round(Math.random() * 1000);
        return new NewInfo(HospiceData.newsCategory[5], title, date, time, descript, true);
    }
    public static class NewInfo {
        private String category;

        private String title;
        private String dateNews;
        private String timeNews;
        private String description;
        private boolean active;
        private String creationDate;


        public NewInfo(String category, String title, String dateNews, String timeNews, String description, boolean active) {
            this.category = category;
            this.title = title;
            this.dateNews = dateNews;
            this.timeNews = timeNews;
            this.description = description;
            this.active = active;
        }

        public String getCategory() {
            return category;
        }

        public String getTitle() {
            return title;
        }

        public String getDateNews() {
            return dateNews;
        }

        public String getTimeNews() {
            return timeNews;
        }

        public void setDateNews(String dateNews) { this.dateNews = dateNews; }

        public void setTimeNews(String timeNews) { this.timeNews = timeNews; }

        public String getDescription() {
            return description;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCreationDate() { return creationDate; }

        public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    }
}
