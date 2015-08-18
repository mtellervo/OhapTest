package fi.oulu.tol.ohaptest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Marja on 11/05/2015.
 */
public class OutgoingMessage
{
    private static final String TAG = "OutgoingMessage";
    /**
     * The internal buffer. It will be grown if the message do not fit to it.
     */
    private byte[] buffer = new byte[256];

    /**
     * The position where the next byte should be appended. The initial position
     * skips the space reserved for the message length.
     */
    private int position = 2;
    /**
     * Character set used to convert strings.
     */
    private final Charset charset = Charset.forName("UTF-8");

    /**
     * Ensures that the internal buffer have room for the specified amount of
     * bytes. Grows the buffer when needed by doubling its size.
     *
     * @param appendLength the amount of bytes to be appended
     */
    public void ensureCapacity(int appendLength) {
        if (position + appendLength < buffer.length) return;
        int newLength = buffer.length * 2;
        while (position + appendLength >= newLength) newLength *= 2;
        buffer = Arrays.copyOf(buffer, newLength);
    }

    public OutgoingMessage binary8(boolean b){
        if( b )buffer[position] = (byte)(0x01);
        else buffer[position] = (byte)0x00;

        position +=1;
        return this;
    }

    public OutgoingMessage integer8(int value){
        ensureCapacity(1);
            buffer[position] = (byte) value;
            position +=1;
            return this;
    }

    public OutgoingMessage integer16(int value) {
        ensureCapacity(2);

        buffer[position] = (byte) (value >> 8);
        buffer[position + 1] = (byte) value;
        position += 2;

        return this;
    }

    public OutgoingMessage integer32(int value){
        ensureCapacity(4);
        buffer[position] = (byte) (value >> 24);
        buffer[position + 1] = (byte) (value >> 16);
        buffer[position + 2] = (byte) (value >> 8);
        buffer[position + 3] = (byte) (value);
        position += 4;

        return this;
    }
    public OutgoingMessage decimal64(double value)
    {
        long lvalue = Double.doubleToLongBits(value);
        ensureCapacity(8);

        for (int i = 0; i<8; i++){
            buffer[(position + 7) - i] = (byte)lvalue;
            lvalue >>= 8;
        }

        position += 8;

        return this;
    }

    public OutgoingMessage text(String tString){
        //Writing bytes to buffer

        //Converting string to bytes
        byte[] textBytes = tString.getBytes(charset);
        int tLength = textBytes.length;
        ensureCapacity(tLength);


        //Inserting the length (int) into buffer
        buffer[position] = (byte) (tLength >> 8);
        buffer[position + 1] = (byte) tLength;
        position += 2;

        //Inserting rest of bytes into buffer as integer16 (2 bytes)
        for(int i = 0; i< tLength; i++){
            buffer[position + i] = textBytes[i];
        }

        position += tLength;
        return this;
    }

    void writeTo(OutputStream outputStream){
        //writes one message into the outputStream from internal buffer

        //data length
        int count = this.position -2;

        //set the first two bytes as the count
        buffer[0] = (byte) (count >> 8);
        buffer[1] = (byte) count;


        //write count amount of bytes to outputStream
        for ( position = 0; position < count + 2; position++) {
            try {
                outputStream.write(buffer[position]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}