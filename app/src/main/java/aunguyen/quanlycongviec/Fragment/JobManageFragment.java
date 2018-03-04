package aunguyen.quanlycongviec.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.R;

/***
 * Created by Au Nguyen on 3/4/2018.
 */

public class JobManageFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton btnAddJob;
    private View view;

    private RecyclerView rvJob;
    private List<String> listJobs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_manage, container, false);

        btnAddJob = view.findViewById(R.id.btnAddJob);
        rvJob = view.findViewById(R.id.rvJob);

        btnAddJob.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddJob:
                Toast.makeText(getContext(), "Add Job", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
