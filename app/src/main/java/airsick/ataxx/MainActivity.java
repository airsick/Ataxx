package airsick.ataxx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button singlePlayerButton, multiPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePlayerButton = (Button) findViewById(R.id.singlePlayerButton);
        multiPlayerButton = (Button) findViewById(R.id.multiPlayerButton);
    }

    public void multiPlayerClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
