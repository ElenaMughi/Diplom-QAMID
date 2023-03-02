package ru.netology.data;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NewsInfo {

    static Faker faker = new Faker();

    public static NewInfo getNewInfo(String category, boolean NewsActive) {
        String title = faker.bothify("Winnie???#??#??#??#");
        String datePublish = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timePublish = LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"));
        String descript = faker.bothify("Boniface???#??#??#??#");
        boolean active = NewsActive;
        return new NewInfo(category, title, datePublish, timePublish, descript, active,
                HospiceData.authorNews.IVANOV.getTitle());
    }

    public static NewInfo getNewInfoWithTitleAndDescr(String title, String descript) {
        String datePublish = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timePublish = LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"));
        return new NewInfo(HospiceData.newsCategory.Help.getTitle(),
                title, datePublish, timePublish, descript, true,
                HospiceData.authorNews.IVANOV.getTitle());
    }

    public static NewInfo getNewsInfoDateTimeChoice(String date, String time) {
        String title = faker.bothify("Winnie???#??#??#??#");
        String descript = faker.bothify("Boniface???#??#??#??#");
        return new NewInfo(HospiceData.newsCategory.Gratitude.getTitle(),
                title, date, time, descript, true,
                HospiceData.authorNews.IVANOV.getTitle());
    }
    public static class NewInfo {
        private String category;
        private String title;
        private String dateNews;
        private String timeNews;
        private String description;
        private boolean active;
        private String creationDate;
        private String author;


        public NewInfo(String category, String title, String dateNews, String timeNews, String description, boolean active, String author) {
            this.category = category;
            this.title = title;
            this.dateNews = dateNews;
            this.timeNews = timeNews;
            this.description = description;
            this.active = active;
            this.author = author;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) { this.category = category; }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) { this.title = title; }

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

        public void setDescription(String description) { this.description = description; }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCreationDate() { return creationDate; }

        public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

        public String getAuthor() { return author; }
    }
}
