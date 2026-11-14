package dam.proyecto.gestionguarderia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;

import org.junit.Test;

import dam.proyecto.gestionguarderia.services.responses.AulaResponse;

/**
 * Prueba de integración del mock de Aula.
 * Verifica la lectura de assets/aula.json, el mapeo de los campos y la
 * coherencia entre el aula y el profesor responsable de la misma.
 */
public class AulaJsonIntegrationTest {

    @Test
    public void testJsonMappingAndAulaRepository() {
        // 1. Cargar el repositorio mock de aulas.
        AulaResponse[] aulas = new Gson().fromJson(
                JsonTestUtils.readArray("aula.json"), AulaResponse[].class);

        // 2. Comprobar que la consulta devuelve resultados.
        assertNotNull("Gson debe crear el array de aulas", aulas);
        assertEquals("El mock debe contener dos aulas", 2, aulas.length);

        // 3. Validar los valores recuperados del aula "Perro".
        assertEquals("Perro", aulas[0].nombreAula);
        assertEquals("Lucia Jimenez Marin", aulas[0].profesorNombre);
        assertEquals(6, aulas[0].idProfesor);

        // 4. Validar los valores recuperados del aula "Gato".
        assertEquals("Gato", aulas[1].nombreAula);
        assertEquals(7, aulas[1].idProfesor);
    }
}
