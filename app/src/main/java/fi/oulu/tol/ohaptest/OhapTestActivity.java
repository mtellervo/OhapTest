package fi.oulu.tol.ohaptest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.henrikhedberg.hbdp.client.HbdpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class OhapTestActivity extends ActionBarActivity {

    private final String TAG = "OhapTestActivity";

    //Streams
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static IncomingMessage incomingMessage;
    private static OutgoingMessage outgoingMessage;

    //Resources

   /* Button button2;
    Button button3;
    Button button4;*/
/*
        public void updateViewRunnable() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d(TAG, "message to write to the ui");
                    int messageType = incomingMessage.integer8();
                    switch (messageType) {
                        case 0x01:
                            text_view.append("\nmessage-type-logout");
                            text_view.append("item-data-description: " + incomingMessage.text() + "\n");
                            break;
                        case 0x02:
                            break;
                        case 0x03:
                            break;
                        case 0x04:
                            break;
                        case 0x05:
                            break;
                        case 0x06:
                            break;
                        case 0x07:
                            break;
                        case 0x08:
                            text_view.append("\nmessage-type-container");

                            long itemIdentifier = incomingMessage.integer32();
                            long itemDataParentIdentifier = incomingMessage.integer32();
                            String itemDataName = incomingMessage.text();
                            String itemDataDescription = incomingMessage.text();
                            boolean itemDataInternal = incomingMessage.binary8();
                            double itemDataX = incomingMessage.decimal64();
                            double itemDataY = incomingMessage.decimal64();
                            double itemDataZ = incomingMessage.decimal64();
                            text_view.append("item-identifier: " + itemIdentifier + "\n");
                            text_view.append("item-data-parent-identifier: " + itemDataParentIdentifier + "\n");
                            text_view.append("item-data-name: " + itemDataName + "\n");
                            text_view.append("item-data-description: " + itemDataDescription + "\n");
                            text_view.append("item-data-internal: " + itemDataInternal + "\n");
                            text_view.append("item-data-x: " + itemDataX + "\n");
                            text_view.append("item-data-y: " + itemDataY + "\n");
                            text_view.append("item-data-z: " + itemDataZ + "\n");
                            break;
                        case 0x09:
                            break;
                        case 0x0a:
                            break;
                        case 0x0b:
                            break;
                        default:
                            text_view.append("Unrecognised message type");
                            break;
                    }
                }
            });
        }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohap_test);

        //Find resources

        TextView text_view = (TextView) findViewById(R.id.text_view);
        Button button1 = (Button) findViewById(R.id.button1);
     /*   button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
*/
        // Set up connection to server and get input and output streams
        try {
            HbdpConnection mHbdpConnection = new HbdpConnection(new URL("http://ohap.opimobi.com:18000/"));
            Log.d(TAG, "Hbdp connection established");
            inputStream = mHbdpConnection.getInputStream();
            Log.d(TAG, "Input -stream got.");
            outputStream = mHbdpConnection.getOutputStream();
            Log.d(TAG, "Output stream got.");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Connection to server succeeded");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                outgoingMessage = new OutgoingMessage();
                try{
                    outgoingMessage.integer8(0x00)      // message-type-login
                            .integer8(0x01)      // protocol-version
                            .text("mtellervo")      // login-name
                            .text("l3l7AtTH")    // login-password l3l7AtTH
                            .writeTo(outputStream);
                } catch ( Exception e) {//IOException
                    e.printStackTrace();
                }
            }
        });
/*
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outgoingMessage = new OutgoingMessage();
                try {
                    outgoingMessage.integer8(0x01) // message-type-logout
                            .text("")
                            .writeTo(outputStream);
                } catch (Exception e) {//IOException
                    e.printStackTrace();
                }
            }
        });

        //ping
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outgoingMessage = new OutgoingMessage();
                try {
                outgoingMessage.integer8(0x02) // message-type-ping
                    .integer32(0x01);
                } catch ( Exception e) {//IOException
                    e.printStackTrace();
                }
            }
        });

        //pong
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outgoingMessage = new OutgoingMessage();

                try
                {
                outgoingMessage.integer8(0x03) // message-type-pong
                        .integer32(0x01);
                } catch ( Exception e) {//IOException
                    e.printStackTrace();
                }
            }
        });*/

     //   thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_ohap_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
       // }

       // return super.onOptionsItemSelected(item);
    return true;
    }

  /*  private Thread thread = new Thread() {

        @Override
        public void run() {

            Log.d(TAG, "Receiving thread started");

            try {
                while(true) {
                    incomingMessage = new IncomingMessage();
                    //Read a message from the input stream
                    incomingMessage.readFrom(inputStream);
                    OhapTestActivity.this.updateViewRunnable();
                    Thread.sleep(2000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          try//not valid
            {
                do {
                    Log.d(TAG, "text written from input stream ");
                    OhapTestActivity.this.updateViewRunnable();
                    Thread.sleep(2000);
                } while ((int ii=IncomingMessage.readFrom(inputStream)) != -1);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };*/
}


