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

import org.bzewdu.tools.perftrend.config.Config;

import java.io.File;
import java.util.Set;

public class VersionHtml extends Html {
    private Config _cfg;
    private String _parentdir;
    private String _title;
    private String _parentname;
    private Set<String> _versions;

    public VersionHtml(final File htmlfile, final String title, final Set<String> versions) {
        super(htmlfile, title);
        this._parentdir = htmlfile.getParent();
        this._parentname = htmlfile.getParentFile().getName();
        this._title = title;
        this._versions = versions;
        this._cfg = Config.getConfig();
    }

    protected String createHeader() {
        return "";
    }

    protected String createBody() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<br><br>");
        if (this._versions != null) {
            for (final String version : this._versions) {
                sb.append("<a href=\"" + version + "/index.html\">");
                sb.append(version);
                sb.append("<br>\n");
            }
        }
        return sb.toString();
    }

    public String getTitle() {
        return this._title;
    }
}
