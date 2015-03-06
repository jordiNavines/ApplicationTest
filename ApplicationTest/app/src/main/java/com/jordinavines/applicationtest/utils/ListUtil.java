package com.jordinavines.applicationtest.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.jordinavines.applicationtest.app.AppController;
import com.jordinavines.applicationtest.constants.Constants;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.mysql.DataSource;
import com.jordinavines.applicationtest.mysql.UserContentProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class ListUtil {

    public interface ListUserListener {
        public void onGetListUserSuccess(ArrayList<User> users);
        public void onRefreshSuccess(ArrayList<User> users);
        public void onGetListUserError();
    }

    private static String tag= "getUSers";

    private static DataSource datasource;

    public static void retrieveUsers(boolean refresh, ListUserListener listener){
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.url, getListUsersSuccessListener(refresh, listener),
                getListUsersErrorListener(listener));

        AppController.getInstance().addToRequestQueue(strReq, tag);
    }


    private static Response.Listener<String> getListUsersSuccessListener(final boolean refresh, final ListUserListener listener) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.LOGD("success",response);

                Gson gson = new Gson();
                User[] users = gson.fromJson(response, User[].class);

                if (users!=null) {
                    ArrayList<User> usersdownloaded= new ArrayList<User>(Arrays.asList(users));
                    listener.onGetListUserSuccess(usersdownloaded);
                    storeUsers(AppController.getInstance().getApplicationContext(), usersdownloaded);
                }else{
                    listener.onGetListUserError();
                }

            }
        };
    }

    private static Response.ErrorListener getListUsersErrorListener(final ListUserListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.LOGD("onErrorResponse", error.toString());

                try {
                    if (error==null || error.networkResponse==null || error.networkResponse.data==null){

                    }else {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        LogUtils.LOGD("onErrorResponse", jsonObject.toString());
                    }

                } catch (UnsupportedEncodingException e) {
                    //e.printStackTrace();
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

                listener.onGetListUserError();

            }
        };
    }


    public static void initDataSource(Context ctx) throws SQLException {
        datasource= new DataSource(ctx);
        datasource.open();
    }

    public static void closeDataSource(){
        if (datasource!=null)
            datasource.close();
    }

    public static User getUserDatabase(Context ctx, int id){

            return datasource.getUser(ctx, id);


    }

    /**
     *
     * store users into a database
     *
     * @param users
     */
    private static void storeUsers(Context context, ArrayList<User> users){
        StoreUsers storeTask= new StoreUsers(context, users);
        storeTask.execute();
    }


    private static class StoreUsers extends AsyncTask<Void, Void, Void> {
        ArrayList<User> users;
        Context context;

        private StoreUsers(Context _context, ArrayList<User> _users) {
            this.users= _users;
            this.context= _context;
        }

        protected Void doInBackground(Void... voids) {

            Uri uri = Uri.parse(UserContentProvider.CONTENT_URI+"");
            context.getContentResolver().delete(uri, null, null);

            for (int i = 0; i < users.size(); i++) {
                datasource.addUser(context, users.get(i));
            }

            return null;
        }

    }
}
