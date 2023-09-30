package com.bangkit.cloudraya.websocket

import android.app.NotificationChannel

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bangkit.cloudraya.MainActivity
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.utils.ACTION_CONFIRMATION
import com.bangkit.cloudraya.utils.NOTIFICATION_CHANNEL_ID
import com.bangkit.cloudraya.utils.NOTIFICATION_ID
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject


class WebSocketService : Service() {

    private lateinit var socket: Socket
    private var listChannel: ArrayList<String> = ArrayList()
    private var channelKey: String = ""
    private val context: Context by inject()

    override fun onCreate() {
        super.onCreate()
        joinConnection()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        joinConnection()
        for (i in listChannel) {
            channelKey = i
            joinConnection()
        }

    }

    private fun joinConnection() {
        try {
            socket = IO.socket("http://202.43.248.187:3002/")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Mendengarkan peristiwa 'notification'
        socket.on("notification", onNotification)
        Log.d("Testing", "Sedang Berjalan")
        // Menghubungkan socket
        socket.connect()
    }


    private val onNotification = Emitter.Listener { args ->
        val response = args[0] as JSONObject
        val message = response.getString("message")
        Log.d("Testing", channelKey)
        Log.d("Testing", listChannel.toString())
        try {
            val messageJson = JSONObject(message)
            getData(messageJson)
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("Testing", "Error parsing JSON", e)
        }
    }

    private fun getData(messageJson: JSONObject) {
        val data = messageJson.getJSONObject("data")
        val notification = data.getJSONObject("notification")
        val notifData = data.getJSONObject("notif_data")
        val title = notification.getString("title")
        val body = notification.getString("body")
        val vmId = notifData.getString("vm_id")
        val bearerToken = notifData.getString("bearer_token")
        val request = notifData.getString("request")
        val site = notifData.getString("site")
        val dataHolder: DataHolder by inject()
        dataHolder.token = bearerToken.toString()
        dataHolder.request = request.toString()
        dataHolder.vmId = vmId.toString()
        dataHolder.siteUrl = site.toString()
        dataHolder.action = ACTION_CONFIRMATION
        notification(title, body)
    }

    private fun notification(title: String, body: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = ACTION_CONFIRMATION// Tambahkan aksi untuk mengarahkan ke fragment
        // Kirim intent ke MainActivity melalui LocalBroadcastManager
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID, // Gantilah dengan kode request yang sesuai
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        startForeground(1, notificationBuilder.build())
    }

    fun stopWebSocketService() {
        socket.disconnect()
        stopSelf()
    }

    fun joinChannel() {
        // Pastikan socket sudah diinisialisasi sebelum menggunakan
        if (::socket.isInitialized) {
            val dataHolder: DataHolder by inject()
            channelKey = dataHolder.channelKey
            listChannel.add(channelKey)
            socket.emit("joinChannel", channelKey)
            Log.d("Testing", "Berhasil masuk channel $channelKey")
        } else {
            // Penanganan jika socket belum diinisialisasi
            Log.e("Testing", "Socket has not been initialized")
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): WebSocketService = this@WebSocketService
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    enum class Actions {
        START, STOP
    }
}