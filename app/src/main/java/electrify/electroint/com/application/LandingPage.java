package electrify.electroint.com.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {

    Button getStarted, tryDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getStarted = (Button) findViewById(R.id.getstarted);
        tryDemo = (Button) findViewById(R.id.trydemo);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoLoginPage = new Intent(getApplicationContext(), SetAccount.class);
                startActivity(gotoLoginPage);
            }
        });

        tryDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
