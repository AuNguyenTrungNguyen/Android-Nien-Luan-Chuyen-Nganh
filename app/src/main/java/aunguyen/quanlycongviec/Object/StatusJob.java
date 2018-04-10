package aunguyen.quanlycongviec.Object;

public class StatusJob {
   private String idMember;
   private String status;

    public StatusJob() {
    }

    public StatusJob(String idMember, String status) {
        this.idMember = idMember;
        this.status = status;
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
