package budget;

public enum Category {
    FOOD(1),
    CLOTHES(2),
    ENTERTAINMENT(3),
    OTHER(4),
    ALL(5);
    int val;
    Category(int val) {
        this.val = val;
    }
}
