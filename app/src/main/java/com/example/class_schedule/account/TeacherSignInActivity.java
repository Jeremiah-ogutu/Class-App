package com.example.class_schedule.account;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class_schedule.MainActivity;
import com.example.class_schedule.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherSignInActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = TeacherSignInActivity.class.getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseAuth mAuth;


    @BindView(R.id.creatingButton)
    Button mCreatingButton;
    @BindView(R.id.nameEditText)
    EditText mNameEditText;
    @BindView(R.id.passwordEditText)
    EditText mPaswordEditText;
    @BindView(R.id.confirmPasswordEditText)
    EditText mConfirmPaswordEditText;
    @BindView(R.id.emailEditText)
    EditText mEmailEditText;
    @BindView(R.id.loginTextView)
    TextView mLoginTextView;
    @BindView(R.id.firebaseProgressBar)
    ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView)
    TextView mLoadingSignUp;

private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_in);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        createAuthStateListener();

        mLoginTextView.setOnClickListener(this);
        mCreatingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mLoginTextView) {
            Intent intent = new Intent(TeacherSignInActivity.this,TeacherLoginActivity.class);
            intent .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(v == mCreatingButton){
            createNewTeacher();
        }

    }

    private void createNewTeacher() {
        final String name = mNameEditText.getText().toString().trim();
        final String email =mEmailEditText.getText().toString().trim();
        String password = mPaswordEditText.getText().toString().trim();
        String confirmPasword = mConfirmPaswordEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password,confirmPasword);

        if(!validEmail || !validName || !validPassword) return;

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            hideProgressBar();

            if (task.isSuccessful()) {
                Log.d(TAG, "Authentication successful");
            } else {
                Toast.makeText(TeacherSignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAuthStateListener() {

        mAuthListener = firebaseAuth -> {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(TeacherSignInActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
    }




    //validate email
    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }

        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPaswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPaswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    //postgress
    private void showProgressBar() {
        mSignInProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        mLoadingSignUp.setText("Sign Up process in Progress");
    }

    private void hideProgressBar() {
        mSignInProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, Objects.requireNonNull(user.getDisplayName()));
                            Toast.makeText(TeacherSignInActivity.this, "The display name has ben set", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}


