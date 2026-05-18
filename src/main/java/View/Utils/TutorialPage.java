package View.Utils;

public class TutorialPage {
    String title, text, imageFile;

    public TutorialPage(String title, String text, String imageFile) {
        this.text = text;
        this.title = title;
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImageFile() {
        return imageFile;
    }
}
