package pl.mateusz.example;

public class AppController {

    private static final String ADD = "Dodaj transakcję";
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
            case ADD -> transactionDao.add();
            case UPDATE -> transactionDao.update();
            case DELETE -> transactionDao.delete();
            case DISPLAY -> transactionDao.displayTransactionsByType();
            case EXIT -> System.out.println("Wyjście z programu");
            default -> System.out.println("Niewłaściwy numer opcji");
        }
    }

    private static void printOptions() {
        System.out.println("Wybierz opcję");
        Option[] values = Option.values();
        for (Option value : values) {
            System.out.println(value.getId() + " - " + value.getDescription());
        }
    }


}
