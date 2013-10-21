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
import org.bzewdu.tools.perftrend.data.dalHelper;
import org.bzewdu.tools.perftrend.util.Combo;
import org.bzewdu.tools.perftrend.util.TrendResultsHelper;

import java.io.File;
import java.util.Hashtable;
import java.util.TreeSet;

public class PlatformHtml extends SummaryHtml {
    private static int SAMPLE;
    private String _type;
    private String[] _benchmarks;
    private Combo _combo;
    private Config _cfg;
    private String _version;
    private TrendResultsHelper _results;
    private File _location;
    private dalHelper _db;
    private TreeSet<String> _builds;

    public PlatformHtml(final String type, final Combo c, final String version, final String[] bench, final File f, final String title, final Hashtable ht) {
        super(f, title, version, ht);
        this._type = type;
        this._combo = c;
        this._version = version;
        this._benchmarks = bench;
        this._db = dalHelper.getdalHelper();
        this._results = this._db.getResults(this._type, c, this._version);
        this._builds = this._db.getBuilds(this._type, this._version);
    }

    public void createBenchmarkHtml() {
        for (final String newVar : this._benchmarks) {
            final File f = this._htmlfile.getParentFile();
            f.mkdirs();
            final BenchHtml b = new BenchHtml(f, newVar, this._version, this._results);
            b.createChart();
        }
    }

    protected String createHeader() {
        String html = "";
        html += this.getTable();
        html = html + "<table><tr><td  colspan=" + this._benchmarks.length + "><font size=+2>" + this._version + "</font></td></tr>\n" + "<tr><td align=center><i>Build</i></td>";
        for (final String newVar : this._benchmarks) {
            html += "<td align=center><i><a href=";
            html += newVar;
            html += "_scatter.jpg>";
            html += newVar;
            html += "</i></td>\n";
        }
        return html;
    }

    protected String createBody() {
        final StringBuffer sb = new StringBuffer();
        if (this._builds != null) {
            for (final String buildname : this._builds) {
                sb.append("<tr>");
                sb.append(this.printTypeInfo(buildname));
                for (final String newVar : this._benchmarks) {
                    sb.append(this.printHtmlPlatform(buildname, this._builds, newVar, this._combo, this._results));
                }
                sb.append("</tr>\n");
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    static {
        PlatformHtml.SAMPLE = 10;
    }
}
