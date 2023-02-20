package ru.netology.data;

public class LoginInfo {
    public static LoginInfo.LogInfo getLogInfo() {
        return new LoginInfo.LogInfo("login2", "password2");
    }

    public static LoginInfo.LogInfo getWrongLogInfo() {
        return new LoginInfo.LogInfo("login", "password");
    }

    public static LoginInfo.LogInfo getLogInfoWithLoginEmpty() {
        return new LoginInfo.LogInfo("", "password2");
    }

    public static LoginInfo.LogInfo getLogInfoWithPasswordEmpty() {
        return new LoginInfo.LogInfo("login2", "");
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
}
