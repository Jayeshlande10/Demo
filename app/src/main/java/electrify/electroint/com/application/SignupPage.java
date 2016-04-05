package electrify.electroint.com.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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



        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.text.method.LinkMovementMethod;
        import android.util.Patterns;
      /*  import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import com.squareup.phrase.Phrase;

        import io.particle.android.sdk.cloud.ParticleCloud;
        import io.particle.android.sdk.cloud.ParticleCloudException;
        import io.particle.android.sdk.cloud.SDKGlobals;
        import io.particle.android.sdk.devicesetup.R;
        import io.particle.android.sdk.ui.BaseActivity;
        import io.particle.android.sdk.ui.NextActivitySelector;
        import io.particle.android.sdk.utils.Async;
        import io.particle.android.sdk.utils.TLog;
        import io.particle.android.sdk.utils.ui.ParticleUi;
        import io.particle.android.sdk.utils.ui.Toaster;
        import io.particle.android.sdk.utils.ui.Ui;*/

        import static io.particle.android.sdk.utils.Py.truthy;

public class SignupPage extends BaseActivity2 {

    private static final TLog log = TLog.get(SignupPage.class);

    /**
     * Keep track of the login task to ensure we can cancel it if requested, ensure against
     * duplicate requests, etc.
     */
    private Async.AsyncApiWorker<ParticleCloud, Void> createAccountTask = null;

    // UI references.
    private EditText emailView;
    private EditText passwordView;
    private EditText verifyPasswordView;

    private boolean useOrganizationSignup;
    Context ctx=this.getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        ParticleUi.enableBrandLogoInverseVisibilityAgainstSoftKeyboard(this);

       // Ui.setText(this, R.id.create_account_header_text,
         //       Phrase.from(this, R.string.create_account_header_text)
           //             .put("brand_name", getString(R.string.brand_name))
             //           .format()
        //);

        emailView = Ui.findView(this, R.id.input_email);
        passwordView = Ui.findView(this, R.id.password);
        verifyPasswordView = Ui.findView(this, R.id.input_password);

        useOrganizationSignup = getResources().getBoolean(R.bool.organization);

