package ru.chuvahina.view.imageloader;

import javax.swing.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageLoader {
    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());

    public Map<IconType, ImageIcon> loadAllImages() {
        Map<IconType, ImageIcon> imageCache = new EnumMap<>(IconType.class);
        for (IconType type : IconType.values()) {
            String imagePath = type.getPath();
            URL url = getClass().getClassLoader().getResource(imagePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                imageCache.put(type, icon);
            } else {
                LOGGER.log(Level.SEVERE, "Image in path {0} not found", imagePath);
            }
        }
        return imageCache;
    }
}
