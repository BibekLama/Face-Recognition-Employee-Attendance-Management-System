package attendancemanagement.service;

import attendancemanagement.controller.TrainFaceScreenController;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.IntBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;



public class TrainFace {

    private final String FACES_DIR = "src/attendancemanagement/faces";
    public static File trainingFile = new File("src/attendancemanagement/face-data/faceData.xml");
    
    public static String trainingPath = (trainingFile.getAbsolutePath()).replaceAll("\\\\", "/");
    
   
    public TrainFace() {
     
    }
    
    public boolean saveFaceData(){
        File root = new File(FACES_DIR);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);
        
        if(imageFiles.length > 0){
            MatVector images = new MatVector(imageFiles.length);

            Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
            IntBuffer labelsBuf = labels.createBuffer();
//            labelsBuf.put(0, 0);
            int counter = 0;
            for (File image : imageFiles) {
                Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

                int label = Integer.parseInt(image.getName().split("\\-")[0]);

                images.put(counter, img);

                labelsBuf.put(counter, label);

                counter++;
//              
//                String filename = image.getName();
////                System.out.println(filename);
//                Image faceimg = new Image(getClass().getResourceAsStream("/attendancemanagement/faces/"+filename));
//                TrainFaceScreenController.setTrainedImage(faceimg);
            }

//            FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
//            FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
            FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

            showFaces(imageFiles);
            
            faceRecognizer.train(images, labels); 
               
            return true;
        }
        return false;
    }
    
    public FaceRecognizer faceRecognizer(){
        File root = new File(FACES_DIR);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);
        
        if(imageFiles.length > 0){
            MatVector images = new MatVector(imageFiles.length);

            Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
            IntBuffer labelsBuf = labels.createBuffer();
//            labelsBuf.put(0, 0);
            int counter = 0;
            for (File image : imageFiles) {
                Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

                int label = Integer.parseInt(image.getName().split("\\-")[0]);

                images.put(counter, img);

                labelsBuf.put(counter, label);

                counter++;
            }
            
            FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

            showFaces(imageFiles);
            
            faceRecognizer.train(images, labels); 
            return faceRecognizer;
        }    
        return null;   
    }

    private void showFaces(File[] files) {
        Executor exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        final int imageBufferSize = files.length;
        BlockingQueue<Image> imageQueue = new ArrayBlockingQueue<>(imageBufferSize);
        exec.execute(() -> {
            int index = 0 ;
            while (true) {
                if(index < files.length){
                    try {
                        Image image = new Image(files[index].toURI().toURL().toExternalForm(), false);

                        try {
                            imageQueue.put(image);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(TrainFace.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    index = (index+1);
                }
            }
            
        });
        
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                Image image = imageQueue.poll();
                if (image != null) {
                    TrainFaceScreenController.setTrainedImage(image);
                }
            }
        };
        timer.start();
    }
   
}