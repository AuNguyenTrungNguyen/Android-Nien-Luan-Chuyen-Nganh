package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.R;

/**
 * Created by Sang on 17/03/2018.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{

    private Context context;
    private List<JobObject> listJobs;

    public JobAdapter(Context context, List<JobObject> listJobs) {
        this.context = context;
        this.listJobs = listJobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        final JobObject jobObject = listJobs.get(position);

        if (jobObject != null){
            holder.tvNameJob.setText(jobObject.getTitleJob());
            holder.tvState.setText(jobObject.getStatusJob());
            holder.tvAmount.setText("Số lượng: " + jobObject.getListIdMember().size());
            holder.cvJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, jobObject.getDescriptionJob(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }
    public class JobViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNameJob;
        private TextView tvState;
        private TextView tvAmount;
        private CardView cvJob;
        public JobViewHolder (View itemView){
            super(itemView);
            tvNameJob = itemView.findViewById(R.id.tv_name_job);
            tvState = itemView.findViewById(R.id.tv_status);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            cvJob = itemView.findViewById(R.id.cv_job);
        }
    }
}
