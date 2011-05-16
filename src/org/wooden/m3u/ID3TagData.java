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

import java.io.IOException;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import org.wooden.util.StringTool;

public class ID3TagData {
  public ID3V1Tag tagV1 = null;
  public ID3V2Tag tagV2 = null;
  private boolean preferV2;

  public ID3TagData(ID3V1Tag tagV1, ID3V2Tag tagV2, boolean preferV2) {
    this.tagV1 = tagV1;
    this.tagV2 = tagV2;
    this.preferV2 = preferV2;
  }

  public static ID3TagData readID3TagData(MP3File mp3file, boolean preferID3v2)
      throws IOException, ID3Exception {
    return new ID3TagData(mp3file.getID3V1Tag(), mp3file.getID3V2Tag(),
        preferID3v2);
  }

  private String selectTagInfo(String sV1, String sV2) {
    sV1 = StringTool.eliminateBlank(sV1);
    sV2 = StringTool.eliminateBlank(sV2);

    if (sV1 != null && (!preferV2 || sV2 == null))
      return sV1;
    else
      return sV2;
  }

  public String getArtist() {
    String artistV1 = null;
    if (this.tagV1 != null)
      artistV1 = this.tagV1.getArtist();

    String artistV2 = null;
    if (this.tagV2 != null)
      artistV2 = this.tagV2.getArtist();

    return selectTagInfo(artistV1, artistV2);
  }

  public String getAlbum() {
    String albumV1 = null;
    if (this.tagV1 != null)
      albumV1 = this.tagV1.getAlbum();

    String albumV2 = null;
    if (this.tagV2 != null)
      albumV2 = this.tagV2.getAlbum();

    return selectTagInfo(albumV1, albumV2);
  }

  public String getTitle() {
    String titleV1 = null;
    if (this.tagV1 != null)
      titleV1 = this.tagV1.getTitle();

    String titleV2 = null;
    if (this.tagV2 != null)
      titleV2 = this.tagV2.getTitle();

    return selectTagInfo(titleV1, titleV2);
  }

  public String getYear() {
    String yearV1 = null;
    String yearV2 = null;
    try {

      if (this.tagV1 != null)
        yearV1 = this.tagV1.getYear();

      if (this.tagV2 != null)
        yearV2 = String.valueOf(this.tagV2.getYear());
    } catch (ID3Exception e) {}

    return selectTagInfo(yearV1, yearV2);
  }
}
