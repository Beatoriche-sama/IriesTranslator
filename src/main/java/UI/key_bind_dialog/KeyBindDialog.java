package UI.key_bind_dialog;

import configs.NativeKeyBind;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class KeyBindDialog extends JDialog {
    private final Map<NativeKeyBind, String> keyBindings;
    private final ArrayList<KeyInputField> allFields;
    private final Font font = new Font("TimesRoman", Font.BOLD, 20);

    public KeyBindDialog(Map<NativeKeyBind, String> keyBindings) {
        this.keyBindings = keyBindings;
        this.allFields = new ArrayList<>(keyBindings.size());

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
            allFields.forEach(keyField->{
                NativeKeyBind keyBind = keyField.getKeyBinding();
                String text =  keyField.getText();
                keyBindings.put(keyBind, text);
            });
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

        KeyInputField keyInputField = new KeyInputField(keyBind, allFields);
        setFieldLook(keyInputField);
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
