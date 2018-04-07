package aunguyen.quanlycongviec.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;


public class EmployeeSelectedAdapter extends RecyclerView.Adapter<EmployeeSelectedAdapter.EmployeeSelectedViewHolder>{

    private Context context;
    private List<EmployeeObject> employeeObjectList;

    public EmployeeSelectedAdapter(Context context, List<EmployeeObject> employeeObjectList) {
        this.context = context;
        this.employeeObjectList = employeeObjectList;
    }

    @NonNull
    @Override
    public EmployeeSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_employee_selected, parent, false);
        return new EmployeeSelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeSelectedViewHolder holder, final int position) {

        EmployeeObject employeeObject = employeeObjectList.get(position);

        Glide.with(context)
                .load(employeeObject.getUrlAvatar())
                .into(holder.imgAvatar);

        holder.tvNameEmployee.setText(employeeObject.getNameEmployee());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeObjectList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeObjectList.size();
    }

    public class EmployeeSelectedViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNameEmployee;
        private ImageView imgAvatar;
        private Button btnRemove;

        public EmployeeSelectedViewHolder (View itemView){
            super(itemView);
            tvNameEmployee = itemView.findViewById(R.id.tv_name_employee);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

    }
}