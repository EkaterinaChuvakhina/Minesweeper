package ru.chuvahina.view;

import ru.chuvahina.common.setting.GameMode;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SettingsWindow {
    private final List<ViewListener> listeners = new ArrayList<>();

    private JRadioButton beginner;
    private JRadioButton amateur;
    private JRadioButton advanced;
    private JRadioButton custom;

    private JSpinner height;
    private JSpinner width;
    private JSpinner bombs;

    public SettingsWindow() {
    }

    public void createParamsSetter() {
        JDialog settingsWindow = new JDialog();
        settingsWindow.setResizable(true);

        JPanel contents = new JPanel();

        contents.setLayout(new GridBagLayout());
        settingsWindow.setContentPane(contents);

        ButtonGroup gameModes = new ButtonGroup();
        beginner = new JRadioButton("BEGINNER");
        beginner.setSelected(true);
        amateur = new JRadioButton("AMATEUR");
        advanced = new JRadioButton("ADVANCED");
        custom = new JRadioButton("CUSTOM");

        height = createInputField(9,9,24);
        width = createInputField(9,9,32);
        bombs = createInputField(10,1,600);


        JLabel nameHeight = new JLabel("height");
        JLabel nameWidth = new JLabel("width");
        JLabel nameBombs = new JLabel("bombs");


        RadioButtonListener radioButtonListener = new RadioButtonListener();

        beginner.addActionListener(radioButtonListener);
        amateur.addActionListener(radioButtonListener);
        advanced.addActionListener(radioButtonListener);
        custom.addActionListener(radioButtonListener);


        gameModes.add(beginner);
        gameModes.add(amateur);
        gameModes.add(advanced);
        gameModes.add(custom);

        GridBagConstrainsTuner constrainsTuner = new GridBagConstrainsTuner();
        constrainsTuner.setInsets(10, 10, 10, 10);

        contents.add(beginner, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(1);
        contents.add(amateur, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(2);
        contents.add(advanced, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(3);
        contents.add(custom, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(1);
        constrainsTuner.setColumnIndex(1);
        contents.add(nameHeight, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(2);
        contents.add(nameWidth, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(3);
        contents.add(nameBombs, constrainsTuner.getConstraints());

        constrainsTuner.setInsets(5, 5, 5, 5);
        constrainsTuner.setRowIndex(1);

        constrainsTuner.setColumnIndex(2);
        contents.add(height, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(2);
        contents.add(width, constrainsTuner.getConstraints());

        constrainsTuner.setRowIndex(3);
        contents.add(bombs, constrainsTuner.getConstraints());

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {

            GameMode mode = getSelectedGameMode();

            String settings = height.getValue() + " "
                    + width.getValue() + " "
                    + bombs.getValue();
            settingsWindow.dispose();
            notifyListenersOnChangeSettings(mode, settings);
        });

        constrainsTuner.setRowIndex(4);
        constrainsTuner.setColumnIndex(2);
        contents.add(okButton, constrainsTuner.getConstraints());
        settingsWindow.pack();
        settingsWindow.setLocationRelativeTo(null);
        settingsWindow.setVisible(true);


    }

    private GameMode getSelectedGameMode() {
        GameMode mode = null;
        if (beginner.isSelected()) {
            mode = GameMode.BEGINNER;
        } else if (amateur.isSelected()) {
            mode = GameMode.AMATEUR;
        } else if (advanced.isSelected()) {
            mode = GameMode.ADVANCED;
        } else if (custom.isSelected()) {
            mode = GameMode.CUSTOM;
        }
        return mode;
    }

    public void addListener(ViewListener listener) {
        listeners.add(listener);
    }

    void notifyListenersOnChangeSettings(GameMode mode, String settings) {
        for (ViewListener listener : listeners) {
            listener.pressChangeModeButton(mode, settings);
        }
    }

    private JSpinner createInputField(int currentValue, int minValue, int maxValue) {
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(currentValue,minValue,maxValue, 1);
        JSpinner inputField = new JSpinner(spinnerNumberModel);
        JFormattedTextField txt = ((JSpinner.NumberEditor)inputField.getEditor()).getTextField();
        ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
        inputField.setEnabled(false);
        return inputField;
    }

    private void inputValueForGameMode(int value, JSpinner field) {
        field.setValue(value);
    }

    public class RadioButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            GameMode mode = getGameMode(actionEvent);
            switch (mode) {
                case BEGINNER:
                case AMATEUR:
                case ADVANCED:
                    inputValueForGameMode(mode.getRow(), height);
                    inputValueForGameMode(mode.getColumn(), width);
                    inputValueForGameMode(mode.getTotalBombCount(), bombs);
                    makeEditableInputField(false);
                    break;
                case CUSTOM:
                    makeEditableInputField(true);
            }
        }
        private void makeEditableInputField(boolean editable) {
            height.setEnabled(editable);
            width.setEnabled(editable);
            bombs.setEnabled(editable);
        }

        private GameMode getGameMode(ActionEvent actionEvent) {
            JRadioButton selectedButton = (JRadioButton) actionEvent.getSource();
            String text = selectedButton.getText();
            return GameMode.valueOf(text);
        }
    }
}
