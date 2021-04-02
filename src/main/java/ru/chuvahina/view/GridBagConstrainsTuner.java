package ru.chuvahina.view;

import java.awt.*;

public class GridBagConstrainsTuner {

    private final GridBagConstraints constraints;

    public GridBagConstrainsTuner() {
        constraints = new GridBagConstraints();
    }

    public void setColumnIndex(int index) {
        constraints.gridx = index;
    }

    public void setRowIndex(int index) {
        constraints.gridy = index;
    }

    public void fillHorizontally() {
        constraints.fill = GridBagConstraints.HORIZONTAL;
    }

    public void alignLeft() {
        constraints.anchor = GridBagConstraints.LINE_START;
    }

    public void alignRight() {
        constraints.anchor = GridBagConstraints.LINE_END;
    }

    public void setInsets(int left, int top, int right, int bottom) {
        constraints.insets = new Insets(top, left, bottom, right);
    }

    public GridBagConstraints getConstraints() {
        return constraints;
    }
}
