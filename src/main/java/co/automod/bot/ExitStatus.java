package co.automod.bot;

public enum ExitStatus {
    INVALID_CONFIG(1);

    private final int code;

    ExitStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
