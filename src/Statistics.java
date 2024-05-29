import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;


public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime, maxTime;
    public HashSet<String> existingPageLinks;
    public HashSet<String> nonExistingPageLinks;
    public HashMap<String, Integer> oSLoggingFrequency;
    public HashMap<String, Integer> browserLoggingFrequency;
    ArrayList<String> ipAddressesList;
    private long userSuccessActions;
    private long userIssues;
    private final int successCode = 200;
    private final int badRequestCode = 400;
    private final int notFoundCode = 404;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
        this.existingPageLinks = new HashSet<>();
        this.nonExistingPageLinks = new HashSet<>();
        this.oSLoggingFrequency = new HashMap<>();
        this.browserLoggingFrequency = new HashMap<>();
        this.ipAddressesList = new ArrayList<>();
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String userAgentString = logEntry.getUserAgent();

        totalTraffic += logEntry.getDataSize();

        if (logEntry.getTimeStamp().isBefore(minTime)) {
            minTime = logEntry.getTimeStamp();
        }

        if (logEntry.getTimeStamp().isAfter(maxTime)) {
            maxTime = logEntry.getTimeStamp();
        }

        if (!isBotRequest(logEntry.getUserAgent())) {
            ipAddressesList.add(logEntry.getIpAddress());
            userSuccessActions++;
        }

        if (logEntry.getResponseCode() >= badRequestCode) {
            userIssues++;
        }

        if (logEntry.getResponseCode() == successCode) {
            existingPageLinks.add(logEntry.getReferer());
        }

        if (logEntry.getResponseCode() == notFoundCode) {
            nonExistingPageLinks.add(logEntry.getReferer());
        }

        String oSName = userAgent.getOsName(userAgentString);

        if (!oSName.isEmpty()) {
            if (oSLoggingFrequency.containsKey(oSName)) {
                oSLoggingFrequency.replace(oSName, oSLoggingFrequency.get(oSName) + 1);
            } else {
                oSLoggingFrequency.put(oSName, 1);
            }
        }

        String browserName = userAgent.getBrowserName(userAgentString);

        if (!browserName.isEmpty()) {
            if (browserLoggingFrequency.containsKey(browserName)) {
                browserLoggingFrequency.replace(browserName, browserLoggingFrequency.get(browserName) + 1);
            } else {
                browserLoggingFrequency.put(browserName, 1);
            }
        }
    }

    public double getTrafficRate() {
        long hourlyDuration = Duration.between(minTime, maxTime).toHours();

        if (maxTime.isAfter(minTime) && hourlyDuration > 0) {
            return (double) totalTraffic / hourlyDuration;
        }

        return 0;
    }

    public HashMap<String, Double> getOsLoggingFrequencyRate() {
        HashMap<String, Double> loggingOsFrequencyRate = new HashMap<>();
        double count = oSLoggingFrequency.values().stream().mapToDouble(t -> t).sum();
        oSLoggingFrequency.forEach((key, value) -> loggingOsFrequencyRate.put(key, value / count));

        return loggingOsFrequencyRate;
    }

    public HashMap<String, Double> getBrowserLoggingFrequencyRate() {
        HashMap<String, Double> loggingBrowserFrequencyRate = new HashMap<>();
        double count = browserLoggingFrequency.values().stream().mapToDouble(t -> t).sum();
        browserLoggingFrequency.forEach((key, value) -> loggingBrowserFrequencyRate.put(key, value / count));

        return loggingBrowserFrequencyRate;
    }

    public boolean isBotRequest(String userAgentString) {
        boolean isBotRequest = false;
        String missingPropertyAttribute = "-";
        String botAttribute = "bot";

        if (!userAgentString.equals(missingPropertyAttribute) && userAgentString.contains(botAttribute)) {
            isBotRequest = true;
        }

        return isBotRequest;
    }

    public double getAverageRequestsPerHour() {
        long hourlyDuration = Duration.between(minTime, maxTime).toHours();

        if (maxTime.isAfter(minTime) && hourlyDuration > 0) {
            return (double) userSuccessActions / hourlyDuration;
        }

        return 0;
    }

    public double getAverageIssuesPerHour() {
        long hourlyDuration = Duration.between(minTime, maxTime).toHours();

        if (maxTime.isAfter(minTime) && hourlyDuration > 0) {
            return (double) userIssues / hourlyDuration;
        }

        return 0;
    }

    public HashSet<String> getExistingPageLinks() {
        return existingPageLinks;
    }

    public HashSet<String> getNonExistingPageLinks() {
        return nonExistingPageLinks;
    }

    public double getAverageUniqueUserAttendanceRate() {
        Stream<String> ipAddressesStream = ipAddressesList.stream();
        double uniqueIpAddresses = ipAddressesStream.distinct().count();

        if (uniqueIpAddresses > 0) {
            return (double) ipAddressesList.size() / uniqueIpAddresses;
        }

        return 0;
    }
}