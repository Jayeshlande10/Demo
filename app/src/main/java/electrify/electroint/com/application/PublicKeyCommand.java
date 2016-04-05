package electrify.electroint.com.application;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 3/11/2016.
 */

/**
 * Created by hp on 3/9/2016.
 */


        import com.google.gson.annotations.SerializedName;

public class PublicKeyCommand extends NoArgsCommand {

    @Override
    public String getCommandName() {
        return "public-key";
    }


    public static class Response {

        @SerializedName("r")
        public final int responseCode;

        // Hex-encoded public key, in DER format
        @SerializedName("b")
        public final String publicKey;

        public Response(int responseCode, String publicKey) {
            this.responseCode = responseCode;
            this.publicKey = publicKey;
        }
    }

}
