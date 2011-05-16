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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Stack;

import org.apache.commons.codec.EncoderException;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.wooden.io.FileTaskImpl;

public class M3UGenerator {
  public static final String M3U_HEADER = "#EXTM3U";
  public static final String M3U_EXTINF = "#EXTINF:";

  public static M3UPlaylist generateM3UPlaylist(File srcDir,
      String pathReplacePattern, String pathReplacement) throws IOException,
      ID3Exception, URISyntaxException, EncoderException {
    if (srcDir.isFile())
      throw new IllegalArgumentException("Not a directoy: "
          + srcDir.getAbsolutePath());

    M3UPlaylist playlist = new M3UPlaylist();
    File[] subFiles = srcDir.listFiles();
    ID3TagData tagData = null;
    FileTaskImpl fileTree = new FileTaskImpl(srcDir, subFiles);
    FileTaskImpl fileTask = null;
    String fileName = null;
    String absPath = null;

    Stack taskStack = new Stack();
    taskStack.push(fileTree);

    while (taskStack.size() > 0) {
      fileTree = (FileTaskImpl) taskStack.pop();

      while (fileTree.hasMoreTasks()) {
        fileTask = (FileTaskImpl) fileTree.nextTask();
        if (fileTask.getType() == 1) {
          fileName = fileTask.getFileName();
          if (fileName.toLowerCase().endsWith(".mp3")) {
            tagData = ID3TagData.readID3TagData(
                new MP3File(new File(fileTask.getAbsolutePath())), true);
            absPath = fileTask.getAbsolutePath().replace('\\', '/');
            if ((pathReplacePattern != null) && (pathReplacement != null)) {
              int iPattern = absPath.indexOf(pathReplacePattern);
              if (iPattern > -1) {
                absPath = absPath.substring(0, iPattern) + pathReplacement
                    + absPath.substring(iPattern + pathReplacePattern.length());
              }
              URI uri = M3UPlaylist
                  .createURI(new URL(absPath).toExternalForm());
              playlist.addEntry(new M3UEntry(createM3UTitle(fileTask, tagData),
                  1, uri));
            }
          }
        } else {
          taskStack.push(fileTask);
        }
      }
    }
    return playlist;
  }

  private static String createM3UTitle(FileTaskImpl fileTask,
      ID3TagData tagContainer) throws IOException {
    String artist = tagContainer.getArtist();
    String album = tagContainer.getAlbum();
    String title = tagContainer.getTitle();
    String year = tagContainer.getYear();
    String fileName = fileTask.getFileName();
    String m3u_title = null;

    if (((tagContainer.tagV1 == null) && (tagContainer.tagV2 == null))
        || ((artist == null) && (album == null) && (title == null))) {
      m3u_title = fileName;
    } else {
      int iDelim;
      if ((artist == null) || (artist.trim().length() == 0)) {
        iDelim = fileName.indexOf(45);
        if (iDelim > -1)
          artist = fileName.substring(0, iDelim);
        else {
          artist = fileName;
        }

      }

      if (album == null)
        album = "";
      else
        album.trim();

      if ((title == null) || (title.trim().length() == 0)) {
        iDelim = fileName.indexOf(45);
        if (iDelim > -1)
          title = fileName.substring(0, iDelim);
        else
          title = "";
      }

      if ((year == null) || (year.trim().length() == 0)) {
        year = "";
      } else {
        year = "(" + year + ")";
        if (album.length() > 0)
          year = " " + year;
      }
      m3u_title = artist.trim() + " - [ " + album.trim() + year + " ] - "
          + title.trim();
    }

    return m3u_title;
  }

  public static void main(String[] args) {

    try {
      File inputDir = new File(args[0]);
      File outputFile = new File(args[1]);
      M3UPlaylist playlist = generateM3UPlaylist(inputDir, args[2], args[3]);
      FileOutputStream out = new FileOutputStream(outputFile);
      playlist.store(out);
      out.close();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
