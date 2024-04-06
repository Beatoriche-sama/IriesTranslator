package translate;

public enum SERVICE {
    DEEPL("DeepL"),
    //GOOGLE_NO_KEY("Google no key"),
    GOOGLE("Google");
    private final String serviceName;

    SERVICE(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
