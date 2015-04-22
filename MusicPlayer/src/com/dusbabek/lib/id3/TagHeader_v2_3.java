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

import java.io.*;

/**
 * ID3v2.3 header. They only get more complicated.
 */
public class TagHeader_v2_3
    extends BasicTagHeader
{
    private ExtendedTagHeader extHeader = null;

    /**
     * construct a header from 10 bytes and an input stream.
     * @param data byte[] a basic header.
     * @param in InputStream
     * @throws java.io.IOException
     */
    public TagHeader_v2_3(byte[] data, InputStream in)
        throws IOException
    {
        super(data);
        if (usesExtendedHeader())
            extHeader = makeExtendedHeader(in);
    }

    /** @return true if this tag uses unsynchronization */
    public boolean usesUnsynchronization()
    {
        return (getFlags() & 0x80) > 0;
    }

    /**
     * create an extended header. Extended header formats differ across versions
     * so the implementation of creating them needs to be specific to the
     * header implementations.
     * @param in InputStream
     * @return ExtendedTagHeader
     * @throws java.io.IOException
     */
    protected ExtendedTagHeader makeExtendedHeader(InputStream in)
        throws IOException
    {
        return new ExtendedTagHeader_v2_3(in);
    }

    /** @return true if an extended header is present */
    public boolean usesExtendedHeader()
    {
        return (getFlags() & 0x40) > 0;
    }

    /** {@inheritDoc} */
    public long getDataSize()
    {
        return getTagSize() - (extHeader == null ? 0 : extHeader.sizeOf());
    }

}
