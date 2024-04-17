package com.example.navbotdialog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView listView;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListAdapter listAdapter;
    ListData listData;
    private MyDBHelper dbHelper;

    public HomeFragment(MyDBHelper dbH) {
        // Required empty public constructor
        this.setDBHelper(dbH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.listview);

        // Obtain writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // get the passed arguments for searching
        String information = getArguments().getString("information");
        String filterBy = getArguments().getString("filterBy");

        // get employees depending on the searching arguments
        ArrayList<Employee> employees;
        employees = getEmployees(information, filterBy);


        // Iterate over filteredEmployees and assign values to variables
        for (Employee employee : employees) {
            // send the fields of each employee into listData
            listData = new ListData(employee.getFirstname(), employee.getLastname(), employee.getDateOfBirth(), employee.getEmail(),
                    employee.getImage(), employee.getNumberPhone(), employee.getAddress(), employee.getJob(), employee.getMat());
            dataArrayList.add(listData);
        }

        // Set the adapter for listView
        listAdapter = new ListAdapter(requireActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        // Set item click listener for listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the clicked item's data
                ListData clickedItem = dataArrayList.get(i);

                // Create an intent to start DetailedActivity and pass the clicked item's data
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("Firstname", clickedItem.firstname);
                intent.putExtra("Lastname", clickedItem.lastname);
                intent.putExtra("Date of birth", clickedItem.dateOfBirth);
                intent.putExtra("Number phone", clickedItem.numberPhone);
                intent.putExtra("Email", clickedItem.email);
                intent.putExtra("Image", clickedItem.image);
                intent.putExtra("Address", clickedItem.address);
                intent.putExtra("Matricule", clickedItem.mat);
                intent.putExtra("Job", clickedItem.job);

                // Start DetailedActivity
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public ArrayList<Employee> getEmployees(String information, String filterBy)
    {
        ArrayList<Employee> employees = null;
        if (information.isEmpty())
            employees = dbHelper.filterEmployees("", "");
        else
        {
            if (filterBy.equals("Matricule"))
                employees = dbHelper.filterEmployees(information, "mat");
            else if (filterBy.equals("Name"))
                employees = dbHelper.filterEmployees(information, "name");
            else if (filterBy.equals("Email"))
                employees = dbHelper.filterEmployees(information, "email");
            else if (filterBy.equals("Phone"))
                employees = dbHelper.filterEmployees(information, "numberPhone");
            else if (filterBy.equals("Job"))
                employees = dbHelper.filterEmployees(information, "job");
            else if (filterBy.equals("Address"))
                employees = dbHelper.filterEmployees(information, "address");
        }
        return employees;
    }

    public void setDBHelper(MyDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
