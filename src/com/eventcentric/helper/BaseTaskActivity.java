package com.eventcentric.helper;

import android.app.Activity;
import android.app.Dialog;

import com.eventcentric.ws.BaseTask;
import com.eventcentric.ws.ErrorResponse;
import com.eventcentric.ws.TaskContext;

abstract public class BaseTaskActivity extends BaseActivity implements TaskContext {

    protected TaskHelper taskHelper;

    protected void startTask(BaseTask<Void, ?, ?> task) {
        if (canStartTask) //don't start the task in onCrate, queue it for onResume
            _startTask(task);
        else
            this.queuedTask = task; 
    }

    private void _startTask(BaseTask<Void, ?, ?> task) {
        taskHelper.startTask(task);
    }

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskHelper = TaskHelper.get(this);
    };
    
    public Activity getContext() {
        return this;
    }

    @Override
    public void onError(ErrorResponse err) {
        taskHelper.onError(err);
    }

    @Override
    public void onTaskFinish() {
        taskHelper.onTaskFinish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dlg = taskHelper.onCreateDialog(id);
        return dlg != null ? dlg : super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        taskHelper.fixProgressDialog(id, dialog);
    }

    @Override
    final public Object onRetainNonConfigurationInstance() {
        taskHelper.setRetainNonConfigurationInstance(onRetainNonConfigurationInstance2());
        taskHelper.detachCurrentTask();
        return taskHelper;
    }

    protected Object onRetainNonConfigurationInstance2() {
        return null;
    }

    @Override
    final public Object getLastNonConfigurationInstance() {
        return super.getLastNonConfigurationInstance();
    }

    final public Object getLastNonConfigurationInstance2() {
        return taskHelper.getLastNonConfigurationInstance();
    }

    public void setInProgress(boolean inProgress) {
        taskHelper.setInProgress(inProgress);
    }

    @Override
    public void onCancel() {
    }

    @Override
    public boolean shouldShowProgress() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            taskHelper.detachCurrentTask();
    }

    private boolean canStartTask;
    private BaseTask<Void, ?, ?> queuedTask;
    @Override
    protected void onResume() {
        super.onResume();
        canStartTask = true;
        if (queuedTask != null)
            _startTask(queuedTask);
        queuedTask = null;
    }
}
