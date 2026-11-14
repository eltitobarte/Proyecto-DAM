package dam.proyecto.gestionguarderia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;

import org.junit.Test;

import dam.proyecto.gestionguarderia.services.responses.IncidenciasResponse;

/**
 * Prueba de integración del mock de Incidencias.
 * Comprueba la lectura de assets/incidencias.json, el mapeo de la entidad y la
 * unicidad de los códigos de registro diario asociados a cada incidencia.
 */
public class IncidenciasJsonIntegrationTest {

    @Test
    public void testJsonMappingAndIncidentCodes() {
        // 1. Cargar el repositorio mock de incidencias.
        IncidenciasResponse[] incidencias = new Gson().fromJson(
                JsonTestUtils.readArray("incidencias.json"), IncidenciasResponse[].class);

        // 2. Comprobar que la consulta devuelve resultados.
        assertNotNull("Gson debe crear el array de incidencias", incidencias);
        assertEquals("El mock debe contener dos incidencias", 2, incidencias.length);

        // 3. Validar el contenido recuperado de cada incidencia.
        assertEquals("Caida en el recreo", incidencias[0].incidencia);
        assertEquals(1, incidencias[0].registroCodigo);
        assertEquals("Caca en clase", incidencias[1].incidencia);
        assertEquals(2, incidencias[1].registroCodigo);

        // 4. La clave de registro diario no puede repetirse entre incidencias.
        assertNotEquals("Cada incidencia debe apuntar a un registro distinto",
                incidencias[0].registroCodigo, incidencias[1].registroCodigo);
    }
}
