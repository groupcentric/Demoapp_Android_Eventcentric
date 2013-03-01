package com.eventcentric.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eventcentric.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.*;



public class Util {
    private Util() {
    }

    public interface DialogFinishListener {
        public void onComplete(String text);
    }

    ;

   /* public static Dialog createInputTextDialog(Context context, String title, boolean multiline, final DialogFinishListener dlgListener) {
        final EditText edit = (EditText) LayoutInflater.from(context).inflate(R.layout.edittext, null);
        if (!multiline) {
            edit.setInputType(~InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            edit.setMinLines(1);
        }
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(edit)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlgListener.onComplete(edit.getEditableText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }*/


    public static void shortToast(Context context, String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

    public static void longToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("EEE M/d");
    static DateFormat sdf4time;

    public static String formatDate(Date d) {
        return sdf.format(d);
    }

    static SimpleDateFormat sdfwt = new SimpleDateFormat("E, M/d @ h:mm aa");

    public static String formatDateWithTime(Date d) {
        return sdfwt.format(d);
    }

    static SimpleDateFormat sdfwte = new SimpleDateFormat("E, MMM d @ h:mm aa");

    public static String formatDateWithTimeForEvent(Date d) {
        return sdfwte.format(d);
    }

    static SimpleDateFormat sdfwt2 = new SimpleDateFormat("E, M/d @ h:mm aa");

    public static String formatDateWithTime2(Date d) {
        return sdfwt2.format(d);
    }

    public static String formatTime(Context ctx, int hour, int minute) {
        if (sdf4time == null)
            sdf4time = android.text.format.DateFormat.getTimeFormat(ctx);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return sdf4time.format(cal.getTime());
    }

    public static String formatTime(Context ctx, Date d) {
        if (sdf4time == null)
            sdf4time = android.text.format.DateFormat.getTimeFormat(ctx);
        return String.format("%s %s", sdf.format(d), sdf4time.format(d));
    }

    public static int getSingleActivityFlags() {
        return Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP;
    }

    public static Intent getMainIntent(Context context) {
        return new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //  return new Intent(context,SearchPlacesEvents.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static long[] convertLongArray(ArrayList<Long> data) {
        int s = data.size();
        long[] res = new long[s];
        for (int i = 0; i < s; i++) {
            res[i] = data.get(i);
        }
        return res;
    }

    public static int[] convertIntegerArray(ArrayList<Integer> data) {
        int s = data.size();
        int[] res = new int[s];
        for (int i = 0; i < s; i++) {
            res[i] = data.get(i);
        }
        return res;
    }

    private static final String FBPICURL = "http://graph.facebook.com/%d/picture";

    public static String getFacebookImageUrl(long id) {
        return String.format(FBPICURL, id);
    }

    public static String uidsToString(int[] uids) {
        StringBuilder sb = new StringBuilder();
        if (uids != null && uids.length > 0) {
            for (int uid : uids) {
                if (uid > -1) {
                    sb.append(uid);
                    sb.append(',');
                }
            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String formatTown(String town, String state) {
        if (TextUtils.isEmpty(state))
            return town;
        return String.format("%s, %s", town, state);
    }
    
    /*public static View inflateButton(Context ctx, int resid, OnClickListener clickListener) {
        View v = View.inflate(ctx, R.layout.button, null);
        Button btn = (Button) v.findViewById(R.id.generic_button);
        btn.setText(resid);
        btn.setOnClickListener(clickListener);
        return v;
    }

    //Blachat - Added Image Button
    public static View inflateImageButton(Context ctx, int resid, OnClickListener clickListener) {
        View v = View.inflate(ctx, R.layout.imagebutton, null);
        ImageButton imgbtn = (ImageButton) v.findViewById(R.id.image_btn_invite_new_friends);
        TextView txtInviteFriend = (TextView) v.findViewById(R.id.txt_Invite_New_Friends);
        txtInviteFriend.setText(resid);
        imgbtn.setOnClickListener(clickListener);
        return v;
    }

    public static String formatTown(String town, String state) {
        if (TextUtils.isEmpty(state))
            return town;
        return String.format("%s, %s", town, state);
    }

    public static void setFriendsInfo(List<Friend> friendsList, ImageLayoutHolder friendsHolder, ImageDownloader imageDownloader) {
        friendsHolder.removeAllViews();
        List<String> pics = new ArrayList<String>();
        for (Friend f : friendsList)
            pics.add(f.getProfilePicURL());
        friendsHolder.setImages(pics, imageDownloader);
    }*/

    public static void postInitHeader(final Activity activity, boolean visibleAddButton) {

        //Blachat 011712 - This is the background that the slider buttons sit on
        //                 The under lying views were accepting clicks if the user missed the button
        //                 this will intercept that click and no sound will be heard.
        View rl_bg = activity.findViewById(R.id.layout_drawer);
        if (rl_bg != null) {
            rl_bg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            rl_bg.setSoundEffectsEnabled(false);
        }


        /*View v = activity.findViewById(R.id.header_btn_add);
        if (v != null) {
            v.setVisibility(visibleAddButton ? View.VISIBLE : View.GONE);
            if (activity instanceof OnClickListener)
                v.setOnClickListener((OnClickListener) activity);
        }
        v = activity.findViewById(R.id.header_btn_main);
        if (v != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(getMainIntent(activity));
                }
            });
        }*/

        // events
        View v = activity.findViewById(R.id.slider_btn_events);
        if (v != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(Util.getMainIntent(activity));
                }
            });
        }

       
        //groups
        v = activity.findViewById(R.id.slider_btn_groups);
        if (v != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	activity.startActivity(Util.getMainIntent(activity));
                    activity.startActivity(new Intent(activity, GroupsActivity.class).setFlags(getSingleActivityFlags()));
                	  //will want to fire off GC activity
                }
            });
        }
        
      //about
        v = activity.findViewById(R.id.slider_btn_about);
        if (v != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	activity.startActivity(new Intent(activity, AboutActivity.class).setFlags(getSingleActivityFlags()));
                }
            });
        }

        
    }

    public static void toggleSlider(Activity activity, View vdw, View vDrawerTab, View vDrawerHldr) {
        {
            final View vDrawer = vdw;
            final View vDrawerHolder = vDrawerHldr;

            Animation anima = AnimationUtils.loadAnimation(activity, R.anim.slide_out_top);
            anima.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //wave.startAnimation(animation1);
                    vDrawer.setVisibility(View.GONE);
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    vDrawerHolder.startAnimation(animation);
                }
            });

            /*int iPlanAlertCount = -1;
            int iFriendAlertCount = -1;

            if (((App) activity.getApplication()).getUpdateCounts() != null) {
                iPlanAlertCount = ((App) activity.getApplication()).getUpdateCounts().getPlanAlertCount();
                iFriendAlertCount = ((App) activity.getApplication()).getUpdateCounts().getNewFriendRequestCount();
            }

            if (iPlanAlertCount > 0 | iFriendAlertCount > 0) {
                if (vDrawer.getVisibility() == View.VISIBLE) {
                    vDrawerHolder.startAnimation(anima);
                    vDrawerTab.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.drawer_tab_up_alert));
                } else {
                    vDrawerHolder.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_top));
                    vDrawer.setVisibility(View.VISIBLE);
                    vDrawerTab.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.drawer_tab_down_alert));
                }
            } else {*/
                
            //}
        }

    }
}
