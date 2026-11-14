package dam.proyecto.gestionguarderia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

import dam.proyecto.gestionguarderia.services.responses.UsuariosResponse;

/**
 * Prueba de integración del mock de Usuarios.
 * Valida la lectura de assets/usuarios.json, el mapeo del rol como enumerado
 * y la distribución de usuarios por perfil dentro del sistema de acceso.
 */
public class UsuariosJsonIntegrationTest {

    @Test
    public void testJsonMappingAndRoleIntegrity() {
        // 1. Cargar el repositorio mock de usuarios.
        JsonArray json = JsonTestUtils.readArray("usuarios.json");
        assertEquals("El mock debe contener siete usuarios", 7, json.size());

        // 2. Mapear el primer registro a la entidad UsuariosResponse.
        UsuariosResponse admin = new Gson().fromJson(json.get(0), UsuariosResponse.class);
        assertNotNull("Gson debe crear el usuario administrador", admin);
        assertEquals(1, admin.idUsuario);
        assertEquals("pepgarlop@ejemplo.com", admin.correoElectronico);
        assertEquals(UsuariosResponse.rol.ADMINISTRADOR.name(),
                json.get(0).getAsJsonObject().get("rol").getAsString());

        // 3. Verificar la distribución de roles del sistema.
        assertEquals(1, countRole(json, "ADMINISTRADOR"));
        assertEquals(2, countRole(json, "ADMINISTRACION"));
        assertEquals(2, countRole(json, "TUTOR LEGAL"));
        assertEquals(2, countRole(json, "PROFESOR"));
    }

    private int countRole(JsonArray usuarios, String rol) {
        int total = 0;
        for (JsonElement element : usuarios) {
            JsonObject usuario = element.getAsJsonObject();
            if (rol.equals(usuario.get("rol").getAsString())) {
                total++;
            }
        }
        return total;
    }
}
