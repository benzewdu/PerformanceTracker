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


import com.jrefinery.data.AbstractSeriesDataset;
import com.jrefinery.data.XYDataset;

public class ScatterXYDataset extends AbstractSeriesDataset implements XYDataset {
    protected Double[][] xValues;
    protected Double[][] yValues;
    private int _points;
    private int _series;

    /**
     * @param x
     * @param y
     * @param p
     * @param s
     */
    public ScatterXYDataset(final Double[][] x, final Double[][] y, final int p, final int s) {
        super();
        this.xValues = x;
        this.yValues = y;
        this._points = p;
        this._series = s;
    }

    /**
     * @param series
     * @param item
     * @return
     */
    public Number getXValue(final int series, final int item) {
        return this.xValues[series][item];
    }

    public Number getY(int i, int i2) {
        return null;
    }

    /**
     * @param series
     * @param item
     * @return
     */
    public Number getYValue(final int series, final int item) {
        return this.yValues[series][item];
    }

    public int getSeriesCount() {
        return this._series;
    }

    public Comparable getSeriesKey(int i) {
        return null;
    }

    public String getSeriesName(final int series) {
        return "Sample " + series;
    }

    public int getItemCount(final int series) {
        return this._points;
    }

    public Number getX(int i, int i2) {
        return null;
    }
}
