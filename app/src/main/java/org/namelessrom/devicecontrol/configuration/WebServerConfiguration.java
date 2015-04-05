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

/**
 * Web server configuration which auto serializes itself to a file
 */
public class WebServerConfiguration extends BaseConfiguration<WebServerConfiguration> {
    public static final String ROOT = "wfm_root";
    public static final String PORT = "wfm_port";

    public static final String USE_AUTH = "wfm_auth";
    public static final String USERNAME = "wfm_username";
    public static final String PASSWORD = "wfm_password";

    public boolean root;
    public int port = 8080;

    public boolean useAuth = true;
    public String username = "root";
    public String password = "toor";

    private static WebServerConfiguration sInstance;

    private WebServerConfiguration(Context context) {
        loadConfiguration(context);
    }

    public static WebServerConfiguration get(Context context) {
        if (sInstance == null) {
            sInstance = new WebServerConfiguration(context);
        }
        return sInstance;
    }

    @Override protected String getConfigurationFile() {
        return "webserver_configuration.json";
    }

    @Override public WebServerConfiguration loadConfiguration(Context context) {
        final WebServerConfiguration config =
                loadRawConfiguration(context, WebServerConfiguration.class);
        if (config == null) {
            return this;
        }

        this.root = config.root;
        this.port = config.port;

        this.useAuth = config.useAuth;
        this.username = config.username;
        this.password = config.password;

        return this;
    }

    @Override public WebServerConfiguration saveConfiguration(Context context) {
        saveConfigurationInternal(context);
        return this;
    }
}
