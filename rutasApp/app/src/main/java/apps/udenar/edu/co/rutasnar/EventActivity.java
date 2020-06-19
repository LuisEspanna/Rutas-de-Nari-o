package apps.udenar.edu.co.rutasnar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import apps.udenar.edu.co.rutasnar.interfaces.RutasNarAPI;
import apps.udenar.edu.co.rutasnar.model.Postit;
import apps.udenar.edu.co.rutasnar.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private ImageView img_event;
    private FloatingActionButton btn_favorite;
    private WebView webView;
    private TextView lbl_title;
    private TextView lbl_desc;
    private TextView lbl_date;
    private TextView lbl_place;
    private String global_id_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        img_event = findViewById(R.id.img_event);
        lbl_title = findViewById(R.id.lbl_event_title);
        lbl_desc = findViewById(R.id.lbl_event_desc);
        lbl_date = findViewById(R.id.lbl_event_date);
        lbl_place = findViewById(R.id.lbl_event_place);

        webView = findViewById(R.id.webview_event);
        webView.getSettings().setDomStorageEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccessFromFileURLs(true);

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);


        //--Caché
        settings.setAppCacheEnabled(true);
        //webView.setRendererPriorityPolicy(RENDERER_PRIORITY_BOUND, true);

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        Bundle b = getIntent().getExtras();

        if (b != null){
            String id_evento = b.getString("id_evento");
            global_id_event = id_evento;
            String id_municipio =b.getString("id_municipio");
            String nom_evento = b.getString("nom_evento");
            String desc_evento = b.getString("desc_evento");
            String img_evento = b.getString("img_evento");
            String fecha_evento = b.getString("fecha_evento");
            String disponible = b.getString("disponible");

            Glide.with(getApplicationContext()).load(img_evento).into(img_event);

            lbl_title.setText(nom_evento);
            lbl_date.setText(fecha_evento);
            lbl_desc.setText(desc_evento);
            lbl_place.setText(id_municipio);

            webView.loadUrl(ApiUtils.MAP_URL_EVENTS + id_evento);
        }

        btn_favorite = findViewById(R.id.btn_event_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(EventActivity.this);
                User user = db.getCurrentUser();
                db.close();
                saveEvent(user.getIdUsuario(),lbl_title.getText().toString(), global_id_event);
                Toast.makeText(EventActivity.this, "Evento guardada :D", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveEvent(String idUsuario, String nom_evento, String id_evento) {
        RutasNarAPI rutasNarAPI = ApiUtils.getAPIService();
        rutasNarAPI.postit(idUsuario, nom_evento, "", id_evento).enqueue(new Callback<List<Postit>>() {
            @Override
            public void onResponse(Call<List<Postit>> call, Response<List<Postit>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EventActivity.this, "Evento guardado :D", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(EventActivity.this, "Ya existe...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Postit>> call, Throwable t) {}
        });
    }
}
