package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.image.Image;

import java.nio.file.Path;
import java.util.Deque;

public class ProducerThread implements Runnable {
    private Path imagePath;

    private Deque<Image> imageDeque;

    public ProducerThread(Path imagePath, Deque<Image> imageDeque) {
        this.imagePath = imagePath;
        this.imageDeque = imageDeque;
    }

    @Override
    public void run() {
        Image image = Image.loadImage(imagePath);
        synchronized (imageDeque) {
            imageDeque.addFirst(image);
            imageDeque.notifyAll();
        }
    }
}
