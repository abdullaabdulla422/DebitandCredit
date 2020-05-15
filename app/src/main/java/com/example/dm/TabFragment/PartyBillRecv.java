package com.example.dm.TabFragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dm.R;
import com.example.dm.db.DatabaseHelper;
import com.example.dm.db.DbHandler;
import com.example.dm.recycler.BillAdapt;
import com.example.dm.recycler.Main;
import com.example.dm.recycler.MyDividerItemDecoration;
import com.example.dm.recycler.RecyclerTouchListener;
import com.example.dm.util.PartyBillRec;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class PartyBillRecv extends Fragment {

    public FloatingActionButton fab;
    public ListView listView;
    Dialog mDialogadd;
    private CustomAdapter customAdapter;
    ArrayList<PartyBillRec> processOrderListArray, processOrderlist;

    public ArrayList<PartyBillRec> billrec=new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    private DbHandler db;
    Button sort;
    int click=0;
    Button btnTotal;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parts_bill_recs, container, false);
        fab = view.findViewById(R.id.fab_add_party);
        btnTotal=view.findViewById(R.id.btnTotal);

        listView = view.findViewById(R.id.list_party_items);
        sort=view.findViewById(R.id.btnsort);

        db = new DbHandler(getActivity());
        billrec.addAll(db.getAll());
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double total=0;
                for(int i=0;i<billrec.size();i++){
                    total=total+Double.parseDouble(billrec.get(i).getAmt());
                    btnTotal.setText("total :"+total+" ");


                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                removeItemFromList(position,billrec.get(position) );


                return true;
            }
        });
        customAdapter=new CustomAdapter(getActivity(),billrec);
        listView.setAdapter(customAdapter);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==0){
                    Collections.sort(billrec, StringworkorderAscComparator);
                    click=1;
                }else if(click==1){
                    Collections.sort(billrec, StringworkorderDseComparator);
                    click=0;

                }


                customAdapter.notifyDataSetChanged();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((mDialogadd == null) || !mDialogadd.isShowing()) {
                    mDialogadd = new Dialog(getActivity());
                    mDialogadd.setCanceledOnTouchOutside(false);
                    mDialogadd.setCancelable(false);
                    mDialogadd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogadd.setContentView(R.layout.dialog_party_bill_rec);
                    final EditText edt_cust_name = mDialogadd.findViewById(R.id.edt_cust_name);
                    final EditText edt_amt = mDialogadd.findViewById(R.id.edt_amt);
                    final EditText edt_date = mDialogadd.findViewById(R.id.edt_date);
                    final EditText edt_mop = mDialogadd.findViewById(R.id.edt_mop);
                    Button btnSave = mDialogadd.findViewById(R.id.btnSave);
                    Button btnCancel = mDialogadd.findViewById(R.id.btnCancel);
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = "dd/MM/yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            edt_date.setText(sdf.format(myCalendar.getTime()));
                        }

                    };
                    edt_date.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(getActivity(), date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(TextUtils.isEmpty(edt_cust_name.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(TextUtils.isEmpty(edt_amt.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(TextUtils.isEmpty(edt_date.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(TextUtils.isEmpty(edt_mop.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                createNote(edt_cust_name.getText().toString(),
                                        edt_amt.getText().toString(),
                                        edt_date.getText().toString(),
                                        edt_mop.getText().toString());
                            }





                            mDialogadd.dismiss();


                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogadd.dismiss();
                        }
                    });
                    mDialogadd.show();


                }

            }
        });


        return view;
    }



    private void removeItemFromList(final int position, final PartyBillRec m) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};


        android.app.AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());
        alert.setTitle("Choose option");
        alert.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,final int which) {
                if (which == 0) {

                    if ((mDialogadd == null) || !mDialogadd.isShowing()) {
                        mDialogadd = new Dialog(getActivity());
                        mDialogadd.setCanceledOnTouchOutside(false);
                        mDialogadd.setCancelable(false);
                        mDialogadd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogadd.setContentView(R.layout.dialog_party_bill_rec);
                        final EditText edt_cust_name=mDialogadd.findViewById(R.id.edt_cust_name);
                        final EditText edt_amt=mDialogadd.findViewById(R.id.edt_amt);
                        final EditText edt_date=mDialogadd.findViewById(R.id.edt_date);
                        final EditText edt_mop=mDialogadd.findViewById(R.id.edt_mop);
                        edt_cust_name.setFocusable(false);
                        edt_cust_name.setText(m.getName());
                        edt_amt.setText(m.getAmt());
                        edt_date.setText(m.getDate());
                        edt_mop.setText(m.getMop());
                        Button btnSave=mDialogadd.findViewById(R.id.btnSave);
                        btnSave.setText("Update");
                        Button btnCancel=mDialogadd.findViewById(R.id.btnCancel);
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd/MM/yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                edt_date.setText(sdf.format(myCalendar.getTime()));
                            }

                        };


                        edt_date.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                new DatePickerDialog(getActivity(), date, myCalendar
                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });


                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                 if(TextUtils.isEmpty(edt_amt.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;

                                }else if(TextUtils.isEmpty(edt_date.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;

                                }else if(TextUtils.isEmpty(edt_mop.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;

                                }else {
                                   String name= edt_cust_name.getText().toString();
                                    String amt=  edt_amt.getText().toString();
                                    String date=  edt_date.getText().toString();
                                    String mop=   edt_mop.getText().toString();
                                    updateNote(name,amt,date,mop,position );
                                }








                                mDialogadd.dismiss();


                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialogadd.dismiss();
                            }
                        });
                        mDialogadd.show();




                    }

                } else {
                    removeItemFromList1(position);
//                    deleteNote(position);
                }
            }
        });

        alert.show();

    }



    private void createNote(String custName,String custAmt,String custDate
    ,String custMop) {

        PartyBillRec model = new PartyBillRec(custName
                , custAmt, custDate, custMop);
        model.setName(custName);
        model.setAmt(custAmt);
        model.setDate(custDate);
        model.setMop(custMop);
        db.BillDetails(getActivity(),model);
        db.GetBills().add(model);



        billrec.add(0,model);

            customAdapter.notifyDataSetChanged();

        }
    private void updateNote(String name,String amt,String date,String mop, int position) {
        PartyBillRec n = billrec.get(position);
        // updating note text
        n.setName(name);
        n.setAmt(amt);
        n.setDate(date);
        n.setMop(mop);

        // updating note in db
        db.updateBill(n);

        // refreshing the list

//            billrec.set(position,n);
        for(position=0;position<billrec.size();position++){

            customAdapter.notifyDataSetChanged();
        }

//            customAdapter.notifyDataSetChanged();



//        toggleEmptyNotes();
    }

    private void removeItemFromList1(final int position) {


        android.app.AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub
                deleteNote(position);


            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }
    private void deleteNote(int position) {
        // deleting the note from db
        db.DeleteUser(billrec.get(position));

        // removing the note from the list
        billrec.remove(position);
        customAdapter.notifyDataSetChanged();


    }


    public class CustomAdapter extends BaseAdapter {
        ArrayList<PartyBillRec> result;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<PartyBillRec> list) {
            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            int size = 0;
            if (result != null) {
                size = result.size();
            }

            return size;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.list_receive, null);

            holder.edtname = (TextView) rowView.findViewById(R.id.customer_name);
            holder.edtamt = (TextView) rowView.findViewById(R.id.customer_amt);
            holder.edtdate = (TextView) rowView.findViewById(R.id.date);
            holder.edtmop = (TextView) rowView.findViewById(R.id.mop);


            holder.edtname.setText(result.get(position).getName());
            holder.edtamt.setText(result.get(position).getAmt());
            holder.edtdate.setText(result.get(position).getDate());
            holder.edtmop.setText(result.get(position).getMop());


            return rowView;
        }

        public class Holder {
            TextView edtname, edtamt, edtdate, edtmop;
        }







    }
    public static Comparator<PartyBillRec> StringworkorderAscComparator =
            new Comparator<PartyBillRec>() {

                public int compare(PartyBillRec app1,
                                   PartyBillRec app2) {

                    PartyBillRec stringName1 = app1;
                    PartyBillRec stringName2 = app2;

                    return stringName1.getDate().compareToIgnoreCase(
                            stringName2.getDate());
                }
            };
    public static Comparator<PartyBillRec> StringworkorderDseComparator =
            new Comparator<PartyBillRec>() {

                public int compare(PartyBillRec app1,
                                   PartyBillRec app2) {

                    PartyBillRec stringName1 = app1;
                    PartyBillRec stringName2 = app2;

                    return stringName2.getDate().compareToIgnoreCase(
                            stringName1.getDate());
                }
            };

}





