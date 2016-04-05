package electrify.electroint.com.application;

/**
 * Created by hp on 3/18/2016.
 */


        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.support.v7.app.AppCompatActivity;

        import io.particle.android.sdk.cloud.SDKGlobals;



/**
 * This class exists solely to avoid requiring SDK users to have to define
 * anything in an Application subclass.  By (ab)using this custom Activity,
 * we can at least be sure that the custom fonts in the device setup screens
 * work correctly without any special instructions.
 */
@SuppressLint("Registered")  // this is a base activity, it shouldn't be registered.
public class BaseActivity2 extends AppCompatActivity {

    private static boolean customFontInitDone = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (!customFontInitDone) {
            // FIXME: make actually customizable via resources
            // (see file extension string formatting nonsense)
             CalligraphyConfig.initDefault(
                    new CalligraphyConfig.Builder()
                            .setDefaultFontPath(newBase.getString(R.string.normal_text_font_name))
                            .setFontAttrId(R.attr.fontPath)
                            .build());
            customFontInitDone = true;
        }
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        SDKGlobals.init(this);
    }

}
