import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int secondNumber = new Scanner(System.in).nextInt();
        int amount = firstNumber + secondNumber;
        System.out.println("Сумма чисел равна: " + amount);
        int difference = firstNumber - secondNumber;
        System.out.println("Разность чисел равна: " + difference);
        int multiplication = firstNumber * secondNumber;
        System.out.println("Произведение чисел равно: " + multiplication);
        double quotient = (double) firstNumber / secondNumber;
        System.out.println("Частное от деления первого числа на второе равно: " + quotient);
    }
}
