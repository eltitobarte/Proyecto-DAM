package dam.proyecto.gestionguarderia.services.responses;

public class UsuariosResponse {
    public int idUsuario;
    public enum rol {
        ADMINISTRADOR, ADMINISTRACION, TUTOR_LEGAL, PROFESOR
    };
    public String contrasenya;
    public String correoElectronico;
}
