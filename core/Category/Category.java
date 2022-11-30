package Category;

public enum Category {
    
    FOOD("Food"),
    ELECTRICITY("Electricity"),
    RESTAURANT("Restaurants"),
    VACATION("Vacations");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Category getCategoryByValue(int value) {
        for (Category item : Category.values()) {
            if (item.value == value) {
                return item;
            }
        }
        return null;
    }

    public final int value = 1 + ordinal();

}
