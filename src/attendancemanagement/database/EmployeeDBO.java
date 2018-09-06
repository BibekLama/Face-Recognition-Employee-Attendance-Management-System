package attendancemanagement.database;


import java.sql.Timestamp;
import java.sql.Date;


public class EmployeeDBO {
    private int empid;//1
    private String emp_code;//2
    private String emp_firstname;//3
    private String emp_lastname;//4
    private String emp_avatar;//5
    private Date emp_dob;//7
    private String emp_gender;//8
    private String emp_email;//9
    private String emp_mobile_no;//10
    private String emp_phone_no;//11
    private String emp_zone;//12
    private String emp_city;//13
    private String emp_district;//14
    private String emp_address;//15
    private String emp_national_id;//16
    private double emp_salary;//17
    private Timestamp added_date;//18
    private Timestamp last_modified_date;//19
    private String emp_status; //20

    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_firstname() {
        return emp_firstname;
    }

    public void setEmp_firstname(String emp_firstname) {
        this.emp_firstname = emp_firstname;
    }

    public String getEmp_lastname() {
        return emp_lastname;
    }

    public void setEmp_lastname(String emp_lastname) {
        this.emp_lastname = emp_lastname;
    }

    public String getEmp_avatar() {
        return emp_avatar;
    }

    public void setEmp_avatar(String emp_avatar) {
        this.emp_avatar = emp_avatar;
    }

    public Date getEmp_dob() {
        return emp_dob;
    }

    public void setEmp_dob(Date emp_dob) {
        this.emp_dob = emp_dob;
    }

    public String getEmp_gender() {
        return emp_gender;
    }

    public void setEmp_gender(String emp_gender) {
        this.emp_gender = emp_gender;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_mobile_no() {
        return emp_mobile_no;
    }

    public void setEmp_mobile_no(String emp_mobile_no) {
        this.emp_mobile_no = emp_mobile_no;
    }

    public String getEmp_phone_no() {
        return emp_phone_no;
    }

    public void setEmp_phone_no(String emp_phone_no) {
        this.emp_phone_no = emp_phone_no;
    }

    public String getEmp_zone() {
        return emp_zone;
    }

    public void setEmp_zone(String emp_zone) {
        this.emp_zone = emp_zone;
    }

    public String getEmp_city() {
        return emp_city;
    }

    public void setEmp_city(String emp_city) {
        this.emp_city = emp_city;
    }

    public String getEmp_district() {
        return emp_district;
    }

    public void setEmp_district(String emp_district) {
        this.emp_district = emp_district;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public String getEmp_national_id() {
        return emp_national_id;
    }

    public void setEmp_national_id(String emp_national_id) {
        this.emp_national_id = emp_national_id;
    }

    public double getEmp_salary() {
        return emp_salary;
    }

    public void setEmp_salary(double emp_salary) {
        this.emp_salary = emp_salary;
    }

    public Timestamp getAdded_date() {
        return added_date;
    }

    public void setAdded_date(Timestamp added_date) {
        this.added_date = added_date;
    }

    public Timestamp getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(Timestamp last_modified_date) {
        this.last_modified_date = last_modified_date;
    }
    
    public String getEmp_status() {
        return emp_status;
    }
    
    public void setEmp_status(String emp_status) {
        this.emp_status = emp_status;
    }

}
