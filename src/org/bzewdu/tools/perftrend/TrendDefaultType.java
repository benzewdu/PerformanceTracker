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

import org.bzewdu.tools.perftrend.config.Benchmarks;
import org.bzewdu.tools.perftrend.config.Config;
import org.bzewdu.tools.perftrend.data.dalHelper;
import org.bzewdu.tools.perftrend.htmlgen.PlatformHtml;
import org.bzewdu.tools.perftrend.htmlgen.VersionHtml;
import org.bzewdu.tools.perftrend.htmlgen.WorkloadHtml;
import org.bzewdu.tools.perftrend.util.Combo;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import static org.bzewdu.tools.perftrend.util.Platforms.getAllPlatforms;
import static org.bzewdu.tools.perftrend.util.Platforms.getClientPlatforms;

public class TrendDefaultType {
    private static final String PLATTITLE = "Benchmark Summary for ";
    private File _versionfile;
    private String _type;
    private String _title;

    public TrendDefaultType(final String type, final String title) {
        super();
        Config _cfg = Config.getConfig();
        this._type = type;
        this._title = title;
        this._versionfile = new File(Config.htmlDir(), this._type + "/index.html");
    }

    public void create(final boolean client) throws Exception {
        final dalHelper db = dalHelper.getdalHelper();
        final Set<String> versions = db.getVersions(this._type);
        ArrayList<Combo> platforms;
        if (client) {
            platforms = getClientPlatforms();
        } else {
            platforms = getAllPlatforms();
        }
        final VersionHtml vh = new VersionHtml(this._versionfile, this._title, versions);
        vh.create();
        if (versions != null) {
            for (final String ver : versions) {
                final Hashtable ht = db.getJobsInQueue(ver);
                final WorkloadHtml wh = new WorkloadHtml(new File(this._versionfile.getParent(), ver + "/index.html"), this._title, this._type, ver, client, ht);
                wh.create();
                for (Combo combo : platforms) {
                    final String compiler = combo.getCompiler();
                    String[] bench;
                    if (compiler.equals(Config.configAllprop.getProperty("C1"))) {
                        bench = Benchmarks.getClient();
                    } else {
                        bench = Benchmarks.getServer();
                    }
                    final File file = new File(Config.htmlDir(), this._type + "/" + ver + "/" + combo.getFullName() + "/" + Config.configAllprop.getProperty("HTMLFILE"));
                    final PlatformHtml phtml = new PlatformHtml(this._type, combo, ver, bench, file, "Benchmark Summary for " + combo.getFullName(), ht);
                    phtml.createBenchmarkHtml();
                    phtml.create();
                }
            }
        }
    }
}
