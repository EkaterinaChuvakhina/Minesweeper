package ru.chuvahina.presenter;

import ru.chuvahina.common.Coords;
import ru.chuvahina.model.MinesweeperGame;
import ru.chuvahina.model.ModelListener;
import ru.chuvahina.model.cell.CellDto;
import ru.chuvahina.mapper.CellMapper;
import ru.chuvahina.mapper.GameStateMapper;
import ru.chuvahina.mapper.NumberMapper;
import ru.chuvahina.mapper.TimeMapper;
import ru.chuvahina.view.imageloader.IconType;
import ru.chuvahina.view.View;
import ru.chuvahina.view.ViewListener;
import ru.chuvahina.common.setting.GameMode;
import ru.chuvahina.common.setting.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Presenter implements ViewListener,ModelListener {
    private final MinesweeperGame minesweeperGame;
    private final View view;

    public Presenter(View view, MinesweeperGame minesweeperGame, Settings settings) {
        this.view = view;
        List<IconType> iconTypes = NumberMapper.toIconType(settings.getBombsCount());
        this.view.updateBombCount(iconTypes);
        this.minesweeperGame = minesweeperGame;
        minesweeperGame.addListener(this);
        view.addListener(this);
        view.addListenerForSettingsWindow(this);
        minesweeperGame.addListenerForStopwatch(this);
    }

    @Override
    public void pressRightButton(Coords coords) {
        Map<Coords, IconType> resultMap = new HashMap<>();
        CellDto result = minesweeperGame.switchFlagged(coords);

        int bombRemnant = minesweeperGame.getBombsRemnant();
        List<IconType> iconType = NumberMapper.toIconType(bombRemnant);
        resultMap.put(result.getCoords(), CellMapper.toIconType(result));

        view.updateGameField(resultMap);
        view.updateBombCount(iconType);
    }

    @Override
    public void pressMediumButton(Coords coords) {
        List<CellDto> changedCells = minesweeperGame.openAllAroundOpenedCell(coords);
        updateChangedCells(changedCells);
    }

    @Override
    public void pressRestartButton() {
        minesweeperGame.restart();
        view.restart();
        int bombRemnant = minesweeperGame.getBombsRemnant();
        List<IconType> iconType = NumberMapper.toIconType(bombRemnant);
        view.updateBombCount(iconType);
    }

    @Override
    public void pressLeftButton(Coords coords) {
        List<CellDto> changedCells = minesweeperGame.openCell(coords);
        updateChangedCells(changedCells);
    }

    @Override
    public void pressChangeModeButton(GameMode mode, String parameters) {
        minesweeperGame.newGame(mode, parameters);
        int bombsRemnant = minesweeperGame.getBombsRemnant();
        List<IconType> bombCountIcon = NumberMapper.toIconType(bombsRemnant);
        view.newGame(minesweeperGame.getSettings());
        view.updateBombCount(bombCountIcon);
    }

    @Override
    public void pressAboutButton() {
        String data = minesweeperGame.showAboutFile();
        view.showAbout(data);
    }

    @Override
    public void pressHighScoreButton() {
        List<List<String>> data = minesweeperGame.showHighScores();
        view.showHighScores(data);
    }

    @Override
    public void pressResetRecordButton() {
        minesweeperGame.resetRecords();
    }

    @Override
    public void timeUpdate(long time) {
        List<IconType> iconType = TimeMapper.toIconType(time);
        view.updateTimer(iconType);
    }

    @Override
    public void newRecord() {
        String data = view.getNameChampion();
        minesweeperGame.addNewRecord(data);
    }

    @Override
    public void pressExitButton() {
        minesweeperGame.exit();
    }

    private void updateChangedCells(List<CellDto> changedCells) {
        Map<Coords, IconType> iconTypes = new HashMap<>();
        for (CellDto cellDto : changedCells) {
            iconTypes.put(cellDto.getCoords(), CellMapper.toIconType(cellDto));

        }
        int bombRemnant = minesweeperGame.getBombsRemnant();
        List<IconType> iconType = NumberMapper.toIconType(bombRemnant);
        IconType gameState = GameStateMapper.toIconType(minesweeperGame.getGameState());
        view.updateGameField(iconTypes);
        view.updateGameState(gameState);
        view.updateBombCount(iconType);
    }
}

