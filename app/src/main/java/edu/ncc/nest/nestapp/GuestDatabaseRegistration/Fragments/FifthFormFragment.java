package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFifthFormBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FifthFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FifthFormFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentGuestDatabaseRegistrationFifthFormBinding binding;

    public FifthFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FifthFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FifthFormFragment newInstance(String param1, String param2) {
        FifthFormFragment fragment = new FifthFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGuestDatabaseRegistrationFifthFormBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fifthToSummary.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                Log.d("***", "clicke");
                Toast.makeText(getContext(), "fdffdsafasdf", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(FifthFormFragment.this)
                        .navigate(R.id.action_DBR_FifthFormFragment_to_DBR_SummaryFragment);
            }
        });
    }
}