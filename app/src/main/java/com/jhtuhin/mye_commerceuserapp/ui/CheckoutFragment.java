package com.jhtuhin.mye_commerceuserapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jhtuhin.mye_commerceuserapp.R;
import com.jhtuhin.mye_commerceuserapp.callbacks.OnActionCompleteListener;
import com.jhtuhin.mye_commerceuserapp.databinding.FragmentCheckoutBinding;
import com.jhtuhin.mye_commerceuserapp.viewmodels.LoginViewModel;
import com.jhtuhin.mye_commerceuserapp.viewmodels.ProductViewModel;

public class CheckoutFragment extends Fragment {
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private FragmentCheckoutBinding binding;

    public CheckoutFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        loginViewModel.getUserData();
        binding = FragmentCheckoutBinding.inflate(inflater);
        binding.paymentRG.setOnCheckedChangeListener((group, checkedId) -> {
            final RadioButton rb = container.findViewById(checkedId);
            productViewModel.paymentMethod = rb.getText().toString();
        });
        loginViewModel.getUserModelMutableLiveData().observe(getViewLifecycleOwner(), userModel -> {
           // Log.e("TAG", "Address"+userModel.getDeliveryAddress() );
            if (userModel.getDeliveryAddress() != null) {
                binding.deliveryAddressET.setText(userModel.getDeliveryAddress());
            }
        });
        binding.nextBtn.setOnClickListener(v -> {
            final String address = binding.deliveryAddressET.getText().toString();
            if (address.isEmpty()) {
                binding.deliveryAddressET.setError("Provide a valid delivery address");
                return;
            }

            loginViewModel.updateDeliveryAddress(address, new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    //Toast.makeText(getActivity(), "address saved", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v)
                            .navigate(R.id.checkout_to_confirmation_action);
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getActivity(), "could not save address", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return binding.getRoot();
    }
}