package UI.key_bind_dialog;

import configs.NativeKeyBind;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeyBindDialog extends JDialog {
    private final Map<NativeKeyBind, String> keyBindings;
    private final Map<NativeKeyBind, JTextField> fieldKey;
    private final ArrayList<JTextField> allFields;
    private final Font font = new Font("TimesRoman", Font.BOLD, 20);

    public KeyBindDialog(Map<NativeKeyBind, String> keyBindings) {
        this.keyBindings = keyBindings;

        int size = keyBindings.size();
        this.fieldKey = new HashMap<>(size);
        this.allFields = new ArrayList<>(size);

        setTitle("Bind global keys.");
        setMinimumSize(new Dimension(500, 500));
        setAlwaysOnTop(true);
        setContentPane(createContentPane());
        setSize(getPreferredSize());
    }

    private JPanel createContentPane() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setLayout(new MigLayout(
                "fill, center, flowy, gapy 10"));

        NativeKeyBind[] nativeKeyBinds = NativeKeyBind.values();
        Arrays.stream(nativeKeyBinds).forEach(key
                -> mainPanel.add(createKeyPanel(key), "grow"));

        JButton confirmButton = new JButton("Ok");
        confirmButton.addActionListener(e -> {
            fieldKey.forEach((key, field)
                    -> keyBindings.put(key, field.getText()));
            KeyBindDialog.this.setVisible(false);
        });
        mainPanel.add(confirmButton, "center");

        return mainPanel;
    }

    private JPanel createKeyPanel(NativeKeyBind keyBind) {
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setBackground(Color.darkGray);
        wrapperPanel.setLayout(new MigLayout("fill"));

        JTextField keyNameField = new JTextField(keyBind.getName());
        keyNameField.setFocusable(false);
        setFieldLook(keyNameField);

        JTextField keyInputField = new KeyInputField(allFields);
        setFieldLook(keyInputField);
        fieldKey.put(keyBind, keyInputField);
        allFields.add(keyInputField);

        wrapperPanel.add(keyNameField, "growx");
        wrapperPanel.add(keyInputField, "growx");
        return wrapperPanel;
    }

    private void setFieldLook(JTextField textField) {
        textField.setEditable(false);
        textField.setFont(font);
        textField.setBackground(Color.LIGHT_GRAY);
    }

}
