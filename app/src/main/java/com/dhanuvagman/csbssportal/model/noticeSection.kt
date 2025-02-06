package com.dhanuvagman.csbssportal.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import com.dhanuvagman.csbssportal.pages.mesk

@SuppressLint("GetInstance")
fun sendSMS(context: Context, phoneNumber: String, message: String) {
    val SECRET_KEY=mesk
    val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    val encryptedBytes = cipher.doFinal(message.toByteArray())
    val enres = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
        != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(context, "SMS permission not granted", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, enres, null, null)
    } catch (_: Exception) {
    }
}

data class Event(
    var EventName:String,
    var Edate:String
    )

data class NoticeS(
    var NoticeName:String,
    var NoticeBy:String
)
data class Assignmentsdata(
    var Subject:String,
    var Assignment:String,
    var DueDate:String
)

data class Misc(
    var NoticeName:String,
    var NoticeBy:String
)

@SuppressLint("StaticFieldLeak")
private val db = FirebaseFirestore.getInstance()

private suspend fun <T> fetchCollection(
    collectionName: String,
    mapper: (document: Map<String, Any?>) -> T,
    default: T
): List<T> {
    return try {
        val snapshot = db.collection(collectionName).get().await()
        snapshot.documents.map { document ->
            mapper(document.data ?: emptyMap())
        }
    } catch (e: Exception) {
        Log.e("fetchCollection", "Error fetching $collectionName: ${e.message}")
        listOf(default)
    }
}
suspend fun getAllEvents(): List<Event> = fetchCollection(
    collectionName = "events",
    mapper = { data ->
        Event(
            EventName = data["Ename"] as? String ?: "",
            Edate = data["Edate"] as? String ?: ""
        )
    },
    default = Event("Oops!, Something Went Wrong!", "Today")
)

suspend fun getAllNotices(): List<NoticeS> = fetchCollection(
    collectionName = "notice",
    mapper = { data ->
        NoticeS(
            NoticeName = data["notice"] as? String ?: "",
            NoticeBy = data["by"] as? String ?: ""
        )
    },
    default = NoticeS("Oops!, Something Went Wrong", "Firebase")
)


suspend fun getAllmisc():List<Misc> = fetchCollection(
    collectionName = "AppUpdates",
    mapper = {data ->
        Misc(
            data["appud"] as String ?: "",
            data["appudby"] as String ?: ""
        )
    }, default = Misc("There Might Be An Error in Server","App Host")
)

suspend fun gettimetable(context: Context, myclass: String, dayname: String): List<String> {
    val sharedPreferences = context.getSharedPreferences("TimetableCache", Context.MODE_PRIVATE)
    val lastRequestDate = sharedPreferences.getString("last_request_date", null)
    val cachedTimetable = sharedPreferences.getStringSet("cached_timetable", null)
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return if (lastRequestDate == currentDate && cachedTimetable != null) {
        cachedTimetable.toList()
    } else {
        val snapshot = withContext(Dispatchers.IO) {
            db.collection(myclass).document("timetable").get().await()
        }
        val todaystime = snapshot.data?.get(dayname) as? Map<String, String>
        val timetable = todaystime?.values?.toList() ?: emptyList()
        sharedPreferences.edit()
            .putString("last_request_date", currentDate)
            .putStringSet("cached_timetable", timetable.toSet())
            .apply()

        timetable
    }
}

suspend fun getRandomQuote(context: Context): String {
//    val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
//    val storedQuote = sharedPref.getString("randomQuote", null)
//
//    if (storedQuote != null) {
//        return storedQuote
//    }

    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("quotes").get().await()
    val count = snapshot.size()

    if (count > 0) {
        val randomIndex = Random.nextInt(count)
        val randomQuoteSnapshot = db.collection("quotes")
            .orderBy(FieldPath.documentId())
            .limitToLast(count.toLong())
            .get()
            .await()

        val randomQuote = randomQuoteSnapshot.documents[randomIndex].getString("text") ?: "No quotes available"
//
//        with(sharedPref.edit()) {
//            putString("randomQuote", randomQuote)
//            apply()
//        }

        return randomQuote
    } else {
        return "No quotes available"
    }
}


fun uploadfeedback(messagefeed: String)
{
    val feedbackRef = db.collection("feedback")
    val feedbackData = hashMapOf(
        "feedbackText" to messagefeed,
        "timestamp" to FieldValue.serverTimestamp()
    )
    feedbackRef.add(feedbackData)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")

        }
        .addOnFailureListener {
            Log.w("Firestore", "Error adding document",)
}}

suspend fun getAllAssignments(context: Context, myclass: String): List<Assignmentsdata> {
    val sharedPreferences = context.getSharedPreferences("AssignmentsCache", Context.MODE_PRIVATE)
    val lastRequestDate = sharedPreferences.getString("last_request_date", null)
    val cachedAssignments = sharedPreferences.getString("cached_assignments", null)
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return if (lastRequestDate == currentDate && cachedAssignments != null) {
        deserializeAssignments(cachedAssignments)
    } else {
        val fetchedAssignments = fetchAssignmentsFromFirebase(myclass)
        sharedPreferences.edit()
            .putString("last_request_date", currentDate)
            .putString("cached_assignments", serializeAssignments(fetchedAssignments))
            .apply()

        fetchedAssignments
    }
}
private suspend fun fetchAssignmentsFromFirebase(myclass: String): List<Assignmentsdata> {
    val db = FirebaseFirestore.getInstance()
    val snapshot = withContext(Dispatchers.IO) {
        db.collection("${myclass}Assignment").get().await()
    }
    return snapshot.documents.map { document ->
        Assignmentsdata(
            Subject = document.getString("AssignmentSub") ?: "",
            Assignment = document.getString("AssignmentDes") ?: "",
            DueDate = document.getString("duedate") ?: ""
        )
    }.ifEmpty {
        listOf(Assignmentsdata("Subject", "No Document Found", "26/05/2005"))
    }
}

// Serialize the list of assignments into a string
private fun serializeAssignments(assignments: List<Assignmentsdata>): String {
    return assignments.joinToString(";") { "${it.Subject}|${it.Assignment}|${it.DueDate}" }
}

// Deserialize the string into a list of assignments
private fun deserializeAssignments(serializedData: String): List<Assignmentsdata> {
    return serializedData.split(";").map {
        val parts = it.split("|")
        Assignmentsdata(
            Subject = parts.getOrElse(0) { "" },
            Assignment = parts.getOrElse(1) { "" },
            DueDate = parts.getOrElse(2) { "" }
        )
    }
}

