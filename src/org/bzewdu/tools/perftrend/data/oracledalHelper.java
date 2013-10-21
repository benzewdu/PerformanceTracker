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
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.TreeSet;

public class oracledalHelper extends dalHelper {
    public static final String DBNAME = "oracle";
    private static String conString;
    private static String username;
    private static String passwd;
    private static DataSource dataSource;
    static int counter;

    public oracledalHelper() {
        super();
        if (Config.debug) {
            System.out.println("cons()");
        }
    }

    public synchronized dalHelper setupHelper() {
        oracledalHelper._cfg = Config.getConfig();
        ++oracledalHelper.counter;
        if (Config.debug) {
            System.out.println(oracledalHelper.counter + ". Setting up data source.");
        }
        oracledalHelper.dataSource = setupDataSource(oracledalHelper.conString);
        if (Config.debug) {
            System.out.println(oracledalHelper.counter + ". Done.");
        }
        oracledalHelper.dalhelper = new oracledalHelper();
        if (Config.debug) {
            System.out.println(oracledalHelper.counter + ". " + oracledalHelper.dalhelper);
        }
        return oracledalHelper.dalhelper;
    }

    /**
     *
     * @param connectURI
     * @return
     */
    public static DataSource setupDataSource(final String connectURI) {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUsername(oracledalHelper.username);
        ds.setPassword(oracledalHelper.passwd);
        ds.setUrl(connectURI);
        return ds;
    }

    /**
     *
     * @param ds
     */
    public static void printDataSourceStats(final DataSource ds) {
        final BasicDataSource bds = (BasicDataSource) ds;
        if (Config.debug) {
            System.out.println("NumActive: " + bds.getNumActive());
        }
        if (Config.debug) {
            System.out.println("NumIdle: " + bds.getNumIdle());
        }
    }

    /**
     *
     * @param ds
     * @throws SQLException
     */
    public static void shutdownDataSource(final DataSource ds) throws SQLException {
        BasicDataSource bds;
        if (ds != null) {
            bds = (BasicDataSource) ds;
        } else {
            bds = (BasicDataSource) oracledalHelper.dataSource;
        }
        bds.close();
    }

    /**
     *
     * @param type
     * @param combo
     * @param version
     * @return
     */
    public TrendResultsHelper getResults(final String type, final Combo combo, final String version) {
        if (Config.debug) {
            System.out.println("Begin: " + type + " " + combo + " " + version);
        }
        final long start = System.currentTimeMillis();
        final String query = "select jvm_info.build, sub_score.name, sub_score.log, sub_score.status, sub_score.score, sub_score.is_higher_better  from jvm_info, computer, workload_score, os, sub_score  where  jvm_info.version = '" + version + "' and os.name ='" + combo.getOS() + "' and os.arch = '" + combo.getArch() + "' and jvm_info.compiler = '" + combo.getCompiler() + "' and jvm_info.size_ = '" + combo.getBit() + "' and  workload_score.computer_id = computer.computer_id  " + " and computer.jvm_info_id = jvm_info.jvm_info_id  " + " and sub_score.scorestatustbl_id = 1  " + " and sub_score.name not in ('reference_client', 'reference_server')  " + " and sub_score.scorestatustbl_id = 1  " + " and sub_score.workload_score_id = workload_score.workload_score_id " + " and computer.os_id = os.os_id " + " and jvm_info.type_ = '" + type + "' " + " order by jvm_info.build DESC, sub_score.name";
        final TrendResultsHelper ar = new TrendResultsHelper();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = oracledalHelper.dataSource.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                final TrendStruct tdt = new TrendStruct();
                final String build = rs.getString("build");
                final String name = rs.getString("name");
                final String log = rs.getString("log");
                final String status = rs.getString("status");
                final double score = rs.getDouble("score");
                final int higher = rs.getInt("is_higher_better");
                if (build != null && name != null && log != null && status != null) {
                    tdt.setBuild(build);
                    tdt.setSubScoreName(name);
                    tdt.setLog(log);
                    tdt.setStatus(status);
                    tdt.setScore(score);
                    tdt.setIsHigher(higher);
                    if (score != 0.0 && status.equals("PASS")) {
                        ar.add(tdt);
                    }
                }
                tdt.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanup(con, ps, rs);
        }
        if (Config.debug) {
            System.out.println("End: " + type + " " + combo + " " + version + " " + (System.currentTimeMillis() - start));
        }
        return ar;
    }

    /**
     *
     * @param con
     * @param ps
     * @param rs
     */
    public void cleanup(final Connection con, final PreparedStatement ps, final ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param type
     * @return
     */
    public synchronized TreeSet<String> getVersions(final String type) {
        if (oracledalHelper.versionsMap.containsKey(type)) {
            return oracledalHelper.versionsMap.get(type);
        }
        final long start = System.currentTimeMillis();
        if (Config.debug) {
            System.out.println("Begin: " + type);
        }
        final String query = "select version from jvm_info where build not in ('fcs') and type_ = '" + type + "' group by version";
        final TreeSet<String> tlist = new TreeSet<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = oracledalHelper.dataSource.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                tlist.add(rs.getString(1));
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanup(con, ps, rs);
        }
        oracledalHelper.versionsMap.put(type, tlist);
        if (Config.debug) {
            System.out.println("End: " + type + " " + (System.currentTimeMillis() - start));
        }
        return tlist;
    }

    public synchronized TreeSet getBuilds(final String type, final String version) {
        if (oracledalHelper.buildMap.containsKey(type + version)) {
            return oracledalHelper.buildMap.get(type + version);
        }
        if (Config.debug) {
            System.out.println("Begin: " + type + " " + version);
        }
        final long start = System.currentTimeMillis();
        final String query = "select build from jvm_info where build not in ('fcs') and type_ = '" + type + "' and version = '" + version + "' group by build";
        final TreeSet<String> tlist = new TreeSet<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = oracledalHelper.dataSource.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                tlist.add(rs.getString(1));
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanup(con, ps, rs);
        }
        oracledalHelper.buildMap.put(type + version, tlist);
        if (Config.debug) {
            System.out.println("End: " + type + " " + version + " " + (System.currentTimeMillis() - start));
        }
        return tlist;
    }

    public synchronized Hashtable getJobsInQueue(final String version) {
        if (oracledalHelper.jobsMap.containsKey(version)) {
            return oracledalHelper.jobsMap.get(version);
        }
        if (Config.debug) {
            System.out.println("Begin: " + version);
        }
        final long start = System.currentTimeMillis();
        final String query = "select job.os, job.arch, jvm_info.compiler, jvm_info.size_, jvm_info.build from job, computer, jvm_info where job.status = 'inqueue' and jvm_info.version = '" + version + "' and job.computer_id = computer.computer_id and computer.jvm_info_id = jvm_info.jvm_info_id";
        final Hashtable ht = new Hashtable();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = oracledalHelper.dataSource.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                final String key = rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4) + rs.getString(5);
                if (ht.get(key) == null) {
                    ht.put(key, key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanup(con, ps, rs);
        }
        oracledalHelper.jobsMap.put(version, ht);
        if (Config.debug) {
            System.out.println("End: " + version + " " + (System.currentTimeMillis() - start));
        }
        return ht;
    }

    static {
        oracledalHelper.conString = "";
        oracledalHelper.username = "";
        oracledalHelper.passwd = "";
    }
}
