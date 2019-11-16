package soen6441riskgame.utils.presenter;

import java.io.IOException;
import java.io.OutputStream;

public class WindowOutputStream extends OutputStream {
    private StringBuilder buffer;
    private WindowPane windowPane;

    public WindowOutputStream(WindowPane windowPane) {
        buffer = new StringBuilder(128);
        this.windowPane = windowPane;
    }

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