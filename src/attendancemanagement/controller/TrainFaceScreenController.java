package attendancemanagement.controller;


import attendancemanagement.model.Employee;
import attendancemanagement.model.JobCategory;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.JobCategoryService;
import attendancemanagement.service.ScreensController;
import attendancemanagement.service.TrainFace;
import attendancemanagement.utils.UIUtils;
import attendancemanagement.utils.Utils;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;


public class TrainFaceScreenController implements Initializable{
    
    private ScreensController screenController;
    @FXML
    private ImageView currentFrame; 
    private static final String CASCADE_FILE = ""
            + "src/attendancemanagement/res/cascades/haarcascades/haarcascade_frontalface_alt.xml";
    @FXML
    private Label cameraDeviceName;
    private static ImageView capturedImage;
    @FXML
    private Label empNameLabel;
    @FXML
    private Label empCodeLabel;
    @FXML
    private Label jobCatLabel;
    @FXML
    private JFXListView<Label> employeeList;
    @FXML
    private Button trainButton;

    Stage stage;
    @FXML
    private Button captureButton;
    public static int deviceId = 0;
    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;
    private CascadeClassifier faceCascade;
    private CascadeClassifier eyesCascade;
    private int absoluteFaceSize;
    private static final String FACE_CASCADE_FILE = ""
            + "src/attendancemanagement/res/cascades/haarcascades/haarcascade_frontalface_alt.xml";
    private static final String EYES_CASCADE_FILE = ""
            + "src/attendancemanagement/res/cascades/haarcascades/haarcascade_eye_tree_eyeglasses.xml";
    private final Image defaultImage = new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/cameraBG.png"));
    private Image capturedImg;
    
    private boolean isCapture = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
    
    int empid = 0;
    String empCode;
    
