package pl.mateusz.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class DataReader {

    private static final Scanner scanner = new Scanner(System.in);

    public static Optional<Transaction> createTransaction() {
        Transaction transaction;
        Type type;
        String description;
        BigDecimal amount;
        try {
            System.out.println("Podaj typ transakcji:");
            Type.printTypes();
            String userType = scanner.nextLine();
            Optional<Type> typeByTranslation = Type.getTypeByTranslation(userType);
            type = typeByTranslation.orElseThrow(() ->
                    new IllegalArgumentException("Niewłaściwy typ transakcji"));
            System.out.println("Podaj opis transakcji");
            description = scanner.nextLine();
            System.out.println("Podaj kwotę transakcji");
            amount = scanner.nextBigDecimal();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Kwota transakcji nie może być <= 0");
            }
            transaction = new Transaction(type, description, amount, LocalDateTime.now());
            return Optional.of(transaction);

        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public static void updateTransaction(Transaction transaction) {
        Type type;
        String description;
        BigDecimal amount;
        try {
            System.out.println("Podaj nowy typ transakcji");
            String translation = scanner.nextLine();
            Optional<Type> typeByTranslation = Type.getTypeByTranslation(translation);
            type = typeByTranslation.orElseThrow(() ->
                    new IllegalArgumentException("Niewłaściwy typ transakcji"));
            System.out.println("Podaj nowy opis transakcji");
            description = scanner.nextLine();
            transaction.setDescription(description);
            System.out.println("Podaj nową kwotę transakcji");
            amount = scanner.nextBigDecimal();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Kwota transakcji nie może być <= 0");
            }
            transaction.setType(type);
            transaction.setDescription(description);
            transaction.setAmount(amount);
            System.out.println("Zaktualizowano transakcję od id: " + transaction.getId());
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println(e.getMessage());
        }

    }

    public static int readId() {
        int id = 0;
        boolean isValidInput = false;
        do {
            try {
                System.out.println("Podaj nr id transakcji");
                id = scanner.nextInt();
                scanner.nextLine();
                isValidInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Id transakcji to wartość liczbowa");
                scanner.nextLine();
            }
        } while (!isValidInput);

        return id;
    }

    public static String readType() {
        System.out.println("Podaj typ transakcji");
        Type.printTypes();
        return scanner.nextLine();
    }

    public static int readOption() {
        int option = 0;
        boolean isValidInput = false;
        do {
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                isValidInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Niewłaściwy format danych. Podaj ponownie opcję:");
                scanner.nextLine();
            }
        } while (!isValidInput);

        return option;
    }

}
