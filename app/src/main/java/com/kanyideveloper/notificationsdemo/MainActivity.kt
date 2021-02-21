package com.kanyideveloper.notificationsdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.kanyideveloper.notificationsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "channelID"
    private val CHANNEL_NAME = "channelName"
    private val NOTIFICATION_ID = 0
    private val KEY_REPLY = "key_reply"
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.notificationButton.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {

        //Opening the Notification in another activity
        val intent = Intent(this, SecondActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Reply action
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert your name here...")
            build()
        }
        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "REPLY", pendingIntent).addRemoteInput(remoteInput).build()

        //Action Buttons in Notification --- Home
        val intent2 = Intent(this, HomeActivity::class.java)
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        val action1: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "Home", pendingIntent2).build()

        //Action Buttons in Notification --- Setting
        val intent3 = Intent(this, SettingsActivity::class.java)
        val pendingIntent3: PendingIntent = PendingIntent.getActivity(this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT)
        val action2: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "Settings", pendingIntent3).build()

        //Building the Notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Jetpack Notifications are Awesome")
                .setContentText("Today I have learnt Notifications in Android")
                .setSmallIcon(R.drawable.ic_baseline_bug_report_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setContentIntent(pendingIntent)

                //Clicking on an action button takes you to its respective activity
                .addAction(action1)
                .addAction(action2)

                //Click on this action enables you to reply directly on the notification bar
                .addAction(replyAction)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager!!.createNotificationChannel(channel)
        }
    }
}