package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */


/**
 * Created by hp on 3/9/2016.
 */



public class StepProgress {

    public static final int STARTING = 1;
    public static final int SUCCEEDED = 2;

    public final int stepId;
    public final int status;

    public StepProgress(int stepId, int status) {
        this.status = status;
        this.stepId = stepId;
    }
}
