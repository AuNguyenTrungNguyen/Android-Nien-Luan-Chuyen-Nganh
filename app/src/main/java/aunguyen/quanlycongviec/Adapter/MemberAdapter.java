package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.Object.StatusJob;
import aunguyen.quanlycongviec.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{

    private Context context;
    private List<StatusJob> listStatusJobs;
    private FirebaseDatabase database;

    public MemberAdapter(Context context, List<StatusJob> listStatusJobs) {
        this.context = context;
        this.listStatusJobs = listStatusJobs;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberViewHolder holder, int position) {
        final StatusJob statusJob = listStatusJobs.get(position);

        if (statusJob != null){
            String idMember = statusJob.getIdMember();

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_NHAN_VIEN).child(idMember);
            myRef.addValueEventListener(new ValueEventListener() {
                EmployeeObject employeeObject;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    employeeObject = dataSnapshot.getValue(EmployeeObject.class);
                    holder.tvNameEmployee.setText(employeeObject.getNameEmployee());
                }
                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


            String status[] = statusJob.getStatus().split("/");
            holder.tvState.setText(status[0]);
            holder.tvDeadline.setText(status[1]);

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
            }else if (status[1].equals(Constant.EARLY_DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobEarly), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobEarly), 0);
            }else if (status[1].equals(Constant.DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobDeadline), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobDeadline), 0);
            }else if (status[1].equals(Constant.PAST_DEADLINE)) {
                holder.timeMarker.setStartLine(context.getResources().getColor(R.color.jobPast), 0);
                holder.timeMarker.setEndLine(context.getResources().getColor(R.color.jobPast), 0);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listStatusJobs.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameEmployee;
        private TextView tvState;
        private TextView tvDeadline;
        private TimelineView timeMarker;

        public MemberViewHolder(View itemView) {
            super(itemView);
            tvNameEmployee = itemView.findViewById(R.id.tv_name_employee);
            tvState = itemView.findViewById(R.id.tv_status);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            timeMarker = itemView.findViewById(R.id.time_marker);
        }
    }
}
