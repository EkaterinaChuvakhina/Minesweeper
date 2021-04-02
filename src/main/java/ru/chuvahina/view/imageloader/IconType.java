package ru.chuvahina.view.imageloader;

public enum IconType {
    FLAGGED("icon/flagged.png"),
    CLOSED("icon/closed.png"),
    BOMB("icon/bomb.png"),
    BOMBED("icon/bombed.png"),
    NO_BOMB("icon/nobomb.png"),
    ONE("icon/one.png"),
    TWO("icon/two.png"),
    THREE("icon/three.png"),
    FOUR("icon/four.png"),
    FIVE("icon/five.png"),
    SIX("icon/six.png"),
    SEVEN("icon/seven.png"),
    EIGHT("icon/eight.png"),
    ZERO("icon/zero.png"),
    WINNER("icon/win.png"),
    CLICKED_RESTART("icon/click_smiley.png"),
    RESTART("icon/smiley.png"),
    LOOSE("icon/loose.png"),
    MAIN_ICON("icon/iconMainWindow.png"),
    DISPLAY_ZERO("icon/displayzero.png"),
    DISPLAY_ONE("icon/displayone.png"),
    DISPLAY_TWO("icon/displaytwo.png"),
    DISPLAY_THREE("icon/displaythree.png"),
    DISPLAY_FOUR("icon/displayfour.png"),
    DISPLAY_FIVE("icon/displayfive.png"),
    DISPLAY_SIX("icon/displaysix.png"),
    DISPLAY_SEVEN("icon/displayseven.png"),
    DISPLAY_EIGHT("icon/displayeight.png"),
    DISPLAY_NINE("icon/displaynine.png"),
    DISPLAY_NEGATIVE("icon/negative.png");


    private final String path;

    IconType(String path) {
        this.path = path;

    }

    public String getPath() {
        return path;
    }
}
