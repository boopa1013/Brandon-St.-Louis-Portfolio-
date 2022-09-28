/**
 * ASSIGNMENT: Final Program: ImageManipulator-Inator
 *
 * @author Michael Riccobono, Brandon St. Louis, Juliana Lauer
 *
 *
 * COURSE: CS1122
 * LAB SECTION: L02-G
 * SEMESTER: Fall 2021
 *
 * DESCRIPTION:
 * This program displays a window that has buttons to load a ppm file to display an image, save it, flip it, invert it, grayify it and pixelate it.
 *
 * CREATED: 12/9/2021
 * LAST MODIFIED: 12/9/2021
 */
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Application;
import java.io.*;
import java.util.Scanner;

public class ImageManipulator extends Application implements ImageManipulatorInterface {
    private int width = 500;
    private int height = 500;
    private WritableImage image = null;


    /**
     * This method loads an image from a ppm file into a writable image
     * @param filename the name of the file being used
     * @return a writable image "image" that can be displayed later in the program
     * @throws FileNotFoundException - If the file cannot be found in the project folder
     */
    @Override
    public WritableImage loadImage(String filename) throws FileNotFoundException {
        File inputFile = new File(filename);
        Scanner scan = new Scanner(inputFile);

        scan.next();
        String stop = scan.next();

        if (stop.charAt(0) == '#') {
            scan.nextLine();
        }

        width = scan.nextInt();
        height = scan.nextInt();
        scan.nextInt();

        WritableImage image = new WritableImage(width, height);
        PixelWriter show = image.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int red = scan.nextInt();
                int blue = scan.nextInt();
                int green = scan.nextInt();

                Color pixColor = Color.rgb(red, blue, green);
                show.setColor(x, y, pixColor);

            }
        }

        return image;

    }

    /**
     * This method saves a WritableImage to a ppm file format and saves it
     * @param filename The name of the file that is being created
     * @param image WritableImage that is going to be turned into a ppm file
     * @throws FileNotFoundException - If the file cannot be found
     */
    @Override
    public void saveImage(String filename, WritableImage image) throws FileNotFoundException {


        PixelReader reader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.println("P3");
            writer.println("#"+" "+filename);
            writer.println(width + " " + height);
            writer.println(255);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = reader.getColor(x, y);
                    int redPixel = (int) (color.getRed() * 255);
                    int greenPixel = (int) (color.getGreen() * 255);
                    int bluePixel = (int) (color.getBlue() * 255);

                            writer.printf("%d\n%d\n%d\n", redPixel, greenPixel, bluePixel);

                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "Save file not found! " );
            e.printStackTrace();

        }
    }

    /**
     * This method inverts the colors of a WritableImage
     * @param image - the image to be inverted, do not modify!
     * @return imageInvert - the inverted WritableImage
     */
    @Override
    public WritableImage invertImage(WritableImage image) {

        WritableImage imageInvert = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = imageInvert.getPixelWriter();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = reader.getColor(x, y);
                int pixelRed = (int) (pixelColor.getRed() * 255);
                int pixelGreen = (int) (pixelColor.getGreen() * 255);
                int pixelBlue = (int) (pixelColor.getBlue() * 255);
                pixelRed = 255 - pixelRed;
                pixelGreen = 255 - pixelGreen;
                pixelBlue = 255 - pixelBlue;
                Color pixelColor1 = Color.rgb(pixelRed, pixelGreen, pixelBlue);
                writer.setColor(x, y, pixelColor1);
            }
        }

        return imageInvert;

    }
    /**
     * This method turns a WritableImage gray so it is black and white
     * @param image - the image to be grayified, do not modify!
     * @return WriteImage - the grayified WritableImage
     */
    @Override
    public WritableImage grayifyImage(WritableImage image) {
        WritableImage WriteImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = WriteImage.getPixelWriter();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = image.getPixelReader().getColor(x, y);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);
                int luminescence = (int) ((.2989 * red) + (.5870 * green) + (.1140 * blue));
                Color grayscale = Color.rgb(luminescence, luminescence, luminescence);
                writer.setColor(x, y, grayscale);
            }

        }
        return WriteImage;

    }

    /**
     * This method pixelates a WritableImage
     * @param image - the image to be pixelated, do not modify!
     * @return WriteImage - the pixelated WritableImage
     */
    @Override
    public WritableImage pixelateImage(WritableImage image) {
        int x = 0;
        PixelReader reader = image.getPixelReader();
        WritableImage WriteImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = WriteImage.getPixelWriter();

        for (; x < image.getWidth() - (image.getWidth() % 5); x += 5) {
            for (int y = 0; y < image.getHeight() - (image.getHeight() % 5); y += 5) {
                Color center = reader.getColor(x + 2, y + 2);

                for (int t = x; t < x + 5; t++) {
                    for (int f = y; f < y + 5; f++) {
                        writer.setColor(t, f, center);
                    }

                }

            }


        }
        if (image.getWidth() % 5 != 0) {
            for (int y = 0; y < image.getHeight() - (image.getHeight() % 5); y++) {
                Color center;
                if (image.getWidth() % 5 == 4) {
                    center = reader.getColor(x + 2, y + 2);
                } else {
                    center = reader.getColor((int) (image.getWidth() % 5) - 2 + x, y + 2);
                }
                for (int w = x; w < x + image.getWidth() % 5; w++) {
                    for (int z = y; z < y + 5; z++) {
                        writer.setColor(w, z, center);
                    }


                }


            }

        }
        return WriteImage;

    }

    /**
     * This method flips a WritableImage upside down.
     * @param image - the image to be flipped, do not modify!
     * @return writeImage - the flipped WritableImage.
     */
    @Override
    public WritableImage flipImage(WritableImage image) {

        PixelReader reader = image.getPixelReader();
        WritableImage writeImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = writeImage.getPixelWriter();

        for(int x = 0;x<image.getWidth();x++){
            for(int y =0;y< image.getHeight();y++){
                writer.setColor(x,y,reader.getColor(x,(int)image.getHeight()-1-y));
            }
        }


        return writeImage;

    }

    /**
     * This method displays a window that has buttons to load a ppm file to display an image, save it, flip it, invert it, grayify it and pixelate it.
     * @param primaryStage - The Primary stage for the JavaFX window.
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();




        Scene scene = new Scene(borderPane, width, height );

        HBox hBox = new HBox();

        ImageView iv = new ImageView();


        javafx.scene.control.Button Button = new Button("Load");
        Button.setOnAction( event -> {
            try {
                FileChooser chooser = new FileChooser();
                File loadFile = chooser.showOpenDialog(primaryStage); //for opening a file from computer
                if(loadFile != null){
                    String name = loadFile.toString();
                    image = loadImage(name);
                    iv.setImage(image);
                }

            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
            }
        });

        javafx.scene.control.Button Button11 = new Button("Save");
        Button11.setOnAction( event -> {
            try {
                FileChooser saver = new FileChooser();
                String filename = String.valueOf(saver.showSaveDialog(primaryStage)); //for saving a file on computer
                saveImage( filename,(WritableImage) iv.getImage() );
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
            }
        });

        javafx.scene.control.Button Button1 = new Button("Invert");
        Button1.setOnAction( event -> {
            image = invertImage(image);
            iv.setImage(image);
        });

        javafx.scene.control.Button Button2 = new Button("Flip");
        Button2.setOnAction( event -> {
            image = flipImage(image);
            iv.setImage(image);
        });

        javafx.scene.control.Button Button3 = new Button("Grayify");
        Button3.setOnAction( event -> {
            image = grayifyImage(image);
            iv.setImage(image);
        });

        javafx.scene.control.Button Button4 = new Button("Pixelate");
        Button4.setOnAction( event -> {
            image = pixelateImage(image);
            iv.setImage(image);
        });

        javafx.scene.control.Button Button5 = new Button("Clear");
        Button5.setOnAction( event -> {
            image = null;
            iv.setImage(null);
        });

        hBox.getChildren().addAll(iv);
        HBox hBox1 = new HBox(10);
        hBox1.setPadding(new Insets(10));
        hBox1.getChildren().addAll(Button,Button11,Button1,Button2,Button3,Button4,Button5);
        borderPane.setCenter(hBox);
        borderPane.setBottom(hBox1);
        hBox1.setAlignment(Pos.BOTTOM_CENTER);

        primaryStage.setTitle("ImageManipulator-Inator");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
