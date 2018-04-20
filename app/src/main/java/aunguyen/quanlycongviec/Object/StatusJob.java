package aunguyen.quanlycongviec.Object;

public class StatusJob {
   private String idMember;
   private String status;
   private String notify;

    public StatusJob() {
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public StatusJob(String idMember, String status, String notify) {

        this.idMember = idMember;
        this.status = status;
        this.notify = notify;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
