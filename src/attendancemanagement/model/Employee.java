package attendancemanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Employee {
    private IntegerProperty empid;
    private StringProperty empcode;
    private StringProperty nationalid;
    private StringProperty fname;
    private StringProperty lname;
    private ObjectProperty<LocalDate> dob;
    private StringProperty gender;
    private StringProperty mobileNumber;
    private IntegerProperty jobTitle;
    private DoubleProperty salary;
    private StringProperty status;
    private StringProperty email;
    private StringProperty phone;
    private StringProperty city;
    private StringProperty district;
    private StringProperty zone;
    private StringProperty address;
    private ObjectProperty<LocalDateTime> addedDate;
    private ObjectProperty<LocalDateTime> updateDate;
    private StringProperty avatar;

    public Employee() {
        this.empid = new SimpleIntegerProperty();
        this.empcode = new SimpleStringProperty();
        this.nationalid = new SimpleStringProperty();
        this.fname = new SimpleStringProperty();
        this.lname = new SimpleStringProperty();
        this.dob = new SimpleObjectProperty<>();
        this.gender = new SimpleStringProperty();
        this.mobileNumber = new SimpleStringProperty();
        this.jobTitle = new SimpleIntegerProperty();
        this.salary = new SimpleDoubleProperty();
        this.status = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.district = new SimpleStringProperty();
        this.zone = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.addedDate = new SimpleObjectProperty<>();
        this.updateDate = new SimpleObjectProperty<>();
        this.avatar = new SimpleStringProperty();
    }

    // id ----------
    public int getEmpid() {
        return empid.get();
    }
    public void setEmpid(int empid) {
        this.empid.set(empid);
    }
    public IntegerProperty empidProperty(){
        return empid;
    }
    // empid ------------
    public String getEmpcode() {
        return empcode.get();
    }
    public void setEmpcode(String empcode) {
        this.empcode.set(empcode);
    }
    public StringProperty empcodeProperty(){
        return empcode;
    }   
    // nationalid ------------
    public String getNationalid() {
        return nationalid.get();
    }
    public void setNationalid(String nationalid) {
        this.nationalid.set(nationalid);
    }
    public StringProperty nationalidProperty(){
        return nationalid;
    } 
    // fname ---------
    public String getFname() {
        return fname.get();
    }
    public void setFname(String fname) {
        this.fname.set(fname);
    }
    public StringProperty fnameProperty(){
        return fname;
    } 
    //lname ----------
    public String getLname() {
        return lname.get();
    }
    public void setLname(String lname) {
        this.lname.set(lname);
    }
    public StringProperty lnameProperty(){
        return lname;
    }
    // dob -------------
    public LocalDate getDob(){
        return dob.get();
    }
    public void setDob(LocalDate dob) {
        this.dob.set(dob);
    }
    public ObjectProperty<LocalDate> dobProperty(){
        return dob;
    }
    // gender ----------
    public String getGender() {
        return gender.get();
    }
    public void setGender(String gender) {
        this.gender.set(gender);
    }
    public StringProperty genderProperty(){
        return gender;
    }
    // mobile number -----------
    public String getMobileNumber() {
        return mobileNumber.get();
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber.set(mobileNumber);
    }
    public StringProperty mobileNumberProperty(){
        return mobileNumber;
    }
    //jobtitle ------------
    public int getJobTitle() {
        return jobTitle.get();
    }
    public void setJobTitle(int jobTitle) {
        this.jobTitle.set(jobTitle);
    }
    public IntegerProperty jobTitleProperty(){
        return jobTitle;
    }
    // salary ---------
    public double getSalary() {
        return salary.get();
    }
    public void setSalary(double salary) {
        this.salary.set(salary);
    }
    public DoubleProperty salaryProperty(){
        return salary;
    }
    // status ----------
    public String getStatus() {
        return status.get();
    }
    public void setStatus(String status) {
        this.status.set(status);
    }
    public StringProperty statusProperty(){
        return status;
    }
    // email ----------
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty(){
        return email;
    }
    // phone ------
    public String getPhone() {
        return phone.get();
    }
    public void setPhone(String phone) {
        this.phone.set(phone);
    }
    public StringProperty phoneProperty(){
        return phone;
    }
    // city ---------------
    public String getCity() {
        return city.get();
    }
    public void setCity(String city) {
        this.city.set(city);
    }   
    public StringProperty cityProperty(){
        return city;
    }
    // district -----------
    public String getDistrict() {
        return district.get();
    }
    public void setDistrict(String district) {
        this.district.set(district);
    }
    public StringProperty districtProperty(){
        return district;
    }
    // zone ---------
    public String getZone() {
        return zone.get();
    }
    public void setZone(String zone) {
        this.zone.set(zone);
    }
    public StringProperty zoneProperty(){
        return zone;
    }
    // address --------
    public String getAddress() {
        return address.get();
    }
    public void setAddress(String address) {
        this.address.set(address);
    }
    public StringProperty addressProperty(){
        return address;
    }
    // activeDate ------------
    public LocalDateTime getAddedDate() {
        return addedDate.get();
    }
    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate.set(addedDate);
    }
    public ObjectProperty<LocalDateTime> addedDateProperty(){
        return addedDate;
    }
    // updateDate ----------
    public LocalDateTime getUpdateDate() {
        return updateDate.get();
    }
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate.set(updateDate);
    }
    public ObjectProperty<LocalDateTime> updateDateProperty(){
        return updateDate;
    }
    // avatar -----------
    public String getAvatar() {
        return avatar.get();
    }
    public void setAvatar(String avatar) {
        this.avatar.set(avatar);
    }
    public StringProperty avatarProperty(){
        return avatar;
    }
   
}
