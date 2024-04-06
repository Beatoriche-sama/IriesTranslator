package configs;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader extends Properties{
    private final static String propFileName = "src/main/resources/config.properties";

    public String getPropertyValue(String property) throws IOException {
        InputStream inputStream = new FileInputStream(propFileName);
        load(inputStream);
        String result = getProperty(property);
        inputStream.close();
        return result;
    }



    public void saveConfig(Map<String, String> properties) throws IOException {
        InputStream in = new FileInputStream(propFileName);
        load(in);
        in.close();

        OutputStream out = new FileOutputStream(propFileName);
        properties.entrySet().stream()
                .filter(e->e.getValue() == null)
                .forEach(e-> e.setValue(""));
        properties.forEach(this::setProperty);
        store(out, null);
        out.close();
    }
}
