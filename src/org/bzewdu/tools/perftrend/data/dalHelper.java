/*
 * Copyright bzewdu
 *
 * 	Licensed under the Apache License, Version 2.0 (the "License");
 * 	you may not use this file except in compliance with the License.
 * 	You may obtain a copy of the License at
 *
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */

package org.bzewdu.tools.perftrend.data;

import org.bzewdu.tools.perftrend.config.Config;
import org.bzewdu.tools.perftrend.util.Combo;
import org.bzewdu.tools.perftrend.util.TrendResultsHelper;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

public abstract class dalHelper {
    static dalHelper dalhelper;
    static int counter;
    static Config _cfg;
    public static HashMap<String, TreeSet<String>> versionsMap;
    public static HashMap<String, TreeSet<String>> buildMap;
    public static HashMap<String, Hashtable> jobsMap;

    public static dalHelper getdalHelper() {
        return dalHelper.dalhelper;
    }

    public abstract dalHelper setupHelper();

    public abstract TrendResultsHelper getResults(final String p0, final Combo p1, final String p2);

    public abstract Set<String> getVersions(final String p0);

    public abstract TreeSet<String> getBuilds(final String p0, final String p1);

    public abstract Hashtable getJobsInQueue(final String p0);

    static {
        try {
            final String _className = Config.configAllprop.getProperty("dalHelper");
            final Class dalClass = Class.forName(_className);
            (dalHelper.dalhelper = (dalHelper) dalClass.newInstance()).setupHelper();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }
        dalHelper.versionsMap = new HashMap<String, TreeSet<String>>();
        dalHelper.buildMap = new HashMap<String, TreeSet<String>>();
        dalHelper.jobsMap = new HashMap<String, Hashtable>();
    }
}
