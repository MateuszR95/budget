package pl.mateusz.example;

import java.util.List;
import java.util.Optional;

public class AppController {

    private static final TransactionDao transactionDao = new TransactionDao();

    public static void mainLoop() {
        int option;
        do {
            printOptions();
            option = DataReader.readOption();
            executeOption(option);

        } while (option != Option.EXIT.getId());

    }

    private static void executeOption(int optionId) {
        Option option = Option.convertFromIdToOption(optionId);
        switch (option) {
            case ADD -> save();
            case UPDATE -> modify();
            case DELETE -> delete();
            case DISPLAY -> printTransactionsByType();
            case EXIT -> transactionDao.close();
            default -> System.out.println("Niewłaściwy numer opcji");
        }
    }

    private static void printTransactionsByType() {
        String typeTranslation = DataReader.readType();
        Optional<Type> optionalType = Type.getTypeByTranslation(typeTranslation);
        optionalType.ifPresentOrElse(
                type -> {
                    List<Transaction> transactions = transactionDao.displayTransactionsByType(type);
                    printTransactionList(transactions);
                },
                () -> System.out.println("Brak transakcji o podanym typpie"));

    }

    private static void printTransactionList(List<Transaction> transactions) {
        if (!transactions.isEmpty()) {
            transactions.stream()
                    .forEach(System.out::println);
        } else {
            System.out.println("Brak transakcji o podanym typie");
        }
    }

    private static void delete() {
        int id = DataReader.readId();
        Optional<Transaction> optionalTransaction = transactionDao.findTransactionById(id);
        optionalTransaction.ifPresentOrElse(transactionDao::delete,
                () -> System.out.println("Brak transkacji o podanym id"));
    }

    private static void modify() {
        int id = DataReader.readId();
        Optional<Transaction> optionalTransaction = transactionDao.findTransactionById(id);
        optionalTransaction.ifPresentOrElse(
                transaction -> {
                    DataReader.updateTransaction(transaction);
                    transactionDao.update(transaction);
                },
                () -> System.out.println("Brak transakcji o podanym id"));
    }

    private static void save() {
        Optional<Transaction> transactionOptional = DataReader.createTransaction();
        transactionOptional.ifPresent(
                transaction -> {
                    transactionDao.add(transaction);
                    System.out.println("Dodano transakcję: " + transaction);
                });

    }

    private static void printOptions() {
        System.out.println("Wybierz opcję");
        Option[] values = Option.values();
        for (Option value : values) {
            System.out.println(value.getId() + " - " + value.getDescription());
        }
    }


}
