package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aunguyen.quanlycongviec.R;

/**
 * Created by Sang on 17/03/2018.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{

    private Context context;
    private String[] nameJob;
    private String[] stateJob;
    private String[] amountEmployee;


    //test
    private String[] stt;

    public JobAdapter(Context context,String[] stt){
        this.context = context;
        this.stt = stt;
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
        holder.tvNameJob.setText("Công việc "+stt[position]);
        /*holder.tvAmount.setText("Số Lượng: "+stt[position]);
        holder.cvJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailJobActivity.class);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return stt.length;
    }
    public class JobViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameJob;
        private TextView tvState;
        private TextView tvAmount;
        private CardView cvJob;
        public JobViewHolder (View itemView){
            super(itemView);
            tvNameJob = itemView.findViewById(R.id.tv_name_job);
            tvState = itemView.findViewById(R.id.tv_state);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            cvJob = itemView.findViewById(R.id.cv_job);
        }
    }
}
