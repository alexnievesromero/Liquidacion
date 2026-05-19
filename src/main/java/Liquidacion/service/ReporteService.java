package Liquidacion.service;

import Liquidacion.entity.EmpleadoEntity;
import Liquidacion.entity.EstacionEntity;
import Liquidacion.entity.FilaReporte;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import Liquidacion.entity.VentasEntity;
import Liquidacion.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Service
public class ReporteService {

    private final JdbcTemplate jdbcTemplate;

    public ReporteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer obtenerIdEstacionPorUsuario(String usuario) {
        String sql = """
                    SELECT e.id_estacion
                    FROM empleado e
                    WHERE e.usuario = ?
                    LIMIT 1
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, usuario);
    }

    public List<FilaReporte> obtenerReporte(int idEstacion) {
        String sql = String.format("""
                SELECT
                    e.nombre,
                    es.nombre AS nombreEstacion,
                    i.isla,
                    t.numero_turno,
                    t.turno,
                    t.fecha,
                    ls.lectura_inicial,
                    ls.lectura_final,
                    ls.Gls,
                    ls.pesos,
                    l.id_ventas,
                    l.venta_total,
                    l.entregas,
                    l.credito_satelite,
                    l.credito_local,
                    l.prepago_local,
                    l.pagos_electronicos,
                    l.pagos_Qr,
                    l.monedas,
                    l.gastos,
                    l.puntos_terpel,
                    l.devoluciones,
                    l.otros,
                    l.cheques,
                    l.regalos,
                    l.prestamos,
                    l.descuadres,
                    l.efectivo_final,
                    l.total,
                    l.diferencia,
                    l.observaciones,
                    l.estado
                FROM ventas l
                LEFT JOIN turno t ON t.id_turno = l.id_turno
                LEFT JOIN isla i ON t.id_isla = i.id_isla
                LEFT JOIN empleado e ON t.id_empleado = e.id_empleado
                LEFT JOIN estacion es ON es.id_estacion = t.id_estacion
                
                -- 🔥 SUBQUERY que evita duplicados
                LEFT JOIN (
                    SELECT 
                        id_turno,
                        MIN(lectura_inicial) AS lectura_inicial,
                        MAX(lectura_final)  AS lectura_final,
                        SUM(gls) AS Gls,
                        SUM(pesos) AS pesos
                    FROM lectura_surtidor
                    GROUP BY id_turno
                ) ls ON ls.id_turno = t.id_turno
                
                WHERE t.id_estacion = %d
                ORDER BY l.id_ventas DESC
                """, idEstacion);


        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FilaReporte fila = new FilaReporte();
            fila.setNombreEmpleado(rs.getString("nombre"));
            fila.setNombreEstacion(rs.getString("nombreEstacion"));
            fila.setIsla(rs.getString("isla"));
            fila.setNumeroTurno(rs.getInt("numero_turno"));
            fila.setTurno(rs.getString("turno"));
            fila.setFecha(rs.getDate("fecha"));
            fila.setIdVentas(rs.getLong("id_ventas"));
            fila.setVentaTotal(rs.getBigDecimal("venta_total"));
            fila.setEntregas(rs.getBigDecimal("entregas"));
            fila.setCreditoSatelite(rs.getBigDecimal("credito_satelite"));
            fila.setCreditoLocal(rs.getBigDecimal("credito_local"));
            fila.setPrepagoLocal(rs.getBigDecimal("prepago_local"));
            fila.setPagosElectronicos(rs.getBigDecimal("pagos_electronicos"));
            fila.setPagosQr(rs.getBigDecimal("pagos_Qr"));
            fila.setMonedas(rs.getBigDecimal("monedas"));
            fila.setGastos(rs.getBigDecimal("gastos"));
            fila.setOtros(rs.getBigDecimal("otros"));
            fila.setCheques(rs.getBigDecimal("cheques"));
            fila.setRegalos(rs.getBigDecimal("regalos"));
            fila.setPrestamos(rs.getBigDecimal("prestamos"));
            fila.setDescuadre(rs.getBigDecimal("descuadres"));
            fila.setEfectivoFinal(rs.getBigDecimal("efectivo_final"));
            fila.setTotal(rs.getBigDecimal("total"));
            fila.setDiferencia(rs.getBigDecimal("diferencia"));
            fila.setObservaciones(rs.getString("observaciones"));
            fila.setEstado(rs.getString("estado"));
            return fila;
        });
    }

    public List<FilaReporte> obtenerFiltrado(String fecha, String numeroTurno, int idEstacion) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM ("
                        + " SELECT "
                        + " e.nombre, es.nombre AS nombreEstacion,"
                        + " i.isla, t.numero_turno, "
                        + " t.turno, "
                        + " t.fecha, "
                        + " t.hora_inicio AS fechaInicio, "
                        + " t.hora_fin AS fechaFin, "
                        + " s.lectura_inicial AS lecturaInicial,"
                        + " s.lectura_final AS lecturaFinal, "
                        + " SUM(s.gls) AS Gls,"
                        + " SUM(s.pesos) AS pesos, "
                        + " l.id_ventas, "
                        + " l.venta_total,"
                        + " l.entregas, "
                        + " l.credito_satelite, "
                        + " l.credito_local,"
                        + " l.prepago_local, "
                        + " l.pagos_electronicos,"
                        + " l.pagos_Qr, "
                        + " l.monedas,"
                        + " l.gastos,"
                        + " l.puntos_terpel,"
                        + " l.devoluciones,"
                        + " l.otros, "
                        + " l.cheques,"
                        + " l.regalos,"
                        + " l.prestamos,"
                        + " l.descuadres,"
                        + " l.efectivo_final, "
                        + " l.total,"
                        + " l.diferencia, l.observaciones "
                        + " FROM ventas l "
                        + "  LEFT JOIN turno t ON t.id_turno = l.id_turno "
                        + "  LEFT JOIN isla i ON t.id_isla = i.id_isla "
                        + "  LEFT JOIN empleado e ON t.id_empleado = e.id_empleado "
                        + "  LEFT JOIN lectura_surtidor s ON t.id_turno = s.id_turno "
                        + "  LEFT JOIN estacion es ON es.id_estacion = t.id_estacion "
                        + " WHERE t.id_estacion = ? "
                        + " GROUP BY e.nombre, es.nombre, i.isla, t.numero_turno, t.turno, t.fecha, t.hora_inicio, t.hora_fin, "
                        + "   s.lectura_inicial, s.lectura_final, l.id_ventas, l.venta_total, l.entregas, "
                        + "   l.credito_satelite, l.credito_local, l.prepago_local, l.pagos_electronicos, "
                        + "   l.pagos_Qr, l.monedas, l.gastos, l.puntos_terpel, l.devoluciones, l.otros, "
                        + "   l.cheques, l.regalos, l.prestamos, l.descuadres, l.efectivo_final, "
                        + "   l.total, l.diferencia, l.observaciones "
                        + ") AS sub WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        params.add(idEstacion); // Este va primero y se enlaza con el primer "?" del WHERE

        if (fecha != null && !fecha.isEmpty()) {
            sql.append(" AND sub.fecha = ?");
            params.add(Date.valueOf(fecha)); // <-- Conversión a java.sql.Date
        }


        if (numeroTurno != null && !numeroTurno.isEmpty()) {
            sql.append(" AND sub.numero_turno = ?");
            params.add(numeroTurno);
        }

        sql.append(" ORDER BY sub.id_ventas DESC");

        return jdbcTemplate.query(sql.toString(), params.toArray(), filaMapper());
    }

    public List<FilaReporte> obtenerPorFechaYTurno(LocalDate fecha, Integer turnoNum, int idEstacion) {
        // 1) La subconsulta con su propio GROUP BY
        StringBuilder sql = new StringBuilder(
                "SELECT *"
                        + "FROM ("
                        + "  SELECT "
                        + "    e.nombre, "
                        + "    es.nombre AS nombreEstacion, "
                        + "    i.isla, "
                        + "    t.numero_turno, "
                        + "    t.turno, "
                        + "    t.fecha, "
                        + "    t.hora_inicio AS fechaInicio, "
                        + "    t.hora_fin AS fechaFin, "
                        + "    l.id_ventas,"
                        + "    l.venta_total, "
                        + "    l.entregas, "
                        + "    l.credito_satelite, "
                        + "    l.credito_local, "
                        + "    l.prepago_local, "
                        + "    l.pagos_electronicos, "
                        + "    l.pagos_Qr, "
                        + "    l.monedas, "
                        + "    l.gastos, "
                        + "    l.puntos_terpel, "
                        + "    l.devoluciones, "
                        + "    l.otros, "
                        + "    l.cheques, "
                        + "    l.regalos, "
                        + "    l.prestamos, "
                        + "    l.descuadres, "
                        + "    l.efectivo_final, "
                        + "    l.total, "
                        + "    l.diferencia, "
                        + "    l.observaciones "
                        + "  FROM ventas l "
                        + "   LEFT JOIN turno t            ON t.id_turno = l.id_turno "
                        + "   LEFT JOIN isla i             ON i.id_isla = t.id_isla "
                        + "   LEFT JOIN empleado e         ON e.id_empleado = t.id_empleado "
                        + "   LEFT JOIN lectura_surtidor s ON s.id_turno = t.id_turno "
                        + "   LEFT JOIN estacion es         ON es.id_estacion = t.id_estacion "
                        + "  GROUP BY "
                        + "    e.nombre, "
                        + "    es.nombre, "
                        + "    i.isla, "
                        + "    t.numero_turno, "
                        + "    t.turno, "
                        + "    t.fecha, "
                        + "    t.hora_inicio, "
                        + "    t.hora_fin,"
                        + "    l.id_ventas, "
                        + "    l.venta_total, "
                        + "    l.entregas, "
                        + "    l.credito_satelite, "
                        + "    l.credito_local, "
                        + "    l.prepago_local, "
                        + "    l.pagos_electronicos, "
                        + "    l.pagos_Qr, "
                        + "    l.monedas, "
                        + "    l.gastos, "
                        + "    l.puntos_terpel, "
                        + "    l.devoluciones, "
                        + "    l.otros, "
                        + "    l.cheques, "
                        + "    l.regalos, "
                        + "    l.prestamos, "
                        + "    l.descuadres, "
                        + "    l.efectivo_final, "
                        + "    l.total, "
                        + "    l.diferencia, "
                        + "    l.observaciones "
                        + ") AS sub "
                        + "WHERE 1=1 "
        );

        // 2) Agregamos los filtros dinámicamente
        List<Object> params = new ArrayList<>();
        if (fecha != null) {
            sql.append("  AND sub.fecha = ? ");
            params.add(Date.valueOf(fecha));
        }
        if (turnoNum != null) {
            sql.append("  AND sub.numero_turno = ? ");
            params.add(turnoNum);
        }

        // 3) Orden final
        sql.append("ORDER BY sub.id_ventas DESC");

        // 4) Ejecutamos con nuestro RowMapper
        return jdbcTemplate.query(sql.toString(), params.toArray(), filaMapper());
    }

    private RowMapper<FilaReporte> filaMapper() {
        return (rs, rowNum) -> {
            FilaReporte f = new FilaReporte();
            f.setNombreEmpleado(rs.getString("nombre"));
            f.setNombreEstacion(rs.getString("nombreEstacion"));
            f.setIsla(rs.getString("isla"));
            f.setNumeroTurno(rs.getInt("numero_turno"));
            f.setTurno(rs.getString("turno"));
            f.setFecha(rs.getDate("fecha"));
            // ✅ Manejo seguro de NULL en fechaInicio
            Timestamp tsInicio = rs.getTimestamp("fechaInicio");
            f.setFechaInicio(tsInicio != null ? tsInicio.toLocalDateTime() : null);

            // ✅ Manejo seguro de NULL en fechaFin
            Timestamp tsFin = rs.getTimestamp("fechaFin");
            f.setFechaFin(tsFin != null ? tsFin.toLocalDateTime() : null);

            f.setVentaTotal(rs.getBigDecimal("venta_total"));
            f.setEntregas(rs.getBigDecimal("entregas"));
            f.setCreditoSatelite(rs.getBigDecimal("credito_satelite"));
            f.setCreditoLocal(rs.getBigDecimal("credito_local"));
            f.setPrepagoLocal(rs.getBigDecimal("prepago_local"));
            f.setPagosElectronicos(rs.getBigDecimal("pagos_electronicos"));
            f.setPagosQr(rs.getBigDecimal("pagos_Qr"));
            f.setMonedas(rs.getBigDecimal("monedas"));
            f.setGastos(rs.getBigDecimal("gastos"));
            f.setPuntosTerpel(rs.getBigDecimal("puntos_terpel"));
            f.setDevoluciones(rs.getBigDecimal("devoluciones"));
            f.setOtros(rs.getBigDecimal("otros"));
            f.setCheques(rs.getBigDecimal("cheques"));
            f.setRegalos(rs.getBigDecimal("regalos"));
            f.setPrestamos(rs.getBigDecimal("prestamos"));
            f.setDescuadre(rs.getBigDecimal("descuadres"));
            f.setEfectivoFinal(rs.getBigDecimal("efectivo_final"));
            f.setTotal(rs.getBigDecimal("total"));
            f.setDiferencia(rs.getBigDecimal("diferencia"));
            f.setObservaciones(rs.getString("observaciones"));
            return f;
        };
    }
    @Autowired
    private VentasRepository ventasRepository;

    public Page<VentasEntity> listarPorEmpleadoYEstacion(
            EmpleadoEntity empleado,
            EstacionEntity estacion,
            int pagina,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                pagina,
                size,
                Sort.by("turno.fecha").descending()
        );

        return ventasRepository.findByTurno_EmpleadoAndEstacion(
                empleado,
                estacion,
                pageable
        );
    }
    public Page<VentasEntity> obtenerVentasPaginadas(int idEstacion, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idVentas").descending());
        return ventasRepository.findByEstacion_Idestacion(idEstacion, pageable);
    }

    public Page<VentasEntity> obtenerVentasPorMes(
            int idEstacion,
            String fecha,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("turno.fecha").descending());

        return ventasRepository.findByMesYEstacion(fecha, idEstacion, pageable);
    }
    public List<VentasEntity> obtenerVentasPorMes(String fecha, int idEstacion) {
        return ventasRepository.obtenerPorMes(fecha, idEstacion);
    }

    public List<VentasEntity> obtenerVentasPorMesActual(int idEstacion) {
        return ventasRepository.obtenerMesActual(idEstacion);
    }
}
