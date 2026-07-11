package ch.immi.pension.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class CalendarButton extends ImageView {

    public CalendarButton() {
        InputStream imageStream = getClass().getResourceAsStream("/images/calendar16.png");

        if (imageStream == null) {
            throw new IllegalArgumentException("Image file not found in resources folder!");
        }

        // 2. Create the Image object from the stream
        Image image = new Image(imageStream);

        setImage(image);

        // Optional: Resize the ImageView while preserving the aspect ratio
        setFitWidth(16);
        setPreserveRatio(true);
    }
}
