package com.lcf.dailayquestion.Question_0001_自定义Handler时如何有效地避免内存泄漏问题;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lcf.dailayquestion.R;

import java.lang.ref.WeakReference;

public class ExampleActivityJv extends AppCompatActivity {

    SafeHandler safeHandler = new SafeHandler(this);
    Handler safeHandler1 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ((TextView) findViewById(R.id.tv)).setText("" + msg.what);
                    break;
                case 2:
                    ((TextView) findViewById(R.id.tv)).setText("" + msg.what * 2);
                    break;
            }
        }
    };

    class SafeHandler extends Handler {
        WeakReference<Activity> weakReference;

        public SafeHandler(Activity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (weakReference.get() != null) {
                        ((TextView) weakReference.get().findViewById(R.id.tv)).setText("" + msg.what);
                    }
                    break;
                case 2:
                    if (weakReference.get() != null) {
                        ((TextView) weakReference.get().findViewById(R.id.tv)).setText("" + msg.what * 2);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeHandler.sendEmptyMessage(1);
            }
        });
        Message msg = Message.obtain();
        msg.what = 2;
        safeHandler1.sendMessageDelayed(msg, 2000);
    }

    @Override
    protected void onDestroy() {
        safeHandler1.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
