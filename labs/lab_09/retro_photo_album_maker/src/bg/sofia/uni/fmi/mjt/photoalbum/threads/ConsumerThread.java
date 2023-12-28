package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.image.Image;

import java.io.File;
import java.nio.file.Path;
import java.util.Deque;

public class ConsumerThread implements Runnable {
    private final Deque<Image> imagesDeque;
    private static int count = 0;
    private String outputDirectory;

    private boolean noMoreImages = false;
    public ConsumerThread(Deque<Image> imagesDeque, String outputDirectory) {
        this.imagesDeque = imagesDeque;
        this.outputDirectory = outputDirectory;
    }

    public void setNoMoreImages() {
        noMoreImages = true;
    }

    @Override
    public void run() {
        Image image;
        while (true) {
            synchronized (imagesDeque) {
                while (imagesDeque.isEmpty() && !noMoreImages) {
                    try {
                        imagesDeque.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (imagesDeque.isEmpty() && noMoreImages) {
                    break;
                }

                image = imagesDeque.pollLast();
            }
            Image result = Image.convertToBlackAndWhite(image);
            String outputPath = outputDirectory + File.separator + "output" + count++ + ".png";
            result.saveImage(Path.of(outputPath));
        }
    }
}
