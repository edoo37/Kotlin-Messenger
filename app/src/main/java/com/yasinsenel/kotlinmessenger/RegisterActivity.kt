package com.yasinsenel.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

var selectedphotoUri : Uri? = null

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register.setOnClickListener {
            performRegister()
        }


        textView_register.setOnClickListener {
            val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)


        }

        selectphoto_button.setOnClickListener {
            Log.d("Main","You have clicked select image button")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    fun uploadimagetoFirebaseDatabase(){
        if (selectedphotoUri==null) return
        val filename = UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedphotoUri!!).addOnSuccessListener { taskSnapshot ->
            Log.d("RegisterActivity","Successfully upload image: ${taskSnapshot.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("RegisterActivity","File Location: $it")
                saveUsertoFirebaseDatabase(it.toString())

            }
        }


    }

    fun performRegister(){

        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(this,"Please enter text in email/pw.",Toast.LENGTH_SHORT).show()
        }

        else if (selectedphotoUri==null){

            Toast.makeText(this,"Please select an image.",Toast.LENGTH_SHORT).show()
        }
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else
                    Log.d("Main", "Created: ${it.result?.user?.uid}")
                    Log.d("Selam","App çalışıyor")
                    val intent = Intent(applicationContext,LatestMessagesActivity::class.java)
                    startActivity(intent)

                    uploadimagetoFirebaseDatabase()

                }
                .addOnFailureListener {
                    Log.d("Main","Failed create user ${it.message}")
                }

        }

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode == Activity.RESULT_OK && data!=null){

            selectedphotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphotoUri)
            selectPhoto_imageView.setImageBitmap(bitmap)
            selectphoto_button.alpha = 0f
        }

    }

    private fun saveUsertoFirebaseDatabase(profileImageUrl : String){

        val uid = FirebaseAuth.getInstance().uid!!
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,username_edittext_register.text.toString(),profileImageUrl)

        ref.setValue(user)
    }

}

