package com.example.ass8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab.lab7dialogrv.Employee
import com.lab.lab7dialogrv.ViewHolder

class StudentsAdapter(val item: ArrayList<Employee>, val context: Context): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view_item = LayoutInflater.from(parent.context).inflate(R.layout.std_item_layout, parent, false)
        return ViewHolder(view_item)
    }

    override fun getItemCount(): Int {
       return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = item[position].emp_name
        holder.tvGender.text = item[position].emp_gender
        holder.tvEmail.text = item[position].emp_email
        holder.tvSalary.text = item[position].emp_salary.toString()

    }
}