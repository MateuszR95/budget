package pl.mateusz.example;

import java.util.Optional;

public enum Type {

    EXPENSE("Wydatek"),
    INCOME("Przychód");

    private final String translation;

    Type(String translation) {
        this.translation = translation;
    }

    public static Optional<Type> getTypeByTranslation(String translation) {
        for (Type type : Type.values()) {
            if (type.getTranslation().equalsIgnoreCase(translation)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public String getTranslation() {
        return translation;
    }

    public static void printTypes() {
        Type[] values = Type.values();
        for (int i = 0; i < values.length; i++) {
            if (i == (values.length - 1)) {
                System.out.print(values[i].getTranslation() + "?");
                System.out.println();
            } else {
                System.out.print(values[i].getTranslation() + " czy ");
            }
        }
    }


}
