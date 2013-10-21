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

public class ExternalCmd {
    private static Runtime m_runtime;
    private String[] cmd_v;
    private Writer owriter;
    private Writer ewriter;
    private OutputStream ostream;
    private OutputStream estream;
    private File ofile;
    private boolean ofile_append;
    private int rc;

    /**
     *
     * @param cmd
     */
    private void init(final String[] cmd) {
        this.rc = 0;
        this.cmd_v = cmd;
        this.ewriter = null;
        this.owriter = null;
        this.estream = null;
        this.ostream = null;
        this.ofile = null;
        this.ofile_append = false;
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     */
    private void init(final String[] cmd, final Writer out, final Writer err) {
        this.init(cmd);
        this.ewriter = err;
        this.owriter = out;
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     */
    private void init(final String[] cmd, final OutputStream out, final OutputStream err) {
        this.init(cmd);
        this.estream = err;
        this.ostream = out;
    }

    /**
     *
     * @param cmd
     */
    public ExternalCmd(final String[] cmd) {
        super();
        this.init(cmd, new StringWriter(), new StringWriter());
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     */
    public ExternalCmd(final String[] cmd, final Writer out, final Writer err) {
        super();
        this.init(cmd, out, err);
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     */
    public ExternalCmd(final String[] cmd, final OutputStream out, final OutputStream err) {
        super();
        this.init(cmd, out, err);
    }

    /**
     *
     * @param cmd
     * @param outfile
     */
    public ExternalCmd(final String[] cmd, final File outfile) {
        super();
        this.init(cmd);
        this.ofile = outfile;
    }

    public ExternalCmd(final String[] cmd, final File outfile, final boolean append) {
        super();
        this.init(cmd);
        this.ofile = outfile;
        this.ofile_append = append;
    }

    public int rc() {
        return this.rc;
    }

    /**
     *
     * @param buflen
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int run(final int buflen) throws IOException, InterruptedException {
        if (this.ofile != null) {
            this.rc = run(this.cmd_v, this.ofile, this.ofile_append, buflen);
        } else if (this.owriter != null) {
            this.rc = run(this.cmd_v, this.owriter, this.ewriter, buflen);
        } else {
            this.rc = run(this.cmd_v, this.ostream, this.estream, buflen);
        }
        return this.rc;
    }

    public int run() throws IOException, InterruptedException {
        return this.run(4096);
    }

    public File getOutputFile() {
        return this.ofile;
    }

    public Writer getErrorWriter() {
        return this.ewriter;
    }

    public Writer getOutputWriter() {
        return this.owriter;
    }

    public StringBuffer getErrorBuffer() {
        return ((StringWriter) this.ewriter).getBuffer();
    }

    public StringBuffer getOutputBuffer() {
        return ((StringWriter) this.owriter).getBuffer();
    }

    /**
     *
     */
    public void appendErrorWriter() {
        try {
            this.owriter.write(this.getErrorBuffer().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OutputStream getErrorStream() {
        return this.estream;
    }

    public OutputStream getOutputStream() {
        return this.ostream;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.cmd_v[0]);
        for (int i = 1; i < this.cmd_v.length; ++i) {
            buf.append(' ');
            buf.append(this.cmd_v[i]);
        }
        buf.append(',');
        buf.append(this.rc);
        return buf.toString();
    }

    /**
     *
     * @param p
     * @throws IOException
     */
    public static void cleanup(final Process p) throws IOException {
        p.getOutputStream().close();
        p.getInputStream().close();
        p.getErrorStream().close();
        p.destroy();
    }

    /**
     *
     * @param p
     * @param in
     * @param out
     * @param err
     * @param buflen
     * @return
     */
    public static IOThread[] createIOThreads(final Process p, final InputStream in, final OutputStream out, final OutputStream err, final int buflen) {
        return new IOThread[]{(in != null) ? new ByteIOThread(in, p.getOutputStream(), buflen) : null, (out != null) ? new ByteIOThread(p.getInputStream(), out, buflen) : null, (err != null) ? new ByteIOThread(p.getErrorStream(), err, buflen) : null};
    }

    public static IOThread[] createIOThreads(final Process p, final Reader in, final Writer out, final Writer err, final int buflen) {
        return (buflen == 0) ? createLineIOThreads(p, in, out, err) : createCharIOThreads(p, in, out, err, buflen);
    }

    /**
     *
     * @param p
     * @param in
     * @param out
     * @param err
     * @param buflen
     * @return
     */
    public static IOThread[] createCharIOThreads(final Process p, final Reader in, final Writer out, final Writer err, final int buflen) {
        final IOThread[] t;
        t = new IOThread[]{(in != null) ? new CharIOThread(in, new OutputStreamWriter(p.getOutputStream()), buflen) : null, (out != null) ? new CharIOThread(new InputStreamReader(p.getInputStream()), out, buflen) : null, (err != null) ? new CharIOThread(new InputStreamReader(p.getErrorStream()), err, buflen) : null};
        return t;
    }

    /**
     *
     * @param p
     * @param in
     * @param out
     * @param err
     * @return
     */
    public static IOThread[] createLineIOThreads(final Process p, final Reader in, final Writer out, final Writer err) {
        return new IOThread[]{(in != null) ? new LineIOThread(in, new OutputStreamWriter(p.getOutputStream())) : null, (out != null) ? new LineIOThread(new InputStreamReader(p.getInputStream()), out) : null, (err != null) ? new LineIOThread(new InputStreamReader(p.getErrorStream()), err) : null};
    }

    /**
     *
     * @param threads
     * @throws IOException
     * @throws InterruptedException
     */
    public static void runIOThreads(final IOThread[] threads) throws IOException, InterruptedException {
        for (IOThread thread : threads) {
            if (thread != null) {
                thread.start();
            }
        }
        for (IOThread thread : threads) {
            if (thread != null) {
                thread.join();
            }
        }
        for (IOThread thread : threads) {
            if (thread != null) {
                thread.rethrow();
            }
        }
    }

    /**
     *
     * @param cmd
     * @param in
     * @param out
     * @param err
     * @param buflen
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static int run(final String[] cmd, final InputStream in, final OutputStream out, final OutputStream err, final int buflen) throws IOException, InterruptedException {
        Process p = null;
        int rc = 1;
        try {
            p = ExternalCmd.m_runtime.exec(cmd);
            final IOThread[] t = createIOThreads(p, in, out, err, buflen);
            runIOThreads(t);
            rc = p.waitFor();
        } finally {
            if (p != null) {
                cleanup(p);
            }
        }
        return rc;
    }

    /**
     *
     * @param cmd
     * @param in
     * @param out
     * @param err
     * @param buflen
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static int run(final String[] cmd, final Reader in, final Writer out, final Writer err, final int buflen) throws IOException, InterruptedException {
        Process p = null;
        int rc = 1;
        try {
            p = ExternalCmd.m_runtime.exec(cmd);
            final IOThread[] t = createIOThreads(p, in, out, err, buflen);
            runIOThreads(t);
            rc = p.waitFor();
        } finally {
            if (p != null) {
                cleanup(p);
            }
        }
        return rc;
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     * @param buflen
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static int run(final String[] cmd, final OutputStream out, final OutputStream err, final int buflen) throws IOException, InterruptedException {
        return run(cmd, null, out, err, buflen);
    }

    /**
     *
     * @param cmd
     * @param out
     * @param err
     * @param buflen
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static int run(final String[] cmd, final Writer out, final Writer err, final int buflen) throws IOException, InterruptedException {
        return run(cmd, null, out, err, buflen);
    }

    public static int run(final String[] cmd, final File file, final boolean append, final int buflen) throws IOException, InterruptedException {
        BufferedOutputStream out = null;
        int rc = 1;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file.getPath(), append));
            rc = run(cmd, null, out, out, buflen);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return rc;
    }

    static {
        ExternalCmd.m_runtime = Runtime.getRuntime();
    }
}
