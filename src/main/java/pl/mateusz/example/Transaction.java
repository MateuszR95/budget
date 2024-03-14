package pl.mateusz.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction {

    private Integer id;
    private Type type;
    private String description;
    private BigDecimal amount;
    private LocalDateTime localDateTime;

    public Transaction(Type type, String description, BigDecimal amount, LocalDateTime localDateTime) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.localDateTime = localDateTime;

    }

    public Transaction(Integer id, Type type, String description, BigDecimal amount, LocalDateTime localDateTime) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.localDateTime = localDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Transakcja: id: " + id + ", typ: " + type.getTranslation() + ", opis: " + description +
                ", kwota: " + amount + ", data realizacji: " +
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
