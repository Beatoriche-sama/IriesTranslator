package configs;

public enum NativeKeyBind {
    SELECT_AREA("Select area"),
    SHOW_APP("Show app"),
    BIND_KEYS("Bind keys"),
    EXIT_APP("Exit app");
    private final String name;

    NativeKeyBind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
