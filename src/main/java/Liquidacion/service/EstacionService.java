package Liquidacion.service;

import Liquidacion.entity.EstacionEntity;
import Liquidacion.repository.EstacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstacionService {

    @Autowired
    private EstacionRepository estacionRepository;

    public List<EstacionEntity> obtenerTodas() {
        return estacionRepository.findAll();
    }
    // ✅ Nuevo método: obtener por ID
    public EstacionEntity obtenerPorId(Long id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estación no encontrada con ID: " + id));
    }
    public void guardar(EstacionEntity estacion) {
        estacionRepository.save(estacion);
    }

    public void eliminar(Long id) {
        estacionRepository.deleteById(id);
    }
}


