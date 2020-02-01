package soen6441riskgame.models.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandOption<T> {
    private Map<String, T> options;

    public CommandOption() {
        options = new HashMap<String, T>();
    }

    public CommandOption(Map<String, T> options) {
        this.options = options;
    }

    public Map<String, T> getOptions() {
        return options;
    }

    public void setOptions(Map<String, T> options) {
        this.options = options;
    }

    public void addOption(String key, T value) {
        options.put(key, value);
    }

    public T getOption(String key) {
        return options.get(key);
    }
}