    @FXML
    private HBox capturedImageBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        setEmployeeList();
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.eyesCascade = new CascadeClassifier();
	this.absoluteFaceSize = 0;
        if(deviceId == 0){
            cameraDeviceName.setText("Integrated Camera");
        }else{
            cameraDeviceName.setText("Camera "+deviceId);
        }
        Image i = new Image(getClass().getResourceAsStream("/attendancemanagement/res/images/avatar_other.png"));
        capturedImage = new ImageView(i);
        capturedImage.setFitHeight(100);
        capturedImage.setFitWidth(100);
        capturedImage.setPreserveRatio(true);
        capturedImageBox.getChildren().add(capturedImage);
        startCamera();
    } 

    void setTrainFaceStage(Stage window) {
        stage = window;
    }
    
    void setEmployeeList(){
        employeeList.getItems().clear();
        EmployeeService service = new EmployeeService();
        for(Employee emp : service.getAllEmployees()){
            Label lbl = new Label(emp.getFname()+" "+emp.getLname());
            lbl.setAccessibleText(emp.getEmpid()+"");
            File file = new File(URI.create(emp.getAvatar()).getPath());
            Image avatar = null;
            if(file.isFile() && file.exists()){
                avatar = new Image(emp.getAvatar());
                
            }
            else
            {
                File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
                try {
                    avatar = new Image(f.getCanonicalFile().toURI().toString());
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            Circle clip = new Circle(30, 15, 15);
            clip.setFill(new ImagePattern(avatar));
            lbl.setGraphic(clip);
            employeeList.getItems().add(lbl);
        }
        
        employeeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>(){
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
                int id = Integer.parseInt(newValue.getAccessibleText());
                EmployeeService eService = new EmployeeService();
                Employee employee = eService.getEmployeeData(id);
                empNameLabel.setText(employee.getFname()+" "+employee.getLname());
                empCodeLabel.setText(employee.getEmpcode());
                JobCategoryService jService = new JobCategoryService();
                JobCategory cat = jService.getJobCatById(employee.getJobTitle());
                String jobCat = "Not assigned";
                if(cat != null){
                    jobCat = cat.getDisplay_name();
                }
                jobCatLabel.setText(jobCat);
                empid = employee.getEmpid();
                empCode = employee.getEmpcode();
//                System.out.println(empid);
//                System.out.println(empCode);
            }
        });
    }

    @FXML
    private void captureFaceImage(ActionEvent event) {
        isCapture = true;
    }
    
    void startCamera(){
        if (!this.cameraActive){
            this.faceCascade.load(FACE_CASCADE_FILE);
            this.eyesCascade.load(EYES_CASCADE_FILE);
            this.capture.open(deviceId);
            if (this.capture.isOpened()){
                this.cameraActive = true;
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        Mat frame = grabFrame();
                        updateImageView(currentFrame, Utils.mat2Image(frame));
                    }
                    
                };
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
            }else{
                System.err.println("Failed to open the camera connection...");
            }
        }else{
            this.cameraActive = false;
            this.stopAcquisition();
        }
    }
    
    private Mat grabFrame(){
        Mat frame = new Mat();
        // check if the capture is open
        if (this.capture.isOpened()){
            try{
                // read the current frame
                this.capture.read(frame);
                // if the frame is not empty, process it
                if (!frame.empty()){
                        // face detection
                        this.detectAndDisplay(frame);
                }

            }catch (Exception e){
                    // log the (full) error
                    System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;
    }
    
    private void detectAndDisplay(Mat frame){
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        if (this.absoluteFaceSize == 0){
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0){
                    this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                        new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        for (int i = 0; i < facesArray.length; i++){
            Mat croppedFace = new Mat(grayFrame,facesArray[i]);
            Mat resizeFace = new Mat();
            Imgproc.resize( croppedFace, resizeFace, new Size(200,200),1.0, 1.0, INTER_CUBIC);
            Utils.drawRectangle(frame,facesArray[i],new Scalar(0, 0, 255));
            if(isCapture){
                Platform.runLater(() -> {
                    saveImage(resizeFace,now);
                });
                isCapture = false;
            }
        }
    }
    
    private void updateImageView(ImageView view, Image image){
        Utils.onFXThread(view.imageProperty(), image);
    }
    
    private void stopAcquisition(){
        if (this.timer!=null && !this.timer.isShutdown()){
            try {
                    // stop the timer
                    this.timer.shutdown();
                    this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }catch (InterruptedException e) {
                    // log any exception
                    System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()){
            // release the camera
            this.capture.release();
        }
        cameraActive = false;
    }
    
    public void setClosed(){
        this.stopAcquisition();
    }
    
    void saveImage(Mat face, Date now){
        if(empid != 0 && !empCode.isEmpty()){
            String date = new SimpleDateFormat("ddMMyyyyHHmmss").format(now);
            String imageName = empid+"-"+empCode+"-"+date+".jpg";
            if(Imgcodecs.imwrite("src/attendancemanagement/faces/"+imageName, face)){
                updateImageView(capturedImage, Utils.mat2Image(face));
                UIUtils.successNotification("Capture Complete!", "Face Image has been captured successfully. \n");
            }else{
                UIUtils.errorNotification("Capture Incomplete!", "Face Image has not been captured successfully. \n");
            }
        }else{
            UIUtils.warningNotification("Capture Incomplete!", "Please Select Employe From List. \n"
                    + "Click on employee name from the list to select.");
        }
    }

    @FXML
    private void trainFaceData(ActionEvent event) {
        TrainFace train = new TrainFace();
        if(train.saveFaceData()){
            UIUtils.successNotification("Train Complete!", "Face data has been trained successfully. \n");
        }else{
            UIUtils.errorNotification("Train Incomplete!", "Face data has not been trained successfully. \n");
        }
    }
    
    public static void setTrainedImage(Image file){
        if(file != null){
            capturedImage.setImage(file);
            Utils.onFXThread(capturedImage.imageProperty(), file);
        }
    }
}

