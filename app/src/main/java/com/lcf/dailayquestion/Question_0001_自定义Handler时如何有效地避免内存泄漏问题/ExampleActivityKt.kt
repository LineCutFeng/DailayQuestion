package com.lcf.dailayquestion.Question_0001_自定义Handler时如何有效地避免内存泄漏问题

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.lcf.dailayquestion.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class ExampleActivityKt : AppCompatActivity() {

    var safeHandler: SafeHandler = SafeHandler(this)
    var safeHandler1: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            super.handleMessage(msg)
            when (msg.what) {
                1 -> tv.let {
                    (it as ExampleActivityKt)?.tv.text = "${msg.what}"
                }
                2 -> tv?.let {
                    (it as ExampleActivityKt)?.tv.text = "${msg.what * 2}"
                }
            }
        }
    }


    companion object {
        class SafeHandler(activity: Activity) : Handler() {
            private val activityReference: WeakReference<Activity> = WeakReference(activity)
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 -> activityReference.get()?.let {
                        (it as ExampleActivityKt)?.tv.text = "${msg.what}"
                    }
                    2 -> activityReference.get()?.let {
                        (it as ExampleActivityKt)?.tv.text = "${msg.what * 2}"
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener { safeHandler.sendEmptyMessage(1) }
        safeHandler1.sendMessageDelayed(Message.obtain().also { it.what = 2 }, 2000)
    }

    override fun onDestroy() {
        safeHandler1.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}