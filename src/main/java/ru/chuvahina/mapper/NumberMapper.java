package ru.chuvahina.mapper;

import ru.chuvahina.view.imageloader.IconType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class NumberMapper {
    private NumberMapper() {
    }

    public static List<IconType> toIconType(int number) {
        List<IconType> result = new ArrayList<>(String.valueOf(number).length());

        int positiveNumber = number;
        if (number < 0) {
            result.add(IconType.DISPLAY_NEGATIVE);
            positiveNumber = Math.abs(number);
        }

        List<IconType> iconTypes = String.valueOf(positiveNumber)
                .chars()
                .mapToObj(ch -> DigitMapper.toIconType(ch - '0'))
                .collect(Collectors.toList());

        return Stream.of(result, iconTypes)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
