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

package org.bzewdu.tools.perftrend.htmlgen;

import java.io.File;

public class TopLevelHtml extends Html {
    private static String FILENAME;
    private static String TITLE;
    private File _promoted;
    private File _nightly;
    private File _cte;
    private File _workspace;
    private File _other;

    public TopLevelHtml(final File htmldir) {
        super(new File(htmldir, TopLevelHtml.FILENAME), TopLevelHtml.TITLE);
    }

    public TopLevelHtml(final File htmldir, final File p, final File n, final File c, final File w, final File o) {
        super(new File(htmldir, TopLevelHtml.FILENAME), TopLevelHtml.TITLE);
        this._promoted = p;
        this._nightly = n;
        this._cte = c;
        this._workspace = w;
        this._other = o;
    }

    public TopLevelHtml(final File htmldir, final File p, final File n, final File c, final File w) {
        super(new File(htmldir, TopLevelHtml.FILENAME), TopLevelHtml.TITLE);
        this._promoted = p;
        this._nightly = n;
        this._cte = c;
        this._workspace = w;
    }

    protected String createHeader() {
        return "<hr>Click on one of the following for a detailed summary.<br>\n";
    }

    protected String createBody() {
        String out = "";
        out += "<h2><a href=\"p/index.html\">RE Promoted Summary</a><br>\n";
        out += "<h2><a href=\"e/index.html\">RE Ergonomics Summary</a><br>\n";
        out += "<h2><a href=\"n/index.html\">RE Nightly Summary</a><br>\n";
        out += "<h2><a href=\"w/index.html\">Hotspot Summary</a><br>\n";
        out += "<h2><a href=\"s/index.html\">Swing Nightly Summary</a><br>\n";
        out += "<h2><a href=\"d/index.html\">Java2D Workspace Summary</a><br>\n";
        return out;
    }

    static {
        TopLevelHtml.FILENAME = "index.html";
        TopLevelHtml.TITLE = "Welcome to perftrend Trend Tool";
    }
}
