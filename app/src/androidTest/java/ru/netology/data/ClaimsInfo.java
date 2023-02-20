package ru.netology.data;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClaimsInfo {

    static Faker faker = new Faker();

    public static ClaimInfo getClaimInfoWithOutFIO() {
        return new ClaimInfo(
                faker.bothify("Elena???#??#??#??#"),
                HospiceData.fio.EMPTY.getTitle(),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")),
                faker.bothify("Elena???#??#??#??#??"),
                HospiceData.fio.IVANOV.getTitle());
    }

    public static ClaimInfo getClaimInfoWithChoiceFIO(String fio) {
        return new ClaimInfo(
                faker.bothify("Elena???#??#??#??#"),
                fio,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")),
                faker.bothify("Elena???#??#??#??#???"),
                HospiceData.fio.IVANOV.getTitle());
    }

    public static ClaimInfo getClaimInfoWithChoiceTitleAndDiscr(String title, String descript) {
        return new ClaimInfo(
                title,
                HospiceData.fio.EMPTY.getTitle(),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")),
                descript,
                HospiceData.fio.IVANOV.getTitle());
    }

    public static ClaimInfo getClaimInfoWithChoiceDateTime(String ddate, String ttime) {
        return new ClaimInfo(
                faker.bothify("Elena???#??#??#??#"),
                HospiceData.fio.EMPTY.getTitle(),
                ddate,
                ttime,
                faker.bothify("Elena???#??#??#??#????"),
                HospiceData.fio.IVANOV.getTitle());
    }

    public static class ClaimInfo {

        private String title;
        private String executor;
        private String planDate;
        private String planTime;
        private String description;
        private String author;
        private String creationDate;
        private String creationTime;
        private String status;
        private int numberOfComments;

        public ClaimInfo(String title, String executor, String planDate, String planTime, String description, String author) {
            this.title = title;
            this.executor = executor;
            this.planDate = planDate;
            this.planTime = planTime;
            this.description = description;
            this.author = author;
            this.numberOfComments = 0;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() { return title; }

        public void setTitle(String title) { this.title = title; }

        public String getExecutor() {
            return executor;
        }

        public void setExecutor(String executor) {
            this.executor = executor;
        }

        public String getPlanDate() {
            return planDate;
        }

        public String getPlanTime() {
            return planTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) { this.description = description; }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getNumberOfComments() { return numberOfComments; }

        public void setNumberOfComments(int numberOfComments) { this.numberOfComments = numberOfComments; }
    }


}
