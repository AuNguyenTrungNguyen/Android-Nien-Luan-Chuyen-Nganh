package aunguyen.quanlycongviec.Object;

/**
 * Created by Au Nguyen on 2/6/2018.
 */

public class Constant {
    public static final String NODE_CONG_VIEC = "CongViec";
    public static final String NODE_NHAN_VIEN = "NhanVien";
    public static final String NODE_DOMAIN = "Domain";
    public static final String NODE_OFFICE = "Office";
    public static final String PREFERENCE_NAME = "QuanLyCongViec";
    public static final String PREFERENCE_KEY_ID = "IdNhanVien";
    public static final String PREFERENCE_DOMAIN = "Domain";
    public static final String URL_MALE = "https://firebasestorage.googleapis.com/v0/b/nienluanchuyennganh-3fcb7.appspot.com/o/male.png?alt=media&token=2d1721fe-f658-4d37-9c3f-4ca7d683f720";
    public static final String URL_FEMALE = "https://firebasestorage.googleapis.com/v0/b/nienluanchuyennganh-3fcb7.appspot.com/o/female.png?alt=media&token=fb6252b7-9250-4aae-a498-1b8730b0bace";

    //Child of a Efinal mployee on firebase
    public static final String KEY_ACCOUNT_TYPE = "accountType";
    public static final String KEY_ADDRESS = "addressEmployee";
    public static final String KEY_BIRTHDAY = "birthdayEmployee";
    public static final String KEY_GENDER = "genderEmployee";
    public static final String KEY_ID_MANAGE = "idManage";
    public static final String KEY_NAME = "nameEmployee";
    public static final String KEY_PHONE = "phoneEmployee";

    //Activity for result
    public static final int REQUEST_CODE = 999;
    public static final int RESULT_CODE = 111;

    //status of job
    public static final String RECEIVED = "Đã Nhận";
    public static final String NOT_RECEIVED = "Chưa Nhận";

    public static final String COMPLETE = "Hoàn Thành";//0
    public static final String STILL_DEADLINE = "Còn Hạn"; //1
    public static final String EARLY_DEADLINE = "Sắp Hết Hạn"; //2
    public static final String DEADLINE = "Hết Hạn"; //3
    public static final String PAST_DEADLINE = "Quá Hạn"; //4

    //statistic
    public static final String KEY_ID_EMPLOYEE_STATISTIC = "idEmployee";
    public static final int TYPE_TOTAL = 0;
    public static final int TYPE_STATUS = 1;
    public static final String KEY_ID_STATISTIC = "idStatistic";
    public static final String KEY_START_STATISTIC = "start";
    public static final String KEY_END_STATISTIC = "end";

    //detail joc
    public static final String KEY_TITLE_JOB = "titleJob";
    public static final String KEY_DESCRIPTION_JOB = "descriptionJob";


}
