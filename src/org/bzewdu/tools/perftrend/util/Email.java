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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

public class Email {
    private Config _cfg;
    private String _toList;
    private String _from;
    private String _subject;
    private String _body;

    public Email() {
        super();
        this.init();
    }

    public Email(final String list, final String from, final String subject) {
        super();
        this.init();
        this._subject = subject;
        this._from = from;
        this._toList = list;
    }

    private void init() {
        this._cfg = Config.getConfig();
        this._body = "";
    }


    public boolean send() {
        boolean retval = true;
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.host", this._cfg.getProperty("mail_host"));
            final Message msg = new MimeMessage(Session.getDefaultInstance(props, null));
            final StringTokenizer st = new StringTokenizer(this._toList, " ");
            final int num = st.countTokens();
            final InternetAddress[] to = new InternetAddress[num];
            for (int i = 0; i < num; ++i) {
                to[i] = new InternetAddress(st.nextToken());
            }
            msg.setFrom(new InternetAddress(this._from));
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(this._subject);
            msg.setSentDate(new Date());
            msg.setText(this._body);
            Transport.send(msg);
        } catch (MessagingException mex) {
            retval = false;
            mex.printStackTrace();
        }
        return retval;
    }
}
