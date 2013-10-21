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

public class Combo {
    private String _os;
    private String _arch;
    private String _compiler;
    private String _bit;

    public Combo(final String os, final String arch, final String compiler, final String bit) {
        super();
        this._os = os;
        this._arch = arch;
        this._compiler = compiler;
        this._bit = bit;
    }

    public String getOS() {
        return this._os;
    }

    public String getArch() {
        return this._arch;
    }

    public String getCompiler() {
        return this._compiler;
    }

    public String getBit() {
        return this._bit;
    }

    public String getFullName() {
        return this._os + "_" + this._arch + "_" + this._compiler + "_" + this._bit;
    }

    public String getConcatFullName() {
        return this._os + this._arch + this._compiler + this._bit;
    }

    public void print() {
        if (Config.debug) {
            System.out.println("Debug Combo");
        }
        if (Config.debug) {
            System.out.println("\tOS=" + this.getOS());
        }
        if (Config.debug) {
            System.out.println("\tArch=" + this.getArch());
        }
        if (Config.debug) {
            System.out.println("\tCompiler=" + this.getCompiler());
        }
        if (Config.debug) {
            System.out.println("\tBig=" + this.getBit());
        }
    }

    public boolean equals(final String os, final String arch, final String compiler, final String bit) {
        return os.equals(this._os) && arch.equals(this._arch) && compiler.equals(this._compiler) && bit.equals(this._bit);
    }
}
