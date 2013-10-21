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

public class TrendStruct {
    private String _build;
    private String _name;
    private String _log;
    private String _status;
    private double _score;
    private int _isHigher;

    public void setBuild(final String str) {
        this._build = str;
    }

    public void setSubScoreName(final String str) {
        this._name = str;
    }

    public void setLog(final String str) {
        this._log = str;
    }

    public void setStatus(final String str) {
        this._status = str;
    }

    public void setScore(final double str) {
        this._score = str;
    }

    public void setIsHigher(final int str) {
        this._isHigher = str;
    }

    public String getBuild() {
        return this._build;
    }

    public String getSubScoreName() {
        return this._name;
    }

    public String getLog() {
        return this._log;
    }

    public String getStatus() {
        return this._status;
    }

    public double getScore() {
        return this._score;
    }

    public int getIsHigher() {
        return this._isHigher;
    }

    public void print() {
        if (Config.debug) {
            System.out.println("\tBuild=" + this._build + " Log=" + this._log + " Status=" + this._status + " Score=" + this._score + " IsHigher=" + this._isHigher);
        }
    }
}
