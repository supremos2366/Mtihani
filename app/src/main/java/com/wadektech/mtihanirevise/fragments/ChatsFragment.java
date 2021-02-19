package com.wadektech.mtihanirevise.fragments;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wadektech.mtihanirevise.R;
import com.wadektech.mtihanirevise.adapter.ChatsAdapter;
import com.wadektech.mtihanirevise.notification.Token;
import com.wadektech.mtihanirevise.utils.Constants;
import com.wadektech.mtihanirevise.viewmodels.UsersViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView ;
    private ChatsAdapter mAdapter;
    DatabaseReference databaseReference ;

    public ChatsFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = v.findViewById(R.id.rv_chat_frag);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList() ;

        updateToken (FirebaseInstanceId.getInstance ().getToken ());

        return v ;
    }

    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance ()
                .getReference ("Tokens");
        Token token1 = new Token (token);
        databaseReference
                .child (Constants
                .getUserId())
                .setValue (token1);
    }

    private void chatList() {

        UsersViewModel viewModel = ViewModelProviders.of(requireActivity())
                .get(UsersViewModel.class);
        viewModel.getChatList().observe(getViewLifecycleOwner(),usersList->{
            mAdapter = new ChatsAdapter (getActivity(),true);
            recyclerView.setAdapter(mAdapter);
            mAdapter.submitList(usersList);
        });
    }
}
