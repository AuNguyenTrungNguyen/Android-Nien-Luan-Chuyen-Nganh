package aunguyen.quanlycongviec.Object;

import java.io.Serializable;

/**
 * Created by Au Nguyen on 3/15/2018.
 */

public class EmployeeObject implements Serializable {
    private String idEmployee;
    private String idManage;
    private String usernameEmployee;
    private String officeEmployee; //Chuc vu
    private String nameEmployee;
    private String birthdayEmployee;
    private String phoneEmployee;
    private String genderEmployee; //0 Male, 1 Female
    private String addressEmployee;
    private String accountType; //0 Admin, 1 User
    private String urlAvatar;

    public EmployeeObject() {
    }

    public EmployeeObject(String idEmployee,
                          String idManage,
                          String usernameEmployee,
                          String officeEmployee,
                          String nameEmployee,
                          String birthdayEmployee,
                          String phoneEmployee,
                          String genderEmployee,
                          String addressEmployee,
                          String accountType,
                          String urlAvatar) {
        this.idEmployee = idEmployee;
        this.idManage = idManage;
        this.usernameEmployee = usernameEmployee;
        this.officeEmployee = officeEmployee;
        this.nameEmployee = nameEmployee;
        this.birthdayEmployee = birthdayEmployee;
        this.phoneEmployee = phoneEmployee;
        this.genderEmployee = genderEmployee;
        this.addressEmployee = addressEmployee;
        this.accountType = accountType;
        this.urlAvatar = urlAvatar;
    }

    public String getPhoneEmployee() {
        return phoneEmployee;
    }

    public void setPhoneEmployee(String phoneEmployee) {
        this.phoneEmployee = phoneEmployee;
    }

    public String getUsernameEmployee() {
        return usernameEmployee;
    }

    public void setUsernameEmployee(String usernameEmployee) {
        this.usernameEmployee = usernameEmployee;
    }

    public String getOfficeEmployee() {
        return officeEmployee;
    }

    public void setOfficeEmployee(String officeEmployee) {
        this.officeEmployee = officeEmployee;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getIdManage() {
        return idManage;
    }

    public void setIdManage(String idManage) {
        this.idManage = idManage;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public String getBirthdayEmployee() {
        return birthdayEmployee;
    }

    public void setBirthdayEmployee(String birthdayEmployee) {
        this.birthdayEmployee = birthdayEmployee;
    }

    public String getGenderEmployee() {
        return genderEmployee;
    }

    public void setGenderEmployee(String genderEmployee) {
        this.genderEmployee = genderEmployee;
    }

    public String getAddressEmployee() {
        return addressEmployee;
    }

    public void setAddressEmployee(String addressEmployee) {
        this.addressEmployee = addressEmployee;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }
}
