package dam.proyecto.gestionguarderia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

import dam.proyecto.gestionguarderia.services.responses.AlumnosResponse;
import dam.proyecto.gestionguarderia.services.responses.AulaResponse;

/**
 * Prueba de integración del mock de Alumnos.
 * Valida la lectura de assets/alumnos.json, el mapeo con Gson y la integridad
 * referencial entre cada alumno y el aula a la que está asociado.
 */
public class AlumnosJsonIntegrationTest {

    @Test
    public void testJsonPersistenceAndReferentialIntegrity() {
        // 1. Cargar el mock y comprobar que Gson crea las entidades Alumno.
        JsonArray json = JsonTestUtils.readArray("alumnos.json");
        assertEquals("El mock debe contener dos alumnos", 2, json.size());

        AlumnosResponse[] alumnos = new Gson().fromJson(json, AlumnosResponse[].class);
        assertNotNull("Gson debe crear el array de alumnos", alumnos);
        assertEquals("Alvaro", alumnos[0].nombre);
        assertEquals("Pulido Torres", alumnos[0].apellidos);
        assertEquals("15/03/2025", alumnos[0].fechaNacimiento);
        assertEquals("Cristina", alumnos[1].nombre);

        // 2. Validar la relación Alumno -> Aula y Alumno -> Tutor legal.
        AulaResponse[] aulas = new Gson().fromJson(
                JsonTestUtils.readArray("aula.json"), AulaResponse[].class);

        for (JsonElement element : json) {
            JsonObject alumno = element.getAsJsonObject();
            int aulaCodigo = alumno.get("aulaCodigo").getAsInt();
            int tutorCodigo = alumno.get("tutorlegalCodigo").getAsInt();

            assertTrue("El aula " + aulaCodigo + " debe existir",
                    containsAula(aulas, aulaCodigo));
            assertTrue("El tutor legal debe tener un código positivo", tutorCodigo > 0);
        }
    }

    private boolean containsAula(AulaResponse[] aulas, int codigo) {
        for (AulaResponse aula : aulas) {
            if (aula.codigo == codigo) {
                return true;
            }
        }
        return false;
    }
}
