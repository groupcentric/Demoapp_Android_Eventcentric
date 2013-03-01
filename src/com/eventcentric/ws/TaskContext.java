package com.eventcentric.ws;

import android.content.Context;

public interface TaskContext {
    public void setInProgress(boolean inProgress);
    public void onError(ErrorResponse err);
    public void onTaskFinish();
    public Context getContext();
	public void onSuccess(BaseTask<?, ?, ?> task);

    public void onCancel();
    public boolean shouldShowProgress();
}