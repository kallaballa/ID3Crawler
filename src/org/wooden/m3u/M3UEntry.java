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

import java.net.URI;

public class M3UEntry {
  private String title;
  private int length;
  private URI uri;

  public M3UEntry(String title, int length, URI uri) {
    this.title = title;
    this.length = length;
    this.uri = uri;
  }

  public String title() {
    return title;
  }

  public int length() {
    return length;
  }

  public URI uri() {
    return uri;
  }
}
