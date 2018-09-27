package com.hackncs.click;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentStudentProfile extends Fragment implements View.OnClickListener {

    View view;
    Context context;
    TextInputEditText course, branch, year, section, univ_roll_no, contact_no, father_name, mother_name, address;
    String PROFILE_ID, TOKEN;
    Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        Iconify.with(new FontAwesomeModule());
        initialize();
        fetchAndDisplay();
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title = item.getTitle().toString();
                if (title.equals("Edit")) {
                    item.setIcon(new IconDrawable(context, FontAwesomeIcons.fa_save)
                            .colorRes(R.color.white)
                            .actionBarSize());
                    item.setTitle("Save");
                    enableViews();

                } else {
                    item.setIcon(new IconDrawable(context, FontAwesomeIcons.fa_edit)
                            .colorRes(R.color.white)
                            .actionBarSize());
                    item.setTitle("Edit");
                    uploadChanges();
                }


                return false;
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    private void fetchAndDisplay() {
        String URL = Endpoints.student_profile_data;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + PROFILE_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            setTexts(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "token " + TOKEN);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setTexts(JSONObject jsonObject) throws JSONException {
        course.setText(jsonObject.getString("course"));
        branch.setText(jsonObject.getString("branch"));
        year.setText(jsonObject.getString("year"));
        section.setText(jsonObject.getString("section"));
        univ_roll_no.setText(jsonObject.getString("univ_roll_no"));
        contact_no.setText(jsonObject.getString("contact_no"));
        father_name.setText(jsonObject.getString("father_name"));
        mother_name.setText(jsonObject.getString("mother_name"));
        address.setText(jsonObject.getString("address"));
        disableViews();
    }

    private void disableViews() {
        course.setEnabled(false);
        branch.setEnabled(false);
        year.setEnabled(false);
        section.setEnabled(false);
        univ_roll_no.setEnabled(false);
        contact_no.setEnabled(false);
        father_name.setEnabled(false);
        mother_name.setEnabled(false);
        address.setEnabled(false);


    }

    private void enableViews() {


        contact_no.setEnabled(true);
        father_name.setEnabled(true);
        mother_name.setEnabled(true);
        address.setEnabled(true);

    }


    private void initialize() {
        context = getActivity().getApplicationContext();
        course = (TextInputEditText) view.findViewById(R.id.course_edit_input);
        branch = (TextInputEditText) view.findViewById(R.id.branch_edit_text);
        year = (TextInputEditText) view.findViewById(R.id.year_edit_text);
        section = (TextInputEditText) view.findViewById(R.id.section_edit_text);
        univ_roll_no = (TextInputEditText) view.findViewById(R.id.uni_roll_no_edit_text);
        contact_no = (TextInputEditText) view.findViewById(R.id.contact_no_edit_text);
        father_name = (TextInputEditText) view.findViewById(R.id.father_edit_text);
        mother_name = (TextInputEditText) view.findViewById(R.id.mother_edit_text);
        address = (TextInputEditText) view.findViewById(R.id.address_edit_text);

        menu = MainActivity.menu;
        menu.getItem(0).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_edit)
                .colorRes(R.color.white)
                .actionBarSize());
        menu.getItem(0).setTitle("Edit");

        menu.getItem(0).setEnabled(true);

        PROFILE_ID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.PROFILE_ID", "0");
        TOKEN = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.TOKEN", "0");
    }


    private void uploadChanges() {
        String URL = Endpoints.student_profile_data;
        Log.d("llll", "upload()");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + PROFILE_ID + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        disableViews();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("course", course.getText().toString());
                params.put("branch", branch.getText().toString());
                params.put("year", year.getText().toString());
                params.put("section", section.getText().toString());
                params.put("univ_roll_no", univ_roll_no.getText().toString());
                params.put("contact_no", contact_no.getText().toString());
                params.put("father_name", father_name.getText().toString());
                params.put("mother_name", mother_name.getText().toString());
                params.put("address", address.getText().toString());
                params.put("display_to_others", String.valueOf(false));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + TOKEN);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
