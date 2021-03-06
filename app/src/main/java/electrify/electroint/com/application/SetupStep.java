package electrify.electroint.com.application;

/**
 * Created by hp on 3/11/2016.
 */
/**
 * Created by hp on 3/9/2016.
 */


        import io.particle.android.sdk.utils.TLog;


public abstract class SetupStep {

    protected final TLog log;
    private final StepConfig stepConfig;
    private volatile int numAttempts;


    public SetupStep(StepConfig stepConfig) {
        log = TLog.get(this.getClass());
        this.stepConfig = stepConfig;
    }

    protected abstract void onRunStep() throws SetupStepException, SetupProcessException;

    public abstract boolean isStepFulfilled();

    public final void runStep() throws SetupStepException, SetupProcessException {
        if (isStepFulfilled()) {
            log.i("Step " + getStepName() + " already fulfilled, skipping...");
            return;
        }
        if (numAttempts > stepConfig.maxAttempts) {
            throw new SetupProcessException(
                    String.format("Exceeded limit of %d retries for step %s",
                            stepConfig.maxAttempts, getStepName()), this);
        } else {
            log.i("Running step " + getStepName());
            numAttempts++;
            onRunStep();
        }
    }

    public StepConfig getStepConfig() {
        return this.stepConfig;
    }

    protected void resetAttemptsCount() {
        numAttempts = 0;
    }

    private String getStepName() {
        return this.getClass().getSimpleName();
    }

}
