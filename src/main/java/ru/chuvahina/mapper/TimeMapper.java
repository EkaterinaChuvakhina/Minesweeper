package ru.chuvahina.mapper;

import ru.chuvahina.view.imageloader.IconType;

import java.util.List;
import java.util.stream.Collectors;

public final class TimeMapper {
    private TimeMapper() {
    }

    public static List<IconType> toIconType(long time) {
        return String.valueOf(time)
                .chars()
                .mapToObj(ch -> DigitMapper.toIconType(ch - '0'))
                .collect(Collectors.toList());
    }
}