        Button submit = Ui.findView(this, R.id.btn_signup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateAccount();
            }
        });

        Ui.setTextFromHtml(this, R.id.link_login, R.string.msg_user_already_has_account)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), LoginPage.class));
                        finish();
                    }
                });

        if (getResources().getBoolean(R.bool.show_sign_up_page_fine_print)) {
            String tosUri = getString(R.string.terms_of_service_uri);
            String privacyPolicyUri = getString(R.string.privacy_policy_uri);

            String finePrintText = Phrase.from(this, R.string.msg_create_account_disclaimer)
                    .put("tos_link", tosUri)
                    .put("privacy_policy_link", privacyPolicyUri)
                    .format().toString();
            Ui.setTextFromHtml(this, R.id.fine_print, finePrintText)
                    .setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            Ui.findView(this, R.id.fine_print).setVisibility(View.GONE);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptCreateAccount() {
        if (createAccountTask != null) {
            log.wtf("Sign up being attempted again even though the sign up button isn't enabled?!");
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

        // Check for a valid email address.
        if (!truthy(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        } else if (!password.equals(verifyPasswordView.getText().toString())) {
            passwordView.setError("Passwords do not match.");
            verifyPasswordView.setError("Passwords do not match.");
            focusView = passwordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           // ParticleUi.showParticleButtonProgress(this, R.id.action_create_account, true);
            final ParticleCloud cloud = ParticleCloud.get(this);
            createAccountTask = Async.executeAsync(cloud, new Async.ApiWork<ParticleCloud, Void>() {
                @Override
                public Void callApi(ParticleCloud particleCloud) throws ParticleCloudException {
                    if (useOrganizationSignup) {
                        particleCloud.signUpAndLogInWithCustomer(email, password,
                                getString(R.string.organization_slug));
                    } else {
                        particleCloud.signUpWithUser(email, password);
                    }
                    return null;
                }

                @Override
                public void onTaskFinished() {
                    createAccountTask = null;
                }

                @Override
                public void onSuccess(Void result) {
                    log.d("onAccountCreated()!");
                    if (isFinishing()) {
                        return;
                    }
                    if (useOrganizationSignup) {
                        // with org setup, we're already logged in upon successful account creation
                        onLoginSuccess(cloud);
                    } else {
                        attemptLogin(email, password);
                    }
                }

                @Override
                public void onFailure(ParticleCloudException error) {
                    // FIXME: look at old Spark app for what we do here UI & workflow-wise
                    log.d("onFailed()");
                 //   ParticleUi.showParticleButtonProgress(SignupPage.this,
                   //         R.id.action_create_account, false);

                    String msg = "Unknown error";
                    if (error.getKind() == ParticleCloudException.Kind.NETWORK) {
                        msg = "Error communicating with server";

                    } else if (error.getResponseData() != null) {

                        if (error.getResponseData().getHttpStatusCode() == 401
                                && getResources().getBoolean(R.bool.organization)) {
                            msg = "An account with this email address may already exist.";
                        } else {
                            msg = error.getServerErrorMsg();
                        }
                    }

                  //  Toaster.l(SignupPage.this, msg, Gravity.CENTER_VERTICAL);
                    emailView.requestFocus();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        // FIXME: we should probably fix this number...  just making sure
        // there are no blank passwords.
        return (password.length() > 0);
    }

    private void onLoginSuccess(ParticleCloud cloud) {
        startActivity(NextActivitySelector.getNextActivityIntent(this.getApplicationContext(),
                cloud,
                SDKGlobals.getSensitiveDataStorage()));
        finish();
    }

    private void attemptLogin(final String username, final String password) {
        final ParticleCloud cloud = ParticleCloud.get(this);
        Async.executeAsync(cloud, new Async.ApiWork<ParticleCloud, Void>() {
            @Override
            public Void callApi(ParticleCloud particleCloud) throws ParticleCloudException {
                particleCloud.logIn(username, password);
                return null;
            }

            @Override
            public void onSuccess(Void result) {
                log.d("Logged in...");
                if (isFinishing()) {
                    return;
                }
                onLoginSuccess(cloud);
            }

            @Override
            public void onFailure(ParticleCloudException error) {
                log.w("onFailed(): " + error.getMessage());
               // ParticleUi.showParticleButtonProgress(SignupPage.this,
                 //       R.id.action_create_account, false);
                passwordView.setError(error.getBestMessage());
                passwordView.requestFocus();
            }
        });

    }
}


    /* private static final String TAG = "SignupActivity";
    String claim_code="";
    String access_token="";
    String email_id;
    String response="";
    int status_code;
    String user_id="";
    String phone_no="";
    String photo="";
    String user_name="";
    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText mobileText;
    Button signupButton;
    TextView loginLink;
    ParticleCloud cloud;
    private static final String REGISTER_URL = "http://electrify.website/api/app/register.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        ParticleDeviceSetupLibrary.init(this.getApplicationContext(), LandingPage.class);
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
        String phone=mobileText.getText().toString();
        register(name,email,phone,password);
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

       // ParticleDeviceSetupLibrary.startDeviceSetup(this,claim_code,user_id);


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
*/
   /* private void register(String name, String email, String phone, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

             // Toast.makeText(SignupPage.this,"33",Toast.LENGTH_SHORT).show();
                try
                {
                   // Toast.makeText(SignupPage.this,"jug",Toast.LENGTH_SHORT).show();
                    if(s!=null) {
                      /*  JSONObject jsonObject = new JSONObject(s);

                        JSONObject object = jsonObject.getJSONObject("0");
                        email_id = object.getString("email");
                        user_id = object.getString("userid");
                        user_name = object.getString("name");
                        phone_no = object.getString("phone");
                        photo = object.getString("photo");
                        access_token = jsonObject.getString("access_token");
                        claim_code = jsonObject.getString("claim_code");
                        response = jsonObject.getString("response");*/

               /*         JSONObject jsonObject=(JSONObject)new JSONTokener(s).nextValue();
                        JSONObject json2 = jsonObject.getJSONObject("0");
                        user_name = (String) json2.get("name");
                        email_id=(String) json2.get("email");
                        user_id=(String) json2.get("userid");
                        phone_no=(String)json2.get("phone");
                        photo=(String) json2.get("photo");
                        claim_code=(String)jsonObject.get("claim_code");
                        response=(String)jsonObject.get("response");
                        access_token=(String)jsonObject.get("access_token");
                        status_code=(Integer)jsonObject.getInt("status_code");
                        Toast.makeText(SignupPage.this,user_name+s.length()+access_token+claim_code+status_code,Toast.LENGTH_SHORT).show();
                        ParticleDeviceSetupLibrary.startDeviceSetup(SignupPage.this, claim_code, user_id);

                    }
                    else
                    {
                        Toast.makeText(SignupPage.this,"you are in else",Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                   Toast.makeText(SignupPage.this,"you are catched!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("phone",params[2]);
                data.put("password",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);
              //  Toast.makeText(SignupPage.this,"jug",Toast.LENGTH_SHORT).show();
                return  result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(name,email,phone,password);
    }

}
class RegisterUserClass
{


    public String sendPostRequest(String requestURL,
                                         HashMap<String, String> postDataParams) {

        URL url;StringBuilder sb=new StringBuilder();
        String response = ""; String response_code="34";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
             response_code=Integer.toString(responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //response = br.readLine();
                while ((response = br.readLine()) != null){
                    sb.append(response);
            }}
            else {
                response="Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
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


 }
