import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Stream;


public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime, maxTime;
    public HashSet<String> existingPageLinks;
    public HashSet<String> nonExistingPageLinks;
    public HashMap<String, Integer> oSLoggingFrequency;
    public HashMap<String, Integer> browserLoggingFrequency;
    public HashMap<LocalDateTime, Integer> loggingFrequency;
    HashMap<String, Integer> userFrequency;
    ArrayList<String> ipAddressesList;
    HashSet<String> referers;
    private long userSuccessActions;
    private long userIssues;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
        this.existingPageLinks = new HashSet<>();
        this.nonExistingPageLinks = new HashSet<>();
        this.oSLoggingFrequency = new HashMap<>();
        this.browserLoggingFrequency = new HashMap<>();
        this.ipAddressesList = new ArrayList<>();
        this.loggingFrequency = new HashMap<>();
        this.referers = new HashSet<>();
        this.userFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        int successCode = 200;
        int badRequestCode = 400;
        int notFoundCode = 404;

        String userAgentString = logEntry.getUserAgent();
        UserAgent userAgent = new UserAgent(userAgentString);
        LocalDateTime timeStamp = logEntry.getTimeStamp();
        String ipAddress = logEntry.getIpAddress();
        int responseCode = logEntry.getResponseCode();
        String referer = logEntry.getReferer();

        totalTraffic += logEntry.getDataSize();

        if (timeStamp.isBefore(minTime)) {
            minTime = timeStamp;
        }

        if (timeStamp.isAfter(maxTime)) {
            maxTime = timeStamp;
        }

        if (!userAgent.isBotRequest(userAgentString)) {
            ipAddressesList.add(ipAddress);

            if (loggingFrequency.containsKey(timeStamp)) {
                loggingFrequency.replace(timeStamp, loggingFrequency.get(timeStamp) + 1);
            } else {
                loggingFrequency.put(timeStamp, 1);
            }

            if (userFrequency.containsKey(ipAddress)) {
                userFrequency.replace(ipAddress, userFrequency.get(ipAddress) + 1);
            } else {
                userFrequency.put(ipAddress, 1);
            }

            userSuccessActions++;
        }

        if (responseCode >= badRequestCode) {
            userIssues++;
        }

        if (responseCode == successCode) {
            existingPageLinks.add(referer);
        }

        if (responseCode == notFoundCode) {
            nonExistingPageLinks.add(referer);
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

        if (referer != null && getHostName(referer) != null) {
            referers.add(getHostName(referer));
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

    public String getHostName(String referer) {
        String regExpression = "http(s)?://|www\\.|/.*";
        return referer.replaceAll(regExpression, "");
    }

    public OptionalDouble getPeakRequestsPerHour() {
        return loggingFrequency.values().stream().mapToDouble(t -> t).max();
    }

    public HashSet<String> getReferals() {
        return referers;
    }

    public OptionalDouble getMaximumUniqueUserAttendanceRate() {
        return userFrequency.values().stream().mapToDouble(t -> t).max();
    }
}