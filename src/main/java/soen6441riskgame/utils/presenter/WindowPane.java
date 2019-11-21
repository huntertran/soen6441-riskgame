package soen6441riskgame.utils.presenter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * re-present a separated view
 */
public class WindowPane extends JPanel {
    private static final long serialVersionUID = 3555490927568246300L;
    private JTextArea textArea;

    /**
     * Presenter constructor
     */
    public WindowPane() {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        Font font = loadFont();
        textArea.setFont(font);
        add(new JScrollPane(textArea));
    }

    /**
     * Load RobotoMono font
     *
     * @return the loaded font, or null is cannot load the font
     */
    public Font loadFont() {
        Font font;
        try {
            InputStream fontFile = new BufferedInputStream(new FileInputStream("src\\main\\java\\soen6441riskgame\\resources\\RobotoMono-Regular.ttf"));
            Font localFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            font = localFont.deriveFont(Font.PLAIN, 14);
        } catch (Exception ex) {
            font = null;
        }

        return font;
    }

    /**
     * Clear the view
     */
    public void clearView() {
        if (textArea != null) {
            textArea.setText(null);
        }
    }

    /**
     * append new text to view
     *
     * @param text the text to append
     */
    public void appendText(final String text) {
        if (EventQueue.isDispatchThread()) {
            textArea.append(text);
            textArea.setCaretPosition(textArea.getText().length());
        } else {
            EventQueue.invokeLater(() -> appendText(text));
        }
    }
}
