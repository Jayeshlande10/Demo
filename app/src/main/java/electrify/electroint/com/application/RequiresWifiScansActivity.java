package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */

/**
 * Created by hp on 3/9/2016.
 */

        import android.Manifest;



        import android.Manifest.permission;
        import android.util.Log;



// FIXME: doing this via Activities is very sketchy.  Find a better way
// when refactoring to use fragments
public class RequiresWifiScansActivity extends BaseActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionsFragment.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.d("RequiresWifiScans", "Location permission appears to have been revoked, finishing activity...");
            finish();
        }
    }
}
