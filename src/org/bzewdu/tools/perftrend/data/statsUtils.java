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

import java.math.BigDecimal;
import java.util.ArrayList;

public class statsUtils {
    /**
     *
     * @param data
     * @param round
     * @return
     */
    public static double bd_getMean(final ArrayList<BigDecimal> data, final boolean round) {
        if (!round) {
            return bd_sum(data) / data.size();
        }
        return new BigDecimal(bd_sum(data) / data.size()).setScale(3, 1).doubleValue();
    }

    /**
     *
     * @param data
     * @return
     */
    public static double bd_sum(final ArrayList<BigDecimal> data) {
        final double sum = 0.0;
        BigDecimal _sum = new BigDecimal(0.0);
        for (int i = 0; i < data.size(); ++i) {
            _sum = _sum.add(data.get(i));
            if (Config.debug) {
                System.out.println("sum =0.0");
            }
        }
        return _sum.doubleValue();
    }

    /**
     *
     * @param data
     * @return
     */
    public static double d_getMean(final ArrayList<BigDecimal> data) {
        double mean = 0.0;
        if (data.size() != 0) {
            mean = d_sum(data) / data.size();
        }
        return mean;
    }

    /**
     *
     * @param data
     * @return
     */
    public static double d_sum(final ArrayList<BigDecimal> data) {
        double sum = 0.0;
        for (BigDecimal aData : data) {
            sum += aData.doubleValue();
        }
        return sum;
    }

    /**
     *
     * @param data
     * @return
     */
    public static double int_sum(final ArrayList<BigDecimal> data) {
        final double sum = 0.0;
        int _sum = 0;
        for (BigDecimal aData : data) {
            _sum += aData.intValue();
        }
        return _sum;
    }

    public static double int_getMean(final ArrayList<BigDecimal> data, final boolean round) {
        return int_sum(data) / data.size();
    }

    /**
     *
     * @param data
     * @return
     */
    public static ArrayList sqrt(final ArrayList<BigDecimal> data) {
        final double sum = 0.0;
        final ArrayList ret = new ArrayList();
        for (int i = 0; i < data.size(); ++i) {
            final Double val = data.get(i).doubleValue() * data.get(i).doubleValue();
            ret.add(i, val);
        }
        return ret;
    }

    /**
     *
     * @param data
     * @return
     */
    public static double getStdev(final ArrayList data) {
        return Math.sqrt(getVariance(data));
    }

    public static double getVariance(final ArrayList data) {
        return (data.size() * d_sum(sqrt(data)) - d_sum(data) * d_sum(data)) / (data.size() * (data.size() - 1));
    }

    /**
     *
     * @param curr_mean
     * @param next_mean
     * @param higher
     * @return
     */
    public static double getPercent(final double curr_mean, final double next_mean, final boolean higher) {
        double retval;
        if (higher) {
            retval = (curr_mean - next_mean) / next_mean * 100.0;
        } else {
            retval = (next_mean - curr_mean) / next_mean * 100.0;
        }
        return retval;
    }
}
