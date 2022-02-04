package com.example.mye_commerceuserapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.mye_commerceuserapp.R;
import com.example.mye_commerceuserapp.adapters.ProductListAdapter;
import com.example.mye_commerceuserapp.callbacks.AddRemoveCartItemListener;
import com.example.mye_commerceuserapp.callbacks.OnActionCompleteListener;
import com.example.mye_commerceuserapp.databinding.FragmentProductListBinding;
import com.example.mye_commerceuserapp.models.CartModel;
import com.example.mye_commerceuserapp.viewmodels.LoginViewModel;
import com.example.mye_commerceuserapp.viewmodels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    private FragmentProductListBinding binding;
    private LoginViewModel loginViewModel;
    private ProductViewModel productViewModel;
    private String category;
    private ProductListAdapter adapter;


    public ProductListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProductListBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel.getAuthLiveData().observe(getViewLifecycleOwner(), authState -> {
            if (authState == LoginViewModel.AuthState.UNAUTHENTICATED) {
                Navigation.findNavController(container)
                        .navigate(R.id.productList_to_login_action);
            }
        });
        adapter = new ProductListAdapter(productId -> {
            // TODO: 2/3/2022 go to details page with this id
        }, cartItemListener);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        binding.productRV.setLayoutManager(gridLayoutManager);
        binding.productRV.setAdapter(adapter);

        productViewModel.productsListLiveData.observe(getViewLifecycleOwner(),
                productList -> {
                    if (!productList.isEmpty()) {
                        productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
                    }
                    //adapter.submitList(productList);
                });

        productViewModel.userProductListLiveData.observe(
                getViewLifecycleOwner(), userProductModels -> {
                    adapter.submitList(userProductModels);
                }
        );

//        productViewModel.categoryListLiveData.observe(
//                getViewLifecycleOwner(), catList -> {
//                    final List<String> categories = new ArrayList<>();
//                    categories.add("All");
//                    categories.addAll(catList);
//                    final ArrayAdapter<String> spinnerAdapter =
//                            new ArrayAdapter<>(getActivity(),
//                                    android.R.layout.simple_dropdown_item_1line,
//                                    categories);
//                    binding.searchCategorySP.setAdapter(spinnerAdapter);
//                });

//        binding.searchCategorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    category = parent.getItemAtPosition(position).toString();
//                    productViewModel.getAllProductsByCategory(category);
//                }else {
//                    productViewModel.getAllProducts();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        return binding.getRoot();
    }
    private AddRemoveCartItemListener cartItemListener = new AddRemoveCartItemListener() {
        @Override
        public void onCartItemAdd(CartModel cartModel, int position) {
            productViewModel.addToCart(cartModel, loginViewModel.getUser().getUid(), new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    adapter.notifyItemChanged(position);
                }

                @Override
                public void onFailure() {

                }
            });
        }

        @Override
        public void onCartItemRemove(String productId, int position) {
            productViewModel.removeFromCart(
                    loginViewModel.getUser().getUid(),
                    productId, new OnActionCompleteListener() {
                        @Override
                        public void onSuccess() {
                            adapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }
            );
        }
    };
}