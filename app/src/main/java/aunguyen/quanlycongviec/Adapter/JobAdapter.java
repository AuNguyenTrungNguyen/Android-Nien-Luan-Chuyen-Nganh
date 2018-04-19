package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import aunguyen.quanlycongviec.Activity.DetailJobActivity;
import aunguyen.quanlycongviec.Activity.DetailJobEmployeeActivity;
import aunguyen.quanlycongviec.Activity.MainActivity;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.R;

/**
 * Created by Sang on 17/03/2018.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

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
        SharedPreferences preferences = context.getSharedPreferences(Constant.PREFERENCE_NAME, context.MODE_PRIVATE);
        final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

        if (jobObject != null) {
            holder.tvNameJob.setText(jobObject.getTitleJob());
            holder.tvState.setText(jobObject.getStatusJob());
            holder.tvAmount.setText("Số lượng: " + jobObject.getListIdMember().size());

            String status[] = jobObject.getStatusJob().split("/");

            if (status[0].equals(Constant.RECEIVED)) {
                holder.timeMarker.setMarkerColor(context.getResources().getColor(R.color.jobReceive));
            } else {
                holder.timeMarker.setMarkerColor(context.getResources().getColor(R.color.jobNotReceive));
            }

            if (status[1].equals(Constant.COMPLETE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobComplete), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobComplete), 0);
            } else if (status[1].equals(Constant.STILL_DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobStill), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobStill), 0);
            } else if (status[1].equals(Constant.EARLY_DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobEarly), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobEarly), 0);
            } else if (status[1].equals(Constant.DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobDeadline), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobDeadline), 0);
            } else if (status[1].equals(Constant.PAST_DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobPast), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobPast), 0);
            }

            holder.cvJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_NHAN_VIEN).
                            child(id);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent intent;
                            EmployeeObject employeeObject = dataSnapshot.getValue(EmployeeObject.class);

                            if (employeeObject.getAccountType().equals("1")) {
                                intent = new Intent(context, DetailJobEmployeeActivity.class);
                            } else {
                                if(context.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
                                    intent = new Intent(context, DetailJobActivity.class);
                                }else{
                                    intent = new Intent(context, DetailJobEmployeeActivity.class);
                                }
                            }


                            intent.putExtra("IDJob", jobObject.getIdJob());
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameJob;
        private TextView tvState;
        private TextView tvAmount;
        private CardView cvJob;
        private TimelineView timeMarker;

        public JobViewHolder(View itemView) {
            super(itemView);
            tvNameJob = itemView.findViewById(R.id.tv_name_job);
            tvState = itemView.findViewById(R.id.tv_status);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            cvJob = itemView.findViewById(R.id.cv_job);
            timeMarker = itemView.findViewById(R.id.time_marker);
        }
    }
}
