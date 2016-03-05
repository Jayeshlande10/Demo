package electrify.electroint.com.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    Button loginButton;
    EditText emailText,passwordText;
    TextView signupText;
    TextInputLayout tilEmail;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().hide();

        loginButton = (Button) findViewById(R.id.btn_login);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        signupText = (TextView) findViewById(R.id.link_signup);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSignup = new Intent(LoginPage.this, SignupPage.class);
                startActivity(gotoSignup);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.finish();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {

            return;
        }

        loginButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/

        loginButton.setText("Loading");
        loginButton.setTextColor(Color.RED);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        //Implement our authentication process here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        loginButton.setText("Login Successfull");
                        loginButton.setTextColor(Color.GREEN);

                        //progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //emailText.setError("enter a valid email address");
            Toast.makeText(LoginPage.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            //passwordText.setError("between 4 and 10 alphanumeric characters");
            passwordText.setHintTextColor(Color.RED);
            Toast.makeText(LoginPage.this, "Password must between 4 and 10 alphanumeric characters", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

}
