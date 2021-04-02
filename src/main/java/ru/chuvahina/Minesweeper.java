package ru.chuvahina;

import ru.chuvahina.common.setting.Settings;
import ru.chuvahina.model.MineSweeperModel;
import ru.chuvahina.model.MinesweeperGame;
import ru.chuvahina.presenter.Presenter;
import ru.chuvahina.view.View;
import ru.chuvahina.view.MinesweeperView;

import javax.swing.*;

public class Minesweeper {
    public Minesweeper() {
        Settings settings = new Settings();
        View view = new MinesweeperView(settings);
        MinesweeperGame game = new MineSweeperModel(settings);
        new Presenter(view, game, settings);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(Minesweeper::new);
    }
}