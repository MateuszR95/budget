package pl.mateusz.example;


import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TransactionDao {

    private final Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/budget";
    private static final String DB_USER_NAME = "root";
    private static final String DB_USER_PASSWORD = "User12345!";

    public TransactionDao() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_USER_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Transaction add(Transaction transaction) {
        String sqlQuery = "INSERT INTO transaction(type, description, amount, date) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transaction.getType().getTranslation());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            Timestamp timestamp = Timestamp.valueOf(transaction.getLocalDateTime());
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }
            return transaction;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean update(Transaction transaction) {
        String sqlQuery = "UPDATE transaction SET type = ?, description = ?, amount = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, transaction.getType().getTranslation());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setInt(4, transaction.getId());
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Transaction> findTransactionById(int id) {
        String sqlQuery = "SELECT * FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
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

    public List<Transaction> displayTransactionsByType(Type type) {
        List<Transaction> transactions = new ArrayList<>();
        String sqlQuery = "SELECT * FROM transaction WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, type.getTranslation());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer transactionId = resultSet.getInt("id");
                String description = resultSet.getString("description");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Transaction transaction = new Transaction(transactionId, type, description, amount, localDateTime);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }


    public boolean delete(Transaction transaction) {
        String sqlQuery = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, transaction.getId());
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
