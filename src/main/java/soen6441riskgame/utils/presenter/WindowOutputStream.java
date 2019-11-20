package soen6441riskgame.utils.presenter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * custom OutputStream for separated view
 */
public class WindowOutputStream extends OutputStream {
    private StringBuilder buffer;
    private WindowPane windowPane;

    /**
     * constructor
     *
     * @param windowPane the window pane to write text to
     */
    public WindowOutputStream(WindowPane windowPane) {
        buffer = new StringBuilder(128);
        this.windowPane = windowPane;
    }

    /**
     * write character b to output stream
     *
     * @param b character to write
     */
    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);
        if (value.equals("\n")) {
            windowPane.appendText(buffer.toString());
            buffer.delete(0, buffer.length());
        }
    }
}