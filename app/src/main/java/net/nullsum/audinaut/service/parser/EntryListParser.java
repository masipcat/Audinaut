/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package net.nullsum.audinaut.service.parser;

import android.content.Context;

import net.nullsum.audinaut.domain.MusicDirectory;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * @author Sindre Mehus
 */
public class EntryListParser extends MusicDirectoryEntryParser {

    public EntryListParser(Context context, int instance) {
        super(context, instance);
    }

    public MusicDirectory parse(InputStream inputStream) throws Exception {
        init(inputStream);

        MusicDirectory dir = new MusicDirectory();
        int eventType;
        do {
            eventType = nextParseEvent();
            if (eventType == XmlPullParser.START_TAG) {
                String name = getElementName();
                switch (name) {
                    case "album": {
                        MusicDirectory.Entry entry = parseEntry("");
                        if (get("isDir") == null) {
                            entry.setDirectory(true);
                        }
                        dir.addChild(entry);
                        break;
                    }
                    case "song": {
                        MusicDirectory.Entry entry = parseEntry("");
                        dir.addChild(entry);
                        break;
                    }
                    case "error":
                        handleError();
                        break;
                }
            }
        } while (eventType != XmlPullParser.END_DOCUMENT);

        validate();

        return dir;
    }
}
