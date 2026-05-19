
package Liquidacion.security;

import Liquidacion.entity.EmpleadoEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class EmpleadoUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private EmpleadoEntity empleado;

    public EmpleadoUserDetails(EmpleadoEntity empleado) {
        this.empleado = empleado;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + empleado.getRol().toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return empleado.getContrasena();
    }

    @Override
    public String getUsername() {
        return empleado.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // Método para acceder al objeto completo
    public EmpleadoEntity getEmpleado() {
        return empleado;
    }
}