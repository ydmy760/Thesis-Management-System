package paperUser.entity.enums;

public enum Confidence {
    VERY_LOW("非常低"),
    LOW("低"),
    VERY_HIGH("非常高"),
    HIGH("高");
    private final String state;

    Confidence(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
