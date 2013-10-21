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

abstract class IOThread extends Thread {
    protected IOException ioe;

    protected IOThread() {
        super();
        this.ioe = null;
    }

    public static void copy(final InputStream is, final OutputStream os, final int buflen) throws IOException {
        final byte[] buf = new byte[buflen];
        int cnt;
        while ((cnt = is.read(buf)) > 0) {
            os.write(buf, 0, cnt);
        }
        os.flush();
    }

    public static void copy(final Reader rd, final Writer wr, final int buflen) throws IOException {
        final char[] buf = new char[buflen];
        int cnt;
        while ((cnt = rd.read(buf)) > 0) {
            wr.write(buf, 0, cnt);
        }
        wr.flush();
    }

    public static void copy(final BufferedReader rd, final BufferedWriter wr) throws IOException {
        String s;
        while ((s = rd.readLine()) != null) {
            wr.write(s);
            wr.newLine();
            wr.flush();
        }
    }

    public void setException(final IOException e) {
        this.ioe = e;
    }

    public IOException getException() {
        return this.ioe;
    }

    public void rethrow() throws IOException {
        if (this.ioe != null) {
            throw this.ioe;
        }
    }
}
