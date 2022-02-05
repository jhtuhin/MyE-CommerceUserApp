package com.jhtuhin.mye_commerceuserapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhtuhin.mye_commerceuserapp.adapters.CartAdapter;
import com.jhtuhin.mye_commerceuserapp.databinding.FragmentCartListBinding;
import com.jhtuhin.mye_commerceuserapp.viewmodels.LoginViewModel;
import com.jhtuhin.mye_commerceuserapp.viewmodels.ProductViewModel;

public class CartListFragment extends Fragment {
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private FragmentCartListBinding binding;

    public CartListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartListBinding.inflate(inflater);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        final CartAdapter adapter = new CartAdapter();
        binding.cartRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.cartRV.setAdapter(adapter);
        productViewModel.getAllCartItemSnapshot(loginViewModel.getUser().getUid());
        productViewModel.cartListLiveData.observe(getViewLifecycleOwner(),
                cartModels -> {
                    adapter.submitList(cartModels);
                });
        return binding.getRoot();
    }
}