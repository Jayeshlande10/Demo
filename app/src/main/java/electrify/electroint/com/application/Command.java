package electrify.electroint.com.application;

/**
 * Created by hp on 3/10/2016.
 */

        import com.google.gson.Gson;

/**
 * Created by hp on 3/9/2016.
 */

public abstract class Command {

    public abstract String getCommandName();

    // override if you want a different implementation
    public String argsAsJsonString(Gson gson) {
        return gson.toJson(this);
    }
}