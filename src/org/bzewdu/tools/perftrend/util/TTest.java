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

public class TTest {
    public static double compute(final double am, final double av, final int an, final double bm, final double bv, final int bn) {
        final randomVar_ rv1 = new randomVar_();
        rv1.mean = am;
        rv1.n = an;
        final randomVar_ rv2 = new randomVar_();
        rv2.mean = bm;
        rv2.n = bn;
        final TTestHelper sf = new TTestHelper();
        double fAlpha;
        if (av > bv) {
            final double f = av / bv;
            fAlpha = TTestHelper.fDist(rv1.n - 1.0, rv2.n - 1.0, f);
        } else {
            final double f = bv / av;
            fAlpha = TTestHelper.fDist(rv2.n - 1.0, rv1.n - 1.0, f);
        }
        final String newln = System.getProperty("line.separator");
        String comment;
        double df;
        double t;
        if (fAlpha <= 0.005) {
            comment = "An F test on the sample variances indicates that they are " + newln + "probably not from the same population (the variances " + newln + "can't be pooled), at an alpha level of " + fAlpha + "." + newln + "Thus, the t-test was set up for samples with unequal varainces. " + newln + "(The degrees of freedom were adjusted.)";
            final double svn1 = av / rv1.n;
            final double svn2 = bv / rv2.n;
            df = Math.pow(svn1 + svn2, 2.0) / (Math.pow(svn1, 2.0) / (rv1.n + 1.0) + Math.pow(svn2, 2.0) / (rv2.n + 1.0)) - 2.0;
            t = Math.abs(rv1.mean - rv2.mean) / Math.sqrt(av / rv1.n + bv / rv2.n);
        } else {
            comment = "An F test on the sample variances indicates that they could be " + newln + "from the same population, (alpha level of 0.005)." + newln + "Accordingly, the t-test was set up for samples with equal population variance.";
            df = rv1.n + rv2.n - 2.0;
            final double sp = Math.sqrt(((rv1.n - 1.0) * av + (rv2.n - 1.0) * bv) / (rv1.n + rv2.n - 2.0));
            t = Math.abs(rv1.mean - rv2.mean) * Math.sqrt(rv1.n * rv2.n / (rv1.n + rv2.n)) / sp;
        }
        final double pVal = TTestHelper.stDist(df, t);
        String pValComment = "" + pVal;
        String comment2;
        if (pVal <= 0.01) {
            comment2 = "This probability indicates that there is a difference in sample means.";
            if (pVal <= 1.0E-4) {
                pValComment = "< 0.0001";
            }
        } else if (pVal <= 0.05) {
            comment2 = "This probability indicates that there may be a difference in sample means.";
        } else {
            comment2 = "There is not a significant difference in the sample means. " + newln + "A difference could not be detected due to large variability, small sample size, " + newln + "or both. Of course, it's possible that the samples really are from the same population!";
        }
        if (rv1.n == 0.0 || rv2.n == 0.0) {
            comment2 = "There is a problem with the data. Valid delimiters are space, " + newln + "comma, tab and newline.";
            comment = "";
        }
        final double sv1 = Math.sqrt(av);
        final double sv2 = Math.sqrt(bv);
        final char tab = '\t';
        final String cs = "Student's t-Test for Comparing Things " + newln + "This test determines if there is a significant " + newln + "difference between " + "the averages of the two samples above. " + newln + newln + "Mean of first data set                   :" + '\t' + rv1.mean + newln + "Standard deviation of first data set     :" + '\t' + sv1 + newln + "Number of observations in the first set  :" + '\t' + rv1.n + newln + "Mean of second data set                  :" + '\t' + rv2.mean + newln + "Standard deviation of second data set    :" + '\t' + sv2 + newln + "Number of observations in the second set :" + '\t' + rv2.n + newln + newln + "Degrees of freedom   :" + '\t' + df + newln + "t Value (one-tailed) :" + '\t' + t + newln + "P(x>t)               :" + '\t' + pValComment + newln + newln + comment2 + newln + newln + comment;
        return pVal;
    }
}
