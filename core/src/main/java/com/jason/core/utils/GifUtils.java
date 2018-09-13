package com.jason.core.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 判断图片是否是一张真正的gif
 * { @link https://github.com/facebook/fresco }
 * 两张典型的gif图展示
 * { @link http://dingyue.nosdn.127.net/iWg8lN5i5jFj4My8Qs5sDuYbuk9t6bVcS7UBTNRzn5fns1460621552294.gif }
 * { @link http://img5.cache.netease.com/m/2016/4/25/2016042518205820f96.gif }
 * Created by xiongcen on 16-4-26.
 */
public class GifUtils {

    /**
     * Every gif image starts with "GIF" bytes followed by
     * bytes indicating version of gif standard
     */
    private static final byte[] GIF_HEADER_87A = asciiBytes("GIF87a");
    private static final byte[] GIF_HEADER_89A = asciiBytes("GIF89a");
    private static final int GIF_HEADER_LENGTH = 6;


    /**
     * 通过图片路径判断该图片是否是一张真正的gif
     *
     * @param path
     * @return
     */
    public static boolean isGif(String path) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
            return isGifFormat(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean isGif(Context context, @DrawableRes int resId) {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(resId);
            return isGifFormat(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 尝试比对InputStream 头文件内容是否符合gif格式
     */
    public static boolean isGifFormat(final InputStream is) throws IOException {
//        Preconditions.checkNotNull(is);
        final byte[] imageHeaderBytes = new byte[GIF_HEADER_LENGTH];
        final int headerSize = readHeaderFromStream(is, imageHeaderBytes);
        return isGifHeader(imageHeaderBytes, headerSize);
    }

    /**
     * Checks if first headerSize bytes of imageHeaderBytes constitute a valid header for a gif image.
     * Details on GIF header can be found <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">
     * on page 7</a>
     *
     * @param imageHeaderBytes
     * @param headerSize
     * @return true if imageHeaderBytes is a valid header for a gif image
     */
    private static boolean isGifHeader(final byte[] imageHeaderBytes, final int headerSize) {
        if (headerSize < GIF_HEADER_LENGTH) {
            return false;
        }
        return matchBytePattern(imageHeaderBytes, 0, GIF_HEADER_87A) ||
                matchBytePattern(imageHeaderBytes, 0, GIF_HEADER_89A);
    }

    /**
     * Checks if byteArray interpreted as sequence of bytes has a subsequence equal to pattern
     * starting at position equal to offset.
     *
     * @param byteArray
     * @param offset
     * @param pattern
     * @return true if match succeeds, false otherwise
     */
    private static boolean matchBytePattern(
            final byte[] byteArray,
            final int offset,
            final byte[] pattern) {
//        Preconditions.checkNotNull(byteArray);
//        Preconditions.checkNotNull(pattern);
//        Preconditions.checkArgument(offset >= 0);
        if (pattern.length + offset > byteArray.length) {
            return false;
        }

        for (int i = 0; i < pattern.length; ++i) {
            if (byteArray[i + offset] != pattern[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Helper method that transforms provided string into it's byte representation
     * using ASCII encoding
     *
     * @param value
     * @return byte array representing ascii encoded value
     */
    private static byte[] asciiBytes(String value) {
//        Preconditions.checkNotNull(value);
        try {
            return value.getBytes("ASCII");
        } catch (UnsupportedEncodingException uee) {
            // won't happen
            throw new RuntimeException("ASCII not found!", uee);
        }
    }

    /**
     * Reads up to GIF_HEADER_LENGTH bytes from is InputStream. If mark is supported by is, it is
     * used to restore content of the stream after appropriate amount of data is read.
     * Read bytes are stored in imageHeaderBytes, which should be capable of storing
     * GIF_HEADER_LENGTH bytes.
     *
     * @param is
     * @param imageHeaderBytes
     * @return number of bytes read from is
     * @throws IOException
     */
    private static int readHeaderFromStream(
            final InputStream is,
            final byte[] imageHeaderBytes)
            throws IOException {
//        Preconditions.checkNotNull(is);
//        Preconditions.checkNotNull(imageHeaderBytes);
//        Preconditions.checkArgument(imageHeaderBytes.length >= GIF_HEADER_LENGTH);

        // If mark is supported by the stream, use it to let the owner of the stream re-read the same
        // data. Otherwise, just consume some data.
        if (is.markSupported()) {
            try {
                is.mark(GIF_HEADER_LENGTH);
                return ByteStreams.read(is, imageHeaderBytes, 0, GIF_HEADER_LENGTH);
            } finally {
                is.reset();
            }
        } else {
            return ByteStreams.read(is, imageHeaderBytes, 0, GIF_HEADER_LENGTH);
        }
    }

    public static final class ByteStreams {

        /**
         * Reads some bytes from an input stream and stores them into the buffer array
         * {@code b}. This method blocks until {@code len} bytes of input data have
         * been read into the array, or end of file is detected. The number of bytes
         * read is returned, possibly zero. Does not close the stream.
         * <p>
         * <p>A caller can detect EOF if the number of bytes read is less than
         * {@code len}. All subsequent calls on the same stream will return zero.
         * <p>
         * <p>If {@code b} is null, a {@code NullPointerException} is thrown. If
         * {@code off} is negative, or {@code len} is negative, or {@code off+len} is
         * greater than the length of the array {@code b}, then an
         * {@code IndexOutOfBoundsException} is thrown. If {@code len} is zero, then
         * no bytes are read. Otherwise, the first byte read is stored into element
         * {@code b[off]}, the next one into {@code b[off+1]}, and so on. The number
         * of bytes read is, at most, equal to {@code len}.
         *
         * @param in  the input stream to read from
         * @param b   the buffer into which the data is read
         * @param off an int specifying the offset into the data
         * @param len an int specifying the number of bytes to read
         * @return the number of bytes read
         * @throws IOException if an I/O error occurs
         */
        public static int read(InputStream in, byte[] b, int off, int len)
                throws IOException {
            if (len < 0) {
                throw new IndexOutOfBoundsException("len is negative");
            }
            int total = 0;
            while (total < len) {
                int result = in.read(b, off + total, len - total);
                if (result == -1) {
                    break;
                }
                total += result;
            }
            return total;
        }
    }
}
