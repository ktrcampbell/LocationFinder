package com.bigbang.myfavoriteplaces.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigbang.myfavoriteplaces.R;
import com.bigbang.myfavoriteplaces.model.User;
import com.bigbang.myfavoriteplaces.util.DebugLogger;
import com.bigbang.myfavoriteplaces.viewmodel.LocationViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    private LocationViewModel viewModel;

    private Observer<Boolean> loginObserver;
    private Observer<Boolean> registeredObserver;

    @BindView(R.id.password_edittext)
    EditText passwordEditText;

    @BindView(R.id.user_name_edittext)
    EditText userNameEditText;

    @BindView(R.id.password_edittext2)
    EditText suPasswordEditText;

    @BindView(R.id.user_name_edittext2)
    EditText suUserNameEditText;

    @BindView(R.id.sign_up_layout)
    CardView signUpLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_frag_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                handleLogin(aBoolean);
            }
        };

        registeredObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                handleRegistration(aBoolean);
            }
        };

        viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        ButterKnife.bind(this, view);
    }

    private void handleLogin(Boolean aBoolean) {
        if (aBoolean) {
            ((MapsActivity) getActivity()).loginSuccess();
            DebugLogger.logDebug("Login success!");
        }
    }

    private void handleRegistration(Boolean aBoolean) {
        if (aBoolean) {
            signUpLayout.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Please check verification email.", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.login_button, R.id.sign_up_button, R.id.sign_up_textview})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_button:
                //Attempt login
                userLogin();
                break;
            case R.id.sign_up_button:
                //Attempt registration
                userRegister();
                break;
            case R.id.sign_up_textview:
                //Attempt registration
                signUpLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void userRegister() {

        String userName = suUserNameEditText.getText().toString().trim();
        String passWord = suPasswordEditText.getText().toString().trim();

        User loginUser = new User(userName, passWord);
        viewModel.registerUser(loginUser);
        viewModel.getRegistrationStatus().observe(this, registeredObserver);
    }


    private void userLogin(){
        String userName = userNameEditText.getText().toString().trim();
        String passWord = passwordEditText.getText().toString().trim();

        User loginUser = new User(userName, passWord);
        viewModel.loginUser(loginUser);
        viewModel.getLoginStatus().observe(this, loginObserver);
    }
}
