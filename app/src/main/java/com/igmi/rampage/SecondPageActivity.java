package com.igmi.rampage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondPageActivity extends AppCompatActivity {

    TextView txt_1;
    TextView txt_2;
    TextView txt_3;
    RecyclerView rec_all_list;
    ArrayList<SetgetAllList> v_list;
    ProgressDialog pd;
    List<String> jobList;
    JSONArray jobjProfile;

    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);


        txt_1=(TextView)findViewById(R.id.txt_1);
        txt_2=(TextView)findViewById(R.id.txt_2);
        txt_3=(TextView)findViewById(R.id.txt_3);

       Id=Contant.Id;
       // Toast.makeText(this, "Id : "+Id, Toast.LENGTH_SHORT).show();


        rec_all_list=(RecyclerView)findViewById(R.id.rec_all_list);
        LinearLayoutManager linearLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec_all_list.setLayoutManager(linearLayoutManagaer);

        pd = new ProgressDialog(SecondPageActivity.this);
        pd.setMessage("loading");
        pd.show();

        EventList();





    }


    public class MyViewholder_anni extends RecyclerView.ViewHolder{



        TextView txt_event;







        public MyViewholder_anni(@NonNull View itemView) {
            super(itemView);


            txt_event=(TextView)itemView.findViewById(R.id.txt_event);






        }
    }


    public class Myadapter_anni extends RecyclerView.Adapter<MyViewholder_anni> {

        @NonNull
        @Override
        public MyViewholder_anni onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.row_all_list, viewGroup, false);
            MyViewholder_anni obj = new MyViewholder_anni(v);
            return obj;

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder_anni myViewholder, final int i) {


            myViewholder.txt_event.setText(v_list.get(i).getKey());
//            Toast.makeText(SecondPageActivity.this, v_list.get(0).getNextBattles(), Toast.LENGTH_SHORT).show();
//




        }

        @Override
        public int getItemCount() {
            return v_list.size();
        }




    }




    private void EventList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.rampagebots.co.uk/api/events/upcoming_battles?id="+Id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            v_list = new ArrayList<>();


                            JSONObject jobjResult = new JSONObject(response);
                            String currentBattle = jobjResult.getString("currentBattle");
                            txt_2.setText(currentBattle);
                            String currentBattleName = jobjResult.getString("currentBattleName");
                            txt_1.setText(currentBattleName);



                            if (!currentBattle.equals("")) {
                                jobList = new ArrayList<>();

                                jobjProfile = jobjResult.getJSONArray("nextBattles");

                                for (int i = 0; i < jobjProfile.length(); i++) {

                                    String key = jobjProfile.getString(i);


                                    pd.dismiss();


                                    SetgetAllList all_product=new SetgetAllList();
                                    all_product.setKey(key);

                                    v_list.add(all_product);


                                }

                                Myadapter_anni myadapter_anni=new Myadapter_anni();
                                rec_all_list.setAdapter(myadapter_anni);

                            }

                            else {


                            }


                        } catch(JSONException e1){
                            e1.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* dialog.dismiss();*/
                        String err = parseVolleyError(error);
                        /*    makeText(Login.this, err, Toast.LENGTH_SHORT).show();*/
                        // Toast.makeText(All_Products.this, err, Toast.LENGTH_SHORT).show();

                    }
                }) {



        /*    @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", AccessToken);
                params.put("limit", "0");
                params.put("offset", "0");


                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance().addToRequestque(stringRequest);


    }


    public String parseVolleyError(VolleyError error) {
        String errors="";
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            errors = data.getString("error");
            Log.e("VolleyError", errors);
            //showSnackBar(errors);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errors;
    }
}
