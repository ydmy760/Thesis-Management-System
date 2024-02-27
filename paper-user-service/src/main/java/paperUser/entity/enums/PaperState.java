package paperUser.entity.enums;

public enum PaperState {
    REVIEWED("已审稿"),
    UNDER_REVIEWED("待审稿");

    private final String state;

    PaperState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
