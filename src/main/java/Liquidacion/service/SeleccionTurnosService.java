package Liquidacion.service;


import Liquidacion.entity.SeleccionTurnosEntity;
import Liquidacion.repository.SeleccionTurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeleccionTurnosService {


    @Autowired
    private SeleccionTurnoRepository turnoRepo;

    public List<SeleccionTurnosEntity> turnosPorEstacion(Long idEstacion) {
        return turnoRepo.findByEstacion_Idestacion(idEstacion);
    }
//    public void guardar(SeleccionTurnosEntity precio) {
//        islaRepository.save(precio);
//    }
}
