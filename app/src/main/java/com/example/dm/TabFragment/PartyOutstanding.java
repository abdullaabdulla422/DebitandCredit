package com.example.dm.TabFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dm.R;
import com.example.dm.db.DbHandler;
import com.example.dm.util.PartiesOutstandingObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class PartyOutstanding extends Fragment {

    public FloatingActionButton fab;
    public ListView listView;
    Dialog mDialogadd;
    private CustomAdapter adapter;
     public ArrayList<PartiesOutstandingObject>equip_list=new ArrayList<>() ;


    final Calendar myCalendar = Calendar.getInstance();


    private  DbHandler db;
    Button sort,btnTotal;
    int click=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parts_outstanding, container, false);
        fab=view.findViewById(R.id.fab_add_party);
        sort=view.findViewById(R.id.btnsort);
        btnTotal=view.findViewById(R.id.btnTotal);

        listView=view.findViewById(R.id.list_party_items);
         db = new DbHandler(getActivity());
        equip_list.addAll(db.getAllBills());
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double total=0.0;
                for(int i=0;i<equip_list.size();i++){
                    total=total+Double.parseDouble(equip_list.get(i).getAmt());
                    btnTotal.setText("total :"+total+" ");


                }

            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==0){
                    Collections.sort(equip_list, StringworkorderAscComparator);
                    click=1;
                }else if(click==1){
                    Collections.sort(equip_list, StringworkorderDseComparator);
                    click=0;

                }



                adapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                removeItemFromList(position,equip_list.get(position) );


                return true;
            }
        });
        adapter = new CustomAdapter(getActivity(), equip_list);
        listView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mDialogadd == null) || !mDialogadd.isShowing()) {
                    mDialogadd = new Dialog(getActivity());
                    mDialogadd.setCanceledOnTouchOutside(false);
                    mDialogadd.setCancelable(false);
                    mDialogadd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogadd.setContentView(R.layout.dialog_part_outstanding);
                    final EditText edt_cust_name=mDialogadd.findViewById(R.id.edt_cust_name);
                    final EditText edt_amt=mDialogadd.findViewById(R.id.edt_amt);
                    final EditText edt_date=mDialogadd.findViewById(R.id.edt_date);
                    Button btnSave=mDialogadd.findViewById(R.id.btnSave);
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

                            if(TextUtils.isEmpty(edt_cust_name.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;
                            }else if(TextUtils.isEmpty(edt_amt.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;

                            }else if(TextUtils.isEmpty(edt_date.getText().toString())){
                                Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                return;

                            }else {
                                createNote(edt_cust_name.getText().toString(),
                                        edt_amt.getText().toString(),edt_date.getText().toString() );
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



    private void createNote(String custName,String custAmt,String custDate) {

        PartiesOutstandingObject model=new PartiesOutstandingObject( custName,custAmt,custDate);
        model.setName(custName);
        model.setAmt(custAmt);
        model.setDate(custDate);
//        equip_list.add(model);
        Log.d("TAG", "onClick: "+equip_list);
        db.insertUserDetails(getActivity(),model);
        db.GetUsers().add(model);


        // inserting note in db and getting
        // newly inserted note id

        // get the newly inserted note from db

        // adding new note to array list at 0 position
        equip_list.add(0, model);

        // refreshing the list
        adapter.notifyDataSetChanged();


    }
    private void deleteNote(int position) {
        // deleting the note from db
        db.DeleteUser(equip_list.get(position));

        // removing the note from the list
        equip_list.remove(position);
        adapter.notifyDataSetChanged();


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
    private void removeItemFromList(final int position, final PartiesOutstandingObject m) {
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
                        mDialogadd.setContentView(R.layout.dialog_part_outstanding);
                        final EditText edt_cust_name=mDialogadd.findViewById(R.id.edt_cust_name);
                        final EditText edt_amt=mDialogadd.findViewById(R.id.edt_amt);

                        final EditText edt_date=mDialogadd.findViewById(R.id.edt_date);
                        edt_cust_name.setFocusable(false);
                        edt_cust_name.setText(m.getName());
                        edt_amt.setText(m.getAmt());
                        edt_date.setText(m.getDate());
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

                                if(TextUtils.isEmpty(edt_cust_name.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;
                                }else if(TextUtils.isEmpty(edt_amt.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;

                                }else if(TextUtils.isEmpty(edt_date.getText().toString())){
                                    Toast.makeText(getActivity(), "please enter all datas", Toast.LENGTH_SHORT).show();
                                    return;

                                }else {
                                    String name=edt_cust_name.getText().toString();
                                    String amt= edt_amt.getText().toString();
                                    String date=edt_date.getText().toString();

                                    updateNote(name,amt,date,position );
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

    private void updateNote(String name, String amt, String date, int position) {
        PartiesOutstandingObject n = equip_list.get(position);
        // updating note text
        n.setName(name);
        n.setDate(date);
        n.setAmt(amt);

        // updating note in db
        db.updateNote(n);
        for(position=0;position<equip_list.size();position++){
            adapter.notifyDataSetChanged();
        }

        // refreshing the list


//        toggleEmptyNotes();
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<PartiesOutstandingObject> result;

        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<PartiesOutstandingObject> list) {
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

        @SuppressLint("LongLogTag")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.list_data, null);

            holder.edtname = (TextView) rowView.findViewById(R.id.customer_name);
            holder.edtamt = (TextView) rowView.findViewById(R.id.customer_amt);
            holder.edtdate = (TextView) rowView.findViewById(R.id.date);

            holder.edtname.setText(result.get(position).getName());
            holder.edtamt.setText(result.get(position).getAmt());
            holder.edtdate.setText(result.get(position).getDate());




            return rowView;
        }

        public class Holder {
            TextView edtname,edtamt,edtdate;
        }

    }


    public static Comparator<PartiesOutstandingObject> StringworkorderAscComparator =
            new Comparator<PartiesOutstandingObject>() {

                public int compare(PartiesOutstandingObject app1,
                                   PartiesOutstandingObject app2) {

                    PartiesOutstandingObject stringName1 = app1;
                    PartiesOutstandingObject stringName2 = app2;

                    return stringName1.getDate().compareToIgnoreCase(
                            stringName2.getDate());
                }
            };
    public static Comparator<PartiesOutstandingObject> StringworkorderDseComparator =
            new Comparator<PartiesOutstandingObject>() {

        public int compare(PartiesOutstandingObject app1,
                           PartiesOutstandingObject app2) {

            PartiesOutstandingObject stringName1 = app1;
            PartiesOutstandingObject stringName2 = app2;

            return stringName2.getDate().compareToIgnoreCase(
                    stringName1.getDate());
        }
    };



}
