/*
 *  Copyright (C) 2013 - 2014 Alexander "Evisceration" Martinz
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
package org.namelessrom.devicecontrol.modules.preferences;

import android.support.v4.app.Fragment;

import org.namelessrom.devicecontrol.DeviceConstants;
import org.namelessrom.devicecontrol.R;
import org.namelessrom.devicecontrol.ui.views.AttachViewPagerFragment;

import java.util.ArrayList;

public class PreferencesFragment extends AttachViewPagerFragment {

    @Override protected int getFragmentId() { return DeviceConstants.ID_PREFERENCES; }

    @Override public ViewPagerAdapter getPagerAdapter() {
        final ArrayList<Fragment> fragments = new ArrayList<>(1);
        final ArrayList<CharSequence> titles = new ArrayList<>(1);

        fragments.add(new MainPreferencesFragment());
        titles.add(getString(R.string.general));

        return new ViewPagerAdapter(getChildFragmentManager(), fragments, titles);
    }

}
