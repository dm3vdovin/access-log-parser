public class UserAgent {
    final String userAgent;
    final String osName;
    final String browserName;


    public UserAgent(String userAgent) {
        this.userAgent = userAgent;
        this.osName = getOsName(userAgent);
        this.browserName = getBrowserName(userAgent);
    }

    public static String getOsName(String userAgent) {
        if (userAgent.contains("Windows")) {
            return OperatingSystem.WINDOWS.name;
        } else if (userAgent.contains("Linux")) {
            return OperatingSystem.LINUX.name;
        } else if (userAgent.contains("Mac OS")) {
            return OperatingSystem.MACOS.name;
        } else {
            return OperatingSystem.OTHER.name;
        }
    }

    public static String getBrowserName(String userAgent) {
        if (userAgent.contains("Edge")) {
            return Browser.EDGE.name;
        } else if (userAgent.contains("Firefox")) {
            return Browser.FIREFOX.name;
        } else if (userAgent.contains("Chrome")) {
            return Browser.CHROME.name;
        } else if (userAgent.contains("Opera")) {
            return Browser.OPERA.name;
        } else if (userAgent.contains("Safari")) {
            return Browser.SAFARI.name;
        } else {
            return Browser.OTHER.name;
        }
    }

    enum Browser {
        EDGE("Edge"),
        FIREFOX("Firefox"),
        CHROME("Chrome"),
        OPERA("Opera"),
        SAFARI("Safari"),
        OTHER("Other");

        public final String name;

        Browser(String name) {
            this.name = name;
        }
    }

    enum OperatingSystem {
        WINDOWS("Windows NT"),
        LINUX("Linux"),
        MACOS("Mac OS X"),
        OTHER("Other");

        public final String name;

        OperatingSystem(String name) {
            this.name = name;
        }
    }
}
