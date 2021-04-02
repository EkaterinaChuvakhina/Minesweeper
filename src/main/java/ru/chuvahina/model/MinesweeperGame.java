package ru.chuvahina.model;

import ru.chuvahina.common.Coords;
import ru.chuvahina.model.cell.CellDto;
import ru.chuvahina.common.setting.GameMode;
import ru.chuvahina.common.setting.Settings;

import java.util.List;

public interface MinesweeperGame {

    void newGame(GameMode mode, String parameters);

    void restart();

    void exit();

    List<CellDto> openCell(Coords coords);

    CellDto switchFlagged(Coords coords);

    List<CellDto> openAllAroundOpenedCell(Coords coords);

    void addNewRecord(String name);

    List<List<String>> showHighScores();

    String showAboutFile();

    void resetRecords();

    int getBombsRemnant();

    GameState getGameState();

    void addListener(ModelListener listener);

    void addListenerForStopwatch(ModelListener listener);

    Settings getSettings();
}
