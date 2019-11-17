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

import soen6441riskgame.App;

public class WindowPane extends JPanel {
    private static final long serialVersionUID = 3555490927568246300L;
    private JTextArea output;

    public WindowPane() {
        setLayout(new BorderLayout());
        output = new JTextArea();
        output.setBackground(Color.BLACK);
        output.setForeground(Color.WHITE);
        Font font = loadFont();
        output.setFont(font);
        add(new JScrollPane(output));
    }

    public Font loadFont() {
        Font font = null;
        try {
            InputStream fontFile = new BufferedInputStream(new FileInputStream("src\\main\\java\\soen6441riskgame\\resources\\RobotoMono-Regular.ttf"));
            Font localFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            font = localFont.deriveFont(Font.PLAIN, 14);
        } catch (Exception ex) {
            font = null;
        }

        return font;
    }

    public void appendText(final String text) {
        if (EventQueue.isDispatchThread()) {
            output.append(text);
            output.setCaretPosition(output.getText().length());
        } else {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    appendText(text);
                }
            });
        }
    }
}