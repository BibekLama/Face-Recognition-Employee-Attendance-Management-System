package attendancemanagement.utils;

import com.mysql.jdbc.StringUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class UIUtils {
    
    public static boolean confirm;
    
    public static ObservableList<Object> showYear(String startYear) {
        
        ObservableList<Object> values = FXCollections.observableArrayList();
        
        DateFormat formatter = new SimpleDateFormat("yyyy");
        
        List<Object> years = new ArrayList<>();
        
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        
        try {
            beginCalendar.setTime(formatter.parse(startYear));
            finishCalendar.setTime(Calendar.getInstance().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        LocalDate localdate = null;
        while (beginCalendar.before(finishCalendar)) {
            date = beginCalendar.getTime();
            years.add(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
            beginCalendar.add(Calendar.YEAR, 1);
        }
        
        for(int i = years.size()-1; i >= 0; i--){
            values.add(years.get(i));
//            System.out.println(years.get(i));
        }
        return values;
    }
    
    public static ObservableList<Object> showMonth(){
        ObservableList<Object> values = FXCollections.observableArrayList();
        
        DateFormat formatter = new SimpleDateFormat("MMM");
        
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        
        try {
            beginCalendar.setTime(formatter.parse("JAN"));
            finishCalendar.setTime(formatter.parse("DEC"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        LocalDate localdate = null;
        while (beginCalendar.before(finishCalendar)) {
            date = beginCalendar.getTime();
            values.add(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth());
            beginCalendar.add(Calendar.MONTH, 1);
        }
        date = beginCalendar.getTime();
        values.add(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth());
//        System.out.println(values.get(1));
        return values;
    }
    
    public static ObservableList<Object> showDay(){
        ObservableList<Object> values = FXCollections.observableArrayList();
        
        DateFormat formatter = new SimpleDateFormat("D");
        
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        
        try {
            beginCalendar.setTime(formatter.parse("0"));
            finishCalendar.setTime(formatter.parse("31"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        LocalDate localdate = null;
        while (beginCalendar.before(finishCalendar)) {
            beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
            date = beginCalendar.getTime();
//            System.out.println(date);
            values.add(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth());
        }
//        System.out.println(values.get(1));
        return values;
    }
    
    public static ObservableList<String> showGender(){
        ObservableList<String> values = FXCollections.observableArrayList();
        values.add("Unspecified");
        values.add("Male");
        values.add("Female");
        return values;
    }
    
    public static ObservableList<String> showZone(){
        ObservableList<String> values = FXCollections.observableArrayList();
        values.add("Mechi");
        values.add("Koshi");
        values.add("Sagarmatha");
        values.add("Janakpur");
        values.add("Bagmati");
        values.add("Narayani");
        values.add("Gandaki");
        values.add("Lumbini");
        values.add("Bheri");
        values.add("Rapti");
        values.add("Dhaulagiri");
        values.add("Karnali");
        values.add("Mahakali");
        values.add("Seti");
        return values;
    }
    
    public static void successNotification(String title, String text){
        Image img = new Image("/attendancemanagement/res/images/tick.png");
        messageBody(img,title,text).show();
    }
    
    public static void errorNotification(String title, String text){
        Image img = new Image("/attendancemanagement/res/images/error.png");
        messageBody(img,title,text).show();
    }
    
    public static void warningNotification(String title, String text){
        Image img = new Image("/attendancemanagement/res/images/alert_.png");
        messageBody(img,title,text).show();
    }
    
    public static boolean confirmNotification(String title, String header, String message){
        Image img = new Image("/attendancemanagement/res/images/alert_.png");
        return confirmDialog(title,header,message);
    }
    
    public static void showError(String title, String text){
        Image img = new Image("/attendancemanagement/res/images/error.png");
        messageBody(img,title,text).showError();
    }
    
    public static boolean isNumeric(String text){
        return StringUtils.isStrictlyNumeric(text);
    }
    
    private static Notifications messageBody(Image img,String title,String text){
        Notifications notify = Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        notify.darkStyle();
        return notify;
    }
    
    private static Notifications confirmBody(String title,String text){
        Notifications notify = Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .action();
        notify.darkStyle();
        return notify;
    }
    
    private static boolean confirmDialog(String title, String header, String message){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            return true;
        } else {
            return false;
        }
    }
 
}
