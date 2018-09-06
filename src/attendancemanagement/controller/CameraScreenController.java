package attendancemanagement.controller;

import static attendancemanagement.controller.AdminWindowController.ATTENDANCESCREENID;
import attendancemanagement.database.AttendanceDBO;
import attendancemanagement.model.Attendance;
import attendancemanagement.model.Employee;
import attendancemanagement.service.AttendanceService;
import attendancemanagement.service.ControlledScene;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.TrainFace;
import attendancemanagement.service.ScreensController;
import attendancemanagement.utils.UIUtils;
import attendancemanagement.utils.Utils;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;


public class CameraScreenController implements Initializable, ControlledScene {

    private ScreensController screenController;
    public static int deviceId = 0;
    private String deviceName;
    
    @FXML
    private HBox CameraScreen;
    @FXML
    private ImageView currentFrame;
    @FXML
    private TableView<Attendance> recordTimeTable;
    private ObservableList<Attendance> attendanceTableData;
    
    // FACE DETECTION VARIABLES
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
    private Image currentImage;
    
   
    private static FaceRecognizer recognizer;
    
    private TrainFace trainFace = new TrainFace();
    
    private Rect[] eyes;

    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private JFXListView<HBox> detectedEmpList;
    @FXML
    private TableColumn<Attendance, Integer> snCol;
    @FXML
    private TableColumn<Attendance, Integer> empCodeCol;
    @FXML
    private TableColumn<Attendance, Integer> empNameCol;
    @FXML
    private TableColumn<Attendance, LocalDateTime> empAttendanceCol;
    @FXML
    private TableColumn<Attendance, String> statusCol;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    
    private String yearMonth;
    private String day;
    
    private boolean clockIn = false;
    private boolean clockOut = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        this.capture = new VideoCapture();
        this.capture.set(3, 1024);
        this.capture.set(4, 768);
        this.faceCascade = new CascadeClassifier();
        this.eyesCascade = new CascadeClassifier();
	this.absoluteFaceSize = 0;
        if(deviceId == 0){
            deviceName = "Integrated Camera";
        }else{
            deviceName = "Camera "+deviceId;
        }
        
        yearMonth = YearMonth.now().toString().replace("-","");
        day = MonthDay.now().getDayOfMonth()+"";
      
