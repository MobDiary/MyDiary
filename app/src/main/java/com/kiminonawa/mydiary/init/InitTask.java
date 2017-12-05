package com.kiminonawa.mydiary.init;

import android.content.Context;
import android.os.AsyncTask;

import com.kiminonawa.mydiary.BuildConfig;
import com.kiminonawa.mydiary.db.DBManager;
import com.kiminonawa.mydiary.shared.OldVersionHelper;
import com.kiminonawa.mydiary.shared.SPFManager;

// AsyncTask를 받아 비동기적으로 동작한다.
public class InitTask extends AsyncTask<Long, Void, Boolean> {

    public interface InitCallBack {
        void onInitCompiled(boolean showReleaseNote);
    }

    private InitCallBack callBack;
    private Context mContext;
    boolean showReleaseNote;


    public InitTask(Context context, InitCallBack callBack) {
        this.mContext = context;
        this.callBack = callBack;
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        try {
            DBManager dbManager = new DBManager(mContext);
            dbManager.opeDB();
            dbManager.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showReleaseNote;
    }

    @Override
    protected void onPostExecute(Boolean showReleaseNote) {
        super.onPostExecute(showReleaseNote);
        callBack.onInitCompiled(showReleaseNote);
    }


}
