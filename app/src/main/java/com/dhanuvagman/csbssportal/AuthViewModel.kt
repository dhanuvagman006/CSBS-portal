package com.dhanuvagman.csbssportal

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        chekAuthState()
    }

    fun login(email: String, password: String, context: Context) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    //fetchUserDetails(context)
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something Went Wrong")

                }
            }
    }

    fun SignUp(
        email: String,
        password: String,
        username: String,
        phoneNumber: String,
        usn: String,
        context: Context
    ) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // val userId = task.result.user?.uid
                    val user = hashMapOf(
                        "username" to username,
                        "phoneNumber" to phoneNumber,
                        "email" to email,
                        "usn" to usn
                    )

                    Firebase.firestore
                        .collection(usn.substring(3, 7).uppercase() + "Students")
                        .document(usn.uppercase())
                        .set(user)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun chekAuthState() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }


    fun fetchUserDetails(context: Context) {
        val currentUserEmail = auth.currentUser?.email
        if (currentUserEmail == null) {
            _authState.value = AuthState.Error("No logged-in user found.")
            return
        }

        Firebase.firestore.collection(currentUserEmail.substring(3, 7).uppercase() + "Students")
            .whereEqualTo("Document ID", currentUserEmail.substring(0, 10))
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userData = document.data
                    val username = userData["username"] as? String ?: "Unknown"
                    val phoneNumber = userData["phoneNumber"] as? String ?: "Unknown"
                    val usn = userData["usn"] as? String ?: "Unknown"

                    val sharedPreferences =
                        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val userinfo = sharedPreferences.edit()
                    userinfo.putString("username", username)
                    userinfo.putString("usn", usn.uppercase())
                    userinfo.putString("email", currentUserEmail)
                    userinfo.putString("phone", phoneNumber)
                    userinfo.putString(
                        "imgurl",
                        "https://img.freepik.com/free-photo/portrait-smiling-boy-helmet-sunglasses-3d-rendering_1142-41369.jpg"
                    )
                    userinfo.apply()
                }
            }
            .addOnFailureListener { exception ->
                _authState.value =
                    AuthState.Error("Failed to fetch user details: ${exception.message}")

            }
    }
}

sealed class AuthState() {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}