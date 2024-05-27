import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int filesCount = 1;
        int totalLinesCount = 0;
        int maxLength = 0;
        int minLength = Integer.MAX_VALUE;
        int lineLengthLimit = 1024;

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
                    if (length > maxLength) {
                        maxLength = length;
                    }
                    if (length < minLength) {
                        minLength = length;
                    }

                    totalLinesCount++;
                }

                System.out.println("Общее количество строк в файле: " + totalLinesCount);
                System.out.println("Длина самой длинной строки в файле: " + maxLength);
                System.out.println("Длина самой короткой строки в файле: " + minLength);

            } catch (FileNotFoundException ex) {
                throw new ExceedMaxLengthException("Указанный файл не найден");
            } catch (IOException ex) {
                throw new ExceedMaxLengthException("Ошибка ввода/вывода");
            }
        }
    }
}