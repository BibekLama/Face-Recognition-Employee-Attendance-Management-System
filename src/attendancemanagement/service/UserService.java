package attendancemanagement.service;

import attendancemanagement.database.AssignedRoleDAO;
import attendancemanagement.database.AssignedUserDAO;
import attendancemanagement.utils.Utils;
import attendancemanagement.database.UserDAO;
import attendancemanagement.database.UserDBO;
import attendancemanagement.model.Employee;
import attendancemanagement.model.Role;
import attendancemanagement.model.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UserService {

    public User login(User user) {
        UserDBO dbo = new UserDBO();
        dbo.setUsername(user.getUsername());
        dbo.setPassword(user.getPassword());
        UserDAO dao = new UserDAO();
        UserDBO temp = dao.login(dbo);
        if(temp != null){
            User data = new User();
            data.setUid(temp.getUid());
            data.setUsername(temp.getUsername());
            data.setPassword(temp.getPassword());
            return data;
        }
        return null;
    }

    public ObservableList<User> getUsersData() {
        ObservableList<User> users = FXCollections.observableArrayList();
        UserDAO dao = new UserDAO();
        for(UserDBO dbo: dao.selectAll()){
            User user = new User();
            user.setUid(dbo.getUid());
            user.setUsername(dbo.getUsername());
            user.setPassword(dbo.getPassword());
            String empName = "n/a";
            
            AssignedUserDAO assignUsr = new AssignedUserDAO();
            int empid = assignUsr.getAssignedEmpId(dbo.getUid());
            if(empid > 0){
                Employee emp = new Employee();
                EmployeeService empService = new EmployeeService();
                emp = empService.getEmployeeData(empid);
                empName = emp.getFname()+" "+emp.getLname();
            }
            user.setEmployee(empName);
            String roleName = "n/a";
            
            AssignedRoleDAO assignRole = new AssignedRoleDAO();
            int rid = assignRole.getAssignedRoleId(dbo.getUid());
            if(rid > 0){
                Role role = new Role();
                RoleService roleService = new RoleService();
                role = roleService.selectByRid(rid);
                roleName = role.getDisplay_name();
            }
            user.setRole(roleName);
            user.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            user.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            users.add(user);
        }
        return users;
    }

    public int addNewUser(User user) {
        UserDBO dbo = new UserDBO();
        dbo.setUsername(user.getUsername());
        dbo.setPassword(user.getPassword());
        dbo.setAdded_date(Timestamp.valueOf(LocalDateTime.now()));
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        UserDAO dao = new UserDAO();
        if(dao.isUserExist(user.getUsername())){
            return 0;
        }
        return dao.addNewUser(dbo); 
        
    }

    public User getUserById(int uid) {
        UserDAO dao = new UserDAO();
        UserDBO dbo = dao.getUserById(uid);
        User user = new User();
        if(dbo != null){
//            user.setRid(dao.getAssignedRoleId(dbo.getUid()));
//            user.setEmpid(dao.getAssignedRoleId(dbo.getUid()));
            user.setUid(dbo.getUid());
            user.setUsername(dbo.getUsername());
            user.setPassword(dbo.getPassword());
            user.setAdded_date(dbo.getAdded_date().toLocalDateTime());
            user.setLast_modified_date(dbo.getLast_modified_date().toLocalDateTime());
            return user;
        }
        return null;
    }

    public int updateUser(User user) {
        UserDBO dbo = new UserDBO();
        dbo.setUid(user.getUid());
        dbo.setUsername(user.getUsername());
        dbo.setLast_modified_date(Timestamp.valueOf(LocalDateTime.now()));
        UserDAO dao = new UserDAO();
        UserDBO temp = dao.getUserById(user.getUid());
        if(!temp.getUsername().equals(user.getUsername())){
            if(dao.isUserExist(user.getUsername())){
                return 0;
            }
        }
        return dao.updateUser(dbo); 
    }

    public int deleteByRid(int uid) {
        UserDAO dao = new UserDAO();
        
        AssignedUserDAO auDao = new AssignedUserDAO();
        auDao.deleteUserAssignedByUid(uid);
        AssignedRoleDAO arDao = new AssignedRoleDAO();
        arDao.deleteRoleAssignedByUid(uid);
        if(dao.deleteByUid(uid) > 0){
            return 1;
        }
        return 0;
    }

    public int updatePass(int uid, String oldPass, String newPass) {
        UserDAO dao = new UserDAO();
        UserDBO dbo = dao.getUserById(uid);
        if(dbo.getPassword().equals(Utils.hashPass(oldPass))){
            UserDBO data = new UserDBO();
            data.setUid(uid);
            data.setPassword(Utils.hashPass(newPass));
            return dao.updatePassword(data);
        }else{
            return 0;
        }
    }

    public int assignRole(int uid, int rid) {
        AssignedRoleDAO dao = new AssignedRoleDAO();
        if(dao.getAssignedRid(uid) > 0){
            return dao.updateAssignRole(uid, rid, Timestamp.valueOf(LocalDateTime.now()));
        }else{
            return dao.assignRole(uid, rid, Timestamp.valueOf(LocalDateTime.now()));
        }
    }
    
    public int assignEmployee(int uid, int empid) {
        AssignedUserDAO dao = new AssignedUserDAO();
        int id = dao.getAssignedEmpId(uid);
        if(id > 0){
            return dao.updateAssignEmp(uid, empid, Timestamp.valueOf(LocalDateTime.now()));
        }else{ 
            return dao.assignEmployee(uid, empid, Timestamp.valueOf(LocalDateTime.now()));
        }
    }
    
    public int getEmpIdFromAssignedUser(int uid){
        AssignedUserDAO dao = new AssignedUserDAO();
        return dao.getAssignedEmpId(uid);
    }
    
    public int getRidFromAssignedRole(int uid){
        AssignedRoleDAO dao = new AssignedRoleDAO();
        return dao.getAssignedRid(uid);
    }
    
}
