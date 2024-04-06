package translate;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TranslatorService {
    private final String DEEPL_KEY;

    public TranslatorService(String deeplKey) {
        DEEPL_KEY = deeplKey;
    }

    public String translate(SERVICE service, LANG target, String text) throws DeepLException,
            InterruptedException, JSONException, IOException {
        String result = null;
        switch (service) {
            case DEEPL -> result = deeplTranslate(target, text);
            //case GOOGLE_NO_KEY -> result = googleTranslateNoKey(target, text);
            case GOOGLE -> result = googleTranslate(target, text);
        }
        return result;
    }

    public String googleTranslate(LANG target, String text) {
        Translate translate = TranslateOptions.newBuilder().build().getService();
        Translation translation = translate.translate(text,
                Translate.TranslateOption.targetLanguage(target.getCode()));
        return translation.getTranslatedText();
    }

   /* public static String googleTranslateNoKey(LANG target,
                                              String text) throws IOException, JSONException {
        String request = "https://translate.googleapis.com/translate_a/single?client=gtx&" +
                 "&tl=" + target.getCode() + "&dt=t&q=" + text;
                 //"sl=" + source.getCode()

        URL oracle = new URL(request);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));

        StringBuilder inputLine = new StringBuilder();
        String currentLine;
        while ((currentLine = in.readLine()) != null)
            inputLine.append(currentLine);
        in.close();

        JSONArray responseArray = new JSONArray(inputLine.toString());
        JSONArray resultArray = responseArray.getJSONArray(0);
        return resultArray.getString(0);
    }*/

    public String deeplTranslate(LANG target, String text)
            throws DeepLException, InterruptedException {
        Translator translator = new Translator(DEEPL_KEY);
        TextResult result =
                translator.translateText(text, null, target.getCode());
        return result.getText();
    }
}
