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

package org.bzewdu.tools.perftrend.htmlgen;

import org.bzewdu.tools.perftrend.config.Config;
import org.bzewdu.tools.perftrend.data.statsUtils;
import org.bzewdu.tools.perftrend.util.Combo;
import org.bzewdu.tools.perftrend.util.TTest;
import org.bzewdu.tools.perftrend.util.TrendResultsHelper;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeSet;

public abstract class SummaryHtml extends Html {
    private String _version;
    private Config _cfg;
    private Hashtable _ht;

    protected abstract String createBody();

    protected abstract String createHeader();

    public SummaryHtml(final File htmlfile, final String title, final String version, final Hashtable ht) {
        super(htmlfile, title);
        this._version = version;
        this._cfg = Config.getConfig();
        this._ht = ht;
    }

    protected String getTable() {
        final String html = "<table border=1><tr><td><b>Status</td><td><b>Display</td></tr><tr><td>Pass</td><td>33.19/+02%</td></tr><tr><td>Fail</td><td><u>-/-</u></td></tr><tr><td>Delete</td><td><u>FAIL</u></td></tr><tr><td>In Queue</td><td><b>...</td></tr><tr><td>Not In Queue</td><td>N/A</td><tr></table>\n";
        return "<table border=1><tr><td><b>Status</td><td><b>Display</td></tr><tr><td>Pass</td><td>33.19/+02%</td></tr><tr><td>Fail</td><td><u>-/-</u></td></tr><tr><td>Delete</td><td><u>FAIL</u></td></tr><tr><td>In Queue</td><td><b>...</td></tr><tr><td>Not In Queue</td><td>N/A</td><tr></table>\n";
    }

    protected String printTypeInfo(final String build) {
        return "<td align=left>" + build + "</td>";
    }

    protected String printHtmlPlatform(final String build, final TreeSet<String> buildlist, final String benchmark, final Combo combo, final TrendResultsHelper tr) {
        String html = "";
        String color = "gray";
        String valstr = "";
        boolean bold = false;
        int totalScores = 0;
        double curr_mean = 0.0;
        ArrayList current = null;
        try {
            totalScores = tr.allScores(build, benchmark);
            curr_mean = tr.getMean(build, benchmark);
            current = tr.getPassedScoreList(build, benchmark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (totalScores > 0 && curr_mean == 0.0) {
            try {
                final File file = new File(tr.getFirstLog(build, benchmark));
                final String logstr = file.getParent();
                valstr = "<a href=\"http://perftrend" + logstr + "\">-/-</a>";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (totalScores == 0) {
            if (this.comboInQueue(combo.getConcatFullName(), build)) {
                valstr = "<b>...</b>";
            } else {
                valstr = "N/A";
            }
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            final String mean = df.format(curr_mean);
            ArrayList next = null;
            double next_mean = 0.0;
            boolean foundbuild = false;
            for (final String currbuild : buildlist) {
                if (currbuild.equals(build)) {
                    foundbuild = true;
                } else {
                    if (!foundbuild) {
                        continue;
                    }
                    try {
                        next_mean = tr.getMean(currbuild, benchmark);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (next_mean != 0.0) {
                        try {
                            next = tr.getPassedScoreList(currbuild, benchmark);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        break;
                    }
                }
            }
            String percentStr;
            if (next != null) {
                assert current != null;
                final double pval = TTest.compute(curr_mean, statsUtils.getVariance(current), current.size(), next_mean, statsUtils.getVariance(next), next.size());
                boolean high = false;
                try {
                    high = tr.isHigherBetter(build, benchmark);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                final double percent = statsUtils.getPercent(curr_mean, next_mean, high);
                if (percent > 0.0 && pval < 0.05) {
                    color = "green";
                    bold = true;
                } else if (percent < 0.0 && pval < 0.05) {
                    color = "red";
                    bold = true;
                }
                df = new DecimalFormat("+00;-00");
                percentStr = df.format(percent);
            } else {
                percentStr = "+00";
            }
            percentStr += "%";
            valstr = mean + "/" + percentStr;
        }
        html = html + "<td align=center><font color=" + color + ">";
        if (bold) {
            html += "<b>";
        }
        html = html + valstr + "</font></td>\n";
        return html;
    }

    private boolean comboInQueue(final String name, final String build) {
        boolean retval = false;
        if (this._ht.get(name + build) != null) {
            retval = true;
        }
        return retval;
    }
}
