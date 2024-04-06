package translate;

public enum LANG {
    EN("EN-US", "English"),
    JA("JA", "Japanese"),
    RU("RU", "Russian");

    private final String code;
    private final String fullName;

    LANG(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }
}
