package Liquidacion.service;


import Liquidacion.controller.PosicionController;
import Liquidacion.entity.IslaEntity;
import Liquidacion.repository.IslaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IslaService {

    @Autowired
    private IslaRepository islaRepository;

   
  public List<IslaEntity> listarTodos() {
        return islaRepository.findAll();
    }
    public List<IslaEntity> listarTodosIdes(Long idestacion) {
        return islaRepository.findByEstacion_Idestacion(idestacion);
    }

    public void guardar(IslaEntity precio) {
        islaRepository.save(precio);
    }

    public IslaEntity buscarPorId(Long id) {
        return islaRepository.findById(id).orElseThrow(() -> new RuntimeException("ID no encontrado: " + id));
    }

    public void eliminar(Long id) {
        islaRepository.deleteById(id);
    }

    // En IslaService.java
    public List<PosicionController.PosicionDTO> getPosicionesPorIsla(Long idIsla) {
        IslaEntity isla = islaRepository.findById(idIsla)
                .orElseThrow(() -> new RuntimeException("Isla no encontrada con ID: " + idIsla));

        return isla.getPosiciones().stream()
                .map(p -> new PosicionController.PosicionDTO(p.getNombrePosicion(), p.getCombustible()))
                .toList();
    }

}
