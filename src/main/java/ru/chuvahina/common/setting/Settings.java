package ru.chuvahina.common.setting;

public class Settings {
    GameMode gameMode;
    private final int rows;
    private final int columns;
    private final int bombsCount;

    public Settings() {
        gameMode = GameMode.BEGINNER;
        this.rows = gameMode.getRow();
        this.columns = gameMode.getColumn();
        this.bombsCount = gameMode.getTotalBombCount();
    }

    public Settings(GameMode gameMode, int rows, int columns, int bombsCount) {
        this.gameMode = gameMode;
        this.rows = rows;
        this.columns = columns;
        this.bombsCount = bombsCount;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getBombsCount() {
        return bombsCount;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
