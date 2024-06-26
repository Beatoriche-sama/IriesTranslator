package ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TesseractOCR { //gives unstable results
    private final Tesseract tesseract;

    public TesseractOCR() {
        OpenCV.loadLocally();
        tesseract = new Tesseract();

        /*File tessDataFolder = LoadLibs.extractTessResources("/ocrTrainedData");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());*/
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setTessVariable("user_defined_dpi", "300");
    }

    private Mat improveImage(Mat image) {
        Mat grey = new Mat();
        Mat blur = new Mat();
        Mat unsharp = new Mat();
        Mat binary = new Mat();

        Imgproc.cvtColor(image, grey, Imgproc.COLOR_RGB2GRAY, 0);
        Imgproc.GaussianBlur(grey, blur, new Size(0, 0), 3);
        Core.addWeighted(grey, 1.5, blur, -0.5, 0, unsharp);
        Imgproc.threshold(unsharp, binary, 127, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imdecode(binary, Imgcodecs.CV_IMWRITE_PNG_COMPRESSION);
        return binary;
    }

    private Mat bufferedImageToMat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()),
                Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", mat, mob);
        byte[] ba = mob.toArray();

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new ByteArrayInputStream(ba));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }

    public String recognizeText(String language, BufferedImage image)
            throws TesseractException, IOException {
        tesseract.setLanguage(language);
        tesseract.setPageSegMode(11);
        tesseract.setOcrEngineMode(3);

        Mat mat = improveImage(bufferedImageToMat(image));
        image = matToBufferedImage(mat);

        return tesseract.doOCR(image);
    }

}
