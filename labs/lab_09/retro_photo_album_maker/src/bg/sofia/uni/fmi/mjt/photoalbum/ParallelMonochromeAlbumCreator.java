package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.image.Image;
import bg.sofia.uni.fmi.mjt.photoalbum.threads.ConsumerThread;
import bg.sofia.uni.fmi.mjt.photoalbum.threads.ProducerThread;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private int imageProcessorCount;
    private Deque<Image> imageDeque;
    private ConsumerThread[] consumerThreads;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorCount = imageProcessorsCount;
        imageDeque = new ArrayDeque<>();
        consumerThreads = new ConsumerThread[imageProcessorsCount];
    }

    private Path openDirectory(String sourceDirectory) {
        Path directory = Paths.get(sourceDirectory);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return directory;
    }

    private void startConsumerThreads(String outputDirectory) {
        for (int i = 0; i < imageProcessorCount; i++) {
            consumerThreads[i] = new ConsumerThread(imageDeque, outputDirectory);
            Thread thread = new Thread(consumerThreads[i]);
            thread.start();
        }
    }

    private void startProducerThreads(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    String fileName = file.getFileName().toString().toLowerCase();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
                        ProducerThread producerThread = new ProducerThread(file, imageDeque);
                        Thread thread = new Thread(producerThread);
                        thread.start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void notifyThereAreNoImagesLeft() {
        for (ConsumerThread consumerThread : consumerThreads) {
            consumerThread.setNoMoreImages();
        }
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path directory = openDirectory(sourceDirectory);

        startConsumerThreads(outputDirectory);
        startProducerThreads(directory);
        notifyThereAreNoImagesLeft();
    }
}
