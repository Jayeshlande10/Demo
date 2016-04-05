package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */

/**
 * Created by hp on 3/9/2016.
 */



public class SetupStepException extends Exception {

    public SetupStepException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SetupStepException(String msg) {
        super(msg);
    }

    public SetupStepException(Throwable throwable) {
        super(throwable);
    }

}
