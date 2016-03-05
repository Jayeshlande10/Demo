package electrify.electroint.com.application;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SignupPage extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText mobileText;
    Button signupButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        getSupportActionBar().hide();

        signupButton  = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        nameText = (EditText) findViewById(R.id.input_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        mobileText = (EditText) findViewById(R.id.input_mobile);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLogin = new Intent(SignupPage.this, LoginPage.class);
                startActivity(gotoLogin);
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            //onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String mobile = mobileText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            //nameText.setError("at least 3 characters");
            Toast.makeText(SignupPage.this, "Name field must have at least 3 character", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //emailText.setError("enter a valid email address");
            Toast.makeText(SignupPage.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            //passwordText.setError("between 4 and 10 alphanumeric characters");
            Toast.makeText(SignupPage.this, "Password must between 4 and 10 alphanumeric characters", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if (mobile.isEmpty() || mobile.length() < 9 || password.length() > 10) {
            //passwordText.setError("between 4 and 10 alphanumeric characters");
            Toast.makeText(SignupPage.this, "Invalid mobile number!!!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
