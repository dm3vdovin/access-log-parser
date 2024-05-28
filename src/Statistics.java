import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime, maxTime;
    private final HashSet<String> pageLinks;
    private final HashMap<String, Integer> oSLoggingFrequency;
    int successCode = 200;
    private int osCount = 0;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
        this.pageLinks = new HashSet<>();
        this.oSLoggingFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getDataSize();

        if (logEntry.getTimeStamp().isBefore(minTime)) {
            minTime = logEntry.getTimeStamp();
        }

        if (logEntry.getTimeStamp().isAfter(maxTime)) {
            maxTime = logEntry.getTimeStamp();
        }

        getPageLinks(logEntry);
        getOsLoggingFrequencyRate(logEntry);

    }

    public double getTrafficRate() {
        Duration timeDuration = Duration.between(minTime, maxTime);
        double differenceInHours = timeDuration.toHours();
        return (double) totalTraffic / differenceInHours;
    }

    public HashSet<String> getPageLinks(LogEntry logEntry) {
        String pageLink = logEntry.getRequestPath();

        if (logEntry.getResponseCode() == successCode) {
            pageLinks.add(pageLink);
        }

        return pageLinks;
    }

    public HashMap<String, Double> getOsLoggingFrequencyRate(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String userAgentString = logEntry.getUserAgent();

        if (userAgent.getOsName(userAgentString) != null) {
            String oSName = userAgent.getOsName(userAgentString);

            if (oSLoggingFrequency.containsKey(oSName)) {
                oSLoggingFrequency.put(oSName, oSLoggingFrequency.get(oSName) + 1);
            } else {
                oSLoggingFrequency.put(oSName, 1);
            }

        }

        HashMap<String, Double> oSLoggingFrequencyRate = new HashMap<>();

        for (String key : oSLoggingFrequency.keySet()) {
            osCount += oSLoggingFrequency.get(key);
            double value = (double) oSLoggingFrequency.get(key) / osCount;
            oSLoggingFrequencyRate.put(key, value);
        }

        for (Integer value : oSLoggingFrequency.values()) {
            osCount += value;
        }

        return oSLoggingFrequencyRate;
    }
}


