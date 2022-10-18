package com.asusoft.chatapp.util.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.chatting.ChattingActivity
import com.asusoft.chatapp.activity.login.LoginActivity
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.api.rx.member.MemberService
import com.asusoft.chatapp.util.eventbus.GlobalBus
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        val id = LoginActivity.myInfo?.id
        if (id != null && token != null) {
            MemberService.uploadFcmToken(id, token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val json = remoteMessage.data["dto"]

            val objectMapper = ObjectMapper()
            val dto = objectMapper.readValue(json, ChattingReadDto::class.java)

            if (ChattingActivity.chatroomId == dto.chatRoomId) {
                val map = HashMap<String, Any>()
                map[FCMService.toString()] = FCMService.toString()
                map["title"] = title ?: ""
                map["message"] = message ?: ""
                map["dto"] = dto
                GlobalBus.post(map)
            } else {
                sendNotification(title!!, message!!, 0)
            }
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }


    private fun sendNotification(title: String, text: String, requestId: Int){
        /*


        TODO: - 채팅 방 id 가 같은 경우
                현재 채팅방이면 알림창 띄우지 않고 채팅을 바로 갱신하기

        TODO: - 채팅 방 id 가 다른 경우
                알림 클릭 시 해당 채팅방으로 이동 - (자동 로그인 부터 구현하기)

         */

//        val intent = Intent(this, CardRequestActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE)

        val channelId = getString(R.string.google_app_id)
        val channelName = getString(R.string.app_name)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentText(text)
            .setContentTitle(title)
//            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_chat_bubble_24)
        // create notification channel
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}