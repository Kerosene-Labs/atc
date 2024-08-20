package io.kerosenelabs.atc.constant;

public enum AnsiCodes {
    RESET("\u001b[0m"),
    COLOR_CYAN("\u001b[36m"),
    COLOR_BLUE("\u001b[34m");

    private String code;

    AnsiCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
