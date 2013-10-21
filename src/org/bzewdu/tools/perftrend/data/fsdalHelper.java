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
import org.bzewdu.tools.perftrend.util.Combo;
import org.bzewdu.tools.perftrend.util.TrendResultsHelper;
import org.bzewdu.tools.perftrend.util.TrendStruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class fsdalHelper extends dalHelper {
    static HashMap<String, HashMap<String, TreeSet<String>>> hm;
    static boolean initCompleted;

    public dalHelper setupHelper() {
        if (fsdalHelper.hm == null) {
            retreivefromfsVersionsBuild();
        }
        while (!fsdalHelper.initCompleted) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (fsdalHelper.hm != null) {
            return new fsdalHelper();
        }
        throw new NullPointerException("Map not init'ed");
    }

    public TrendResultsHelper getResults(final String type, final Combo combo, final String version) {
        final TrendResultsHelper ar = new TrendResultsHelper();
        final String _os = combo.getOS();
        final String _arch = combo.getArch();
        final String _compiler = combo.getCompiler();
        final String _bit = combo.getBit();
        BufferedReader in = null;
        final TreeSet<String> list = fsdalHelper.hm.get(type).get(version);
        if (list.size() > 1) {
            for (final String build : list) {
                final String log = Config.configAllprop.getProperty("perftrend_logs") + type + File.separator + version + build + "_" + Config._idproperties.getProperty("os." + _os) + Config._idproperties.getProperty("arch." + _arch) + Config._idproperties.getProperty("compiler." + _compiler) + _bit;
                if (new File(log).exists()) {
                    final HashSet<String> logs = this.getXMLLogFilenames(log);
                    if (Config.debug) {
                        System.err.println(logs);
                    }
                    try {
                        for (final String fname : logs) {
                            final File file = new File(fname);
                            in = new BufferedReader(new FileReader(file));
                            String str;
                            while ((str = in.readLine()) != null) {
                                final int index;
                                if ((index = str.indexOf("<sub_score")) >= 0) {
                                    String benchname = null;
                                    String score = null;
                                    String logsName = null;
                                    String ihb = null;
                                    String status = null;
                                    final String _s = str.substring(index, str.length());
                                    final String[] arr$;
                                    final String[] __s = arr$ = _s.split(" ");
                                    for (final String value : arr$) {
                                        if (value.contains("status=")) {
                                            status = value.split("=")[1].replace('\"', ' ').trim();
                                            if (!status.equalsIgnoreCase("pass")) {
                                                break;
                                            }
                                        } else if (value.contains("name=")) {
                                            benchname = value.split("=")[1].replace('\"', ' ').trim();
                                        } else if (value.contains("log=")) {
                                            logsName = value.split("=")[1].replace('\"', ' ').replace('>', ' ').trim();
                                        } else if (value.contains("score=")) {
                                            score = value.split("=")[1].replace('\"', ' ').trim();
                                        } else if (value.contains("is_higher_better=")) {
                                            ihb = value.split("=")[1].replace('\"', ' ').trim();
                                        }
                                    }
                                    if (Config.debug) {
                                        System.err.println(Arrays.toString(__s) + " :: " + benchname + " :: " + logsName + " :: " + score + " :: " + status + " :: " + ihb);
                                    }
                                    if (benchname == null || score == null || logsName == null || ihb == null || status == null) {
                                        if (!Config.debug) {
                                            continue;
                                        }
                                        System.err.println("\n\nOne of more elements NULL\n\n");
                                    } else {
                                        final TrendStruct tdt = new TrendStruct();
                                        if (build == null || benchname == null || logsName == null || status == null) {
                                            continue;
                                        }
                                        tdt.setBuild(build);
                                        tdt.setSubScoreName(benchname);
                                        tdt.setLog(logsName);
                                        tdt.setStatus(status);
                                        final double dblScore;
                                        tdt.setScore(dblScore = Double.parseDouble(score));
                                        tdt.setIsHigher(Integer.parseInt(ihb));
                                        if (dblScore <= 0.0 || !status.equals("PASS")) {
                                            continue;
                                        }
                                        ar.add(tdt);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (in == null) {
                            continue;
                        }
                        try {
                            in.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        if (Config.debug) {
            System.out.println(ar + "\nEnd\n\n\n");
        }
        return ar;
    }

    private HashSet<String> getXMLLogFilenames(final String log) {
        final HashSet<String> logList = new HashSet<String>();
        final File logfile;
        if ((logfile = new File(log)).exists()) {
            final String[] arr$;
            final String[] logPathlist = arr$ = logfile.list();
            for (final String runLogPath : arr$) {
                final String[] arr$2;
                final String[] runFiles = arr$2 = new File(log + File.separator + runLogPath).list();
                for (final String files : arr$2) {
                    if (files.indexOf("esult.") > 0 && files.indexOf(".xml") > 0) {
                        final String tmpfilename;
                        if (!new File(tmpfilename = log + File.separator + runLogPath + File.separator + files).exists()) {
                            throw new NullPointerException("Found result.xml file??");
                        }
                        logList.add(tmpfilename);
                    }
                }
            }
        }
        return logList;
    }

    public Set getVersions(final String type) {
        if (Config.debug) {
            System.err.println(type);
        }
        final HashMap<String, TreeSet<String>> _hm = fsdalHelper.hm.get(type);
        if (_hm == null || _hm.size() == 0) {
            return null;
        }
        return _hm.keySet();
    }

    public TreeSet getBuilds(final String type, final String version) {
        return (fsdalHelper.hm.get(type).get(version).size() > 1) ? fsdalHelper.hm.get(type).get(version) : null;
    }

    static synchronized void retreivefromfsVersionsBuild() {
        final String logs = Config.configAllprop.getProperty("perftrend_logs");
        if (Config.debug) {
            System.err.println(logs);
        }
        String[] _vb_str = null;
        final File topLogDir = new File(logs);
        fsdalHelper.hm = new HashMap<String, HashMap<String, TreeSet<String>>>();
        if (topLogDir.isDirectory()) {
            final String[] arr$;
            final String[] typeDir = arr$ = topLogDir.list();
            for (final String runType : arr$) {
                final File typeLogDir = new File(topLogDir + File.separator + runType);
                if (typeLogDir.isDirectory()) {
                    final String[] arr$2;
                    final String[] buildLogDir = arr$2 = typeLogDir.list();
                    for (final String buildLog : arr$2) {
                        _vb_str = getVersionAndBuildStrings(buildLog);
                        HashMap<String, TreeSet<String>> _vb_map;
                        if ((_vb_map = fsdalHelper.hm.get(runType)) == null) {
                            _vb_map = new HashMap<String, TreeSet<String>>();
                        }
                        TreeSet<String> buildList;
                        if ((buildList = _vb_map.get(_vb_str[0])) == null) {
                            buildList = new TreeSet<String>();
                        }
                        if (!buildList.contains(_vb_str[1])) {
                            buildList.add(_vb_str[1]);
                            _vb_map.put(_vb_str[0], buildList);
                            fsdalHelper.hm.put(runType, _vb_map);
                        }
                    }
                }
            }
        }
        fsdalHelper.initCompleted = true;
        if (Config.debug) {
            System.err.println("initCompleted ... " + fsdalHelper.hm);
        }
    }

    private static String[] getVersionAndBuildStrings(final String buildLog) {
        int endChar = 5;
        if (buildLog.length() < 5) {
            throw new NullPointerException("Invalid path");
        }
        if (buildLog.indexOf("baseline") == 0) {
            return new String[]{"baseline", buildLog.substring(8, buildLog.lastIndexOf(95))};
        }
        if (buildLog.charAt(5) == '_') {
            endChar = 8;
        }
        final String version = buildLog.substring(0, endChar);
        final String build = buildLog.substring(endChar, buildLog.lastIndexOf(95));
        return new String[]{version, build};
    }

    public Hashtable getJobsInQueue(final String version) {
        return new Hashtable();
    }

    public static void main(final String[] args) {
    }
}
