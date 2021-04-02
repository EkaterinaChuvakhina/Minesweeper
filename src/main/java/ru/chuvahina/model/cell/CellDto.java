package ru.chuvahina.model.cell;

import ru.chuvahina.common.Coords;

public class CellDto {
    private final Coords coords;
    private final CellState state;
    private final CellContent cellContent;

    public CellDto(Coords coords, CellState state, CellContent cellContent) {
        this.state = state;
        this.cellContent = cellContent;
        this.coords = coords;
    }

    public CellState getState() {
        return state;
    }

    public CellContent getCellContent() {
        return cellContent;
    }

    public Coords getCoords() {
        return coords;
    }
}
