import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.core.Core;

class ImageProcessor {
    private Mat image;

    ImageProcessor(String imagePath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV load success => Version : " + Core.VERSION);
        this.image = Imgcodecs.imread(imagePath);
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public void convertToGrayScale() {
        if (image.channels() > 1) {
            System.out.println("Converting input image to grayscale...");
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        }
    }

    public void equalizeHistogram() {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(image, grayFrame, Imgproc.COLOR_BayerBG2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        setImage(grayFrame);
    }
}

class FaceDetector extends ImageProcessor {
    private CascadeClassifier faceCascade;

    FaceDetector(String imagePath, String cascadeFilePath) {
        super(imagePath);
        this.faceCascade = new CascadeClassifier();
        this.faceCascade.load(cascadeFilePath);
    }

    public void detectAndSave(String outputImagePath) {
        MatOfRect faces = new MatOfRect();

        System.out.println("Before color conversion - Number of channels: " + getImage().channels());

        convertToGrayScale();
        equalizeHistogram();

        int height = getImage().height();
        int absoluteFaceSize = (int) Math.round(height * 0.2);

        faceCascade.detectMultiScale(getImage(), faces, 1.1, 2,
                0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        Mat colorImage = new Mat(getImage().rows(), getImage().cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(getImage(), colorImage, Imgproc.COLOR_GRAY2BGR); // Convert grayscale to BGR

        Rect[] faceArray = faces.toArray();
        drawRectangles(colorImage, faceArray);

        writeToFile(colorImage, outputImagePath);
        System.out.println("Write success: " + faceArray.length);
    }

    private void drawRectangles(Mat colorImage, Rect[] faceArray) {
        for (Rect rect : faceArray) {
            Imgproc.rectangle(colorImage, rect.tl(), rect.br(), new Scalar(0, 0, 255), 3);
        }
    }

    private void writeToFile(Mat colorImage, String outputPath) {
        Imgcodecs.imwrite(outputPath, colorImage);
    }
}


