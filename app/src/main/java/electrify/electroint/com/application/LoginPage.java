package electrify.electroint.com.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.squareup.phrase.Phrase;

import io.particle.android.sdk.cloud.SDKGlobals;
import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;

import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.TLog;


import static io.particle.android.sdk.utils.Py.list;
import static io.particle.android.sdk.utils.Py.truthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.SDKGlobals;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.TLog;
import static io.particle.android.sdk.utils.Py.truthy;
public class LoginPage extends BaseActivity2 {
    private static final TLog log = TLog.get(LoginPage.class);

    /**
     * Keep track of the login task to ensure we can cancel it if requested, ensure against
     * duplicate requests, etc.
     */
    private Async.AsyncApiWorker<ParticleCloud, Void> loginTask = null;

    // UI references.
    private EditText emailView;
    private EditText passwordView;

    private ParticleCloud sparkCloud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ParticleUi.enableBrandLogoInverseVisibilityAgainstSoftKeyboard(this);

        sparkCloud = ParticleCloud.get(this);

        // Set up the login form.
        emailView = Ui.findView(this, R.id.email);
        passwordView = Ui.findView(this, R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_log_in || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        for (EditText tv : list(emailView, passwordView)) {
            tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    emailView.setError(null);
                    passwordView.setError(null);
                }
            });
        }

        Button submit = Ui.findView(this, R.id.action_log_in);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Ui.setText(this, R.id.log_in_header_text,
                Phrase.from(this, R.string.log_in_header_text)
                        .put("brand_name", getString(R.string.brand_name))
                        .format()
        );

        Ui.setTextFromHtml(this, R.id.user_has_no_account, R.string.msg_no_account)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), SignupPage.class));
                        finish();
                    }
                });

        Ui.setTextFromHtml(this, R.id.forgot_password, R.string.msg_forgot_password);
    }

    public void onPasswordResetClicked(View v) {
        Intent intent;
        if (getResources().getBoolean(R.bool.organization)) {
            intent = PasswordResetActivity.buildIntent(this, emailView.getText().toString());
        } else {
            intent = WebViewActivity.buildIntent(this,
                    Uri.parse(getString(R.string.forgot_password_uri)));
        }
        startActivity(intent);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (loginTask != null) {
            log.wtf("Login being attempted again even though the button isn't enabled?!");
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;

        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ParticleUi.showParticleButtonProgress(this, R.id.action_log_in, true);
            loginTask = Async.executeAsync(sparkCloud, new Async.ApiWork<ParticleCloud, Void>() {
                @Override
                public Void callApi(ParticleCloud sparkCloud) throws ParticleCloudException {
                    sparkCloud.logIn(email, password);
                    // Get all devices too, in case this user already has configured
                    // devices and is just logging in to this device for the first time,
                    // or has re-installed the app (etc)
                    sparkCloud.getDevices();
                    return null;
                }

                @Override
                public void onTaskFinished() {
                    loginTask = null;
                }

                @Override
                public void onSuccess(Void result) {
                    log.d("Logged in...");
                    if (isFinishing()) {
                        return;
                    }
                    startActivity(NextActivitySelector.getNextActivityIntent(
                            LoginPage.this,
                            sparkCloud,
                            SDKGlobals.getSensitiveDataStorage()));
                    finish();
                }

                @Override
                public void onFailure(ParticleCloudException error) {
                    log.d("onFailed(): " + error.getMessage());
                    ParticleUi.showParticleButtonProgress(LoginPage.this,
                            R.id.action_log_in, false);
                    // FIXME: check specifically for 401 errors
                    // and set a better error message
                    passwordView.setError(error.getBestMessage());
                    passwordView.requestFocus();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return truthy(email) && email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        // FIXME: we should probably fix this number...  just making sure
        // there are no blank passwords.
        return (password.length() > 0);
    }

}

  /*  Button loginButton;
    EditText emailText,passwordText;
    TextView signupText;
    TextInputLayout tilEmail;
    private static final String REGISTER_URL = "https://electrify.website/api/app/login.php";
    private static final String TAG = "LoginActivity";
    String email_id,name,phone;


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

    /*    loginButton.setText("Loading");
        loginButton.setTextColor(Color.RED);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
          register(password,email);
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


    private void register( String password, String userid) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass2 ruc = new RegisterUserClass2();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(SignupPage.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                try
                {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONObject object=jsonObject.getJSONObject("0");
                    JSONObject object1=jsonObject.getJSONObject("access_token");
                    //JSONObject object2=jsonObject.getJSONObject("claim_code");
                    email_id=object.getString("email");

                      name=object.getString("name");
                       phone=object.getString("phone");

                   // access_token=object1.getString("access_token");
                    //claim_code=object2.getString("claim_code");
                }
                catch (Exception e)
                {

                }

              /*  try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject json_data = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            json_data = jsonArray.getJSONObject(i);
                            fd_id = json_data.getString("user");
                            fd_name = json_data.getString("pass");
                        } catch (Exception e2) {

                        }
                    }
                }
                catch (Exception e)
                {

                }*/

        /*    }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
              //  data.put("name",params[0]);
               // data.put("username",params[1]);
                data.put("password",params[0]);
                data.put("userid",params[1]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result+"jayesh";
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(password, userid);
    }

}
class RegisterUserClass2
{


    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            }
            else {
                response="Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}*/
