package com.example.sporty;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private int[] backgrounds = {R.drawable.background1, R.drawable.background2, R.drawable.background3};
    private int currentBackgroundIndex = 0;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rootView = findViewById(R.id.home_root_view);
        changeBackgroundPeriodically();

        Button enterPinButton = findViewById(R.id.enter_pin_button);
        Button organizerButton = findViewById(R.id.log_in_text);

        enterPinButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EventActivity.class);
            startActivity(intent);
        });

        organizerButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void changeBackgroundPeriodically() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
                    rootView.setBackgroundResource(backgrounds[currentBackgroundIndex]);
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); // change every 5 seconds
    }
}
