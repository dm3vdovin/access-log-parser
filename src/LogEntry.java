import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogEntry {
    final String ipAddress;
    final LocalDateTime timeStamp;
    final HttpMethod requestMethod;
    final String requestPath;
    final int responseCode;
    final long dataSize;
    final String referer;
    final String userAgent;
    public static Pattern regexPattern = Pattern.compile(
            "(?<ipAddress>\\d+.\\d+.\\d+.\\d+) " +
                    "(?<property1>.*?) " +
                    "(?<property2>.*?) \\[" +
                    "(?<timeStamp>.*?)] \"" +
                    "(?<requestMethod>.*) " +
                    "(?<requestPath>/.*?) HTTP/\\d+.\\d+\" " +
                    "(?<responseCode>\\d+?) " +
                    "(?<dataSize>\\d+?) " +
                    "(?<referer>.*?) " +
                    "(?<userAgent>.*?)");

    public LogEntry(String line) {
        Matcher matcher = regexPattern.matcher(line);

        if (matcher.matches()) {
            this.ipAddress = matcher.group("ipAddress");
            this.timeStamp = LocalDateTime.parse(matcher.group("timeStamp"), DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
            this.requestMethod = HttpMethod.valueOf(matcher.group("requestMethod"));
            this.requestPath = matcher.group("requestPath");
            this.responseCode = Integer.parseInt(matcher.group("responseCode"));
            this.dataSize = Long.parseLong(matcher.group("dataSize"));
            this.referer = matcher.group("referer");
            this.userAgent = matcher.group("userAgent");
        } else {
            throw new IllegalArgumentException("Строка имеет неверный формат: " + line);
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }
}



