package aunguyen.quanlycongviec.Object;

/**
 * Created by Au Nguyen on 2/6/2018.
 */

public class Constant {
    public static String NODE_CONG_VIEC = "CongViec";
    public static String NODE_NHAN_VIEN = "NhanVien";
    public static String NODE_DOMAIN = "Domain";
    public static String NODE_OFFICE = "Office";

    public static String PREFERENCE_NAME = "QuanLyCongViec";
    public static String PREFERENCE_KEY_ID = "IdNhanVien";

    public static String PREFERENCE_DOMAIN = "Domain";

    public static String URL_MALE = "https://firebasestorage.googleapis.com/v0/b/nienluanchuyennganh-3fcb7.appspot.com/o/male.png?alt=media&token=2d1721fe-f658-4d37-9c3f-4ca7d683f720";
    public static String URL_FEMALE = "https://firebasestorage.googleapis.com/v0/b/nienluanchuyennganh-3fcb7.appspot.com/o/female.png?alt=media&token=fb6252b7-9250-4aae-a498-1b8730b0bace";

    //Child of a Employee on firebase
    public static String KEY_ACCOUNT_TYPE = "accountType";
    public static String KEY_ADDRESS = "addressEmployee";
    public static String KEY_BIRTHDAY = "birthdayEmployee";
    public static String KEY_GENDER = "genderEmployee";
    public static String KEY_ID_MANAGE = "idManage";
    public static String KEY_NAME = "nameEmployee";
    public static String KEY_PHONE = "phoneEmployee";

    //Activity for result
    public static int REQUEST_CODE = 999;
    public static int RESULT_CODE = 111;

    //status of job
    public static String RECEIVED = "Đã Nhận";
    public static String NOT_RECEIVED = "Chưa Nhận";

    public static String COMPLETE = "Hoàn Thành";//0
    public static String STILL_DEADLINE = "Còn Hạn"; //1
    public static String EARLY_DEADLINE = "Sắp Hết Hạn"; //2
    public static String DEADLINE = "Hết Hạn"; //3
    public static String PAST_DEADLINE = "Quá Hạn"; //4

}
