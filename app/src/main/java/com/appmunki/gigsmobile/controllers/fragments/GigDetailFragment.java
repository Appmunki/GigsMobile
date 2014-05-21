package com.appmunki.gigsmobile.controllers.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.appmunki.gigsmobile.R;
import com.appmunki.gigsmobile.helpers.Utils;
import com.appmunki.gigsmobile.models.Gig;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * Use the {@link GigDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GigDetailFragment extends Fragment {

    private Gig mGig;
    private String TAG = this.getClass().getSimpleName();
    private OnGigDetailInteractionListener mListener;

    //views
    @InjectView(R.id.gig_detail_fragment_gig_image_imageView)
    ImageView gigImage;
    @InjectView(R.id.gig_detail_fragment_gig_title_textView)
    TextView gigTitle;
    @InjectView(R.id.gig_detail_fragment_gig_description_textView)
    TextView gigDescripiton;
    @InjectView(R.id.gig_detail_fragment_gig_location_imageView)
    ImageView gigLocationImageView;
    @InjectView(R.id.gig_detail_fragment_gig_location_textView)
    TextView gigLocationTextView;
    @InjectView(R.id.gig_detail_fragment_gig_updated_at_textView)
    TextView gigUpdatedAtTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gig Gig.
     * @return A new instance of fragment GigDetailFragment.
     */
    public static GigDetailFragment newInstance(Gig gig) {
        GigDetailFragment fragment = new GigDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Utils.ARG_GIG, gig);
        fragment.setArguments(args);
        return fragment;
    }
    public GigDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mGig = getArguments().getParcelable(Utils.ARG_GIG);
        }
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
            case R.id.action_messages:
                if (null != mListener) {

                    mListener.onGigDetailMessageInteraction(mGig);

                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gig_detail, container, false);
        ButterKnife.inject(this,view);

        //initializing views
        gigImage.setImageBitmap(mGig.getPicture(getActivity()));
        gigDescripiton.setText(mGig.getDescription());
        gigTitle.setText(mGig.getTitle());
        gigLocationTextView.setText("");
        gigUpdatedAtTextView.setText("");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gig_messages, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGigDetailInteractionListener) activity;
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
    public interface OnGigDetailInteractionListener {
        public void onGigDetailMessageInteraction(Gig gig);
    }

}
