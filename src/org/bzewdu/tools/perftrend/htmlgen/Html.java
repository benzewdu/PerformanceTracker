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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Html {
    protected File _htmlfile;
    private String _title;

    protected abstract String createHeader();

    protected abstract String createBody();

    public Html(final File htmlfile, final String title) {
        super();
        this._htmlfile = htmlfile;
        this._title = title;
    }

    public void create() throws IOException {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.createTitle());
        sb.append(this.createHeader());
        sb.append(this.createBody());
        sb.append(this.createFooter());
        this.writeToFile(sb.toString());
    }

    public String getName() {
        return this._htmlfile.getName();
    }

    public File getFile() {
        return this._htmlfile;
    }

    public File getParentFile() {
        return this._htmlfile.getParentFile();
    }

    public String getParentName() {
        return this._htmlfile.getParentFile().getName();
    }

    public boolean exists() {
        return this._htmlfile.exists();
    }

    protected String createTitle() {
        String html = "<html><head><title>" + this._title + "</title></head>\n";
        html = html + "<body bgcolor=\"#FFFFFF\"><h1>" + this._title + "</h2>\n";
        return html;
    }

    protected String createFooter() {
        return "<hr><address><a href=\"mailto:perftrend@Sun.COM\">perftrend@sun.com</a></address></body></html>";
    }

    protected void writeToFile(final String str) throws IOException {
        final File dir = this._htmlfile.getParentFile();
        dir.mkdirs();
        final BufferedWriter out = new BufferedWriter(new FileWriter(this._htmlfile));
        out.write(str);
        out.close();
    }
}
