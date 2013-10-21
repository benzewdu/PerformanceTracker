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

package org.bzewdu.tools.perftrend;

import org.bzewdu.tools.perftrend.config.Config;
import org.bzewdu.tools.perftrend.htmlgen.TopLevelHtml;

import java.util.HashMap;
import java.util.Set;

public class Main {
    private static Config _cfg;

    public Main() {
        super();
        Main._cfg = Config.getConfig();
    }

    public void run() {
        try {
            final TopLevelHtml th = new TopLevelHtml(Config.htmlDir());
            th.create();
            this.createTrendTool();
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    private void createTrendTool() throws Exception {
        if (Config.debug) {
            System.out.println(" creating trend...");
        }
        final HashMap<Character, String> hm = 
                            new HashMap<Character, String>();
        hm.put(Config.configAllprop.getProperty("SWINGTYPE").charAt(0), "Swing Summary");
        hm.put(Config.configAllprop.getProperty("ERGTYPE").charAt(0), "ERG Summary");
        hm.put(Config.configAllprop.getProperty("PROMOTEDTYPE").charAt(0), "Promoted Summary");
        hm.put(Config.configAllprop.getProperty("WORKSPACETYPE").charAt(0), "WS Summary");
        hm.put(Config.configAllprop.getProperty("NIGHTLYTYPE").charAt(0), "Nightly Summary");
        hm.put(Config.configAllprop.getProperty("JAVA2DTYPE").charAt(0), "Java2D Summary");
        hm.put(Config.configAllprop.getProperty("CMSTYPE").charAt(0), "CMS Summary");
        final Set<Character> set = hm.keySet();
        for (final char type : set) {
            new Thread() {
                public void run() {
                    try {
                        new TrendDefaultType(type + "", hm.get(type)).create(type == 's' || type == 'd');
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public static void main(final String[] args) {
        final Main m = new Main();
        m.run();
    }
}
