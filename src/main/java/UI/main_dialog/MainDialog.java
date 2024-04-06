package UI.main_dialog;

import configs.Configs;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Objects;

public class MainDialog extends JDialog{
    private final UpdateTranslateRequest updateTranslateRequest;
    private final Configs configs;
    private String translatedText;
    private boolean isOriginalTextVisible;
    private JTextArea translatedTextArea, originalTextArea;
    private JLabel statusLabel;
    private final Font font = new Font("TimesRoman", Font.BOLD, 17);

    public MainDialog(Configs configs,
                      UpdateTranslateRequest updateTranslateRequest) {
        this.updateTranslateRequest = updateTranslateRequest;
        this.configs = configs;

        setJMenuBar(new MainDialogMenuBar(configs));

        setTitle("Translate window (´｡• ω •｡`)");
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(MainDialog.class
                    .getResource("/penguin.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinimumSize(new Dimension(500,500));
        setAlwaysOnTop(true);
        setContentPane(createContentPane());
        setSize(getPreferredSize());
    }

    public void updateStatusLabel(String text){
        statusLabel.setText(text);
    }
    public void updateOriginalTextArea(String text){
        originalTextArea.setText(text);
    }

    public void updateTranslatedTextArea(String text){
        translatedText = text;
        translatedTextArea.setText(text);
    }

    private JScrollPane createContentPane(){
        JPanel contentPanel = new JPanel();
        this.translatedTextArea = createTextArea(false);
        this.originalTextArea = createTextArea(true);

        this.statusLabel = new JLabel("Current status.");

        JLabel sourceTextLabel = new JLabel("Original:");

        contentPanel.setLayout(new MigLayout());

        contentPanel.add(createOriginalTextButton(sourceTextLabel));
        contentPanel.add(createTranslateButton(), "wrap");
        contentPanel.add(statusLabel, "wrap");
        contentPanel.add(sourceTextLabel, "hidemode 3");
        contentPanel.add(new JLabel("Translated:"), "wrap");
        contentPanel.add(originalTextArea, "hidemode 3");
        contentPanel.add(translatedTextArea);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(contentPanel);
        return scrollPane;
    }

    /*private void copyToClipboard(String textToCopy){
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }*/

    private JButton createTranslateButton(){
        JButton button = new JButton("Translate again");
        button.addActionListener(e -> {
            translatedText = updateTranslateRequest.update(translatedText);
            updateTranslatedTextArea(translatedText);
        });
        return button;
    }

    private JButton createOriginalTextButton(JLabel sourceLabel) {
        JButton button = new JButton("Hide original.");
        button.addActionListener(e -> {
            System.out.println(isOriginalTextVisible);
            String textToAppend
                    = isOriginalTextVisible? "Hide" : "Show";
            button.setText(textToAppend + " original.");
            originalTextArea.setVisible(isOriginalTextVisible);
            sourceLabel.setVisible(isOriginalTextVisible);
            setSize(getPreferredSize());

            this.isOriginalTextVisible = !isOriginalTextVisible;
        });
        return button;
    }

    private JTextArea createTextArea(boolean isEditable) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(isEditable);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(Color.WHITE);
        textArea.setFont(font);
        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        textArea.setMinimumSize(new Dimension(300,200));
        return textArea;
    }


}
