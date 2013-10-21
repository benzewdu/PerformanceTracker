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

package org.bzewdu.tools.perftrend.chartdataset;

public class LineXYDataset extends ScatterXYDataset {
    private int _set;

    /**
     *
     * @param x
     * @param y
     * @param p
     * @param s
     * @param set
     */
    public LineXYDataset(final Double[][] x, final Double[][] y, final int p, final int s, final int set) {
        super(x, y, p, s);
        this._set = set;
    }

    /**
     *
     * @param series
     * @return
     */
    public String getSeriesName(final int series) {
        return "Mean";
    }

    /**
     *
     * @param series
     * @param item
     * @return
     */
    public Number getYValue(final int series, final int item) {
        int count = 0;
        double total = 0.0;
        Double num = null;
        for (int i = 0; i < this._set; ++i) {
            final Double val = this.yValues[i][item];
            if (val != null) {
                total += val;
                ++count;
            }
        }
        if (count > 0) {
            num = total / count;
        }
        return num;
    }
}
