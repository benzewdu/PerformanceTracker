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

import org.bzewdu.tools.perftrend.config.Config;

import java.util.ArrayList;

public class Platforms {
    public static final int PLATFORMS = 10;

    public static ArrayList<Combo> getAllPlatforms() {
        final ArrayList<Combo> alist = new ArrayList<Combo>();
        for (int i = 0; i < 10; ++i) {
            alist.add(combo(i));
        }
        return alist;
    }

    public static ArrayList<Combo> getClientPlatforms() {
        final ArrayList<Combo> alist = new ArrayList<Combo>();
        alist.add(combo(0));
        alist.add(combo(4));
        alist.add(combo(7));
        return alist;
    }

    public static String getTable() {
        final String html = "<table border=0><tr><td><b>Status</td><td><b>Display</td></tr><tr><td>Pass</td><td>33.19/+02%</td></tr><tr><td>Fail</td><td><u>-/-</u></td></tr><tr><td>Delete</td><td><u>FAIL</u></td></tr><tr><td>In Queue</td><td><b>...</td></tr><tr><td>Not In Queue</td><td>N/A</td><tr></table>\n";
        return "<table border=0><tr><td><b>Status</td><td><b>Display</td></tr><tr><td>Pass</td><td>33.19/+02%</td></tr><tr><td>Fail</td><td><u>-/-</u></td></tr><tr><td>Delete</td><td><u>FAIL</u></td></tr><tr><td>In Queue</td><td><b>...</td></tr><tr><td>Not In Queue</td><td>N/A</td><tr></table>\n";
    }

    public static String createDefaultHeader() {
        String html = getTable();
        html += "<table border=0><tr><td></td><td ALIGN=CENTER COLSPAN=4>Solaris</td><td ALIGN=CENTER COLSPAN=3>Win32</td><td ALIGN=CENTER COLSPAN=3>Linux</td></tr>\n";
        html += "<tr><td></td><td ALIGN=CENTER COLSPAN=3>Sparc</td><td ALIGN=CENTER COLSPAN=1>i386</td><td ALIGN=CENTER COLSPAN=2>i386</td><td ALIGN=CENTER COLSPAN=1>amd64</td><td ALIGN=CENTER COLSPAN=2>i386</td><td ALIGN=CENTER COLSPAN=1>amd64</td></tr>\n";
        html += "<tr><td align=center><i>Build</i></td>";
        html += platformLinks();
        return html;
    }

    public static String createClientHeader() {
        String html = getTable();
        html += "<table border=0><tr><td></td><td>Solaris</td><td>Win32</td><td>Linux</td></tr>\n";
        html += "<tr><td></td><td>Sparc</td><td>i386</td><td>i386</td>\n";
        html += "<tr><td align=center><i>Chart</i></td>";
        html += platformClientLinks();
        return html;
    }

    public static String platformLinks() {
        String html = "";
        html += "<td align=center><a href=";
        html = html + combo(0).getFullName() + "/index.html";
        html += "><i>Client</i></a></td><td align=center><a href=";
        html = html + combo(1).getFullName() + "/index.html";
        html += "><i>Server</i></a></td><td align=center><a href=";
        html = html + combo(2).getFullName() + "/index.html";
        html += "><i>64bit</i></a></td><td align=center><a href=";
        html = html + combo(3).getFullName() + "/index.html";
        html += ">Server</i></a></td><td align=center><a href=";
        html = html + combo(4).getFullName() + "/index.html";
        html += "><i>Client</i></a></td><td align=center><i><a href=";
        html = html + combo(5).getFullName() + "/index.html";
        html += ">Server</i></a></td><td align=center><a href=";
        html = html + combo(6).getFullName() + "/index.html";
        html += ">Server</i></a></td><td align=center><a href=";
        html = html + combo(7).getFullName() + "/index.html";
        html += "><i>Client</i></a></td><td align=center><a href=";
        html = html + combo(8).getFullName() + "/index.html";
        html += "><i>Server</i></a></td><td align=center><a href=";
        html = html + combo(9).getFullName() + "/index.html";
        html += "><i>Server</i></a></td></tr>\n";
        return html;
    }

    public static String platformClientLinks() {
        String html = "";
        html += "<td align=center><a href=";
        html = html + combo(0).getFullName() + "/index.html";
        html += "><i>Client</i></a></td><td align=center><a href=";
        html = html + combo(4).getFullName() + "/index.html";
        html += "><i>Client</i></a></td><td align=center><i><a href=";
        html = html + combo(7).getFullName() + "/index.html";
        html += "><i>Client</i></a></td>";
        html += "</tr>\n";
        return html;
    }

    public static Combo combo(final int index) {
        switch (index % 10) {
            case 0: {
                return new Combo(Config.configAllprop.getProperty("SOLARIS"), Config.configAllprop.getProperty("SPARCV9"), Config.configAllprop.getProperty("C1"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 1: {
                return new Combo(Config.configAllprop.getProperty("SOLARIS"), Config.configAllprop.getProperty("SPARCV9"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 2: {
                return new Combo(Config.configAllprop.getProperty("SOLARIS"), Config.configAllprop.getProperty("SPARCV9"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE64BIT"));
            }
            case 3: {
                return new Combo(Config.configAllprop.getProperty("SOLARIS"), Config.configAllprop.getProperty("IA32"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 4: {
                return new Combo(Config.configAllprop.getProperty("WINDOWS"), Config.configAllprop.getProperty("IA32"), Config.configAllprop.getProperty("C1"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 5: {
                return new Combo(Config.configAllprop.getProperty("WINDOWS"), Config.configAllprop.getProperty("IA32"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 6: {
                return new Combo(Config.configAllprop.getProperty("WINDOWS"), Config.configAllprop.getProperty("AMD64"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE64BIT"));
            }
            case 7: {
                return new Combo(Config.configAllprop.getProperty("LINUX"), Config.configAllprop.getProperty("IA32"), Config.configAllprop.getProperty("C1"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 8: {
                return new Combo(Config.configAllprop.getProperty("LINUX"), Config.configAllprop.getProperty("IA32"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
            case 9: {
                return new Combo(Config.configAllprop.getProperty("LINUX"), Config.configAllprop.getProperty("AMD64"), Config.configAllprop.getProperty("C2"), Config.configAllprop.getProperty("SIZE64BIT"));
            }
            default: {
                return new Combo(Config.configAllprop.getProperty("SOLARIS"), Config.configAllprop.getProperty("SPARCV9"), Config.configAllprop.getProperty("C1"), Config.configAllprop.getProperty("SIZE32BIT"));
            }
        }
    }
}
