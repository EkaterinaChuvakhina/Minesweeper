package ru.chuvahina.view;

import ru.chuvahina.common.Coords;
import ru.chuvahina.common.setting.GameMode;

public interface ViewListener {
    void pressLeftButton(Coords coords);

    void pressRightButton(Coords coords);

    void pressMediumButton(Coords coords);

    void pressRestartButton();

    void pressChangeModeButton(GameMode mode, String text);

    void pressAboutButton();

    void pressHighScoreButton();

    void pressExitButton();

    void pressResetRecordButton();
}

