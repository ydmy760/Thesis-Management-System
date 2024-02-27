package paperUser.entity.enums;

public enum InvitationState {
    PENDING("未处理"),
    ACCEPT("已接受"),
    REFUSE("已拒绝");

    private final String state;

    InvitationState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
