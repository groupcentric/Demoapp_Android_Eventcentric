package com.eventcentric.helper;

import android.view.Menu;
import android.view.MenuItem;

import com.eventcentric.ws.TaskContext;

abstract public class BaseActionActivity extends BaseTaskActivity implements TaskContext {

    protected static int MENU_GO_TO_MAIN = 645234;

    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuItem m = menu.add(Menu.NONE, MENU_GO_TO_MAIN, Menu.NONE, "Go To Main");
        //m.setIcon(android.R.drawable.ic_menu_agenda);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_GO_TO_MAIN) {
            goToMain();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
