package aunguyen.quanlycongviec.Object;

import java.util.List;

/**
 * Created by Au Nguyen on 3/15/2018.
 */

public class JobObject {
    private String idJob;
    private String idManageJob;
    private String titleJob;
    private String descriptionJob;
    private String startDateJob;
    private String endDateJob;
    private String statusJob;
    private List<String> listIdMember;

    public JobObject() {
    }

    public JobObject(String idJob,
                     String idManageJob,
                     String titleJob,
                     String descriptionJob,
                     String startDateJob,
                     String endDateJob,
                     String statusJob,
                     List<String> listIdMember) {
        this.idJob = idJob;
        this.idManageJob = idManageJob;
        this.titleJob = titleJob;
        this.descriptionJob = descriptionJob;
        this.startDateJob = startDateJob;
        this.endDateJob = endDateJob;
        this.statusJob = statusJob;
        this.listIdMember = listIdMember;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdManageJob() {
        return idManageJob;
    }

    public void setIdManageJob(String idManageJob) {
        this.idManageJob = idManageJob;
    }

    public String getTitleJob() {
        return titleJob;
    }

    public void setTitleJob(String titleJob) {
        this.titleJob = titleJob;
    }

    public String getDescriptionJob() {
        return descriptionJob;
    }

    public void setDescriptionJob(String descriptionJob) {
        this.descriptionJob = descriptionJob;
    }

    public String getStartDateJob() {
        return startDateJob;
    }

    public void setStartDateJob(String startDateJob) {
        this.startDateJob = startDateJob;
    }

    public String getEndDateJob() {
        return endDateJob;
    }

    public void setEndDateJob(String endDateJob) {
        this.endDateJob = endDateJob;
    }

    public String getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(String statusJob) {
        this.statusJob = statusJob;
    }

    public List<String> getListIdMember() {
        return listIdMember;
    }

    public void setListIdMember(List<String> listIdMember) {
        this.listIdMember = listIdMember;
    }
}
