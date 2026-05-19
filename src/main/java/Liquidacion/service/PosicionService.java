
package Liquidacion.service;

import Liquidacion.entity.PosicionEntity;
import Liquidacion.repository.PosicionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PosicionService {

    @Autowired
    private PosicionRepository posicionRepository;

    public List<PosicionEntity> listarTodos() {
        return posicionRepository.findAll();
    }

    public PosicionEntity buscarPorId(Long id) {
        return posicionRepository.findById(id).orElse(null);
    }

    public void guardar(PosicionEntity posicion) {
        posicionRepository.save(posicion);
    }

    public void eliminar(Long id) {
        posicionRepository.deleteById(id);
    }
}
