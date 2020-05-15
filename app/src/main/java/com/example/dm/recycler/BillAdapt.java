package com.example.dm.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dm.R;
import com.example.dm.util.PartyBillRec;

import java.util.List;

public class BillAdapt extends RecyclerView.Adapter<BillAdapt.MyViewHolder> {
    private Context context;
    private List<PartyBillRec> partyList;
    public BillAdapt(Context context, List<PartyBillRec> partsList) {
        this.context = context;
        this.partyList = partsList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_receive, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PartyBillRec partyBillRec=partyList.get(position);
        holder.edtname.setText(partyBillRec.getName());
        holder.edtamt.setText(partyBillRec.getAmt());
        holder.edtdate.setText(partyBillRec.getDate());
        holder.edtmop.setText(partyBillRec.getMop());
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView edtname, edtamt, edtdate, edtmop;

        public MyViewHolder(View view) {
            super(view);
            edtname = view.findViewById(R.id.customer_name);
            edtamt = view.findViewById(R.id.customer_amt);
            edtdate = view.findViewById(R.id.date);
            edtmop = view.findViewById(R.id.mop);
        }
    }
}
