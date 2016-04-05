package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */


/**
 * Created by hp on 3/9/2016.
 */





public class SetupProcessException extends Exception {

    public final SetupStep failedStep;

    public SetupProcessException(String msg, SetupStep failedStep) {
        super(msg);
        this.failedStep = failedStep;
    }
}
