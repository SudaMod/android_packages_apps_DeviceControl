/*
 *  Copyright (C) 2013 - 2015 Alexander "Evisceration" Martinz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.namelessrom.devicecontrol.configuration;

import android.content.Context;
import android.support.annotation.NonNull;

import org.namelessrom.devicecontrol.Logger;
import org.namelessrom.devicecontrol.database.DatabaseHandler;
import org.namelessrom.devicecontrol.tasker.TaskerItem;
import org.namelessrom.devicecontrol.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tasker configuration which auto serializes itself to a file
 */
public class TaskerConfiguration extends BaseConfiguration<TaskerConfiguration> {
    public static final String USE_TASKER = "use_tasker";

    public static final String FSTRIM = "fstrim";
    public static final String FSTRIM_INTERVAL = "fstrim_interval";

    public boolean enabled;

    public boolean fstrimEnabled;
    public int fstrimInterval = 480;

    public int migrationLevel;

    public ArrayList<TaskerItem> items;

    private static final int MIGRATION_LEVEL_CURRENT = 2;

    private static TaskerConfiguration sInstance;

    private TaskerConfiguration(Context context) {
        loadConfiguration(context);
        migrateFromDatabase(context);
    }

    public static TaskerConfiguration get(Context context) {
        if (sInstance == null) {
            sInstance = new TaskerConfiguration(context);
        }
        return sInstance;
    }

    @Override protected String getConfigurationFile() {
        return "tasker_configuration.json";
    }

    @Override protected boolean migrateFromDatabase(Context context) {
        if (MIGRATION_LEVEL_CURRENT == migrationLevel) {
            Logger.i(this, "already up to date :)");
            return false;
        }

        if (migrationLevel < 1) {
            enabled = PreferenceHelper.getBoolean(USE_TASKER, false);

            fstrimEnabled = PreferenceHelper.getBoolean(FSTRIM, false);
            fstrimInterval = PreferenceHelper.getInt(FSTRIM_INTERVAL, 480);

            migrationLevel = 1;
        }

        if (migrationLevel < 2) {
            items = new ArrayList<>(DatabaseHandler.getInstance().getAllTaskerItems(""));

            // always bump if we need to further migrate
            migrationLevel = MIGRATION_LEVEL_CURRENT;
        }

        saveConfiguration(context);
        return true;
    }

    @Override public TaskerConfiguration loadConfiguration(Context context) {
        final TaskerConfiguration config = loadRawConfiguration(context, TaskerConfiguration.class);
        if (config == null) {
            return this;
        }

        this.enabled = config.enabled;

        this.fstrimEnabled = config.fstrimEnabled;
        this.fstrimInterval = config.fstrimInterval;

        this.migrationLevel = config.migrationLevel;

        this.items = config.items;

        return this;
    }

    @Override public TaskerConfiguration saveConfiguration(Context context) {
        saveConfigurationInternal(context);
        return this;
    }

    public ArrayList<TaskerItem> getItemsByTrigger(String trigger) {
        final ArrayList<TaskerItem> filteredItems = new ArrayList<>();

        for (final TaskerItem item : items) {
            if (item != null && trigger.equals(item.trigger)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    public synchronized TaskerConfiguration addItem(@NonNull TaskerItem taskerItem) {
        items.add(taskerItem);
        Logger.d(this, "added item -> %s", taskerItem.toString());

        return this;
    }

    public TaskerConfiguration deleteItem(@NonNull TaskerItem taskerItem) {
        final Iterator<TaskerItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            TaskerItem item = iterator.next();
            if (taskerItem.equals(item)) {
                iterator.remove();
                Logger.d(this, "removed item -> %s", item.toString());
            }
        }

        return this;
    }

}
