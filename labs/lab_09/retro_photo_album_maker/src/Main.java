import bg.sofia.uni.fmi.mjt.photoalbum.ParallelMonochromeAlbumCreator;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ParallelMonochromeAlbumCreator a = new ParallelMonochromeAlbumCreator
            (2);
        a.processImages("C:/Users/rados/IdeaProjects/retro_photo_album_maker/",
            "C:/Users/rados/IdeaProjects/retro_photo_album_maker/result");
        }
}