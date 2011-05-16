/*******************************************************************************
 * Copyright (C) 2009-2011 Amir Hassan <amir@viel-zu.org>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/
package org.wooden.m3u;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

public class M3UPlaylist {
  private static URLCodec urlCodec = new URLCodec();
  public static final String M3U_HEADER = "#EXTM3U";
  public static final String M3U_EXTINF = "#EXTINF:";

  public Vector<M3UEntry> entryList = new Vector<M3UEntry>();

  public void addEntry(M3UEntry entry) {
    entryList.add(entry);
  }

  public void insertEntry(M3UEntry entry, int index) {
    entryList.insertElementAt(entry, index);
  }

  public void removeEntry(int index) {
    entryList.remove(index);
  }

  public void store(OutputStream out) throws IOException {
    M3UEntry[] entries = (M3UEntry[]) entryList.toArray(new M3UEntry[0]);

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
    writer.write(M3U_HEADER);
    writer.newLine();
    char colon = ',';

    for (int i = 0; i < entries.length; i++) {
      writer.write(M3U_EXTINF);
      writer.write(String.valueOf(entries[i].length()));
      writer.write(colon);
      writer.write(entries[i].title());
      writer.newLine();
      writer.write(entries[i].uri().toString());
      writer.newLine();
    }
    writer.close();
  }

  public static URI createURI(String unescaped) throws EncoderException,
      URISyntaxException {
    return new URI(urlCodec.encode(unescaped));
  }
}
