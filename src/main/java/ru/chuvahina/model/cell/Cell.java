package ru.chuvahina.model.cell;

import ru.chuvahina.common.Coords;

public class Cell {
    private final Coords coords;
    private CellState state;
    private CellContent cellContent;

    public Cell(Coords coords) {
        this.state = CellState.CLOSED;
        this.cellContent = CellContent.ZERO;
        this.coords = coords;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public CellContent getCellContent() {
        return cellContent;
    }

    public void setCellContent(CellContent cellContent) {
        this.cellContent = cellContent;
    }

    public boolean isBomb() {
        return CellContent.BOMB == cellContent;
    }

    public CellDto toCellDto(){
        return new CellDto(coords, state, cellContent);
    }
}
