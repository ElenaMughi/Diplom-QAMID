package ru.netology.data;

public class HospiceInfo {

    public static final String fio[] = {"Ivanov Ivan Ivanovich"};
    public static final String comment[] = {"Let's fun!"};

    public static LogInfo getLogInfo() {
        return new LogInfo("login2", "password2");
    };

    public static ClaimInfo getClaimInfo() {
        return new ClaimInfo("Title", fio[0], "16/01/2023", "00:00", "You make my day!");
    };

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

    public static final class ClaimInfo {
        private String title;
        private String executor;
        private String dateClaim;
        private String timeClaim;
        private String description;

        public ClaimInfo(String title, String executor, String dateClaim, String timeClaim, String description) {
            this.title = title;
            this.executor = executor;
            this.dateClaim = dateClaim;
            this.timeClaim = timeClaim;
            this.description = description;
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
    }

}
