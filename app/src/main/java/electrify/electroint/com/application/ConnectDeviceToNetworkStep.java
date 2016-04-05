package electrify.electroint.com.application;

/**
 * Created by hp on 3/10/2016.
 */

/**
 * Created by hp on 3/9/2016.
 */

        import android.content.Context;

        import java.io.IOException;




public class ConnectDeviceToNetworkStep extends SetupStep {

    private final CommandClient commandClient;
    private final Context ctx;

    private volatile boolean commandSent = false;

    public ConnectDeviceToNetworkStep(StepConfig stepConfig, CommandClient commandClient, Context ctx) {
        super(stepConfig);
        this.commandClient = commandClient;
        this.ctx = ctx;
    }

    @Override
    protected void onRunStep() throws SetupStepException {
        try {
            log.d("Sending connect-ap command");
            ConnectAPCommand.Response response = commandClient.sendCommandAndReturnResponse(
                    new ConnectAPCommand(0), ConnectAPCommand.Response.class,
                    new InterfaceBindingSocketFactory(ctx)
            );
            if (!response.isOK()) {
                throw new SetupStepException("ConnectAPCommand returned non-zero response code: " +
                        response.responseCode);
            }

            commandSent = true;

        } catch (IOException e) {
            throw new SetupStepException(e);
        }
    }

    @Override
    public boolean isStepFulfilled() {
        return commandSent;
    }

}
