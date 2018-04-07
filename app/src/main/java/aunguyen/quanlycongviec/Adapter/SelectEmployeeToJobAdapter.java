package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;


public class SelectEmployeeToJobAdapter extends RecyclerView.Adapter<SelectEmployeeToJobAdapter.SelectEmployeeToJobViewHolder>{

    private Context context;
    private List<EmployeeObject> employeeObjectList;
    private List<Boolean> listCheck;

    public SelectEmployeeToJobAdapter(Context context, List<EmployeeObject> employeeObjectList, List<Boolean> listCheck) {
        this.context = context;
        this.employeeObjectList = employeeObjectList;
        this.listCheck = listCheck;
    }

    @NonNull
    @Override
    public SelectEmployeeToJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_select_employee_to_job, parent, false);
        return new SelectEmployeeToJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectEmployeeToJobViewHolder holder, final int position) {

        EmployeeObject employeeObject = employeeObjectList.get(position);

        Glide.with(context)
                .load(employeeObject.getUrlAvatar())
                .into(holder.imgAvatar);

        holder.tvNameEmployee.setText(employeeObject.getNameEmployee());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = listCheck.get(position);

                if(isCheck){
                    listCheck.set(position, false);
                    holder.cvSelected.setCardBackgroundColor(Color.WHITE);
                }else{
                    listCheck.set(position, true);
                    holder.cvSelected.setCardBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeObjectList.size();
    }

    public class SelectEmployeeToJobViewHolder extends RecyclerView.ViewHolder{

        private CardView cvSelected;
        private TextView tvNameEmployee;
        private ImageView imgAvatar;

        public SelectEmployeeToJobViewHolder (View itemView){
            super(itemView);
            cvSelected = itemView.findViewById(R.id.cv_selected);
            tvNameEmployee = itemView.findViewById(R.id.tv_name_employee);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }

    }
}