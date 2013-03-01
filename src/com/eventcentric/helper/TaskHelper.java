package com.eventcentric.helper;

import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.eventcentric.ws.BaseTask;
import com.eventcentric.ws.ErrorResponse;
import com.eventcentric.ws.TaskContext;
import com.eventcentric.R;

public class TaskHelper {

    static protected final int ID_DLG_FATAL_ERROR = 106743510;
    static protected final int ID_DLG_ERROR = 107465519;
    static protected final int ID_DLG_PROGRESS = 118437336;

    static protected final int ID_MENU_REFRESH = 176524356;
    
    protected String dialogMessage;
    protected String dialogButtonTitle;
    protected String dialogButtonAction;

    protected BaseTask<?, ?, ?> task;

    Object other;

    private TaskContext mTaskContext;

    private OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            handleCancelDialog();
            mTaskContext.onCancel();
        }
    };

    private void setTaskContext(TaskContext taskContext) {
        mTaskContext = taskContext;
    }
    
    public static TaskHelper get(TaskContext taskContext) {
        Context ctx = taskContext.getContext();
        if (ctx instanceof Activity) {
            TaskHelper helper = (TaskHelper) ((Activity) ctx).getLastNonConfigurationInstance();
            if (helper == null) {
                helper = new TaskHelper();
            } else if (helper.task != null) {
                helper.task.setTaskContext(taskContext);
            }
            helper.setTaskContext(taskContext);
            return helper;
        }
        return null;
    }

    private TaskHelper() {
    };

    public void handleCancelDialog() {
        if (task != null && task.isInProgress())
            task.setTaskContext(null);
    }

    public void setInProgress(boolean inProgress) {
        if (mTaskContext.shouldShowProgress()) {
            Activity activity = getActivity();
            if (inProgress) {
                activity.showDialog(ID_DLG_PROGRESS);
            } else {
                try { 
                    //there is a bug when our activity is placed in a tab activity, then the dialog is not restored on screen orientation
                    //i have a workaround for this ( getActivity() ), but still it doesn't hurt to be safe here
                    activity.dismissDialog(ID_DLG_PROGRESS);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } else {
//            findViewById(R.id.tasklist_activity_loading).setVisibility(inProgress ? View.VISIBLE : View.GONE);
//            getContentHolder().setVisibility(inProgress ? View.INVISIBLE : View.VISIBLE);
            Context ctx = mTaskContext.getContext();
            if (ctx instanceof Activity)
                ((Activity) mTaskContext.getContext()).setProgressBarIndeterminateVisibility(inProgress);
        }
    }
    
    public void onError(ErrorResponse err) {
        dialogMessage = findErrorForCode(err);
        if (dialogMessage == null)
            dialogMessage = err.getErrorMessage();
        dialogButtonAction = err.getButtonAction();
        dialogButtonTitle = err.getButtonTitle();
        Context ctx = mTaskContext.getContext();
        if (ctx instanceof Activity)
            ((Activity) mTaskContext.getContext()).showDialog(isFatalError(err) ? ID_DLG_FATAL_ERROR : ID_DLG_ERROR);
    }

    public void onTaskFinish() {
        task = null;
    }

    public boolean isFatalError(ErrorResponse err) {
        return err.isFatalError();
    }

    public String findErrorForCode(ErrorResponse err) {
        if (err.getException() instanceof UnknownHostException)
            return "connection_problem";
        return null;
    }

    private Activity getActivity() {
        Context ctx = mTaskContext.getContext();
        if (ctx instanceof Activity) {
            Activity activity = (Activity) ctx;
            if (activity.getParent() != null)
                activity = activity.getParent();
            return activity;
        }
        return null;
    }

    public Dialog onCreateDialog(int id) {
        DialogInterface.OnClickListener listener = null;
        final Activity actv = getActivity();
        switch (id) {
            case ID_DLG_PROGRESS:
                View v = View.inflate(actv, R.layout.dialog_progress_layout, null);
                return new AlertDialog.Builder(actv)
                    .setCancelable(isDialogCancellable())
                    .setOnCancelListener(getCancelListener())
                    .setView(v)
                    //.setMessage(getProgressMessage())
                    //.setTitle(R.string.error)
                    .create();
            case ID_DLG_FATAL_ERROR:
                //break;
            case ID_DLG_ERROR:
                final boolean finish = id == ID_DLG_FATAL_ERROR;
                final String btnTitle = dialogButtonTitle != null ? dialogButtonTitle : actv.getString(android.R.string.ok);
                final String action = dialogButtonAction;
                if (action != null || finish) {
                    listener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (action != null) actv.startActivity(new Intent(action));
                            if (finish) actv.finish();
                        }
                    };
                }
                String msg = dialogMessage;
                dialogMessage = null;
                dialogButtonAction = null;
                dialogButtonTitle = null;
                return new AlertDialog.Builder(actv)
                    .setCancelable(false)
                    .setMessage(msg)
                    .setPositiveButton(btnTitle, listener)
                    .setTitle("error")
                    .create();
                    
        }
        return null;
    }

    public void fixProgressDialog(int id, Dialog dialog) {
        if (ID_DLG_PROGRESS == id) {
            View progress = dialog.findViewById(R.id.dialog_progress_indicator);
            progress.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE); //fix frozen progressIndicator
            ((TextView) dialog.findViewById(R.id.dialog_progress_text)).setText(getProgressMessage());
        }
    }

    public boolean isDialogCancellable() {
        return true;
    }

    public String getProgressMessage() {
        return "loading...";
    }

    public OnCancelListener getCancelListener() {
        return cancelListener;
    }

    public void detachCurrentTask() {
        if (task != null) {
            task.setTaskContext(null);
        }
    }

    public boolean isTaskRunning() {
        return task != null && task.isInProgress();
    }

    public void setTask(BaseTask<Void, ?, ?> task) {
        detachCurrentTask();
        this.task = task;
    }

    public BaseTask<?, ?, ?> getTask() {
        return task;
    }

    public void setRetainNonConfigurationInstance(Object obj) {
        other = obj;
    }

    public Object getLastNonConfigurationInstance() {
        return other;
    }

    public void startTask(BaseTask<Void, ?, ?> task) {
        setTask(task);
        task.execute();
    }
}
