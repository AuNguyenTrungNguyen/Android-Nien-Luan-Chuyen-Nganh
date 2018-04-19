package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.R;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder>{

    private Context context;
    private List<JobObject> listJobs;
    private int type;

    public StatisticAdapter(Context context, List<JobObject> listJobs, int type) {
        this.context = context;
        this.listJobs = listJobs;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_statistic, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final JobObject jobObject = listJobs.get(position);

        if (jobObject != null){

            holder.tvNameJob.setText(jobObject.getTitleJob());
            holder.tvDescription.setText(jobObject.getDescriptionJob());

            SharedPreferences preferences = context.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String idEmployee = preferences.getString(Constant.KEY_ID_EMPLOYEE_STATISTIC, null);

            if(idEmployee != null){
                for (int i = 0; i < jobObject.getListIdMember().size(); i++) {
                    if(idEmployee.equals(jobObject.getListIdMember().get(i).getIdMember())) {
                        String statusJob = jobObject.getListIdMember().get(i).getStatus();
                        String status = "";
                        if(type == Constant.TYPE_TOTAL){
                            status = statusJob.substring(0, statusJob.indexOf("/"));
                        }else if(type == Constant.TYPE_STATUS){
                            status = statusJob.substring(statusJob.indexOf("/")+1);
                        }

                        holder.tvState.setText(status);
                        break;
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameJob;
        private TextView tvState;
        private TextView tvDescription;

        ViewHolder(View itemView){
            super(itemView);
            tvNameJob = itemView.findViewById(R.id.tv_name_job);
            tvState = itemView.findViewById(R.id.tv_status);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
