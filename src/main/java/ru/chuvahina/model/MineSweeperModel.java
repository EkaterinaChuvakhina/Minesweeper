package ru.chuvahina.model;

import ru.chuvahina.common.Coords;
import ru.chuvahina.common.setting.GameMode;
import ru.chuvahina.common.setting.Settings;
import ru.chuvahina.model.cell.Cell;
import ru.chuvahina.model.cell.CellContent;
import ru.chuvahina.model.cell.CellDto;
import ru.chuvahina.model.cell.CellState;
import ru.chuvahina.model.highscore.Record;
import ru.chuvahina.model.highscore.RecordTable;
import ru.chuvahina.model.reader.FileReader;
import ru.chuvahina.model.reader.RecordTableReader;
import ru.chuvahina.model.reader.RecordTableWriter;
import ru.chuvahina.stopwatch.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MineSweeperModel implements MinesweeperGame {
    private static final String SPACE = " ";
    private static final int MAX_COUNT_NEIGHBORS = 8;
    private static final String ABOUT_FILE_PATH = "about.txt";
    private static final RecordTableWriter RECORD_TABLE_WRITER = new RecordTableWriter();
    private static final RecordTableReader RECORD_TABLE_READER = new RecordTableReader();
    private static final Logger LOGGER = Logger.getLogger(MineSweeperModel.class.getName());
    private static final String EMPTY_STRING = "";
    private final List<ModelListener> modelListeners;
    private final Stopwatch stopwatch;
    private Map<Coords, Cell> cells;
    private RecordTable recordTable;
    private Settings settings;
    private GameState gameState;
    private int closedCellsCount;
    private int bombTotalCount;
    private int bombsRemnant;
    private boolean isFirstMove;

    public MineSweeperModel(Settings settings) {
        modelListeners = new ArrayList<>();
        this.stopwatch = new Stopwatch();
        this.settings = settings;
        initGame();
        initRecordTable();
    }

    @Override
    public void newGame(GameMode mode, String parameters) {
        this.settings = buildSettings(mode, parameters);
        restart();
    }

    @Override
    public void restart() {
        stopwatch.stop();
        initGame();
    }

    @Override
    public List<CellDto> openCell(Coords coords) {
        List<CellDto> cellDtoList = new ArrayList<>();
        for (Cell cell : openCellByCoords(coords)) {
            cellDtoList.add(cell.toCellDto());
        }
        return cellDtoList;
    }

    @Override
    public CellDto switchFlagged(Coords coords) {
        return switchFlaggedByCoords(coords).toCellDto();
    }

    @Override
    public List<CellDto> openAllAroundOpenedCell(Coords coords) {
        List<CellDto> cellDtoList = new ArrayList<>();
        for (Cell cell : openAllCellsAroundOpenedCellByCoords(coords)) {
            cellDtoList.add(cell.toCellDto());
        }
        return cellDtoList;
    }

    @Override
    public void addNewRecord(String name) {
        Record record = new Record(name, stopwatch.elapsed());
        recordTable.addNewRecord(settings.getGameMode(), record);
        RECORD_TABLE_WRITER.writeTableRecord(recordTable.getRecordMap());
    }

    @Override
    public List<List<String>> showHighScores() {
        return recordTable.printRecordTable();
    }

    @Override
    public String showAboutFile() {
        try (FileReader reader = new FileReader(ABOUT_FILE_PATH)) {
            return reader.readAllLines();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error file read in path " + ABOUT_FILE_PATH, e);
        }

        return EMPTY_STRING;
    }

    @Override
    public void resetRecords() {
        recordTable.resetRecords();
        RECORD_TABLE_WRITER.writeTableRecord(recordTable.getRecordMap());
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public int getBombsRemnant() {
        return bombsRemnant;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void addListener(ModelListener listener) {
        modelListeners.add(listener);
    }

    @Override
    public void addListenerForStopwatch(ModelListener listener) {
        stopwatch.addListener(listener);
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    private void initGame() {
        isFirstMove = true;
        bombsRemnant = settings.getBombsCount();
        gameState = GameState.CONTINUES;
        int rows = settings.getRows();
        int columns = settings.getColumns();
        bombTotalCount = settings.getBombsCount();
        closedCellsCount = rows * columns;

        cells = buildCellCoordsMap(rows, columns);
    }

    private Map<Coords, Cell> buildCellCoordsMap(int rows, int columns) {
        Map<Coords, Cell> cells = new HashMap<>(rows * columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Coords newCoords = new Coords(i, j);
                cells.put(newCoords, new Cell(newCoords));
            }
        }

        return cells;
    }

    private List<Cell> openCellByCoords(Coords coords) {
        if (isFirstMove) {
            isFirstMove = false;
            generateField(coords);
            stopwatch.activateTimer();
        }
        List<Cell> result;
        if (GameState.CONTINUES == gameState) {
            Cell current = getCellForCoords(coords);
            switch (current.getState()) {
                case CLOSED:
                    switch (current.getCellContent()) {
                        case BOMB: {
                            stopwatch.stop();
                            gameState = GameState.LOST;
                            result = new ArrayList<>();
                            result.add(makeBombed(coords));
                            result.addAll(openAllBombs());
                            return result;
                        }
                        case ZERO:
                            result = new ArrayList<>(openEmptyCell(coords));
                            if (checkForWin()) {
                                result.addAll(winGame());
                            }
                            return result;
                        case ONE:
                        case TWO:
                        case THREE:
                        case FOUR:
                        case FIVE:
                        case SIX:
                        case SEVEN:
                        case EIGHT:
                            result = new ArrayList<>();
                            result.add(openNumber(coords));
                            if (checkForWin()) {
                                result.addAll(winGame());
                            }
                            return result;
                        default:
                            break;
                    }
                    break;
                case OPENED:
                case FLAGGED:
                    break;
            }
        }
        return Collections.emptyList();
    }

    private Cell switchFlaggedByCoords(Coords coords) {
        if (GameState.CONTINUES == gameState) {
            switch (getCellForCoords(coords).getState()) {
                case CLOSED:
                    setFlagToCell(coords);
                    decrementBombsRemnant();
                    return getCellForCoords(coords);
                case FLAGGED:
                    removeFlagToCell(coords);
                    incrementBombsRemnant();
                    return getCellForCoords(coords);
                case OPENED:
                    break;
            }
        }
        return getCellForCoords(coords);
    }

    private List<Cell> winGame() {
        stopwatch.stop();
        gameState = GameState.VICTORY;
        bombsRemnant = settings.getBombsCount() - closedCellsCount;
        checkForRecord(settings.getGameMode(), stopwatch.elapsed());
        return new ArrayList<>(flagAllClosed());
    }

    private boolean checkForWin() {
        return closedCellsCount == bombTotalCount;
    }

    private void checkForRecord(GameMode gameMode, long time) {
        if (gameMode != GameMode.CUSTOM) {
            if (recordTable.isRecord(gameMode, time)) {
                notifyAboutNewRecords();
            }
        }
    }

    private void generateField(Coords coords) {
        int rows = settings.getRows();
        int columns = settings.getColumns();
        List<Coords> bombsCoords = placedBombs(bombTotalCount, rows, columns, coords);
        setNumbersAroundBomb(bombsCoords);
    }

    public List<Cell> openAllCellsAroundOpenedCellByCoords(Coords coords) {
        List<Cell> result = new ArrayList<>();
        if (GameState.CONTINUES.equals(gameState)) {
            Cell current = getCellForCoords(coords);
            switch (current.getState()) {
                case OPENED: {
                    int bombsCountAroundCells = current.getCellContent().getCode();
                    int flagsCountAroundCells = getFlagsCount(coords);
                    if (bombsCountAroundCells == flagsCountAroundCells) {
                        List<Coords> neighbors = findAllNeighbors(coords);
                        result.addAll(openAllNeighbors(neighbors));
                    }
                }
                break;
                case CLOSED:
                case FLAGGED:
                    break;
            }
        }
        if (checkForWin()) {
            result.addAll(winGame());
        }
        return result;
    }

    private void notifyAboutNewRecords() {
        for (ModelListener listener : modelListeners) {
            listener.newRecord();
        }
    }

    private List<Cell> openEmptyCell(Coords coords) {
        List<Cell> cells = new ArrayList<>();
        Cell snap = openNumber(coords);
        cells.add(snap);
        List<Coords> neighbors = findAllNeighbors(coords);
        cells.addAll(openAllNeighbors(neighbors));
        return cells;
    }

    private List<Cell> openAllNeighbors(List<Coords> neighbors) {
        List<Cell> snapshots = new ArrayList<>();
        for (Coords neighbor : neighbors) {
            snapshots.addAll(openCellByCoords(neighbor));
        }
        return snapshots;
    }

    private List<Coords> findAllNeighbors(Coords coords) {
        List<Coords> neighbors = new ArrayList<>(MAX_COUNT_NEIGHBORS);
        for (int i = coords.getX() - 1; i <= coords.getX() + 1; i++) {
            for (int j = coords.getY() - 1; j <= coords.getY() + 1; j++) {
                Coords current = new Coords(i, j);
                if (!current.equals(coords)) {
                    if (isValidCoords(current)) {
                        neighbors.add(current);
                    }
                }
            }
        }
        return neighbors;
    }

    private List<Coords> placedBombs(int bombTotalCount, int rows, int columns, Coords coords) {
        List<Coords> bombsCoords = new ArrayList<>(bombTotalCount);
        int leftBomb = bombTotalCount;
        while (leftBomb > 0) {
            Coords randomCoords = getRandomCoords(rows, columns);
            Cell randomCell = getCellForCoords(randomCoords);
            if (!randomCoords.equals(coords)) {
                if (!randomCell.isBomb()) {
                    randomCell.setCellContent(CellContent.BOMB);
                    bombsCoords.add(randomCoords);
                    leftBomb--;
                }
            }
        }
        return bombsCoords;
    }

    private void setNumbersAroundBomb(List<Coords> bombsCoords) {
        for (Coords bombCoords : bombsCoords) {
            List<Coords> bombNeighbors = findAllNeighbors(bombCoords);
            for (Coords neighbor : bombNeighbors) {
                Cell current = getCellForCoords(neighbor);
                if (!current.isBomb()) {
                    int code = current.getCellContent().getCode();
                    current.setCellContent(CellContent.contentByNumber(++code));
                }
            }
        }
    }

    private Cell openNumber(Coords coords) {
        Cell current = getCellForCoords(coords);
        current.setState(CellState.OPENED);
        decrementClosedCellsCount();
        return current;
    }

    private List<Cell> openAllBombs() {
        List<Cell> bombs = new ArrayList<>(bombTotalCount);
        for (Cell cell : cells.values()) {
            if (CellState.FLAGGED == cell.getState()) {
                if (CellContent.BOMB != cell.getCellContent()) {
                    cell.setCellContent(CellContent.NO_BOMB);
                    cell.setState(CellState.OPENED);
                    bombs.add(cell);
                }
            } else if (CellContent.BOMB == cell.getCellContent()) {
                cell.setState(CellState.OPENED);
                bombs.add(cell);
            }
        }
        return bombs;
    }

    private Cell makeBombed(Coords coords) {
        Cell bombedCell = getCellForCoords(coords);
        bombedCell.setState(CellState.OPENED);
        bombedCell.setCellContent(CellContent.BOMBED);
        return bombedCell;
    }

    private List<Cell> flagAllClosed() {
        List<Cell> flaggedCells = new ArrayList<>();
        for (Cell cell : cells.values()) {
            if (CellState.CLOSED == cell.getState()) {
                cell.setState(CellState.FLAGGED);
                flaggedCells.add(cell);
            }
        }
        return flaggedCells;
    }

    private boolean isValidCoords(Coords coords) {
        return coords.getX() >= 0 && coords.getX() < settings.getRows() &&
                coords.getY() >= 0 && coords.getY() < settings.getColumns();
    }

    private Cell getCellForCoords(Coords coords) {
        return cells.get(coords);
    }

    private void setFlagToCell(Coords coords) {
        getCellForCoords(coords).setState(CellState.FLAGGED);
    }

    private void removeFlagToCell(Coords coords) {
        getCellForCoords(coords).setState(CellState.CLOSED);
    }

    private Coords getRandomCoords(int maxX, int maxY) {
        Random random = new Random();
        int x = random.nextInt(maxX);
        int y = random.nextInt(maxY);
        return new Coords(x, y);
    }

    private int getFlagsCount(Coords coords) {
        int flagsCount = 0;
        for (Coords neighbor : findAllNeighbors(coords)) {
            Cell neighborCell = getCellForCoords(neighbor);
            if (CellState.FLAGGED == neighborCell.getState()) {
                flagsCount++;
            }
        }
        return flagsCount;
    }

    private void initRecordTable() {
        Map<GameMode, Record> recordTableFromFile = RECORD_TABLE_READER.readRecordTable();
        recordTable = new RecordTable(recordTableFromFile);
    }

    private Settings buildSettings(GameMode mode, String parameters) {
        String[] parametersArray = parameters.strip().split(SPACE);
        int height = Integer.parseInt(parametersArray[0]);
        int width = Integer.parseInt(parametersArray[1]);
        int passedBombCount = Integer.parseInt(parametersArray[2]);
        int validBombCount = getValidCountBomb(height, width, passedBombCount);

        return new Settings(mode, height, width, validBombCount);
    }

    private int getValidCountBomb(int height, int width, int bombCount) {
        int fieldSize = height * width;
        return fieldSize <= bombCount ? fieldSize / 2 : bombCount;
    }

    private void decrementClosedCellsCount() {
        closedCellsCount--;
    }

    private void decrementBombsRemnant() {
        bombsRemnant--;
    }

    private void incrementBombsRemnant() {
        bombsRemnant++;
    }
}
