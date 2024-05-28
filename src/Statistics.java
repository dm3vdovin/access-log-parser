import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime, maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getDataSize();

        if (logEntry.getTimeStamp().isBefore(minTime)) {
            minTime = logEntry.getTimeStamp();
        }

        if (logEntry.getTimeStamp().isAfter(maxTime)) {
            maxTime = logEntry.getTimeStamp();
        }
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        double diffInHours = duration.toHours();
        return (double) totalTraffic / diffInHours;
    }
}


