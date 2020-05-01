package apps.udenar.edu.co.rutasnar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import apps.udenar.edu.co.rutasnar.interfaces.RutasNarAPI;
import apps.udenar.edu.co.rutasnar.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {

    EditText txtUser, txtPassword;
    Button mButtonLogin;
    DatabaseHelper db;

    private RutasNarAPI rutasNarAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setTitle("Inicio de sesión");


        db = new DatabaseHelper(this);
        txtUser = findViewById(R.id.edittext_username);
        txtPassword = findViewById(R.id.edittext_passwd);
        mButtonLogin = findViewById(R.id.btn_login);

        rutasNarAPI = ApiUtils.getAPIService();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = txtUser.getText().toString();
                String pwd = txtPassword.getText().toString().trim();
                createUser(user,pwd);
                /*
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                boolean res = db.checkUser(user, pwd);
                if(res)
                {
                    Intent MainActivity = new Intent(UserLoginActivity.this,MainActivity.class);
                    startActivity(MainActivity);
                }
                else
                {
                    Toast.makeText(UserLoginActivity.this,"Error de inicio de sesión",Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private void createUser(String user, String pwd){
        rutasNarAPI.newUser(System.currentTimeMillis()+"", user, pwd).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Log.d("NOTICIAS", "onResponse: " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
