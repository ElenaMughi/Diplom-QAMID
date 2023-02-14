package ru.netology.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClaimsInfo {

    public static ClaimsInfo.LogInfo getLogInfo() {
        return new ClaimsInfo.LogInfo("login2", "password2");
    }

    public static ClaimsInfo.LogInfo getWrongLogInfo() {
        return new ClaimsInfo.LogInfo("login", "password");
    }

    public static ClaimsInfo.LogInfo getLogInfoWithLoginEmpty() {
        return new ClaimsInfo.LogInfo("", "password2");
    }

    public static ClaimsInfo.LogInfo getLogInfoWithPasswordEmpty() {
        return new ClaimsInfo.LogInfo("login2", "");
    }

    public static ClaimInfo getClaimInfoWithChoiceFIO(int i) {
        String title = HospiceData.titles[0] + Math.round(Math.random() * 1000);
        String descript = HospiceData.descriptions[0] + Math.round(Math.random() * 1000);
        String dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        return new ClaimInfo(title, HospiceData.fio[i - 1], dateText, timeText, descript, HospiceData.fio[0]);
    }

    public static ClaimInfo getClaimInfoWithChoiceTitleAndDiscr(String title, String descript) {
        title = title + Math.round(Math.random() * 10);
        descript = descript + Math.round(Math.random() * 10);
        String dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
        return new ClaimInfo(title, HospiceData.fio[0], dateText, timeText, descript, HospiceData.fio[0]);
    }

    public static ClaimInfo getClaimInfoWithChoiceDateTime(String ddate, String ttime) {
        String title = HospiceData.titles[1] + Math.round(Math.random() * 1000);
        String descript = HospiceData.descriptions[1] + Math.round(Math.random() * 1000);

        return new ClaimInfo(title, HospiceData.fio[0], ddate, ttime, descript, HospiceData.fio[0]);
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
        private String planDate;
        private String planTime;
        private String description;
        private String author;
        private String CreationDate;
        private String CreationTime;

        public ClaimInfo(String title, String executor, String dateClaim, String timeClaim, String description, String author) {
            this.title = title;
            this.executor = executor;
            this.planDate = dateClaim;
            this.planTime = timeClaim;
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

        public String getCreationDate() { return CreationDate; }

        public void setCreationDate(String creationDate) { CreationDate = creationDate; }

        public String getCreationTime() { return CreationTime; }

        public void setCreationTime(String creationTime) { CreationTime = creationTime; }
    }


}
