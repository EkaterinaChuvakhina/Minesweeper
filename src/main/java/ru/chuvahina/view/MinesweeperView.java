package ru.chuvahina.view;

import ru.chuvahina.common.Coords;
import ru.chuvahina.common.setting.Settings;
import ru.chuvahina.view.imageloader.IconType;
import ru.chuvahina.view.imageloader.ImageLoader;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MinesweeperView implements View {
    private static final int SCOREBOARDS_SIZE = 3;
    private static final Map<IconType, ImageIcon> IMAGE_CACHE = new ImageLoader().loadAllImages();
    private final List<ViewListener> listeners;
    private final ListenerGameFieldAction listener;
    private final JFrame mainWindow;
    private final SettingsWindow settingsWindow;
    private JLabel[] timeDisplay;
    private JLabel[] remainingBombsDisplay;
    private Settings settings;
    private JButton gameStateDisplay;
    private JPanel informPanel;
    private JPanel gameField;
    private JMenuBar menuBar;
    private Map<Coords, JButton> cells;

    public MinesweeperView(Settings settings) {
        this.settings = settings;
        mainWindow = new JFrame("Minesweeper");
        settingsWindow = new SettingsWindow();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        listeners = new ArrayList<>();
        listener = new ListenerGameFieldAction();
        initMenuBar();
        init();
    }

    @Override
    public void updateGameField(Map<Coords, IconType> types) {
        types.forEach((k, v) -> {
            ImageIcon icon = IMAGE_CACHE.get(v);
            cells.get(k).setIcon(icon);
        });
    }

    @Override
    public void restart() {
        init();
    }

    @Override
    public void updateGameState(IconType type) {
        gameStateDisplay.setIcon(IMAGE_CACHE.get(type));
    }

    @Override
    public void updateBombCount(List<IconType> numbers) {
        setZeroValue();
        updateScoreboard(numbers, remainingBombsDisplay);
    }

    @Override
    public void updateTimer(List<IconType> numbers) {
        updateScoreboard(numbers, timeDisplay);
    }

    @Override
    public void updateScoreboard(List<IconType> iconTypeList, JLabel[] scoreboard) {
        int iconTypeListIterator = iconTypeList.size() - 1;
        int scoreboardIterator = scoreboard.length - 1;
        while (iconTypeListIterator >= 0) {
            Icon newIconType = IMAGE_CACHE.get(iconTypeList.get(iconTypeListIterator));
            scoreboard[scoreboardIterator].setIcon(newIconType);
            iconTypeListIterator--;
            scoreboardIterator--;
        }
    }

    @Override
    public String getNameChampion() {
        return JOptionPane.showInputDialog(String.format("You are champion in %s level! " +
                "Input your name", settings.getGameMode()));
    }

    @Override
    public void showAbout(String information) {
        JOptionPane.showMessageDialog(menuBar, information, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void newGame(Settings settings) {
        this.settings = settings;
        init();
    }

    @Override
    public void showHighScores(List<List<String>> data) {
        JDialog highScoreWindow = new JDialog();
        BoxLayout layout = new BoxLayout(highScoreWindow.getContentPane(), BoxLayout.Y_AXIS);
        highScoreWindow.getContentPane().setLayout(layout);
        highScoreWindow.setTitle("High score");

        String[] headers = {"Level", "Name", "Time"};

        Vector<Vector<String>> lines = new Vector<>();
        Vector<String> header = new Vector<>();

        for (int i = 0; i < data.size(); i++) {
            header.add(headers[i]);
            Vector<String> row = new Vector<>(data.get(i));
            lines.add(row);
        }


        JTable recordTable = new JTable(lines, header);
        recordTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane();
        highScoreWindow.add(scrollPane);
        scrollPane.setViewportView(recordTable);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            notifyListenersOnPressResetRecordButton();
            highScoreWindow.dispose();
        });


        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> highScoreWindow.dispose());

        buttonPanel.add(resetButton);
        buttonPanel.add(okButton);
        highScoreWindow.getContentPane().add(scrollPane);
        highScoreWindow.add(buttonPanel);

        highScoreWindow.setSize(220, 150);
        highScoreWindow.setLocationRelativeTo(null);
        highScoreWindow.setVisible(true);
    }

    @Override
    public void addListener(ViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void addListenerForSettingsWindow(ViewListener listener) {
        settingsWindow.addListener(listener);
    }

    public void init() {
        informPanelInit();
        gameFieldInit();
        initMainWindow();
    }

    private void initMainWindow() {
        mainWindow.setSize(gameField.getWidth(), gameField.getHeight() + informPanel.getHeight());
        mainWindow.setIconImage(getIcon(IconType.MAIN_ICON).getImage());
        mainWindow.setJMenuBar(menuBar);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstrainsTuner constrainsTuner = new GridBagConstrainsTuner();
        constrainsTuner.setInsets(5, 5, 5, 5);

        constrainsTuner.fillHorizontally();
        contentPane.add(informPanel, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(1);
        contentPane.add(gameField, constrainsTuner.getConstraints());

        mainWindow.setContentPane(contentPane);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(e -> settingsWindow.createParamsSetter());

        JMenuItem highScore = new JMenuItem("High scores");
        highScore.addActionListener(e -> notifyListenersOnPressHighScoreButton());

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> notifyListenersOnPressAboutButton());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> notifyExitAction());

        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(highScore);
        gameMenu.addSeparator();
        gameMenu.add(about);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        menuBar.add(gameMenu);
    }

    private void notifyExitAction() {
        for (ViewListener listener : listeners) {
            listener.pressExitButton();
        }
    }

    private void informPanelInit() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        informPanel = new JPanel(new GridBagLayout());
        informPanel.setBackground(Color.LIGHT_GRAY);
        informPanel.setBorder(border);

        GridBagConstrainsTuner constraintsTuner = new GridBagConstrainsTuner();
        constraintsTuner.setInsets(20, 1, 1, 1);

        JPanel timerPanel = new JPanel(new GridLayout(1, 3));
        timeDisplay = new JLabel[SCOREBOARDS_SIZE];
        for (int i = 0; i < timeDisplay.length; i++) {
            timeDisplay[i] = createNumber();
            timerPanel.add(timeDisplay[i]);
        }
        gameStateDisplay = initGameStateDisplay();

        JPanel remnantBombPanel = new JPanel(new GridLayout(1, 3));
        remainingBombsDisplay = new JLabel[SCOREBOARDS_SIZE];
        for (int i = 0; i < remainingBombsDisplay.length; i++) {
            remainingBombsDisplay[i] = createNumber();
            remnantBombPanel.add(remainingBombsDisplay[i]);
        }
        constraintsTuner.alignLeft();
        informPanel.add(remnantBombPanel, constraintsTuner.getConstraints());

        gameStateDisplay = initGameStateDisplay();

        constraintsTuner.setColumnIndex(1);
        informPanel.add(gameStateDisplay, constraintsTuner.getConstraints());

        constraintsTuner.setColumnIndex(2);
        constraintsTuner.alignRight();
        informPanel.add(timerPanel, constraintsTuner.getConstraints());
    }

    private void setZeroValue() {
        for (JLabel label : remainingBombsDisplay) {
            label.setIcon(IMAGE_CACHE.get(IconType.DISPLAY_ZERO));
        }
    }


    private JLabel createNumber() {
        JLabel time = new JLabel();
        time.setIcon(MinesweeperView.IMAGE_CACHE.get(IconType.DISPLAY_ZERO));
        time.setBorder(BorderFactory.createEmptyBorder());
        return time;
    }

    private JButton initGameStateDisplay() {
        JButton gameState = new JButton(IMAGE_CACHE.get(IconType.RESTART));
        gameState.setBorder(BorderFactory.createEmptyBorder());
        gameState.setFocusPainted(false);
        gameState.setContentAreaFilled(false);
        gameState.addMouseListener(new ListenerRestartAction());
        return gameState;
    }

    private void gameFieldInit() {
        int rows = settings.getRows();
        int columns = settings.getColumns();

        cells = new HashMap<>(rows * columns);

        gameField = new JPanel(new GridLayout(rows, columns, 0, 0));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton cell = createCell();
                cell.setActionCommand(i + " " + j);
                gameField.add(cell);
                cells.put(new Coords(i, j), cell);
            }
        }
    }

    private JButton createCell() {
        JButton cell = new JButton();
        cell.addMouseListener(listener);
        cell.setIcon(getIcon(IconType.CLOSED));
        cell.setBorder(BorderFactory.createEmptyBorder());
        cell.setFocusPainted(false);
        cell.setContentAreaFilled(false);
        return cell;
    }

    private ImageIcon getIcon(IconType iconType) {
        return IMAGE_CACHE.get(iconType);
    }


    public void notifyListenersOnPressRightButton(Coords coords) {
        for (ViewListener listener : listeners) {
            listener.pressRightButton(coords);
        }
    }

    void notifyListenersOnPressLeftButton(Coords coords) {
        for (ViewListener listener : listeners) {
            listener.pressLeftButton(coords);
        }
    }

    void notifyListenersOnPressMediumButton(Coords coords) {
        for (ViewListener listener : listeners) {
            listener.pressMediumButton(coords);
        }
    }

    void notifyListenersOnPressRestartButton() {
        for (ViewListener listener : listeners) {
            listener.pressRestartButton();
        }
    }

    void notifyListenersOnPressAboutButton() {
        for (ViewListener listener : listeners) {
            listener.pressAboutButton();
        }
    }

    private void notifyListenersOnPressHighScoreButton() {
        for (ViewListener listener : listeners) {
            listener.pressHighScoreButton();
        }
    }

    private void notifyListenersOnPressResetRecordButton() {
        for (ViewListener listener : listeners) {
            listener.pressResetRecordButton();
        }
    }

    class ListenerRestartAction extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JButton pressedJButton = (JButton) e.getSource();
            pressedJButton.setIcon(IMAGE_CACHE.get(IconType.CLICKED_RESTART));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JButton pressedJButton = (JButton) e.getSource();
            pressedJButton.setIcon(IMAGE_CACHE.get(IconType.RESTART));
            notifyListenersOnPressRestartButton();
        }

    }

    class ListenerGameFieldAction extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String[] buttonCoords;
            buttonCoords = clickedButton.getActionCommand().split(" ");
            int x = Integer.parseInt(buttonCoords[0]);
            int y = Integer.parseInt(buttonCoords[1]);
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    notifyListenersOnPressLeftButton(new Coords(x, y));
                    break;
                case MouseEvent.BUTTON2:
                    notifyListenersOnPressMediumButton(new Coords(x, y));
                    break;
                case MouseEvent.BUTTON3:
                    notifyListenersOnPressRightButton(new Coords(x, y));
                    break;
            }
        }
    }
}
