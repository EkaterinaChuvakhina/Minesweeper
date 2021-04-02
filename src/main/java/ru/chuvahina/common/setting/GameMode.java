package ru.chuvahina.common.setting;

public enum GameMode {
    BEGINNER(9, 9, 10),
    AMATEUR(16, 16, 40),
    ADVANCED(30, 16, 99),
    CUSTOM(0, 0, 0);

    private final int row;
    private final int column;
    private final int totalBombCount;

    GameMode(int column, int row, int totalBombCount) {
        this.row = row;
        this.column = column;
        this.totalBombCount = totalBombCount;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getTotalBombCount() {
        return totalBombCount;
    }
}
