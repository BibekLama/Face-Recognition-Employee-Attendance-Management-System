package attendancemanagement.service;

import attendancemanagement.database.AssignedRoleDAO;
import attendancemanagement.database.RoleDAO;
import attendancemanagement.database.RoleDBO;
import attendancemanagement.model.Role;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class RoleService {

    public ObservableList<Role> getRoleData() {
       ObservableList<Role> data = FXCollections.observableArrayList();
       RoleDAO dao = new RoleDAO();
       for(RoleDBO dbo: dao.selectAll()){
           Role role = new Role();
           role.setRid(dbo.getRid());
           role.setRole(dbo.getRole());
           role.setDisplay_name(dbo.getDisplay_name());
           role.setAdded_date(dbo.getAdded_date().toLocalDateTime());
           role.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
           data.add(role);
       } 
       return data;
    }

    public int addRole(Role data) {
        RoleDBO dbo = new RoleDBO();
        dbo.setRole(data.getRole());
        dbo.setDisplay_name(data.getDisplay_name());
        dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        RoleDAO dao = new RoleDAO();
        
        if(dao.isRoleExist(data.getRole()) == 3){
            return dao.addRole(dbo);
        }
        else{
            return 4;
        }
    }

    public Role selectByRid(int rid) {
        RoleDBO dbo = new RoleDBO();
        RoleDAO dao = new RoleDAO();
        dbo = dao.selectByRid(rid);
        if(dbo != null){
            Role data= new Role();
            data.setRid(dbo.getRid());
            data.setRole(dbo.getRole());
            data.setDisplay_name(dbo.getDisplay_name());
            data.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            data.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            return data;
        }
        return null;
    }

    public int editRole(Role data) {
        RoleDBO dbo = new RoleDBO();
        dbo.setRid(data.getRid());
        dbo.setRole(data.getRole());
        dbo.setDisplay_name(data.getDisplay_name());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        RoleDAO dao = new RoleDAO();
        RoleDBO temp = dao.selectByRid(data.getRid());
        if(temp.getRole().equals(data.getRole())){
            return dao.editRole(dbo);
        }else{
            if(dao.isRoleExist(data.getRole()) == 3){
                return dao.editRole(dbo);
            }
            else{
                return 4;
            }
        }
    }

    public int deleteByRid(int rid) {
        RoleDAO dao = new RoleDAO();
        if(dao.deleteByRid(rid) > 0){
            AssignedRoleDAO assign = new AssignedRoleDAO();
            if(assign.deleteRoleAssignedByRid(rid) > 0){
                return 1;
            }
        }
        return 0;
    }
}
