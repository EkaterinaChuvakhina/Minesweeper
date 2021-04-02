package ru.chuvahina.mapper;

import ru.chuvahina.model.GameState;
import ru.chuvahina.view.imageloader.IconType;

public final class GameStateMapper {
    private GameStateMapper() {
    }

    public static IconType toIconType(GameState state) {
        switch (state) {
            case CONTINUES:
                return IconType.RESTART;
            case LOST:
                return IconType.LOOSE;
            case VICTORY:
                return IconType.WINNER;
            default:
        }
        return null;
    }
}
