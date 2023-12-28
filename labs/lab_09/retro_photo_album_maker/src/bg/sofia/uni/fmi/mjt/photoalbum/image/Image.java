package bg.sofia.uni.fmi.mjt.photoalbum.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class Image {
    String name;
    public BufferedImage data;

    public Image(String name, BufferedImage data) {
        this.name = name;
        this.data = data;
    }

    public static Image loadImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            return new Image(imagePath.getFileName().toString(), imageData);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", imagePath.toString()), e);
        }
    }

    public static Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData = new BufferedImage(image.data.getWidth(),
            image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        return new Image(image.name, processedData);
    }

    public void saveImage(Path outputPath) {
        try {
            String format = "png";
            ImageIO.write(data, format, outputPath.toFile());
            System.out.println("Image saved successfully to: " + outputPath.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to save image to %s", outputPath.toString()), e);
        }
    }
}
