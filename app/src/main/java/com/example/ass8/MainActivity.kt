package com.example.ass8

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.lab.lab7dialogrv.Employee
import com.example.ass8.EmployeeAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val empList = arrayListOf<Employee>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recycler_view.adapter = EmployeeAdapter(this.empList, applicationContext)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callEmployeedata()
    }

    fun callEmployeedata(){
        empList.clear()
        val serv: EmpAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmpAPI::class.java).apply {

                retrieveEmployee()
                    .enqueue(object : Callback<List<Employee>> {

                        override fun onResponse(
                            call: Call<List<Employee>>,
                            response: Response<List<Employee>>
                        ) {
                            response.body()?.forEach{
                                empList.add(Employee(it.emp_name, it.emp_gender, it.emp_email, it.emp_salary))

                            }
                            recycler_view.adapter = EmployeeAdapter(empList, applicationContext)

                        }

                        override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                            return t.printStackTrace()
                        }
                    })
            }
    }

    fun addStudent(view: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_dialog_layout, null)
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.setView(mDialogView)
        val mAlertDialog = myBuilder.show()
        mAlertDialog.btnAdd.setOnClickListener(){
            var gender = ""
            if(mAlertDialog.radioMale.isChecked){
                gender = "Male"
            }else if(mAlertDialog.radioFemale.isChecked){
                gender = "Female"
            }
            empList.add(Employee(mAlertDialog.edt_name.text.toString(), gender.toString(), mAlertDialog.edt_email.text.toString(), mAlertDialog.edt_salary.text.toString().toFloat()))
            val serv: EmpAPI = Retrofit.Builder()
                .baseUrl( "http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmpAPI::class.java)
            serv.insertEmp(
                mAlertDialog.edt_name.text.toString(),
                gender.toString(),
                mAlertDialog.edt_email.text.toString(),
                mAlertDialog.edt_salary.text.toString().toFloat()).enqueue(object :Callback<Employee>{
                override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                    if (response.isSuccessful()) {
                        Toast.makeText(applicationContext, "Successfully Inserted", Toast.LENGTH_LONG)

                    } else {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Employee>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
            recycler_view.adapter?.notifyDataSetChanged()
            Toast.makeText(applicationContext, "The Employee is added successfully", Toast.LENGTH_LONG).show()
            mAlertDialog.dismiss()
        }
        mAlertDialog.btnCancel.setOnClickListener(){
            mAlertDialog.dismiss()
        }
    }
}