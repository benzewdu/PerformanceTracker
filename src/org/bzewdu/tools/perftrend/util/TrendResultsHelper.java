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

package org.bzewdu.tools.perftrend.util;

import org.bzewdu.tools.perftrend.config.Config;

import java.text.NumberFormat;
import java.util.*;

public class TrendResultsHelper extends Hashtable {
    private Config _cfg;

    public TrendResultsHelper() {
        super();
        this._cfg = Config.getConfig();
    }

    public void add(final TrendStruct ts) {
        final String build = ts.getBuild();
        final String name = ts.getSubScoreName();
        final Hashtable ht = (Hashtable) this.get(build);
        if (ht != null) {
            final ArrayList scorelist = (ArrayList) ht.get(name);
            if (scorelist != null) {
                scorelist.add(ts);
            } else {
                final ArrayList newList = new ArrayList();
                newList.add(ts);
                ht.put(name, newList);
            }
        } else {
            final Hashtable<String, ArrayList<TrendStruct>> newTable = new Hashtable<String, ArrayList<TrendStruct>>();
            final ArrayList<TrendStruct> newList = new ArrayList<TrendStruct>();
            newList.add(ts);
            newTable.put(name, newList);
            this.put(build, newTable);
        }
    }

    public boolean isHigherBetter(final String build, final String benchmark) {
        boolean retval = true;
        final ArrayList al = this.getScoreList(build, benchmark);
        if (al.size() > 0) {
            final TrendStruct ts = (TrendStruct) al.get(0);
            final int ishigher = ts.getIsHigher();
            if (ishigher == 0) {
                retval = false;
            }
        }
        return retval;
    }

    public int getMaxScore(final String benchmark) {
        final Enumeration e = this.keys();
        int maxcount = 0;
        while (e.hasMoreElements()) {
            final String build = (String) e.nextElement();
            final ArrayList al = this.getScoreList(build, benchmark);
            int tempcount = 0;
            for (Object anAl : al) {
                final TrendStruct ts = (TrendStruct) anAl;
                final String status = ts.getStatus();
                if (status.equals("PASS")) {
                    ++tempcount;
                }
            }
            maxcount = Math.max(maxcount, tempcount);
        }
        return maxcount;
    }

    public String getBuildName(final int num) {
        String retval = "";
        final Set s = this.keySet();
        final Object[] builds = s.toArray();
        Arrays.sort(builds);
        if (num < builds.length) {
            retval = (String) builds[num];
        }
        return retval;
    }

    public int getBuildCount() {
        final Set s = this.keySet();
        final Object[] builds = s.toArray();
        return builds.length;
    }

    public void printBuilds() throws Exception {
        final Set s = this.keySet();
        final Object[] builds = s.toArray();
        Arrays.sort(builds);
        final String build = "";
        for (final Object newVar : builds) {
            if (Config.debug) {
                System.out.print("[" + newVar + "] ");
            }
        }
    }

    private ArrayList getScoreList(final String build, final String name) {
        final Hashtable ht = (Hashtable) this.get(build);
        ArrayList al = new ArrayList();
        if (ht != null) {
            al = (ArrayList) ht.get(name);
            if (al == null) {
                al = new ArrayList();
            }
        }
        return al;
    }

    public int allScores(final String build, final String name) {
        return this.getScoreList(build, name).size();
    }

    public double getMean(final String build, final String name) {
        final ArrayList<Double> al = this.getPassedScoreList(build, name);
        double mean = 0.0;
        int num = 0;
        double total = 0.0;
        for (Double anAl : al) {
            ++num;
            total += anAl;
        }
        if (num > 0) {
            final NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            final String val = nf.format(total / num);
            final StringTokenizer st = new StringTokenizer(val, ",");
            String newval = "";
            while (st.hasMoreTokens()) {
                newval += st.nextToken();
            }
            mean = new Double(newval);
        }
        return mean;
    }

    public ArrayList<Double> getPassedScoreList(final String build, final String name) {
        final ArrayList al = this.getScoreList(build, name);
        final ArrayList<Double> returnList = new ArrayList<Double>();
        for (Object ts : al) {
            if (((TrendStruct) ts).getStatus().equals("PASS")) {
                returnList.add(((TrendStruct) ts).getScore());
            }
        }
        return returnList;
    }

    public String getFirstLog(final String build, final String name) {
        String retval = "";
        final ArrayList al = this.getScoreList(build, name);
        if (al.size() > 0) {
            final TrendStruct ts = (TrendStruct) al.get(0);
            if (ts != null) {
                retval = ts.getLog();
            }
        }
        return retval;
    }

    public Double getPassedScore(final String build, final String name, final int index) {
        Double retval = null;
        final ArrayList al = this.getScoreList(build, name);
        if (al.size() > index) {
            final TrendStruct ts = (TrendStruct) al.get(index);
            if (ts != null && ts.getStatus().equals("PASS")) {
                retval = ts.getScore();
            }
        }
        return retval;
    }
}
