package com.appmunki.gigsmobile.controllers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmunki.gigsmobile.GigsWebServiceManager;
import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.models.GigAdapter;
import com.appmunki.gigsmobile.helpers.BusProvider;
import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.Gig;
import com.appmunki.gigsmobile.models.User;
import com.appmunki.gigsmobile.models.UserManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.appmunki.gigsmobile.helpers.Utils.IntoString;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link GigListFragment.OnGigListInteractionListener}
 * interface.
 */
public class GigListFragment extends Fragment implements AbsListView.OnItemClickListener {




    private OnGigListInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    @InjectView(android.R.id.list)
    AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private GigAdapter mGigAdapter;
    private User mUser;
    private String TAG = this.getClass().getSimpleName();

    public static GigListFragment newInstance(User user) {
        GigListFragment fragment = new GigListFragment();
        Bundle args = new Bundle();
        args.putParcelable(Utils.ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GigListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        if (getArguments() != null) {
            mUser = getArguments().getParcelable(Utils.ARG_USER);
        }
        ArrayList<Gig> gigsList = new ArrayList<Gig>();
        mGigAdapter = new GigAdapter(getActivity(), R.layout.gig_list_item, gigsList);

        if(UserManager.getCurrentUser()!=null){
            GigsWebServiceManager.getInstance().getUserGigs(UserManager.getCurrentUser().getEmail(), UserManager.getCurrentUser().getAuthToken(), ListGigsCallback);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mListView.performItemClick(null,0,0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gig, container, false);
        ButterKnife.inject(this,view);

        // Set the adapter
        mListView.setAdapter(mGigAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        setEmptyText("Loading...");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gig_list_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"item: "+item.getItemId());
        FragmentManager fm= getFragmentManager();

        switch (item.getItemId()) {
            case android.R.id.home:
                if(fm.getBackStackEntryCount()>0){
                    fm.popBackStack();
                }
                break;
            case R.id.action_create_gig:
                if (null != mListener) {

                    mListener.onCreateListInteraction();

                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGigListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onGigListInteraction(mGigAdapter.getItem(position));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onResume() {
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    /**
     *
     */
    private Callback<Response> ListGigsCallback = new Callback<Response>() {
        private String TAG = this.getClass().getSimpleName();

        @Override
        public void success(Response gigs, Response response) {
            Log.i(TAG, Arrays.toString(response.getHeaders().toArray()));
            Log.i(TAG, String.valueOf(response.getStatus()));

            String body = null;
            try {
                body = IntoString(response.getBody().in());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, body);


            JsonObject resp = new JsonParser().parse(body).getAsJsonObject();
            Toast.makeText(getActivity(), resp.getAsJsonPrimitive("info").getAsString(), Toast.LENGTH_SHORT).show();


            //data from the http
            Type listType = new TypeToken<ArrayList<Gig>>() {
            }.getType();
            List<Gig> gigsList = new Gson().fromJson(resp.getAsJsonArray("data"), listType);
            boolean success = resp.getAsJsonPrimitive("success").getAsBoolean();
            String info = resp.getAsJsonPrimitive("info").getAsString();

            Log.i(TAG, "size:" + gigsList.size());

            //updatedHeader();


            mGigAdapter.addAll(gigsList);
            mGigAdapter.notifyDataSetChanged();

        }

        @Override
        public void failure(RetrofitError error) {

            Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnGigListInteractionListener {
        // TODO: Update argument type and name
        public void onGigListInteraction(Gig gig);
        public void onCreateListInteraction();
    }

}
