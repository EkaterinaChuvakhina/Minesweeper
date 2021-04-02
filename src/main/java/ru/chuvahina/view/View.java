package ru.chuvahina.view;

import ru.chuvahina.common.Coords;
import ru.chuvahina.common.setting.Settings;
import ru.chuvahina.view.imageloader.IconType;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface View {
    void updateGameField(Map<Coords, IconType> types);

    void updateGameState(IconType type);

    void updateBombCount(List<IconType> numbers);

    void updateTimer(List<IconType> numbers);

    void updateScoreboard(List<IconType> iconTypeList, JLabel[] scoreboard);

    String getNameChampion();

    void showAbout(String information);

    void showHighScores(List<List<String>> data);

    void restart();

    void newGame(Settings settings);

    void addListener(ViewListener listener);

    void addListenerForSettingsWindow(ViewListener listener);
}
