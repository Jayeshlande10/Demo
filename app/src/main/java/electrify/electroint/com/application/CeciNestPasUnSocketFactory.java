package electrify.electroint.com.application;

/**
 * Created by hp on 3/10/2016.
 */



/**
 * Created by hp on 3/9/2016.
 */


        import java.io.IOException;
        import java.net.Socket;

// Because it's not a java SocketFactory.
//
// This is going away soon, it exists temporarily just to get
// *something* in place to #SHIPIT.
@Deprecated
public interface CeciNestPasUnSocketFactory {

    Socket buildSocket(int readTimeoutMillis) throws IOException;

}
