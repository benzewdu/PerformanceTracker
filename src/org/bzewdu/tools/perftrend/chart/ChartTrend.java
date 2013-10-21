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

package org.bzewdu.tools.perftrend.chart;

import com.jrefinery.chart.*;
import com.jrefinery.chart.tooltips.StandardXYToolTipGenerator;
import com.jrefinery.data.XYDataset;
import org.bzewdu.tools.perftrend.chartdataset.LineXYDataset;
import org.bzewdu.tools.perftrend.chartdataset.ScatterXYDataset;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

public class ChartTrend {
    private File _file;
    private String _title;
    private int _xval;
    private int _yval;
    private int _points;
    Double[][] x;
    Double[][] y;
    String[] builds;
    int sample;
    boolean allNull;

    public void setAllNull(final boolean allNull) {
        this.allNull = allNull;
    }

    public void setSample(final int sample) {
        this.sample = sample;
    }

    public void setY(final Double[][] y) {
        this.y = y;
    }

    public void setX(final Double[][] x) {
        this.x = x;
    }

    public void setBuilds(final String[] builds) {
        this.builds = builds;
    }

    public ChartTrend(final File file, final String title, final int points) {
        super();
        this._file = file;
        this._title = title;
        this._points = points;
        this._xval = 600 + points * 10;
        this._yval = 500;
    }

    public void generateChart() {
        final OverlaidXYPlot oplot = new OverlaidXYPlot("Build", "Score");
        final VerticalNumberAxis yva = new VerticalNumberAxis("Scores");
        oplot.setRangeAxis(yva);
        ValueAxis xva;
        if (this.builds != null) {
            final HorizontalSymbolicAxis xAxis = new HorizontalSymbolicAxis("Builds", this.builds);
            xAxis.setGridLinesVisible(true);
            xAxis.setVerticalTickLabels(true);
            xAxis.setSymbolicGridPaint(Color.white);
            final NumberTickUnit ntu = new NumberTickUnit(1.0, NumberFormat.getInstance());
            xAxis.setTickUnit(ntu);
            oplot.setDomainAxis(xAxis);
            xva = xAxis;
        } else {
            final HorizontalNumberAxis hna = new HorizontalNumberAxis("Builds");
            oplot.setDomainAxis(hna);
            xva = hna;
        }
        if (!this.allNull) {
            final XYDataset line = new LineXYDataset(this.x, this.y, this._points, 1, this.sample);
            final XYPlot lplot = new XYPlot(line, xva, yva);
            final XYDataset data = new ScatterXYDataset(this.x, this.y, this._points, this.sample);
            final XYPlot plot = new XYPlot(data, xva, yva);
            plot.setXYItemRenderer(new StandardXYItemRenderer(1, new StandardXYToolTipGenerator()));
            final NumberAxis axis = (NumberAxis) plot.getRangeAxis();
            axis.setAutoRangeIncludesZero(false);
            oplot.add(plot);
            oplot.add(lplot);
        }
        final JFreeChart chart = new JFreeChart(this._title, JFreeChart.DEFAULT_TITLE_FONT, oplot, true);
        chart.setBackgroundPaint(new GradientPaint(0.0f, 0.0f, Color.white, 0.0f, 1000.0f, Color.orange));
        final XYPlot xyp = (XYPlot) chart.getPlot();
        final NumberAxis range = (NumberAxis) xyp.getRangeAxis();
        range.setAutoRangeIncludesZero(false);
        this.writeToJPG(chart);
    }

    private void writeToJPG(final JFreeChart chart) {
        try {
            final FileOutputStream f = new FileOutputStream(this._file);
            ChartUtilities.writeChartAsJPEG(f, chart, this._xval, this._yval);
            f.flush();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
