public class UserAgent {
    final String userAgent;
    final Enum<OperatingSystem> osName;
    final Enum<Browser> browserName;


    public UserAgent(String userAgent) {
        this.userAgent = userAgent;
        this.osName = getOsName(userAgent);
        this.browserName = getBrowserName(userAgent);
    }

    private Enum<OperatingSystem> getOsName(String userAgent) {
        if (userAgent.contains("Windows")) {
            return OperatingSystem.WINDOWS;
        } else if (userAgent.contains("Linux")) {
            return OperatingSystem.LINUX;
        } else if (userAgent.contains("Mac OS")) {
            return OperatingSystem.MACOS;
        } else {
            return OperatingSystem.OTHER;
        }
    }

    private Enum<Browser> getBrowserName(String userAgent) {
        if (userAgent.contains("Edge")) {
            return Browser.EDGE;
        } else if (userAgent.contains("Firefox")) {
            return Browser.FIREFOX;
        } else if (userAgent.contains("Chrome")) {
            return Browser.CHROME;
        } else if (userAgent.contains("Opera")) {
            return Browser.OPERA;
        } else if (userAgent.contains("Safari")) {
            return Browser.SAFARI;
        } else {
            return Browser.OTHER;
        }
    }

    enum Browser {
        EDGE("Edge"),
        FIREFOX("Firefox"),
        CHROME("Chrome"),
        OPERA("Opera"),
        SAFARI("Safari"),
        OTHER("Other");

        private final String name;

        Browser(String name) {
            this.name = name;
        }
    }

    enum OperatingSystem {
        WINDOWS("Windows NT"),
        LINUX("Linux"),
        MACOS("Mac OS X"),
        OTHER("Other");

        private final String name;

        OperatingSystem(String name) {
            this.name = name;
        }
    }

    public Enum<OperatingSystem> getOsName() {
        return osName;
    }

    public Enum<Browser> getBrowserName() {
        return browserName;
    }
}
