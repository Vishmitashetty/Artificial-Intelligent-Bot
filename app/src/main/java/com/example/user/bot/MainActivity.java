package com.example.user.bot;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.alicebot.ab.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    EditText chat_text;
    Button SEND;
    boolean position = false;
    ChatAdapter adapter;
    Context ctx = this;
    String pathMap;
    //Bot bot;

    //program ab object added

    String botname = "alice2";
    String p = Environment.getExternalStorageDirectory().getAbsolutePath()+"";
   Bot bot = new Bot(botname,p);

    //Bot bot = new Bot(botname,p);
    //String path="C:\\Users\\User\\AndroidStudioProjects\\Bot\\app\\src\\main\\assets";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.wtf("TAG","we are inside bundle");//aadded flag
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.chat_list_view);
        chat_text = (EditText) findViewById(R.id.chatTxt);
        SEND = (Button) findViewById(R.id.send_button);
        adapter = new ChatAdapter(ctx, R.layout.single_message_layout);
        listview.setAdapter(adapter);

        //reading files


        //copyAssets();
        //copyFilesToSdCard();


            listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listview.setSelection(adapter.getCount() - 1);
                }
            });

            SEND.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str2 = chat_text.getText().toString();
                   Chat chatSession = new Chat(bot);
                    if (str2.length() != 0) {
                        adapter.add(new DataProvider(position, str2));
                        position = !position;
                        //bot object added

                        String request = str2;
                        String response = chatSession.multisentenceRespond(request); //problem in this line
                        //System.out.println(response);
                        adapter.add(new DataProvider(position, response));
                        position = !position;
                        chat_text.setText("");
                    }
                }
            });
        }

/*
    static String extStorageDirectory =  Environment.getDataDirectory().toString();
    final static String TARGET_BASE_PATH = extStorageDirectory+"/Android/data/";

    private void copyFilesToSdCard() {
        copyFileOrDir("");
    }

    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            Log.i("tag", "copyFileOrDir() "+path);
            assets = assetManager.list(path);
            Log.wtf("tag","list path by assest manager");//added by me
            if (assets.length == 0) {
                Log.wtf("tag","assests.length is zero");//added by me
                copyFile(path);
            } else {
                String fullPath =  TARGET_BASE_PATH + path;
                Log.i("tag", "path="+fullPath);
                Log.wtf("tag", "assests.length is not zero");//added by me
                File dir = new File(fullPath); //dir name directory created on fullpath
                if (!dir.exists())
                    if (!dir.mkdirs());
                Log.i("tag", "could not create dir "+fullPath);
                for (int i = 0; i < assets.length; ++i) {
                    Log.wtf("tag","inside for loop");//added by me
                    String p;
                    if (path.equals("")) {
                        p = "";
                        Log.wtf("tag", "inside for if loop");//added by me
                    }
                    else {
                        p = path + "/";

                        copyFileOrDir(p + assets[i]);
                        Log.wtf("tag", "inside for else loop");//added by me
                    }
                }
            }
        } catch (IOException ex) {
            Log.wtf("tag","you are inside exception");//added by me
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();
        Log.wtf("tag","inside copy file string");//added by me

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() "+filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".aiml")) // extension was added to avoid compression on APK file
                newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length()-4);
            else
                newFileName = TARGET_BASE_PATH + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.wtf("tag","you are inside copy file exception");//added by me
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }

    }
*/

    /* private void copyAssets() {
         AssetManager assetManager = getAssets();

         if (getExternalCacheDir() != null) {
             pathMap = Environment.getExternalStorageDirectory().getAbsolutePath();
         }
         else
         {
             pathMap = Environment.getDataDirectory().getAbsolutePath();
         }
         String[] files = null;
         try {
             files = assetManager.list("bots");
         } catch (IOException e) {
             Log.e("tag", e.getMessage());
         }
         for (String fileName :files){
             System.out.println("Files =>" +fileName);
             InputStream in = null;
             OutputStream out = null;

             try{
                 in = assetManager.open("Files/"+fileName);
                 out = new FileOutputStream(pathMap+ "/"+ fileName);
                 copyFiles(in, out);
                 in.close();
                 in = null;
                 out.flush();
                 out.close();
                 out = null;
             }
             catch(Exception e){
                 Log.e("tag", e.getMessage());
             }

         }

     }*/
    private void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        Log.wtf("Tag", "writing...");
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        Log.wtf("Tag", "Written 1024 bytes");

    }


    //copying file ends here
    class DataProvider {
        public boolean position;
        public String message;

        public DataProvider(boolean position, String message) {
            super();
            this.position = position;
            this.message = message;
        }
    }

}

class ChatAdapter extends ArrayAdapter<MainActivity.DataProvider> {
    private List<MainActivity.DataProvider> chat_list = new ArrayList<MainActivity.DataProvider>();
    private TextView CHAT_TXT;
    Context CTX;

    public ChatAdapter(Context context, int resource) {
        super(context, resource);
        CTX = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void add(MainActivity.DataProvider object) {
        // TODO Auto-generated method stub
        chat_list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return chat_list.size();
    }

    @Override
    public MainActivity.DataProvider getItem(int position) {
        // TODO Auto-generated method stub
        return chat_list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.single_message_layout, parent, false);
        }
        CHAT_TXT = (TextView) convertView.findViewById(R.id.singleMessage);
        String Message;
        boolean POSITION;
        MainActivity.DataProvider provider = getItem(position);
        Message = provider.message;
        POSITION = provider.position;
        CHAT_TXT.setText(Message);
        CHAT_TXT.setBackgroundResource(POSITION ? R.drawable.left : R.drawable.right);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        if (!POSITION) {
            params.gravity = Gravity.START;
        } else {
            params.gravity = Gravity.END;
        }
        CHAT_TXT.setLayoutParams(params);
        return convertView;
    }


}
