package fi.oulu.tol.ohaptest;

import android.renderscript.Byte2;
import android.renderscript.Byte4;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Marja on 11/05/2015.
 */


public class IncomingMessage {
    private static final String TAG = "IncomingMessage";

    /**
     * The internal buffer. It is reserved in the readExactly() method.
     */
    private byte[] buffer;
    /**
     * The position where the next byte should be taken from.
     */
    private int position;
    /**
     * Character set used to convert strings.
     */
    private final Charset charset = Charset.forName("UTF-8");

    /**
     * Reads the specified amount of bytes from the given InputStream.
     *
     * @param inputStream the InputStream from which the bytes are read
     * @param length the amount of bytes to be read
     * @return the byte array of which length is the given length
     * @throws IOException when the actual read throws an exception
     */
    private static byte[] readExactly(InputStream inputStream, int length) throws IOException {
        byte[] bytes = new byte[length];
        int offset = 0;

        while (length > 0) {
            int got = inputStream.read(bytes, offset, length);
            if (got == -1)
                throw new EOFException("End of message input.");
            offset += got;
            length -= got;
        }

        return bytes;
    }

    public void readFrom(InputStream inputStream) throws IOException{
        //Reads one message from the given InputStream into the internal buffer.

    int count = 0;
        try {
            //Reading 2 bytes as length from the inputStream
            buffer = readExactly(inputStream, 2);
            count = (buffer[0] & 0xff)  << 8 | (buffer[1] & 0xff);
//            Log.d(TAG, "ReadFrom: Message length is " + count);

          //Inserting count amount of bytes into buffer
            buffer = readExactly(inputStream, count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean binary8(){
        if (position + 1  > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        boolean value = false;
        if(buffer[position] == 0x00) value = false;
        else if(buffer[position] == 0x01) value = true;
        position += 1;

        return value;
    }

    public int integer8(){
        if (position + 1  > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        int value = buffer[position];
        position += 1;
        return value;
    }

    public int integer16(){
        if (position + 2 > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        int value = (buffer[position] & 0xff) << 8 |
                (buffer[position + 1] & 0xff);
        position += 2;

            return value;
    }

    public int integer32(){
        if (position + 4 > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        int value =
                (buffer[position] & 0xff) << 24 |
                (buffer[position + 1] & 0xff) << 16 |
                (buffer[position + 2] & 0xff) << 8 |
                (buffer[position + 3] & 0xff);

        position += 4;

        return value;
    }

    public double decimal64(){
        if (position + 8 > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        long lvalue = 0L;

        for (int i=0; i<8; i++)
        {
            lvalue |= buffer[position+i] & 0xff;
            if(i != 7)
                lvalue <<= 8;
        }

        position += 8;

        return Double.longBitsToDouble(lvalue);
    }

    public String text(){
        //Reading data from the buffer

        int length = (buffer[position] & 0xff) << 8 | buffer[position + 1] & 0xff;
        if (length > buffer.length)
            throw new ArrayIndexOutOfBoundsException();

        String textString = "";

        //Two bytes has been read (length), read the next integer from buffer until the whole string is read
        position += 2;

        for (int i = 0; i<length; i++){
            textString  += (char)buffer[position + i];
        }

        position += length;

        return textString;
    }
}
