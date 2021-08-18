package com.igmi.rampage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirstPageActivity extends AppCompatActivity {

    RecyclerView rec_event;
    ArrayList<Setget_EventList> v_list;

Button but_submit;

    ProgressDialog pd;

    TextView txt_spinner_set;

    LinearLayout ll_spinner;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage_main);

        rec_event=(RecyclerView)findViewById(R.id.rec_event);
        LinearLayoutManager linearLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec_event.setLayoutManager(linearLayoutManagaer);
/*
        pd = new ProgressDialog(FirstPageActivity.this);
        pd.setMessage("loading");
        pd.show();*/
        
        EventList();

        but_submit=(Button)findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Id=Contant.Id;
                Intent i=new Intent(getApplicationContext(),SecondPageActivity.class);
                startActivity(i);
                //Toast.makeText(FirstPageActivity.this, "Click on that", Toast.LENGTH_SHORT).show();
            }
        });

        txt_spinner_set=(TextView)findViewById(R.id.txt_spinner_set);
        ll_spinner=(LinearLayout)findViewById(R.id.ll_spinner);
        ll_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rec_event.setVisibility(View.VISIBLE);
            }
        });


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
            View v = inflater.inflate(R.layout.row_upcoming_list, viewGroup, false);
            MyViewholder_anni obj = new MyViewholder_anni(v);
            return obj;

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder_anni myViewholder, final int i) {



            myViewholder.txt_event.setText(v_list.get(i).getName());

            myViewholder.txt_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contant.Text=v_list.get(i).getName();
                    txt_spinner_set.setText(Contant.Text);
                    rec_event.setVisibility(View.GONE);
                    Contant.Id=v_list.get(i).getEvent_id();
                }
            });



        }

        @Override
        public int getItemCount() {
            return v_list.size();
        }




    }







    private void EventList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.rampagebots.co.uk/api/events/find?status=active",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            v_list = new ArrayList<>();

                            JSONObject jobjResult = new JSONObject(response);
                            int total = jobjResult.getInt("total");
                            int pages = jobjResult.getInt("pages");
                            int currentPage = jobjResult.getInt("currentPage");



                            if (pages==1) {

                                JSONArray jobjProfile = jobjResult.getJSONArray("events");

                                for (int i = 0; i < jobjProfile.length(); i++) {
                                    JSONObject jobjImagelist = jobjProfile.getJSONObject(i);
                                    String event_id = jobjImagelist.getString("event_id");
                                    String name = jobjImagelist.getString("name");
                                    String location = jobjImagelist.getString("location");
                                    String date_of_event = jobjImagelist.getString("date_of_event");
                                    String event_organiser = jobjImagelist.getString("event_organiser");
                                    String weight_class = jobjImagelist.getString("weight_class");
                                    String status = jobjImagelist.getString("status");
                                    // Toast.makeText(UserLikeListActivity.this, "type :"+type, Toast.LENGTH_SHORT).show();





                                        Setget_EventList all_product=new Setget_EventList();

                                        all_product.setEvent_id(event_id);
                                        all_product.setName(name);
                                        all_product.setLocation(location);
                                        all_product.setDate_of_event(date_of_event);
                                        all_product.setEvent_organiser(event_organiser);
                                        all_product.setWeight_class(weight_class);
                                        all_product.setStatus(status);




                                    v_list.add(all_product);




                                   // pd.dismiss();






                                    // player_progressbar.setVisibility(View.GONE);


                                }

                                Myadapter_anni myadapter_anni=new Myadapter_anni();
                                rec_event.setAdapter(myadapter_anni);

                            }

                            else {

                               /* ll_noproduct_found.setVisibility(View.VISIBLE);
                                player_progressbar.setVisibility(View.VISIBLE);;*/
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
