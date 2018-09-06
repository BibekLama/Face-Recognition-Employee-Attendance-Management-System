package attendancemanagement.controller;


import attendancemanagement.model.AttendanceData;
import attendancemanagement.model.DashboardTable;
import attendancemanagement.utils.UIUtils;
import attendancemanagement.model.Employee;
import attendancemanagement.model.MyPieChart;
import attendancemanagement.service.AttendanceService;
import attendancemanagement.service.ControlledScene;
import attendancemanagement.service.EmployeeService;
import attendancemanagement.service.ScreensController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class DashboardScreenController implements Initializable, ControlledScene {
    private ScreensController screenController;
    @FXML
    private HBox DashboardScreen;
    @FXML
    private ComboBox<Object> yearPieChart;
    @FXML
    private PieChart employeePieChart;
    
    private EmployeeService empService;
    
    private AttendanceService attenService;
    
    @FXML
    private AnchorPane areaChartPane;
    
    
    ObservableList<XYChart.Data> oneSeries = FXCollections.observableArrayList();
    
    ObservableList<XYChart.Data> twoSeries = FXCollections.observableArrayList();
    
    ObservableList<XYChart.Data> threeSeries = FXCollections.observableArrayList();

    
    @FXML
    private TextField searchField;
    private TableColumn<DashboardTable,Integer> daysCol;
    
    private TableColumn<DashboardTable,Integer> monthOne;
    
    private TableColumn<DashboardTable,Integer> monthTwo;
    
    private TableColumn<DashboardTable,Integer> monthThree;
    
    private TableColumn attendanceCol;
    
    @FXML
    private JFXListView<Label> recentEmpList;
    
    private ListViewPopUpController popup;
    @FXML
    private TableView<DashboardTable> areaChartTable;
    @FXML
    private TextField maleField;
    @FXML
    private TextField femaleField;
    @FXML
    private TextField unspecifiedField;
    @FXML
    private Label totalEmployee;
    
    private final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    
    private final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
    
    private Year thisYear;
    private YearMonth thisMonth;
    private YearMonth lastMonth;
    private YearMonth twoMonthsAgo;
    
    private ObservableList<DashboardTable> dashboardTableData = FXCollections.observableArrayList();
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
        empService = new EmployeeService();
        attenService = new AttendanceService();
        
        popup = new ListViewPopUpController();
        yearPieChart.setItems(UIUtils.showYear("1920"));
        yearPieChart.getSelectionModel().selectFirst();
        
        thisMonth    = YearMonth.now();
        lastMonth    = thisMonth.minusMonths(1);
        twoMonthsAgo = thisMonth.minusMonths(2);
        
        
        employeePieChart(); // DISPLAY PIECHART
        areaChartDisplay(); // DISPLAY AREACHART
        avgEmpAttendaceTableDisplay(); // DISPLAY TABLE
        setAvgEmpAttendaceTableData(); // SET DATA TO TABLE
        recentEmpList(); // DISPLAY RECENT EMPLOYEE'S LISTVIEW
        showEmpCount();
        
    }    

    @FXML
    private void displayPieChart(ActionEvent event) {
        employeePieChart();
        showEmpCount();
    }

    private void employeePieChart(){
        employeePieChart.setTitle("Employees on "+yearPieChart.getValue());
        ObservableList<PieChart.Data> datas = FXCollections.observableArrayList();
        for(MyPieChart chart: empService.getTotalEmployeeByGender()){
            datas.add(new PieChart.Data(chart.getGender(), chart.getNumber()));
        }
        employeePieChart.setData(datas);
    }
    
    public void areaChartDisplay(){
        AttendanceService service = new AttendanceService();
        
        areaChartPane.getChildren().clear();
       
        final NumberAxis xAxis = new NumberAxis(1,31,1);
        
        int thisMonthMax = service.getMaxTotalOfMonth(thisMonth);
        int lastMonthMax = service.getMaxTotalOfMonth(lastMonth);
        int twoMonthsAgoMax = service.getMaxTotalOfMonth(twoMonthsAgo);
        
        int max = maXTotal(thisMonthMax,lastMonthMax,twoMonthsAgoMax);
        
        final NumberAxis yAxis = new NumberAxis(0,max,10);
        final AreaChart<Number,Number> areachart = new AreaChart<>(xAxis,yAxis);
        areachart.setTitle("Total Attendace");
        XYChart.Series<Number,Number> seriesOne = new XYChart.Series<>();
        seriesOne.setName(twoMonthsAgo.getMonth().toString());
        XYChart.Series<Number,Number> seriesTwo = new XYChart.Series<>();
        seriesTwo.setName(lastMonth.getMonth().toString());
        XYChart.Series<Number,Number> seriesThree = new XYChart.Series<>();
        seriesThree.setName(thisMonth.getMonth().toString());
        
        areachart.getData().clear();
        seriesOne.getData().clear();
        seriesTwo.getData().clear();
        seriesThree.getData().clear();
        
        
        for(AttendanceData data: service.getTotalAttendanceOfMonth(twoMonthsAgo)){
            seriesOne.getData().add(new XYChart.Data(data.getDay(), data.getTotal()));
        }
        
        for(AttendanceData data: service.getTotalAttendanceOfMonth(lastMonth)){
            seriesTwo.getData().add(new XYChart.Data(data.getDay(), data.getTotal()));
        }
        
        for(AttendanceData data: service.getTotalAttendanceOfMonth(thisMonth)){
            seriesThree.getData().add(new XYChart.Data(data.getDay(), data.getTotal()));
        }
        
        
        areachart.getData().addAll(seriesOne, seriesTwo, seriesThree);
//        
//        seriesAttendance.getName().replaceAll("", "Attendace");
        
        areaChartPane.getChildren().add(areachart);
        
        areaChartPane.setTopAnchor(areaChartPane.getChildren().get(0),0.0);
        areaChartPane.setBottomAnchor(areaChartPane.getChildren().get(0),0.0);
        areaChartPane.setRightAnchor(areaChartPane.getChildren().get(0),0.0);
        areaChartPane.setLeftAnchor(areaChartPane.getChildren().get(0),0.0);
    }
    

    @FXML
    private void reloadAvgEmpAttendanceTable(ActionEvent event) {
        setAvgEmpAttendaceTableData();
    }

    @FXML
    private void searchAvgEmpAttendance(ActionEvent event) {
    }
    
    private void avgEmpAttendaceTableDisplay(){
        daysCol = new TableColumn<>("Day");
        daysCol.setMinWidth(100);
        monthOne = new TableColumn<>(twoMonthsAgo.format(monthFormatter));
        monthOne.setMinWidth(100);
        monthTwo = new TableColumn<>(lastMonth.format(monthFormatter));
        monthTwo.setMinWidth(100);
        monthThree = new TableColumn<>(thisMonth.format(monthFormatter));
        monthThree.setMinWidth(100);
        attendanceCol = new TableColumn("Attendance");
        attendanceCol.setMinWidth(300);
        attendanceCol.getColumns().addAll(monthOne,monthTwo,monthThree);
        areaChartTable.getColumns().addAll(daysCol, attendanceCol);
    }
    
    public void setAvgEmpAttendaceTableData(){  
        daysCol.setCellValueFactory(
            cellData -> cellData.getValue().dayProperty().asObject());
        monthOne.setCellValueFactory(
            cellData -> cellData.getValue().monthOneValProperty().asObject());
        monthTwo.setCellValueFactory(
            cellData -> cellData.getValue().monthTwoValProperty().asObject());
        monthThree.setCellValueFactory(
            cellData -> cellData.getValue().monthThreeValProperty().asObject());
       
        getDashboardTableData();
        areaChartTable.setItems(dashboardTableData);
        
    }

   public void recentEmpList() {
        recentEmpList.getItems().clear();
        for(Employee emp : empService.getRecentEmployee()){
            Label lbl = new Label(emp.getFname()+" "+emp.getLname());
            lbl.setAccessibleText(emp.getEmpid()+"");
            
            Image avatar = null;
            if(emp.getAvatar() != null){
                File file = new File(URI.create(emp.getAvatar()).getPath());
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
            }else{
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
            recentEmpList.getItems().add(lbl);
        }
    }

    @FXML
    private void showPopup(MouseEvent event) {
        String id = recentEmpList.getSelectionModel().getSelectedItem().getAccessibleText();
        if(!id.isEmpty()){
            popup.initPopup();
            popup.showPopupList(recentEmpList,id, event);
        }
    }

    @Override
    public void setScreenParent(ScreensController parent) {
        screenController = parent;
    }

    public void showEmpCount() {
        int maleNum = getTotalGender("Male");
        int femaleNum = getTotalGender("Female");
        int unspecifiedNum = getTotalGender("Unspecified");
        int totalEmp = maleNum + femaleNum + unspecifiedNum;
        maleField.setText(maleNum+"");
        femaleField.setText(femaleNum+"");
        unspecifiedField.setText(unspecifiedNum+"");
        totalEmployee.setText("TOTAL: "+totalEmp);
    }

    @FXML
    private void reloadRecentEmps(ActionEvent event) {
        recentEmpList();
    }

    
    private int getTotalGender(String name){ 
        for(MyPieChart chart: empService.getTotalEmployeeByGender()){
            if(chart.getGender().equals(name)){
                return chart.getNumber();
            }
        }
        return 0;
    }

    private void getDashboardTableData() {
        dashboardTableData = FXCollections.observableArrayList();
        AttendanceService service = new AttendanceService();
        for(int i = 1; i <= 31; i++){
            ObservableList<AttendanceData> datas = FXCollections.observableArrayList();
            DashboardTable tbl = new DashboardTable();
            datas  = service.getTotalAttendanceOfDay(i);
            if(datas != null){
                tbl.setDay(i);
                for(AttendanceData data: datas){
                    if(data.getMonth().equals(thisMonth.toString().replace("-", ""))){
                        tbl.setMonthThreeVal(data.getTotal());
                    }
                    if(data.getMonth().equals(lastMonth.toString().replace("-", ""))){
                        tbl.setMonthTwoVal(data.getTotal());
                    }
                    if(data.getMonth().equals(twoMonthsAgo.toString().replace("-", ""))){
                        tbl.setMonthOneVal(data.getTotal());
                    }
                }
            }
            dashboardTableData.add(tbl);
        }
        
    }

    private int maXTotal(int a, int b, int c) {
        int i = 0;
        if(a < b){
            if(b < c){
                i = c;
            }else{
               i = b; 
            }
        }else{
            if(b > c){
                i = a;
            }else{
                if(c > a){
                    i = c;
                }else{
                    i = a;
                }
            }
        }
        return i;
    }


}
