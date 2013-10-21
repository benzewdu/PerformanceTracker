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

package org.bzewdu.tools.perftrend.config;

import org.bzewdu.tools.perftrend.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public final class Config extends Properties {
    private static Config m_defaultCfg;
    public static boolean debug;
    private static Properties m_properties;
    private static long m_startTime;
    private static File m_homeDir;
    private static File m_configDir;
    private static final String perftrend_home = "perftrend_home";
    private static File _config;
    private static File _idconfig;
    public static Properties _idproperties;
    public static Properties configAllprop;
    private static String[] m_platforms;

    public Config() {
        super(Config.m_properties);
    }

    public static Config getConfig() {
        return Config.m_defaultCfg;
    }

    public static void sleep(final String seconds) {
        try {
            Thread.sleep(new Integer(seconds) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File htmlDir() {
        return new File(Config.m_homeDir, "public_html");
    }

    private static void readConfigFile(final File f) throws IOException {
        final FileInputStream fis = new FileInputStream(f);
        Config.m_properties.load(fis);
        fis.close();
    }

    /**
     *
     * @param home
     * @throws IOException
     */
    private static void initialize(final String home) throws IOException {
        Config.m_startTime = System.currentTimeMillis();
        Config.m_properties = new Properties(null);
        Config.m_homeDir = new File(home);
        Config.m_configDir = new File(Config.m_homeDir, "etc");
        initDefaultProperties(Config.m_properties);
        readConfigFile(Config._config);
        final String p = Config.m_properties.getProperty("platforms");
        Config.m_platforms = Util.tokenize(p);
    }

    private static void initDefaultProperties(final Properties p) {
        p.setProperty(Config.configAllprop.getProperty("PERF_HOME"), Config.m_homeDir.getAbsolutePath());
    }

    static {
        String home = null;
        final StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), File.pathSeparator);
        while (st.hasMoreTokens()) {
            final String name = st.nextToken();
            Config._config = new File(name, "config.all");
            Config._idconfig = new File(name, "idconfig.prop");
            if (Config._config.exists()) {
                try {
                    Config.configAllprop = new Properties();
                    final FileInputStream fis = new FileInputStream(Config._config);
                    Config.configAllprop.load(fis);
                    fis.close();
                    home = Config.configAllprop.getProperty("perftrend_home");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error in getting property config.all perftrend_HOME\nExiting...");
                }
            }
            if (Config._idconfig.exists()) {
                try {
                    final FileInputStream fis;
                    (Config._idproperties = new Properties()).load(fis = new FileInputStream(Config._idconfig));
                    fis.close();
                    System.err.println("Loading.." + Config._idproperties);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error in getting property idconfig.prop perftrend_HOME\nExiting...");
                }
            }
            if (Config._idproperties != null && Config.configAllprop != null) {
                break;
            }
        }
        if (home != null) {
            try {
                initialize(home);
                Config.m_defaultCfg = new Config();
                if (Config.m_defaultCfg == null) {
                    System.err.println("Config is null exiting...\n" + home);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                System.err.println("Error in getting setting perftrend_HOME\nExiting...:" + home);
            }
        } else {
            System.err.println("Error in setting perftrend_HOME\nExiting...");
        }
    }
}
