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

import org.bzewdu.tools.perftrend.util.Platforms;

import java.io.File;
import java.util.Hashtable;

public class WorkloadHtml extends SummaryHtml {
    private String _version;
    private String _type;
    private boolean _client;

    public WorkloadHtml(final File htmlfile, final String title, final String type, final String version, final boolean client, final Hashtable ht) {
        super(htmlfile, title, version, ht);
        this._version = version;
        this._type = type;
        this._client = client;
    }

    protected String createHeader() {
        String retstr;
        if (this._client) {
            retstr = Platforms.createClientHeader();
        } else {
            retstr = Platforms.createDefaultHeader();
        }
        return retstr;
    }

    protected String createBody() {
        final StringBuffer sb = new StringBuffer();
        sb.append("</table>");
        return sb.toString();
    }
}
