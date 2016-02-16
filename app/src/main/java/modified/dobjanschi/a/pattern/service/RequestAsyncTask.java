package modified.dobjanschi.a.pattern.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import modified.dobjanschi.a.pattern.R;
import modified.dobjanschi.a.pattern.database.DatabaseUtils;
import modified.dobjanschi.a.pattern.database.tables.RequestsTable;
import modified.dobjanschi.a.pattern.service.model.RequestItem;

/**
 * @author Artur Vasilov
 */
public class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private final Context mContext;

    public RequestAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!DatabaseUtils.isUriEmpty(mContext, RequestsTable.URI)) {
            //show the idea that downloading will be completed even if user has closed the application
            return null;
        }

        SystemClock.sleep(5000);

        RequestItem requestItem = new RequestItem("request1");
        String json = loadResource();
        requestItem.setResponse(json);
        RequestsTable.save(mContext, requestItem);
        mContext.getContentResolver().notifyChange(RequestsTable.URI, null);
        return null;
    }

    private String loadResource() {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.news_list);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferReader = new BufferedReader(inputReader);
            int n;
            while ((n = bufferReader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            inputStream.close();
        } catch (IOException ioException) {
            return null;
        }

        return writer.toString();
    }
}

