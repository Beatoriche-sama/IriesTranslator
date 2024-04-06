package configs;

import translate.LANG;
import translate.SERVICE;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configs {
    private final PropertiesReader propertiesReader;
    private static final String TARGET_LANG = "target_lang";
    private static final String PREF_SERVICE = "pref_service";

    private SERVICE currentService;
    private LANG targetLanguage;
    private final Map<SERVICE, String> serviceKeys;
    private final Map<NativeKeyBind, String> keyBindings;

    public Configs() throws IOException {
        propertiesReader = new PropertiesReader();
        serviceKeys = new HashMap<>(SERVICE.values().length);
        NativeKeyBind[] nativeKeyBinds = NativeKeyBind.values();
        keyBindings = new HashMap<>(nativeKeyBinds.length);

        readConfigs();
    }

    private void readConfigs() throws IOException {

        String languageCode = propertiesReader.getPropertyValue(TARGET_LANG);
        this.targetLanguage = Arrays.stream(LANG.values())
                .filter(l -> l.getCode().equals(languageCode))
                .findAny().orElse(LANG.EN);

        String serviceCode = propertiesReader.getPropertyValue(TARGET_LANG);
        this.currentService = Arrays.stream(SERVICE.values())
                .filter(s -> s.getServiceName().equals(serviceCode))
                .findAny().orElse(SERVICE.GOOGLE);

        SERVICE[] s = SERVICE.values();
        for (SERVICE service : s) {
            String serviceKey = propertiesReader
                    .getPropertyValue(service.getServiceName());
            serviceKeys.put(service, serviceKey);
        }

        NativeKeyBind[] bindings = NativeKeyBind.values();
        for (NativeKeyBind keyBind : bindings) {
            String keyText = propertiesReader
                    .getPropertyValue(keyBind.toString());
            keyBindings.put(keyBind, keyText);
        }

    }

    public void saveConfigs() {
        Map<String, String> properties = new HashMap<>();
        properties.put(TARGET_LANG, targetLanguage.getCode());
        properties.put(PREF_SERVICE, currentService.getServiceName());

        serviceKeys.forEach((service, serviceKey)
                -> properties.put(service.getServiceName(), serviceKey));

        keyBindings.forEach((binding, key)
                -> properties.put(binding.toString(), key));

        try {
            propertiesReader.saveConfig(properties);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setTargetLanguage(LANG targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public LANG getTargetLanguage() {
        return targetLanguage;
    }


    public void setCurrentService(SERVICE currentService) {
        this.currentService = currentService;
    }

    public SERVICE getCurrentService() {
        return currentService;
    }

    public String getServiceKey(SERVICE service) {
        return serviceKeys.get(service);
    }


    public Map<NativeKeyBind, String> getKeyBindings() {
        return keyBindings;
    }

    public boolean isKeyBound(String name) {
        return keyBindings.values().stream()
                .anyMatch(key -> key.equals(name));
    }

    public NativeKeyBind getBind(String name) {
        Map.Entry<NativeKeyBind, String> entry = keyBindings.entrySet()
                .stream().filter(e -> e.getValue().equals(name)).
                findFirst().orElse(null);
        return entry == null ? null : entry.getKey();
    }

    public boolean hasEmptyBindings() {
        return keyBindings.containsValue(null);
    }

}
