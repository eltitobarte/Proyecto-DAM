package dam.proyecto.gestionguarderia.services;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import dam.proyecto.gestionguarderia.services.interfaces.ApiTest;
import dam.proyecto.gestionguarderia.services.responses.TestResponse;

/**
 * Clase Mocks para recibir datos
 */
public class ApiServiceMock implements ApiTest {

    private Context ctx;
    private static Gson gson;
    public ApiServiceMock(Context ctx) {
        this.ctx = ctx;
        this.gson = new Gson();
    }

    /**
     * Método genérico para leer cualquier JSON de Assets
     * @param <T> Clase de retorno
     * @param file Mock JSON
     * @param classDest Clase de destino
     * @return T Clase
     */
    public <T> T GET(String file, Class<T> classDest) {
        try {
            InputStream is = this.ctx.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            return gson.fromJson(json, classDest);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo API Test
     * @return true or false
     */
    @Override
    public boolean getTest() {
        // Llamamos al método genérico indicando qué archivo y qué clase queremos
        TestResponse data = GET("text.json", TestResponse.class);
        // Retornamos el valor, controlando que no sea nulo
        return data != null && data.response;
    }

}
