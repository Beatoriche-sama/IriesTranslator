package UI;

import UI.key_bind_dialog.KeyBindDialog;
import UI.main_dialog.MainDialog;
import UI.main_dialog.UpdateTranslateRequest;
import UI.overlay.AreaSelectionOverlay;
import UI.overlay.MouseReleaseEvent;
import com.deepl.api.DeepLException;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import configs.Configs;
import configs.NativeKeyBind;
import ocr.GoogleVisionOCR;
import org.json.JSONException;
import translate.LANG;
import translate.SERVICE;
import translate.TranslatorService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App implements MouseReleaseEvent, UpdateTranslateRequest, NativeKeyListener {
    private final AreaSelectionOverlay overlay;
    private final MainDialog mainDialog;
    private final KeyBindDialog keyBindDialog;

    private final TranslatorService translatorService;
    private final Configs configs;

    public static void main(String[] args) throws IOException {
        new App();
    }

    public App() throws IOException {
        this.configs = new Configs();
        this.translatorService = new TranslatorService(
                configs.getServiceKey(SERVICE.DEEPL));

        this.overlay = new AreaSelectionOverlay(this);
        this.mainDialog = new MainDialog(configs, this);
        mainDialog.setLocationRelativeTo(overlay);

        keyBindDialog = new KeyBindDialog(configs.getKeyBindings());
        keyBindDialog.setLocationRelativeTo(overlay);
        if (configs.hasEmptyBindings()) showKeyBindDialog();

        URL imageURL = App.class.getResource("/penguin.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        TrayIcon trayIcon = new TrayIcon(icon,
                "Iries Translator", createTrayMenu());
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage("Iries Translator", "Iries Translator is ready to translate",
                TrayIcon.MessageType.INFO);

        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        if (keyBindDialog.isVisible()) return;

        int code = e.getKeyCode();
        int modifiers = e.getModifiers();
        String nativeKeyName = NativeKeyEvent.getKeyText(code);
        String fullName = NativeKeyEvent.getModifiersText(modifiers) + nativeKeyName;

        if (configs.isKeyBound(fullName))
            redirectToAction(configs.getBind(fullName));

    }

    private void redirectToAction(NativeKeyBind nativeKeyBind) {
        switch (nativeKeyBind) {
            case SELECT_AREA -> selectArea();
            case SHOW_APP -> showMainDialog();
            case BIND_KEYS -> showKeyBindDialog();
            case EXIT_APP -> exit();
        }
    }

    private void selectArea() {
        overlay.setClickable();
        mainDialog.setVisible(false);
    }

    private void showMainDialog() {
        mainDialog.setVisible(true);
    }

    private void showKeyBindDialog() {
        keyBindDialog.setVisible(true);
    }

    private void exit() {
        configs.saveConfigs();
        System.exit(0);
    }

    private PopupMenu createTrayMenu() {
        PopupMenu menu = new PopupMenu();

        Arrays.stream(NativeKeyBind.values()).forEach(boundKey -> {
            MenuItem menuItem = new MenuItem(boundKey.getName());
            menuItem.addActionListener(e -> redirectToAction(boundKey));
            menu.add(menuItem);
        });

        return menu;
    }

    @Override
    public String update(String text) {
        try {
            SERVICE service = configs.getCurrentService();
            LANG lang = configs.getTargetLanguage();
            return translatorService.translate(service, lang, text);
        } catch (DeepLException | InterruptedException
                 | JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRelease(Point pointSelected, Dimension dimensionSelected) {
        if (dimensionSelected == null) return;
        mainDialog.setVisible(true);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {

            BufferedImage areaScreenshot
                    = screenshot(pointSelected, dimensionSelected);

            String recognizedText, translatedText;
            try {
                mainDialog.updateStatusLabel("Text recognition...");
                recognizedText = GoogleVisionOCR.doOCR(areaScreenshot);
                mainDialog.updateOriginalTextArea(recognizedText);
                System.out.println(recognizedText);

                mainDialog.updateStatusLabel("Text translation...");
                translatedText = translatorService.translate(
                        configs.getCurrentService(), configs.getTargetLanguage(), recognizedText);
                mainDialog.updateTranslatedTextArea(translatedText);

                mainDialog.updateStatusLabel("Text is translated.");
            } catch (IOException | DeepLException
                     | InterruptedException | JSONException e) {
                throw new RuntimeException(e);
            }

        });

        executorService.shutdown();
    }

    private BufferedImage screenshot(Point point, Dimension dimension) {
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        Rectangle captureRect = new Rectangle(point.x, point.y,
                dimension.width, dimension.height);
        return robot.createScreenCapture(captureRect);
    }


}
