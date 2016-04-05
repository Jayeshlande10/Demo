package electrify.electroint.com.application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;


    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v4.content.Loader;
    import android.view.View;

    import java.util.Set;



    public class SelectNetworkActivity extends RequiresWifiScansActivity
            implements WifiListFragment.Client<ScanAPCommandResult> {

        private WifiListFragment wifiListFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_network);

            wifiListFragment = Ui.findFrag(this, R.id.wifi_list_fragment);
            Ui.findView(this, R.id.action_rescan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParticleUi.showParticleButtonProgress(SelectNetworkActivity.this, R.id.action_rescan, true);
                    wifiListFragment.scanAsync();
                }
            });
        }

        public void onManualNetworkEntryClicked(View view) {
            startActivity(new Intent(this, ManualNetworkEntryActivity.class));
            finish();
        }

        @Override
        public void onNetworkSelected(ScanAPCommandResult selectedNetwork) {
            wifiListFragment.stopAggroLoading();

            String softApSSID = WiFi.getCurrentlyConnectedSSID(this);
            if (selectedNetwork.isSecured()) {
                startActivity(PasswordEntryActivity.buildIntent(this, selectedNetwork.scan));
            } else {
                startActivity(ConnectingActivity.buildIntent(this, softApSSID, selectedNetwork.scan));
            }
            finish();
        }

        @Override
        public Loader<Set<ScanAPCommandResult>> createLoader(int id, Bundle args) {
            // FIXME: make the address below use resources instead of hardcoding
            CommandClient client = CommandClient.newClientUsingDefaultSocketAddress();
            return new ScanApCommandLoader(getApplicationContext(), client,
                    new InterfaceBindingSocketFactory(this));
        }

        @Override
        public void onLoadFinished() {
            ParticleUi.showParticleButtonProgress(this, R.id.action_rescan, false);
        }

        @Override
        public String getListEmptyText() {
            return getString(R.string.no_wifi_networks_found);
        }

        @Override
        public int getAggroLoadingTimeMillis() {
            return 10000;
        }

    }
