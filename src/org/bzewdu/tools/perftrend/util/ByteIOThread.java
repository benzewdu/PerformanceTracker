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
import java.io.InputStream;
import java.io.OutputStream;

class ByteIOThread extends IOThread {
    private InputStream src;
    private OutputStream dst;
    private int buflen;

    ByteIOThread(final InputStream is, final OutputStream os) {
        super();
        this.src = is;
        this.dst = os;
        this.buflen = 4096;
    }

    ByteIOThread(final InputStream is, final OutputStream os, final int bufsize) {
        super();
        this.src = is;
        this.dst = os;
        this.buflen = bufsize;
    }

    public void run() {
        try {
            IOThread.copy(this.src, this.dst, this.buflen);
        } catch (IOException e) {
            this.setException(e);
            try {
                this.dst.flush();
            } catch (IOException ignored) {
            }
        }
    }
}
