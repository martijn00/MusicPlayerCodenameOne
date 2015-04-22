/*
(c) Copyright 2004, 2005 Gary Dusbabek gdusbabek@gmail.com

ALL RIGHTS RESERVED.

By using this software, you acknowlege and agree that:

1. THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND
FITNESS FOR A PARTICULAR PURPOSE.

2. This product may be freely copied and distributed in source or binary form
given that the license (this file) and any copyright declarations remain in
tact.

The End
*/

package com.dusbabek.lib.id3;


/**
 * Interface implemented by all tag headers.
 */
public interface TagHeader
{
    /** @return size of all frames (no headers included) */
    public long getDataSize();

    /** @return true if unsynchronization is used. */
    public boolean usesUnsynchronization();
}
