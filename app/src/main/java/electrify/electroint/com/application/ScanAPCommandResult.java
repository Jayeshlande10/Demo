package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */


/**
 * Created by hp on 3/9/2016.
 */



// FIXME: this naming... is not ideal.
public class ScanAPCommandResult implements WifiNetwork {

    public final ScanApCommand.Scan scan;

    public ScanAPCommandResult(ScanApCommand.Scan scan) {
        this.scan = scan;
    }

    @Override
    public String getSsid() {
        return scan.ssid;
    }

    @Override
    public boolean isSecured() {
        return scan.wifiSecurityType != WifiSecurity.OPEN.asInt();
    }

    @Override
    public String toString() {
        return "ScanAPCommandResult{" +
                "scan=" + scan +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScanAPCommandResult that = (ScanAPCommandResult) o;

        if (getSsid() != null ? !getSsid().equals(that.getSsid()) : that.getSsid() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getSsid() != null ? getSsid().hashCode() : 0;
    }

}
