package electrify.electroint.com.application;

/**
 * Created by hp on 3/10/2016.
 */

import android.content.Context;
        import android.support.v4.content.AsyncTaskLoader;


public abstract class BetterAsyncTaskLoader<T> extends AsyncTaskLoader<T> {


    public abstract boolean hasContent();

    public abstract T getLoadedContent();


    public BetterAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        // How is this not on AsyncTaskLoader already?
        if (hasContent()) {
            deliverResult(getLoadedContent());
        }
        if (takeContentChanged() || !hasContent()) {
            forceLoad();
        }
    }
}
