package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */

/**
 * Created by hp on 3/9/2016.
 */


        import com.google.gson.Gson;

/**
 * Convenience class for commands with no argument data
 */
public abstract class NoArgsCommand extends Command {

    @Override
    public String argsAsJsonString(Gson gson) {
        // this command has no argument data
        return null;
    }
}
