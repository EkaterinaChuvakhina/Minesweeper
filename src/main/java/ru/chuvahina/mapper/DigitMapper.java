package ru.chuvahina.mapper;

import ru.chuvahina.view.imageloader.IconType;

public final class DigitMapper {
    private DigitMapper() {
    }

    public static IconType toIconType(int number) {
        switch (number) {
            case 0:
                return IconType.DISPLAY_ZERO;
            case 1:
                return IconType.DISPLAY_ONE;
            case 2:
                return IconType.DISPLAY_TWO;
            case 3:
                return IconType.DISPLAY_THREE;
            case 4:
                return IconType.DISPLAY_FOUR;
            case 5:
                return IconType.DISPLAY_FIVE;
            case 6:
                return IconType.DISPLAY_SIX;
            case 7:
                return IconType.DISPLAY_SEVEN;
            case 8:
                return IconType.DISPLAY_EIGHT;
            case 9:
                return IconType.DISPLAY_NINE;
        }
        return null;
    }
}
