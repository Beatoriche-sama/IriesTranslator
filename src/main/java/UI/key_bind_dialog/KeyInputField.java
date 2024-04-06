package UI.key_bind_dialog;

import configs.NativeKeyBind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class KeyInputField extends JTextField {
    private final NativeKeyBind keyBinding;
    private boolean isMarkedAsSame = false;
    private JTextField fieldWithSameInput = null;
    private final ArrayList<KeyInputField> allFields;

    KeyInputField(NativeKeyBind keyBinding,
                  ArrayList<KeyInputField> allFields) {
        this.keyBinding = keyBinding;
        this.allFields = allFields;

        addFocusListener(new FocusLostListener());
        addKeyListener(new KeyPressedListener());
    }

    private void removeBorder(JTextField textField) {
        textField.setBorder(BorderFactory.createEmptyBorder());
    }

    private class FocusLostListener extends FocusAdapter {
        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            if (!isMarkedAsSame) return;
            removeBorder(fieldWithSameInput);
        }
    }

    private class KeyPressedListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int i = e.getModifiersEx();
            String maskName = InputEvent.getModifiersExText(i);
            String keyText = KeyEvent.getKeyText(e.getKeyCode());
            String fullName = maskName.equals(keyText) ?
                    keyText : maskName + keyText;

            JTextField copy = fieldWithSameInput;
            fieldWithSameInput = allFields.stream()
                    .filter(field -> field.getText().equals(fullName))
                    .findFirst().orElse(null);

            isMarkedAsSame = fieldWithSameInput != null;

            if (isMarkedAsSame) {
                fieldWithSameInput
                        .setBorder(BorderFactory.createLineBorder(Color.red));
            } else {
                if (copy != null) removeBorder(copy);
                setText(fullName);
            }
        }
    }

    public NativeKeyBind getKeyBinding() {
        return keyBinding;
    }
}
