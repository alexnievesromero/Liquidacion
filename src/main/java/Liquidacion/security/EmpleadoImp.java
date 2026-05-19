

package Liquidacion.security;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// EmpleadoImp.java
@Service
public class EmpleadoImp implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // ✅ Devuelve tu propia implementación
        return new EmpleadoUserDetails(empleado);
    }
}


