package soen6441riskgame.views;

import java.io.PrintStream;

import soen6441riskgame.utils.ConsolePrinter;
import soen6441riskgame.utils.presenter.WindowOutputStream;
import soen6441riskgame.utils.presenter.WindowPane;

/**
 * Abstract class to separate the view for Observer classes
 */
public abstract class SeparatedView {
    protected WindowPane presenter;
    protected PrintStream printStream;

    /**
     * Init the class. Use standard output when running in JUnit test Use custom PrintStream when
     * running normally
     */
    public SeparatedView() {
        if (ConsolePrinter.isJUnitTest()) {
            printStream = System.out;
        } else {
            presenter = ConsolePrinter.createWindowPane("Players World Domination", 600, 600);
            printStream = new PrintStream(new WindowOutputStream(presenter));
        }
    }

    public void clearView() {
        if (presenter != null) {
            presenter.clearView();
        }
    }
}