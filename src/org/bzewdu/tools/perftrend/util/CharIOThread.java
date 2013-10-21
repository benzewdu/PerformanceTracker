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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

class CharIOThread extends IOThread {
    private Reader src;
    private Writer dst;
    private int buflen;

    CharIOThread(final Reader rd, final Writer wr) {
        super();
        this.src = rd;
        this.dst = wr;
        this.buflen = 4096;
    }

    CharIOThread(final Reader rd, final Writer wr, final int bufsize) {
        super();
        this.src = rd;
        this.dst = wr;
        this.buflen = bufsize;
    }

    public void run() {
        try {
            IOThread.copy(this.src, this.dst, this.buflen);
        } catch (IOException e) {
            this.setException(e);
            try {
                this.dst.flush();
            } catch (IOException ex) {
            }
        }
    }
}
