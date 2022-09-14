package com.example.studentinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var myDB: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnGet = findViewById<Button>(R.id.btnGet)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tfId = findViewById<TextView>(R.id.txtId)
        val tfName = findViewById<TextView>(R.id.txtName)
        val tfProg = findViewById<TextView>(R.id.txtProg)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val tfEmail = findViewById<TextView>(R.id.txtEmail)
        val tfPass = findViewById<TextView>(R.id.txtPass)
        myDB = FirebaseDatabase.getInstance()
        val myRef = myDB.getReference("Student")
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login(tfEmail.text.toString(), tfPass.text.toString())
            //register("modassignmentuse@gmail.com", "12345678")
        }

        btnGet.setOnClickListener {
            myRef.child("W123").get()
                .addOnSuccessListener { record ->
                    tfId.text = record.child("studentId").value.toString()
                    tfName.text = record.child("studentName").value.toString()
                    tfProg.text = record.child("programme").value.toString()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                }
        }

        btnSave.setOnClickListener {
            val studentId = tfId.text.toString()
            val studentName = tfName.text.toString()
            val studentProg = tfProg.text.toString()

            val student = Student(studentId, studentName, studentProg)

            myRef.child(student.studentId).setValue(student)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Record Added", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                }

        }
    }

    override fun onStop() {
        super.onStop()
        signOut()
    }

    fun register(email: String, psw: String) {

        auth.createUserWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser
                Toast.makeText(applicationContext, "User Registered", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun login(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser
                Toast.makeText(applicationContext, "User Logged In", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()

    }
}