package attendancemanagement.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_core.cvTranspose;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class Utils {
    
    public static String hashPass(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            StringBuffer hPass = new StringBuffer();
            for(byte byt: digest){
                hPass.append(String.format("%02x",byt & 0xff));
            }
            return hPass.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static Image mat2Image(Mat frame){
        try{
                return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e){
                System.err.println("Cannot convert the Mat object: " + e);
                return null;
        }
    }
    public static IplImage mat2IplImage(Mat frame){
        IplImage iplImg;
        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        iplImg = iplConverter.convert(java2dConverter.convert(matToBufferedImage(frame)));
        return iplImg;
    }
    
    public static BufferedImage iplImage2Buff(IplImage src){
        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        Frame frame = iplConverter.convert(src);
        BufferedImage img = bimConverter. convert(frame);
        BufferedImage result = (BufferedImage)img.getScaledInstance(
            img.getWidth(), img.getHeight(), java.awt.Image.SCALE_DEFAULT);  
        img.flush();
        return result;
    }
    
    public static <T> void onFXThread(final ObjectProperty<T> property, final T value){
        Platform.runLater(() -> {
                property.set(value);
        });
    }
    
    private static BufferedImage matToBufferedImage(Mat original){
            // init
            BufferedImage image = null;
            int width = original.width(), height = original.height(), channels = original.channels();
            byte[] sourcePixels = new byte[width * height * channels];
            original.get(0, 0, sourcePixels);

            if (original.channels() > 1){
                    image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            }
            else{
                    image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            }
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

            return image;
    }
    
    public static org.bytedeco.javacpp.opencv_core.Mat convertMatToCVMat(Mat image) {
        BufferedImage b = matToBufferedImage(image);
        org.bytedeco.javacpp.opencv_core.Mat cnvt = bufferedImageToMat(b);
        return cnvt;
    }
    
    public static org.bytedeco.javacpp.opencv_core.Mat bufferedImageToMat(BufferedImage bi) {
        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        return cv.convertToMat(new Java2DFrameConverter().convert(bi)); 
    }
    
    public static void drawRectangle(Mat frame,Rect rect, Scalar color) {
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        
        Imgproc.rectangle(frame, rect.tl(), rect.br(), color, 1);
        Imgproc.line(frame, new Point(x+6,y+6), new Point(x+25,y+6), color, 2);
        Imgproc.line(frame, new Point(x+6,y+6), new Point(x+6,y+25), color, 2);
        Imgproc.line(frame, new Point(x+(w-31),y+6), new Point(x+w-6,y+6), color, 2);
        Imgproc.line(frame, new Point(x+w-6,y+6), new Point(x+w-6,y+25), color, 2);

        Imgproc.line(frame, new Point(x+6,y+h-6), new Point(x+25,y+h-6), color, 2);
        Imgproc.line(frame, new Point(x+6,y+h-6), new Point(x+6,y+(h-25)), color, 2);
        Imgproc.line(frame, new Point(x+(w-31),y+h-6), new Point(x+w-6,y+h-6), color, 2);
        Imgproc.line(frame, new Point(x+w-6,y+h-6), new Point(x+w-6,y+(h-25)), color, 2);
    }
    
    public static void putTextOnImage(Mat frame,String text,double fontSize,Scalar fontColor, Point point){
        Imgproc.putText (
            frame,                      // Matrix obj of the image
            text,                  // Text to be added
            point,    // point
            Core.FONT_HERSHEY_COMPLEX ,   // front face
            fontSize,                        // front scale
            fontColor,  // Color
            1      //Size
         );
    }
    
    public static Mat rotate(IplImage src, int angle) {
        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        cvTranspose(src, img);
        cvFlip(img, img, angle);
        BufferedImage buffImg = iplImage2Buff(img);
        Mat mat = new Mat(buffImg.getHeight(), buffImg.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) buffImg.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    } 
    
}
