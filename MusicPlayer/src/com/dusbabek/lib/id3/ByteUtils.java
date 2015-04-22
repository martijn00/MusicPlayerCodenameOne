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
 * Utilities for converting long to bytes and vice versa.  Please note that the
 * longs I deal with are only 4 bytes in size (not 8).  I could have gotten
 * away with using ints, but java has a nasty habit of using sign extension
 * which would have foiled me for large UNSIGNED values.
 */
public class ByteUtils
{
    /**
     * convert a 4 byte array to an 8 byte long. only last 4 bytes matter though.
     * @param b byte[]
     * @param offset int
     * @return long
     */
    public static long byte4ToLong(byte[] b, int offset)
    {
        long l = 0;
        for (int i = 0; i < 4; i++)
        {
            l <<= 8;
            l |= (0x00000000000000ffL & b[offset+i]);
        }
        return l;
    }

    /**
     * This is unique to Id3s. It doesn't really belong here. Tag headers are
     * encoded in 4 bytes, but only the 7 least bits are significant. So take
     * the long; convert it to 4 bytes; then throw away the first bit of each
     * byte. It is IMPORTANT to get this method right because it is used to
     * report the tag size, which is crucial.
     * @param l long
     * @return long
     */
    public static long removeZeroBits(long l)
    {
        long c = 0;
        for (int i = 0; i < 4; i++)
            c |= ((0x0000000000007fL << (8 * i)) & l) >> i ;
        return c;

    }

    public static String byteToString(byte b)
    {
        String s = "0x";
        for (int i = 0; i < 8; i++)
        {
            if ((b & (1 << 7-i)) > 0)
                s += "1";
            else
                s += "0";
        }
        return s;
    }
}
