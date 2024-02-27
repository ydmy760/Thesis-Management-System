package paperUser.entity.enums;

public enum ConferenceState {
    UNDER_REVIEW("审核中"),
    NOT_APPROVED("未通过"),
    APPROVED("已通过"),
    IN_PREPARATION("准备中"),
    UNDER_SUBMISSION("投稿中"),
    UNDER_PEER_REVIEW("审稿中"),
    FINAL_REVIEW("终评中"),
    PEER_REVIEW_COMPLETED("审稿结束");

    private final String state;

    ConferenceState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