        recognizer = trainFace.faceRecognizer();
    
            
//        recognizer = faceRecognizer.getRecognizer();
//        if(TrainFace.trainingFile.exists()){
//            recognizer.
//            recognizer.load(TrainFace.trainingPath);
//        }
//        
        loadTableData();
        setAttendanceTable();
        
    } 
   
    @Override
    public void setScreenParent(ScreensController parent) {
        screenController = parent;
    }

    
    public static void setDeviceId(int id){
        deviceId = id;
    }

    @FXML
    private void openTrainFaceScreen(MouseEvent event) {
        setClosed();
        try {
            FXMLLoader loader = new FXMLLoader(AdminWindowController.class.getResource(
                    "/attendancemanagement/view/TrainFaceScreen.fxml"));
            Parent root = loader.load();
            TrainFaceScreenController trainController = loader.getController();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("TRAIN FACES");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            trainController.setTrainFaceStage(window);
           
            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(final WindowEvent arg0) {
                    trainController.setClosed();
                }
            });
            
            window.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openAttendaceScreen(MouseEvent event) {
//        screenController.setScreen(ATTENDANCESCREENID, MainContainer);
    }

    @FXML
    private void StartCamera(ActionEvent event) {
        if (!this.cameraActive){
            this.faceCascade.load(FACE_CASCADE_FILE);
            this.eyesCascade.load(EYES_CASCADE_FILE);
            trainFace = new TrainFace();
            recognizer = trainFace.faceRecognizer();
            this.capture.open(deviceId);
            if (this.capture.isOpened()){
                this.cameraActive = true;
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        Mat frame = grabFrame();
                        currentImage = Utils.mat2Image(frame);
                        updateImageView(currentFrame, currentImage);
                    }
                    
                };
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                playButton.setDisable(true);
                stopButton.setDisable(false);
                pauseButton.setDisable(false);
            }else{
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        }else{
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            playButton.setDisable(false);
            stopButton.setDisable(true);
            pauseButton.setDisable(true);
            // stop the timer
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
        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);
        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0){
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0){
                    this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                        new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        
        Utils.putTextOnImage(frame, deviceName, 0.4, new Scalar(255, 229, 8), new Point(10,30));
        
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String currentdate = new SimpleDateFormat("EEE, MMM d, yyyy h:mm:ss a").format(now);
        
        Utils.putTextOnImage(frame, currentdate, 0.35, new Scalar(255, 229, 8), new Point(10,15));
        
        IntPointer label = new IntPointer(0);
        DoublePointer confidence = new DoublePointer(0);
        
        
        for (int i = 0; i < facesArray.length; i++){
            
            Mat croppedFace = new Mat(grayFrame,facesArray[i]);
            Mat resizeFace = new Mat();
            Imgproc.resize( croppedFace, resizeFace, new Size(200,200),1.0, 1.0, INTER_CUBIC);
            recognizer.predict(Utils.convertMatToCVMat(resizeFace), label, confidence);
            System.out.println("Label = "+label.get(0));
            System.out.println("Confidence = "+confidence.get(0));
//            System.out.println("Threshold = "+((BasicFaceRecognizer)recognizer).getThreshold());
//            
            int empid = label.get(0);
            if(confidence.get(0) > 40){
                int pos_x = (int) Math.max(facesArray[i].tl().x, 0);
                int pos_y = (int) Math.max(facesArray[i].br().y + 30, 0);
                Utils.drawRectangle(frame,facesArray[i],new Scalar(0, 0, 255));
                Utils.putTextOnImage(frame, "Unknown Employee", 0.6, new Scalar(0, 0, 255), new Point(pos_x,pos_y));
            }else{
                showEmployeeName(frame,empid,facesArray[i]);
            }
        }

    }

    @FXML
    private void pauseCamera(ActionEvent event) {
        setClosed();
        updateImageView(currentFrame, currentImage);
        stopButton.setDisable(true);
        playButton.setDisable(false);
        pauseButton.setDisable(true);
    }

    @FXML
    private void stopCamera(ActionEvent event) {
        setClosed();
        updateImageView(currentFrame, defaultImage);
        playButton.setDisable(false);
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
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
    
    protected void setClosed(){
        this.stopAcquisition();
    }

    private Rect[] findEyes(Mat face) {
        // detect eyes
        MatOfRect eyes = new MatOfRect();
        this.eyesCascade.detectMultiScale(face, eyes, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                        new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size()); 
        Rect[] eyeArray = eyes.toArray();
        return eyeArray;
    }
    
    public void showEmployeeName(Mat frame, int empid,Rect rect){
        EmployeeService service = new EmployeeService();
        Employee emp = service.getEmployeeData(empid);
        int pos_x = (int) Math.max(rect.tl().x, 0);
        int pos_y = (int) Math.max(rect.br().y + 30, 0);
        if(emp != null){
            String name = emp.getFname()+" "+emp.getLname();
            Utils.drawRectangle(frame,rect,new Scalar(255, 229, 8));
            Utils.putTextOnImage(frame, name, 0.6, new Scalar(255, 229, 8), new Point(pos_x,pos_y));
            insertDetected(emp);
            getAttendances(emp);
        }else{
            Utils.drawRectangle(frame,rect,new Scalar(0, 0, 255));
            Utils.putTextOnImage(frame, "Unknown Employee", 0.6, new Scalar(0, 0, 255), new Point(pos_x,pos_y));
        }
    }

    private void insertDetected(Employee emp) {
        boolean inertEmp = true;
        for(int i = 0; i < detectedEmpList.getItems().size(); i++){
            if(detectedEmpList.getItems().get(i).getAccessibleText().equals(emp.getEmpid()+"")){
                inertEmp = false;
            }
        }
        if(inertEmp){
            Label lbl = new Label(emp.getFname()+" "+emp.getLname());
            File file = new File(URI.create(emp.getAvatar()).getPath());
            Image avatar = null;
            if(file.isFile() && file.exists()){
                avatar = new Image(emp.getAvatar());
            }
            else{
                File f = new File(".\\src\\attendancemanagement\\res\\images\\avatar_other.png");
                try {
                    avatar = new Image(f.getCanonicalFile().toURI().toString());
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            Circle clip = new Circle(80, 40, 40);
            clip.setFill(new ImagePattern(avatar));
            lbl.setGraphic(clip);
            lbl.setGraphicTextGap(10);
            lbl.setContentDisplay(ContentDisplay.TOP);
            lbl.setFont(Font.font(14));
            HBox hbox = new HBox(lbl);
            hbox.setAccessibleText(emp.getEmpid()+"");
            hbox.setAlignment(Pos.CENTER);
            Platform.runLater(() -> {
                detectedEmpList.getItems().add(hbox);
            });
        }
    }
    
    
    private void setAttendanceTable() {
        snCol.setCellValueFactory(
            cellData -> cellData.getValue().snProperty().asObject());
        
        empCodeCol.setCellValueFactory(
            cellData -> cellData.getValue().empidProperty().asObject());
        
        empNameCol.setCellValueFactory(
                cellData -> cellData.getValue().empidProperty().asObject());
        
        empAttendanceCol.setCellValueFactory(
                cellData -> cellData.getValue().timeProperty());
        
        statusCol.setCellValueFactory(
                cellData -> cellData.getValue().statusProperty());
        
        empCodeCol.setCellFactory(column -> {
                return new TableCell<Attendance, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    EmployeeService service = new EmployeeService();
                                    Employee emp = service.getEmployeeData(item);
                                    setText(emp.getEmpcode());
                                }
                        }
                };
        });
        
        empNameCol.setCellFactory(column -> {
                return new TableCell<Attendance, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    EmployeeService service = new EmployeeService();
                                    Employee emp = service.getEmployeeData(item);
                                    setText(emp.getFname()+" "+emp.getLname());
                                }
                        }
                };
        });
        
        empAttendanceCol.setCellFactory(column -> {
                return new TableCell<Attendance, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    // Format date.
                                    setText(item.format(formatter));
                                }
                        }
                };
        });
        recordTimeTable.setItems(attendanceTableData);
        
    }

    private void getAttendances(Employee emp) {
        AttendanceService aService = new AttendanceService();
        clockIn = false;
        clockOut = false;
        for(Attendance data: aService.getTodayAttendance(emp.getEmpid(),yearMonth,day)){
            
            if(data != null){
                if(data.getStatus().equals("Clock In")){
                    clockIn = true;
                }
                if(data.getStatus().equals("Clock Out")){
                    clockOut = true;
                }
            }
            
        }
        
       
        if(!clockIn){
            if(aService.clockInAttendance(emp)){
                System.out.println("Clock In Attendance Done");
                Platform.runLater(() -> {
                    UIUtils.successNotification("Clock In Attendance Complete!", emp.getFname()+" "+emp.getLname()+" "
                            + "has attended.");
                });
                loadTableData();
                setAttendanceTable();
            }
        }
        if(!clockOut){
            if(aService.clockOutAttendance(emp)){
                System.out.println("Clock Out Attendance Done");
                Platform.runLater(() -> {
                UIUtils.successNotification("Clock Out Attendance Complete!", emp.getFname()+" "+emp.getLname()+" "
                        + "has attended.");
                });
                loadTableData();
                setAttendanceTable();
            }
        }
    }
    
    private void loadTableData(){
        AttendanceService service = new AttendanceService();
        attendanceTableData = service.getTodayAttendance(yearMonth,day);
    }
}
