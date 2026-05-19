package Liquidacion.service;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<EmpleadoEntity> listarTodos() {
        return empleadoRepository.findAll();
    }

    public Optional<EmpleadoEntity> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    public EmpleadoEntity guardar(EmpleadoEntity empleado) {
        // Solo encriptar si es una nueva contraseña o no está ya encriptada
        if (empleado.getContrasena() != null && !empleado.getContrasena().startsWith("$2a$")) {
            String passwordEncriptada = passwordEncoder.encode(empleado.getContrasena());
            empleado.setContrasena(passwordEncriptada);
        }
        return empleadoRepository.save(empleado);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }

    public Optional<EmpleadoEntity> buscarPorUsuario(String usuario) {
        return empleadoRepository.findByUsuario(usuario);
    }

// EmpleadoService.java
    public Optional<EmpleadoEntity> login(String usuario, String contrasena) {
        // Buscar por usuario
        Optional<EmpleadoEntity> empleadoOpt = empleadoRepository.findByUsuario(usuario);

        if (empleadoOpt.isPresent()) {
            EmpleadoEntity empleado = empleadoOpt.get();
            // Validar contraseña con PasswordEncoder
            if (passwordEncoder.matches(contrasena, empleado.getContrasena())) {
                return Optional.of(empleado); // ✅ Login exitoso
            }
        }
        return Optional.empty(); // ❌ Credenciales inválidas
    }
    // En EmpleadoService.java
    public List<EmpleadoEntity> findByEstacion(EstacionEntity estacion) {
        return empleadoRepository.findByEstacion(estacion);
    }
}
