package com.eventcentric.ws;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public abstract class BaseTask<ParameterT, ProgressT, ReturnT> extends
        AsyncTask<ParameterT, ProgressT, TaskResult<ReturnT>> {

    protected WeakReference<TaskContext> taskContext;

    protected boolean inProgress;
    protected TaskResult<ReturnT> taskResult = new TaskResult<ReturnT>();
    
    public BaseTask(TaskContext taskContext) {
        setTaskContext(taskContext);
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = new WeakReference<TaskContext>(taskContext);
    }

    public TaskResult<ReturnT> getSyncedResult(ParameterT... params) {
        onPreExecute();
        TaskResult<ReturnT> res = doInBackground(params);
        onPostExecute(res);
        return res;
    }

    protected final void onPreExecute() {
        if (taskContext == null) return;
        TaskContext tc = taskContext.get();
        inProgress = true;
        if (tc != null)
            tc.setInProgress(true);
    }

    @Override
    protected final TaskResult<ReturnT> doInBackground(ParameterT... params) {
        try {
            taskResult.setData(getResult(params));
        } catch (Exception e) {
            e.printStackTrace();
            taskResult.response.setException(e);
        }
        return taskResult;
    }

    abstract protected ReturnT getResult(ParameterT... params) throws Exception;

    @Override
    protected final void onPostExecute(TaskResult<ReturnT> result) {
        finish(result);
    }

    protected final void finish(TaskResult<ReturnT> result) {
        if (taskContext == null) return;
        TaskContext tc = taskContext.get();
        inProgress = false;
        if (tc == null || (tc.getContext() instanceof Activity
                        && ((Activity) tc.getContext()).isFinishing())) {
            Log.d(BaseTask.class.getSimpleName(), "skipping post-exec handler for task "
                    + hashCode() + " (context is null)");
            return;
        }
        tc.setInProgress(false);
        tc.onTaskFinish();
        if (failed()) {
            setNegativeResult();
        } else {
            setPositiveResult(result);
        }
    }

    public boolean isInProgress() {
        return inProgress;
    }
    
    protected void setPositiveResult(TaskResult<ReturnT> result) {
        taskResult = result;
        taskContext.get().onSuccess(this);
//        OnTaskCompleteListener<ReturnT> listener = taskCompleteListener.get();
//        if (listener != null)
//          listener.onSuccess(result);
    }
    
    protected void setNegativeResult() {
        TaskContext tc = taskContext.get();
        tc.onError(taskResult.response);
    }
    
    protected boolean failed() {
        return !taskResult.response.isEmpty();
    }

    public TaskResult<ReturnT> getTaskResult() {
        return taskResult;
    }

    public ReturnT getActualResult() {
        return taskResult.getResult();
    }
}

