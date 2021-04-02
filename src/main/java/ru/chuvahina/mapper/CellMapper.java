package ru.chuvahina.mapper;

import ru.chuvahina.model.cell.CellContent;
import ru.chuvahina.model.cell.CellDto;
import ru.chuvahina.model.cell.CellState;
import ru.chuvahina.view.imageloader.IconType;

public final class CellMapper {
    private CellMapper() {
    }

    public static IconType toIconType(CellDto cellDto) {
        CellState cellState = cellDto.getState();
        CellContent cellContent = cellDto.getCellContent();
        switch (cellState) {
            case CLOSED:
            case FLAGGED:
                return getIconTypeForCellState(cellState);
            case OPENED:
                return getIconTypeForCellContent(cellContent);
        }
        return null;
    }

    public static IconType getIconTypeForCellContent(CellContent cellContents) {
        switch (cellContents) {
            case ZERO:
                return IconType.ZERO;
            case ONE:
                return IconType.ONE;
            case TWO:
                return IconType.TWO;
            case THREE:
                return IconType.THREE;
            case FOUR:
                return IconType.FOUR;
            case FIVE:
                return IconType.FIVE;
            case SIX:
                return IconType.SIX;
            case SEVEN:
                return IconType.SEVEN;
            case EIGHT:
                return IconType.EIGHT;
            case BOMB:
                return IconType.BOMB;
            case BOMBED:
                return IconType.BOMBED;
            case NO_BOMB:
                return IconType.NO_BOMB;
        }
        return null;
    }

    public static IconType getIconTypeForCellState(CellState cellState) {
        switch (cellState) {
            case CLOSED:
                return IconType.CLOSED;
            case FLAGGED:
                return IconType.FLAGGED;
        }
        return null;
    }
}
