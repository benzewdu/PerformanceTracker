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

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

public class Util {
    public static final String SlashNet;
    static String _localHost;

    public static StringBuffer createMsgBuf(final Thread t) {
        final StringBuffer buf = new StringBuffer(t.toString());
        buf.append(":  ");
        buf.append(new Date());
        buf.append(' ');
        return buf;
    }

    public static StringBuffer createMsgBuf() {
        return createMsgBuf(Thread.currentThread());
    }

    public static String[] findAllPatterns(final File f, final String pattern) throws IOException {
        final int len = pattern.length();
        BufferedReader in = null;
        int count = 0;
        try {
            in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(pattern)) {
                    ++count;
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        final String[] strarr = new String[count];
        count = 0;
        try {
            in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(pattern)) {
                    strarr[count] = line;
                    ++count;
                }
            }
        } finally {
            in.close();
        }
        return strarr;
    }

    public static String fgrep(final String pattern, final BufferedReader in) throws IOException {
        final int len = pattern.length();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains(pattern)) {
                return line;
            }
        }
        return null;
    }

    public static String fgrep(final String pattern, final File f) throws FileNotFoundException, IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(f));
            return fgrep(pattern, in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static String fileContains(final File f, final String pattern) throws FileNotFoundException, IOException {
        return fgrep(pattern, f);
    }

    public static long copy(final InputStream src, final OutputStream dst) throws IOException {
        long total = 0L;
        final byte[] buf = new byte[4096];
        int cnt;
        while ((cnt = src.read(buf, 0, 4096)) > 0) {
            dst.write(buf, 0, cnt);
            total += cnt;
        }
        return total;
    }

    public static long copy(final Reader src, final Writer dst) throws IOException {
        long total = 0L;
        final char[] buf = new char[4096];
        int cnt;
        while ((cnt = src.read(buf, 0, 4096)) > 0) {
            dst.write(buf, 0, cnt);
            total += cnt;
        }
        return total;
    }

    public static long copy(final File src, final File dst, final boolean append) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst.getPath(), append);
            return copy(is, os);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static long copy(final File src, final File dst) throws IOException {
        return copy(src, dst, false);
    }

    public static long copy(final File src, final OutputStream dst) throws IOException {
        FileInputStream loc_src = null;
        try {
            loc_src = new FileInputStream(src);
            return copy(loc_src, dst);
        } finally {
            if (loc_src != null) {
                loc_src.close();
            }
        }
    }

    public static long copy(final File src, final Writer dst) throws IOException {
        FileReader loc_src = null;
        try {
            loc_src = new FileReader(src);
            return copy(loc_src, dst);
        } finally {
            if (loc_src != null) {
                loc_src.close();
            }
        }
    }

    public static int tail(final BufferedReader src, final BufferedWriter dst, final int count) throws IOException {
        int i = 0;
        boolean wrapped = false;
        String[] lines;
        for (lines = new String[count]; (lines[i] = src.readLine()) != null; i = 0, wrapped = true) {
            if (++i == count) {
            }
        }
        if (!wrapped) {
            return 0;
        }
        final int end = i;
        if (++i == count) {
            i = 0;
        }
        do {
            dst.write(lines[i]);
            dst.newLine();
            if (++i == count) {
                i = 0;
            }
        } while (i != end);
        dst.flush();
        return count;
    }

    public static int tail(final BufferedReader src, final BufferedWriter dst) throws IOException {
        return tail(src, dst, 10);
    }

    public static long tail(final File src, final BufferedWriter dst, final int count) throws IOException {
        BufferedReader loc_src = null;
        try {
            loc_src = new BufferedReader(new FileReader(src));
            return tail(loc_src, dst, count);
        } finally {
            if (loc_src != null) {
                loc_src.close();
            }
        }
    }

    public static boolean stringContains(final String src, final String key, final String token) {
        final StringTokenizer st = new StringTokenizer(src, token);
        boolean retval = false;
        while (st.hasMoreTokens()) {
            if (st.nextToken().equals(key)) {
                retval = true;
                break;
            }
        }
        return retval;
    }

    public static boolean stringContains(final String src, final String key) {
        boolean retval = false;
        for (int i = 0; i < src.length(); ++i) {
            for (int j = 0; j < key.length() && key.charAt(j) == src.charAt(i + j); ++j) {
                if (j == key.length() - 1) {
                    retval = true;
                }
            }
        }
        return retval;
    }

    public static String[] tokenize(final String s) {
        final StringTokenizer tokens = new StringTokenizer(s, " \t");
        final int count = tokens.countTokens();
        final String[] v = new String[count];
        for (int i = 0; i < count; ++i) {
            v[i] = tokens.nextToken();
        }
        return v;
    }

    public static boolean samePath(final String s1, final String s2, final String domain) {
        return s1.equals(s2) || (slashNetPath(s1).equals(slashNetPath(s2)) && sameHost(slashNetHost(s1), slashNetHost(s2), domain));
    }

    public static String slashNetHost(final String path) {
        if (path.startsWith(Util.SlashNet)) {
            final int start = Util.SlashNet.length();
            final int end = path.indexOf(File.separator, start);
            return path.substring(start, end);
        }
        if (Util._localHost != null) {
            return Util._localHost;
        }
        try {
            Util._localHost = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException("no hostname");
        }
        return Util._localHost;
    }

    public static String slashNetPath(final String path) {
        final int len = Util.SlashNet.length();
        return path.startsWith(Util.SlashNet) ? path.substring(path.indexOf(File.separator, len + 1)) : path;
    }

    public static boolean sameHost(final String host1, final String host2, final String domain) {
        if (host1.equals(host2)) {
            return true;
        }
        final String h1 = (host1.indexOf(46) < 0) ? (host1.concat(domain).toLowerCase() + '.') : (host1.toLowerCase() + '.');
        final String h2 = (host2.indexOf(46) < 0) ? (host2.concat(domain).toLowerCase() + '.') : (host2.toLowerCase() + '.');
        return h1.startsWith(h2) || h2.startsWith(h1);
    }

    public static String[] readFromFile(final File readFrom) throws FileNotFoundException, IOException {
        final Vector<String> v = new Vector<String>();
        final BufferedReader in = new BufferedReader(new FileReader(readFrom));
        int i = 0;
        String line;
        while ((line = in.readLine()) != null) {
            v.add(i, line);
            ++i;
        }
        in.close();
        final String[] str = new String[v.size()];
        for (i = 0; i < v.size(); ++i) {
            str[i] = (String) v.get(i);
        }
        return str;
    }

    public static boolean commandNotFound(final File log, final String cmd) throws IOException {
        if (log.length() > cmd.length() + 1024) {
            return false;
        }
        String s = null;
        try {
            s = fileContains(log, cmd);
        } catch (FileNotFoundException ignored) {
        }
        return s != null && s.indexOf("not found") > 0;
    }

    static {
        SlashNet = File.separator + "net" + File.separator;
    }
}
