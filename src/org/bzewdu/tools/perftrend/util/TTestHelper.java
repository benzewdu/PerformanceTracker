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

public class TTestHelper {
    public static double logGamma(final double xx) {
        final double stp = 2.50662827465;
        final double[] cof = {76.18009173, -86.50532033, 24.01409822, -1.231739516, 0.00120858003, -5.36382E-6};
        double x = xx - 1.0;
        double tmp = x + 5.5;
        tmp = (x + 0.5) * Math.log(tmp) - tmp;
        double ser = 1.0;
        for (int j = 0; j < 6; ++j) {
            ++x;
            ser += cof[j] / x;
        }
        final double retVal = tmp + Math.log(2.50662827465 * ser);
        return retVal;
    }

    public static double gamma(double x) {
        double g = 1.0;
        double f;
        if (x > 0.0) {
            while (x < 3.0) {
                g *= x;
                ++x;
            }
            f = (1.0 - 2.0 / (7.0 * Math.pow(x, 2.0)) * (1.0 - 2.0 / (3.0 * Math.pow(x, 2.0)))) / (30.0 * Math.pow(x, 2.0));
            f = (1.0 - f) / (12.0 * x) + x * (Math.log(x) - 1.0);
            f = Math.exp(f) / g * Math.pow(6.283185307179586 / x, 0.5);
        } else {
            f = Double.POSITIVE_INFINITY;
        }
        return f;
    }

    static double betacf(final double a, final double b, final double x) {
        final int maxIterations = 50;
        int m = 1;
        final double eps = 3.0E-5;
        double am = 1.0;
        double bm = 1.0;
        double az = 1.0;
        final double qab = a + b;
        final double qap = a + 1.0;
        final double qam = a - 1.0;
        double bz = 1.0 - qab * x / qap;
        double ap;
        double bp;
        double app;
        double bpp;
        for (double aold = 0.0; m < 50 && Math.abs(az - aold) >= 3.0E-5 * Math.abs(az); aold = az, am = ap / bpp, bm = bp / bpp, az = app / bpp, bz = 1.0, ++m) {
            final double em = m;
            final double tem = em + em;
            double d = em * (b - m) * x / ((qam + tem) * (a + tem));
            ap = az + d * am;
            bp = bz + d * bm;
            d = -(a + em) * (qab + em) * x / ((a + tem) * (qap + tem));
            app = ap + d * az;
            bpp = bp + d * bz;
        }
        return az;
    }

    public static double betai(final double a, final double b, final double x) {
        double bt = 0.0;
        if (x == 0.0 || x == 1.0) {
            bt = 0.0;
        } else if (x > 0.0 && x < 1.0) {
            bt = gamma(a + b) * Math.pow(x, a) * Math.pow(1.0 - x, b) / (gamma(a) * gamma(b));
        }
        double beta;
        if (x < (a + 1.0) / (a + b + 2.0)) {
            beta = bt * betacf(a, b, x) / a;
        } else {
            beta = 1.0 - bt * betacf(b, a, 1.0 - x) / b;
        }
        return beta;
    }

    public static double fDist(final double v1, final double v2, final double f) {
        final double p = betai(v1 / 2.0, v2 / 2.0, v1 / (v1 + v2 * f));
        return p;
    }

    public static double student_c(final double v) {
        return Math.exp(logGamma((v + 1.0) / 2.0)) / (Math.sqrt(3.141592653589793 * v) * Math.exp(logGamma(v / 2.0)));
    }

    public static double student_tDen(final double v, final double t) {
        return student_c(v) * Math.pow(1.0 + t * t / v, -0.5 * (v + 1.0));
    }

    public static double stDist(final double v, final double t) {
        double sm = 0.5;
        double sign = 1.0;
        final double stepSize = t / 5000.0;
        if (t < 0.0) {
            sign = -1.0;
        } else if (t == 0.0 || Double.isInfinite(t)) {
            return sm;
        }
        for (double u = 0.0; u <= sign * t; u += stepSize) {
            sm += stepSize * student_tDen(v, u);
        }
        if (sign < 0.0) {
            sm = 0.5 - sm;
        } else {
            sm = 1.0 - sm;
        }
        if (sm < 0.0) {
            sm = 0.0;
        } else if (sm > 1.0) {
            sm = 1.0;
        }
        return sm;
    }
}
