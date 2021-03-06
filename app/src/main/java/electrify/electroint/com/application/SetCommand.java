package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */


/**
 * Created by hp on 3/9/2016.
 */


        import com.google.common.base.Preconditions;
        import com.google.gson.annotations.SerializedName;

public class SetCommand extends Command {

    @SerializedName("k")
    public final String key;

    @SerializedName("v")
    public final String value;

    public SetCommand(String key, String value) {
        Preconditions.checkNotNull(key, "Key cannot be null");
        Preconditions.checkNotNull(value, "Value cannot be null");
        this.key = key;
        this.value = value;
    }

    @Override
    public String getCommandName() {
        return "set";
    }


    public static class Response {

        @SerializedName("r")
        public final int responseCode;

        public Response(int responseCode) {
            this.responseCode = responseCode;
        }
    }

}
