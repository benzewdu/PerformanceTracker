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

import org.bzewdu.tools.perftrend.chart.ChartTrend;
import org.bzewdu.tools.perftrend.config.Config;
import org.bzewdu.tools.perftrend.util.TrendResultsHelper;

import java.io.File;

public class BenchHtml extends Html {
    private String _benchmark;
    private String _version;
    private TrendResultsHelper _results;

    public BenchHtml(final File htmlfile, final String benchmark, final String version, final TrendResultsHelper results) {
        super(new File(htmlfile, benchmark + ".html"), benchmark + " Summary");
        Config _cfg = Config.getConfig();
        this._benchmark = benchmark;
        this._version = version;
        this._results = results;
    }

    public void createChart() {
        String buildName = "_";
        try {
            String title = this._benchmark + " scores for " + this._htmlfile.getParentFile().getName();
            final int points = this._results.size();
            final int sample = this._results.getMaxScore(this._benchmark);
            final Double[][] xValues = new Double[sample][points];
            final Double[][] yValues = new Double[sample][points];
            String[] build = null;
            if (points > 0) {
                build = new String[points];
            }
            boolean allNull = true;
            boolean init = true;
            int k = 0;
            for (int j = 0; j < points; ++j) {
                buildName = this._results.getBuildName(j);
                build[j] = this._version + " " + buildName;
                for (int i = 0; i < sample; ++i) {
                    xValues[i][k] = (double) k;
                    final Double score = this._results.getPassedScore(buildName, this._benchmark, i);
                    if ((yValues[i][k] = score) != null) {
                        allNull = false;
                        if (init) {
                            title += this.getTitle(buildName);
                            init = false;
                        }
                    }
                }
                ++k;
            }
            final ChartTrend ct = new ChartTrend(new File(this._htmlfile.getParentFile(), this._benchmark + "_scatter.jpg"), title, points);
            ct.setAllNull(allNull);
            ct.setX(xValues);
            ct.setY(yValues);
            ct.setSample(sample);
            ct.setBuilds(build);
            ct.generateChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTitle(final String build) {
        String title = "";
        try {
            if (this._results.isHigherBetter(build, this._benchmark)) {
                title += " (higher is better)";
            } else {
                title += " (lower is better)";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    protected String createHeader() {
        final String html = "";
        return "";
    }

    protected String createBody() {
        return "<img src=" + this._benchmark + "_scatter.jpg>";
    }
}
