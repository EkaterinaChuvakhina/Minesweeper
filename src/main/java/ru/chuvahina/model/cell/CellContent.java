package ru.chuvahina.model.cell;

public enum CellContent {
    BOMB(-1),
    BOMBED(-2),
    NO_BOMB(-3),
    TWO(2),
    ONE(1),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    ZERO(0);

    private final int code;

    CellContent(int code) {
        this.code = code;
    }

    public static CellContent contentByNumber(int code) {
        for (CellContent cellContent : values()) {
            if (cellContent.code == code) {
                return cellContent;
            }
        }
        throw new IllegalArgumentException("Unknown code " + code + " for cell type");
    }

    public int getCode() {
        return code;
    }
}
