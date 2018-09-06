package attendancemanagement.service;

import attendancemanagement.database.OptionDAO;
import attendancemanagement.database.OptionDBO;
import attendancemanagement.model.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class OptionService {

    public Option getOption(String name) {
        Option opt = new Option();
        OptionDAO dao = new OptionDAO();
        OptionDBO dbo = dao.selectOption(name);
        if(dbo != null){
            opt.setId(dbo.getId());
            opt.setName(dbo.getName());
            opt.setValue(dbo.getValue());
            opt.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            opt.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            return opt;
        }
        return null;
    }
    
    public String getClockTime(String name) {
        Option opt = new Option();
        OptionDAO dao = new OptionDAO();
        OptionDBO dbo = dao.selectOption(name);
        if(dbo != null){
            return dbo.getValue();
        }
        return "";
    }

    public int addStartTime(Option start) {
        OptionDAO dao = new OptionDAO();
        OptionDBO dbo = new OptionDBO();
        dbo.setName(start.getName());
        dbo.setValue(start.getValue());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        Option temp1 = getOption(start.getName());
        if(temp1 != null){
            return dao.updateTime(dbo);
        }else{
            dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
            return dao.addTime(dbo);
        }
       
    }
    
    public int addEndTime(Option end) {
        OptionDAO dao = new OptionDAO();
        OptionDBO dbo = new OptionDBO();
        dbo.setName(end.getName());
        dbo.setValue(end.getValue());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        Option temp2 = getOption(end.getName());
        if(temp2 != null){
            return dao.updateTime(dbo);
        }else{
            dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
            return dao.addTime(dbo);
        }
    }
    
    
}
