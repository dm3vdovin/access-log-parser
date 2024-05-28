import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime, maxTime;
    private final HashSet<String> existingPageLinks;
    private final HashSet<String> nonExistingPageLinks;
    private final HashMap<String, Integer> oSLoggingFrequency;
    private final HashMap<String, Integer> browserLoggingFrequency;
    int successCode = 200;
    int notFoundCode = 404;
    private int osCount = 0;
    private int browserCount = 0;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
        this.existingPageLinks = new HashSet<>();
        this.nonExistingPageLinks = new HashSet<>();
        this.oSLoggingFrequency = new HashMap<>();
        this.browserLoggingFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getDataSize();

        if (logEntry.getTimeStamp().isBefore(minTime)) {
            minTime = logEntry.getTimeStamp();
        }

        if (logEntry.getTimeStamp().isAfter(maxTime)) {
            maxTime = logEntry.getTimeStamp();
        }

        getPageLinks(logEntry, existingPageLinks, successCode);
        getPageLinks(logEntry, nonExistingPageLinks, notFoundCode);
        getLoggingFrequencyRate(logEntry, oSLoggingFrequency, osCount);
        getLoggingFrequencyRate(logEntry, browserLoggingFrequency, browserCount);
    }

    public double getTrafficRate() {
        Duration timeDuration = Duration.between(minTime, maxTime);
        double differenceInHours = timeDuration.toHours();

        return (double) totalTraffic / differenceInHours;
    }

    public HashSet<String> getPageLinks(LogEntry logEntry, HashSet<String> pageLinks, int statusCode) {
        String pageLink = logEntry.getRequestPath();

        if (logEntry.getResponseCode() == statusCode) {
            pageLinks.add(pageLink);
        }

        return pageLinks;
    }

    public HashMap<String, Double> getLoggingFrequencyRate(LogEntry logEntry, HashMap<String, Integer> loggingFrequency, int count) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String userAgentString = logEntry.getUserAgent();

        if (userAgent.getOsName(userAgentString) != null) {
            String name = userAgent.getOsName(userAgentString);

            if (loggingFrequency.containsKey(name)) {
                loggingFrequency.put(name, loggingFrequency.get(name) + 1);
            } else {
                loggingFrequency.put(name, 1);
            }
        }

        HashMap<String, Double> oSLoggingFrequencyRate = new HashMap<>();

        for (String key : loggingFrequency.keySet()) {
            count += loggingFrequency.get(key);
            double value = (double) loggingFrequency.get(key) / count;
            oSLoggingFrequencyRate.put(key, value);
        }

        for (Integer value : loggingFrequency.values()) {
            count += value;
        }

        return oSLoggingFrequencyRate;
    }
}