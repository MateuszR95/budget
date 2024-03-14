package pl.mateusz.example;


import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;


public class TransactionDao {

    private final Connection connection;

    public TransactionDao() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget","root",
                    "User12345!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void add() {
        System.out.println("Dodawanie transakcji");
        Optional<Transaction> optionalTransaction = DataReader.createTransaction();
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            String sqlQuery = "INSERT INTO transaction(type, description, amount, date) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, transaction.getType().getTranslation());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            Timestamp timestamp = Timestamp.valueOf(transaction.getLocalDateTime());
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                transaction.setId(generatedKeys.getInt(1));
                System.out.println("Udało dodać się transakcję: " + transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            }
    }

    public boolean update() {
        System.out.println("Aktualizowanie transakcji");
        Optional<Transaction> transactionById = findTransactionById();
        if (transactionById.isPresent()) {
            Transaction transaction = transactionById.get();
            DataReader.updateTransaction(transaction);
            String sqlQuery = "UPDATE transaction SET type = ?, description = ?, amount = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setString(1, transaction.getType().getTranslation());
                preparedStatement.setString(2, transaction.getDescription());
                preparedStatement.setBigDecimal(3, transaction.getAmount());
                preparedStatement.setInt(4, transaction.getId());
                int updatedRows = preparedStatement.executeUpdate();
                System.out.println("Zaktualizowano " + updatedRows + " rekordy(ów)");
                return updatedRows != 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Transakcja o podanym id nie istnieje");
        }
        return false;
    }
    private Optional<Transaction> findTransactionById() {
        int id = DataReader.readId();
        String sqlQuery = "SELECT * FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer transactionId = resultSet.getInt("id");
                String typeTranslation = resultSet.getString("type");
                Type type = Type.getTypeByTranslation(typeTranslation)
                        .orElse(null);
                String description = resultSet.getString("description");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                return Optional.of(new Transaction(transactionId, type, description, amount, localDateTime));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();

    }
    public void displayTransactionsByType() {
        System.out.println("Wyświetlanie transakcji według typu");
        try {
            Optional<Type> typeOptional = Type.getTypeByTranslation(DataReader.readType());
            String sqlQuery = "SELECT * FROM transaction WHERE type = ?";
            if (typeOptional.isPresent()) {
                Type type = typeOptional.get();
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                    preparedStatement.setString(1, type.getTranslation());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    System.out.println("Wszystkie transakcje - typ: " + type.getTranslation());
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String transactionType = resultSet.getString("type");
                        String description = resultSet.getString("description");
                        BigDecimal amount = resultSet.getBigDecimal("amount");
                        Date date = resultSet.getDate("date");
                        System.out.println("id: " + id + ", typ: " + transactionType + ", opis: " + description
                                + ", kwota: " + amount + ", data transakcji: " + date);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                throw new IllegalArgumentException("Nieprawidłowy typ transakcji");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }



    }
    public boolean delete() {
        System.out.println("Usuwanie transakcji");
        Optional<Transaction> transactionOptional = findTransactionById();
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            String sqlQuery = "DELETE FROM transaction WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
                preparedStatement.setInt(1, transaction.getId());
                int updatedRows = preparedStatement.executeUpdate();
                System.out.println("Usunięto " + updatedRows + " rekordy(ów)");
                return updatedRows != 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Transakcja o podanym id nie istnieje");
        }
        return false;
    }

    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
