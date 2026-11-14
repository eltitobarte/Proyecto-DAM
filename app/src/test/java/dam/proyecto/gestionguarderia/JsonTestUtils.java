package dam.proyecto.gestionguarderia;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilidades compartidas por las pruebas de integración de los mocks JSON.
 * Localiza el directorio de assets tanto si Gradle ejecuta el test desde el
 * módulo app como si se ejecuta desde la raíz del proyecto.
 */
final class JsonTestUtils {

    private static final Gson GSON = new Gson();
    private static final Path ASSETS = resolveAssets();

    private JsonTestUtils() {
    }

    static JsonArray readArray(String fileName) {
        JsonElement element = GSON.fromJson(readFile(fileName), JsonElement.class);
        if (element == null || !element.isJsonArray()) {
            throw new IllegalStateException("El mock " + fileName + " no contiene un array JSON");
        }
        return element.getAsJsonArray();
    }

    static JsonObject readObject(String fileName) {
        JsonElement element = GSON.fromJson(readFile(fileName), JsonElement.class);
        if (element == null || !element.isJsonObject()) {
            throw new IllegalStateException("El mock " + fileName + " no contiene un objeto JSON");
        }
        return element.getAsJsonObject();
    }

    private static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(ASSETS.resolve(fileName)), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el mock " + fileName, ex);
        }
    }

    private static Path resolveAssets() {
        Path moduleAssets = Paths.get("src", "main", "assets");
        if (Files.isDirectory(moduleAssets)) {
            return moduleAssets;
        }

        Path rootAssets = Paths.get("app", "src", "main", "assets");
        if (Files.isDirectory(rootAssets)) {
            return rootAssets;
        }

        throw new IllegalStateException("No se encontró el directorio de assets del proyecto");
    }
}
