package paperUser.entity.enums;

public enum Role {
    CHAIR("会议主管"),
    PC_MEMBER("审稿人"),
    AUTHOR("投稿人");

    private final String info;

    Role(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
