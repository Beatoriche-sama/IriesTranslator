package ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GoogleVisionOCR {
    public static String doOCR(BufferedImage image) throws IOException {
        /*String key = "110a510153edb4088d7d93ead739043dfb1158d3";
        String url = "https://vision.googleapis.com/v1/images:annotate?key=" + key;*/

        // The path to the image file to annotate
        //String fileName = "./resources/wakeupcat.jpg";
        // String fileName = "C:\\Users\\Iri\\Pictures\\Screenshots\\Screenshot (40).png";

        // Reads the image file into memory
        /*Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        ByteString imgBytes = ByteString.copyFrom(data);*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        ByteString imgBytes = ByteString.copyFrom(bytes);

        // Builds the image annotation request
        ArrayList<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        String ocrResult;
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            List<EntityAnnotation> ress = responses.get(0).getTextAnnotationsList();
            ocrResult = ress.get(0).getDescription();
        }

        return ocrResult;
    }
}
