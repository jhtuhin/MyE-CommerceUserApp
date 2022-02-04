package com.example.mye_commerceuserapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mye_commerceuserapp.R;
import com.example.mye_commerceuserapp.databinding.FragmentUserLoginBinding;
import com.example.mye_commerceuserapp.viewmodels.LoginViewModel;


public class UserLoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private FragmentUserLoginBinding binding;
    private boolean isLogin;
    public UserLoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLoginBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        binding.loginBtn.setOnClickListener(v -> {
            isLogin = true;
            authenticate();
        });
        binding.registerBtn.setOnClickListener(v -> {
            isLogin = false;
            authenticate();
        });

        loginViewModel.getAuthLiveData()
                .observe(getViewLifecycleOwner(), authState -> {
                    if (authState == LoginViewModel.AuthState.AUTHENTICATED) {
                        Navigation.findNavController(container)
                                .navigate(R.id.login_to_productList_action);
                    }
                });

        loginViewModel.getErrMsgLiveData()
                .observe(getViewLifecycleOwner(), errMsg -> {
                    binding.errMsgTV.setText(errMsg);
                });


        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.emailInputET.getText().toString();
        final String password = binding.passwordInputET.getText().toString();
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(getActivity(), "Please provide all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isLogin) {
            loginViewModel.login(email, password);
        }else {
            loginViewModel.register(email, password);
        }
    }
}