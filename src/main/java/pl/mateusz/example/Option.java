package pl.mateusz.example;

public enum Option {

    ADD(1,"Dodaj transakcję"),
    UPDATE(2,"Zaktualizuj transakcję"),
    DELETE(3,"Usuń transakcję"),
    DISPLAY(4,"Wyświetl transakcje według typu"),
    EXIT(5, "Wyjście");

    private int id;
    private String description;

    Option(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Option convertFromIdToOption(int id) {
        Option option = null;
        Option[] values = Option.values();
        for (Option value : values) {
            if (value.getId() == id) {
                option = value;
            }
        }
        return option;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
