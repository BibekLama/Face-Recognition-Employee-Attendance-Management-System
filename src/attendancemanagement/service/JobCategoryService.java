package attendancemanagement.service;

import attendancemanagement.database.AssignedJobCatDAO;
import attendancemanagement.database.JobCategoryDAO;
import attendancemanagement.database.JobCategoryDBO;
import attendancemanagement.model.JobCategory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class JobCategoryService {

    public JobCategory getJobCategory(int empid) {
        JobCategoryDAO dao = new JobCategoryDAO();
        JobCategoryDBO dbo = new JobCategoryDBO();
        int jid = dao.getJobCategoryId(empid);
        System.out.println("Employee id: "+empid);
        System.out.println("Job id: "+jid);
        if(jid > 0){
            JobCategoryDBO dbo1 = dao.selectJobCatById(jid);
            JobCategory cat = new JobCategory();
            cat.setJid(dbo1.getJid());
            cat.setTitle(dbo1.getTitle());
            cat.setDisplay_name(dbo1.getDisplay_name());
            cat.setAdded_date(dbo1.getAdded_date().toLocalDateTime());
            cat.setLast_modified_date(dbo1.getLast_modified_date().toLocalDateTime());
            System.out.println("Job title: "+dbo1.getTitle());
            return cat;
        }
        return null;
    }

    public ObservableList<JobCategory> getJobCategories() {
        JobCategoryDAO dao = new JobCategoryDAO();
        ObservableList<JobCategory> cats = FXCollections.observableArrayList();
        for(JobCategoryDBO dbo: dao.selectAll()){
            JobCategory cat = new JobCategory();
            cat.setJid(dbo.getJid());
            cat.setTitle(dbo.getTitle());
            cat.setDisplay_name(dbo.getDisplay_name());
            cat.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            cat.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            cats.add(cat);
        }
        return cats;
    }

    public int addJobCategory(JobCategory cat) {
        JobCategoryDBO dbo = new JobCategoryDBO();
        dbo.setTitle(cat.getTitle());
        dbo.setDisplay_name(cat.getDisplay_name());
        dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        JobCategoryDAO dao = new JobCategoryDAO();
        if(dao.isJobCatExist(cat.getTitle()) == 3){
            return dao.addJobCategory(dbo); 
        }
        else{
            return 4;
        }          
    }

    public JobCategory getJobCatById(int jid) {
        JobCategoryDAO dao = new JobCategoryDAO();
        JobCategoryDBO dbo = dao.selectJobCatById(jid);
        if(dbo != null){
            JobCategory cat = new JobCategory();
            cat.setJid(dbo.getJid());
            cat.setTitle(dbo.getTitle());
            cat.setDisplay_name(dbo.getDisplay_name());
            cat.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            cat.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            return cat;
        }
        return null;
    }

    public int editJobCategory(JobCategory cat) {
        JobCategoryDBO dbo = new JobCategoryDBO();
        dbo.setJid(cat.getJid());
        dbo.setTitle(cat.getTitle());
        dbo.setDisplay_name(cat.getDisplay_name());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        JobCategoryDAO dao = new JobCategoryDAO();
        JobCategoryDBO temp = dao.selectJobCatById(cat.getJid());
        if(temp.getTitle().equals(cat.getTitle())){
            return dao.editJobCategory(dbo);
        }else{
            if(dao.isJobCatExist(cat.getTitle()) == 3){
                return dao.editJobCategory(dbo); 
            }
            else{
                return 4;
            }
        }
        
    }

    public int deleteJobCategory(int jid) {
       JobCategoryDAO dao = new JobCategoryDAO();
       if(dao.deleteJobCategory(jid) > 0){
           AssignedJobCatDAO assign = new AssignedJobCatDAO();
           if(assign.deleteJobCatAssignedByJid(jid) > 0){
               return 1;
           }
       }
       return 0;
    }
    
}
