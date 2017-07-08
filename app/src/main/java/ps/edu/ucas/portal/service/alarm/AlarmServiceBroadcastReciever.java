/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package ps.edu.ucas.portal.service.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import io.realm.Realm;
import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.ui.SettingsActivity;
import ps.edu.ucas.portal.utils.UtilityUCAS;

public class AlarmServiceBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!UtilityUCAS.isLogin())
			return;
		PreferenceManager.setDefaultValues(context, R.xml.preference, false);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		if (!sharedPreferences.getBoolean(SettingsActivity.MY_NOTIFICATIONS_SETTING,true))
			return;
		Intent serviceIntent = new Intent(context, AlarmService.class);
		context.startService(serviceIntent);
	}

}
