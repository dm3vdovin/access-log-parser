import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        int filesCount = 1;
        int totalLinesCount = 0;
        int lineLengthLimit = 1024;
        int botTotalCountGoogle = 0;
        int botTotalCountYandex = 0;
        String botNameGoogle = "Googlebot";
        String botNameYandex = "YandexBot";

        while (true) {
            System.out.println("Укажите абсолютный путь к файлу логов: ");
            String path = new Scanner(System.in).nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                System.out.println("Указанный файл не существует или указанный путь является путём к папке, а не к файлу!");
                continue;
            }

            System.out.println("Путь указан верно");
            System.out.println("Это файл номер " + filesCount);
            filesCount++;

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();

                    if (length > lineLengthLimit) {
                        throw new ExceedMaxLengthException("В файле встретилась строка длиннее " + lineLengthLimit + " символов");
                    }

                    Pattern regexPattern = LogEntry.regexPattern;
                    Matcher userAgentMatcher = regexPattern.matcher(line);

                    if (userAgentMatcher.matches()) {
                        String userAgent = userAgentMatcher.group("userAgent");
                        int index = userAgent.indexOf("(compatible;");
                        String botDataBrackets = userAgent.substring(index + 1);

                        String[] parts = botDataBrackets.split(";");

                        if (parts.length >= 2) {
                            String fragment = parts[1];
                            parts = fragment.split("/");
                            String botName = parts[0].trim();

                            if (botName.equals(botNameYandex)) {
                                botTotalCountYandex++;
                            }

                            if (botName.equals(botNameGoogle)) {
                                botTotalCountGoogle++;
                            }
                        }
                    }

                    totalLinesCount++;

                    Statistics statistics = new Statistics();
                    LogEntry logEntry = new LogEntry(line);
                    String userAgentString = logEntry.getUserAgent();
                    UserAgent userAgent = new UserAgent(userAgentString);
                    System.out.println(userAgent.getOsName());
                    System.out.println(userAgent.getBrowserName());
                    statistics.addEntry(logEntry);
                    statistics.addEntry(logEntry);
                    System.out.println("Пропускная способность: " + statistics.getTrafficRate());
                }

                System.out.println("Общее количество строк в файле: " + totalLinesCount);
                DecimalFormat formatter = new DecimalFormat("#0.00");
                double botYandexContribution = (double) botTotalCountYandex / totalLinesCount * 100;
                double botGoogleContribution = (double) botTotalCountGoogle / totalLinesCount * 100;
                System.out.println("Доля запросов от YandexBot относительно общего числа запросов: " + formatter.format(botYandexContribution) + " (" + botTotalCountYandex + ")");
                System.out.println("Доля запросов от GoogleBot относительно общего числа запросов: " + formatter.format(botGoogleContribution) + " (" + botTotalCountGoogle + ")");

            } catch (FileNotFoundException ex) {
                throw new ExceedMaxLengthException("Указанный файл не найден");
            } catch (IOException ex) {
                throw new ExceedMaxLengthException("Ошибка ввода/вывода");
            }
        }
    }
}