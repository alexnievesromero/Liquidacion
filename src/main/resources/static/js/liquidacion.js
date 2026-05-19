// ======================================================
//  FORMATEO DE CAMPOS DE MONEDA (Miles con es-CO)
// ======================================================
document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('.formatear-moneda').forEach(input => {

        // ✅ SOLO formatea si ya tiene valor
        if (input.value && input.value.trim() !== '') {
            input.value = new Intl.NumberFormat('es-CO').format(
                parseInt(input.value.replace(/\D/g, ''))
            );
        } else {
            input.value = ''; // 👈 vacío, no 0
        }

        input.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            e.target.value = value ? new Intl.NumberFormat('es-CO').format(value) : '';
        });

        input.addEventListener('focus', (e) => {
            e.target.value = e.target.value.replace(/\D/g, '');
        });

        input.addEventListener('blur', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            e.target.value = value ? new Intl.NumberFormat('es-CO').format(value) : '';
        });
    });

    // Limpia separadores antes de enviar
    const form = document.querySelector("form");
    form.addEventListener("submit", function () {
        document.querySelectorAll(".formatear-moneda").forEach(input => {
            input.value = input.value.replace(/\D/g, '');
        });
    });
});


// ======================================================
//  CALCULAR TOTALES Y DESCUADRE
// ======================================================

function getValue(id) {
    const input = document.getElementById(id);
    return input ? parseFloat(input.value.replace(/\D/g, '')) || 0 : 0;
}

function formatNumber(num) {
    return new Intl.NumberFormat("es-CO").format(num);
}

function calcularTotalYDescuadre() {
    const ventaTotal = getValue('venta_total');

    const totalCalculado =
        getValue('entregas') +
        getValue('credito_satelite') +
        getValue('credito_local') +
        getValue('prepago_local') +
        getValue('pagos_electronicos') +
        getValue('pagos_Qr') +
        getValue('monedas') +
        getValue('gastos') +
        getValue('puntos_terpel') +
        getValue('devoluciones') +
        getValue('cheques') +
        getValue('regalos') +
        getValue('prestamos') +
        getValue('otros') +
        getValue('efectivo_final');

    const diferencia = ventaTotal - totalCalculado;

    document.getElementById('total').value = formatNumber(totalCalculado);
    document.getElementById('diferencia').value = formatNumber(diferencia);
    document.getElementById('descuadres').value = formatNumber(diferencia);
}

document.addEventListener("DOMContentLoaded", () => {
    const campos = [
        'entregas', 'credito_satelite', 'credito_local', 'prepago_local',
        'pagos_electronicos', 'pagos_Qr', 'monedas', 'gastos', 'puntos_terpel',
        'devoluciones', 'cheques', 'regalos', 'prestamos', 'otros', 'efectivo_final'
    ];

    campos.forEach(id => {
        const campo = document.getElementById(id);
        if (campo) campo.addEventListener("input", calcularTotalYDescuadre);
    });

    calcularTotalYDescuadre();
});

// ======================================================
//  LOCAL STORAGE PARA NO PERDER DATOS
// ======================================================

document.querySelectorAll("form input, form select, form textarea").forEach(el => {
    el.addEventListener("change", () => {
        localStorage.setItem("liquidacion_" + el.name, el.value);
    });
});

window.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("form input, form select, form textarea").forEach(el => {
        const saved = localStorage.getItem("liquidacion_" + el.name);
        if (saved !== null) el.value = saved;
    });
});

// Limpiar localStorage al enviar
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("form").addEventListener("submit", () => {
        document.querySelectorAll("form input, form select, form textarea").forEach(el => {
            localStorage.removeItem("liquidacion_" + el.name);
        });
    });
});

// ======================================================
//  AUTO HORA LOCAL
// ======================================================

document.addEventListener("DOMContentLoaded", function () {
    const ahora = new Date();
    const horas = String(ahora.getHours()).padStart(2, '0');
    const minutos = String(ahora.getMinutes()).padStart(2, '0');
    const horaLocal = document.getElementById("horaLocal");
    if (horaLocal) horaLocal.value = `${horas}:${minutos}`;
});

// ======================================================
//  SUMAR TODOS LOS PESOS DE LOS SURTIDORES
// ======================================================

/*function sumarTotales() {
    let totalVenta = 0;

    document.querySelectorAll(".pesos").forEach(input => {
        let limpio = input.value.replace(/\./g, "").replace(",", ".");
        const valor = parseFloat(limpio);
        if (!isNaN(valor)) totalVenta += valor;
    });

    document.getElementById("venta_total").value = totalVenta.toLocaleString("es-CO", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    });
}*/

// ======================================================
//  CALCULAR GALONES Y PESOS POR TARJETA
// ======================================================

document.addEventListener("input", function (e) {

    if (
        e.target.classList.contains("lectura-inicial") ||
        e.target.classList.contains("lectura-final") ||
        e.target.classList.contains("precio-galon")
    ) {
        const card = e.target.closest(".lectura-card");

        const inicial = parseFloat(card.querySelector(".lectura-inicial").value) || 0;
        const final = parseFloat(card.querySelector(".lectura-final").value) || 0;
        const precioGalon = parseFloat(card.querySelector(".precio-galon").value) || 0;

        let galones = final - inicial;
        if (galones < 0) galones = 0;

        card.querySelector(".gls").value = galones.toFixed(2);

        let pesos = galones * precioGalon;

        card.querySelector(".pesos").value = pesos.toLocaleString("es-CO", {
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        });

        sumarTotales();
    }
});

// ======================================================
//  CARGAR POSICIONES POR ISLA (FETCH BACKEND)
// ======================================================

document.addEventListener("DOMContentLoaded", () => {
    const selectIsla = document.getElementById("islaSelect");
    const contenedor = document.getElementById("contenedorLecturas");
    const template = document.getElementById("templatePosicion");

    if (!selectIsla) return;

    selectIsla.addEventListener("change", async function () {
        const islaId = this.value;

        contenedor.innerHTML = "";
        if (!islaId) return;

        try {
            const res = await fetch(`/liquidar/posicion/isla/${islaId}`);
            if (!res.ok) throw new Error("Error al obtener posiciones");

            const posiciones = await res.json();

            if (!posiciones.length) {
                contenedor.innerHTML = `<div class="alert alert-info">No hay posiciones para esta isla.</div>`;
                return;
            }

         posiciones.forEach((pos, index) => {
             const clone = template.content.cloneNode(true);

             // Nombre visible
             const posicionVisible = clone.querySelector(".posicion");
             posicionVisible.removeAttribute("name");
             posicionVisible.value = pos.nombre || "";

             // ID correcto (usa idPosicion, NO id)
             const posicionId = clone.querySelector(".posicion-id");
            posicionId.setAttribute("name", `lecturas[${index}].posicion.idPosicion`);
             posicionId.value = pos.idPosicion;

             // Asignar combustible (esto faltaba)
             const combustible = clone.querySelector(".combustible");
             combustible.value = pos.tipoCombustible || "";
             combustible.setAttribute("name", `lecturas[${index}].tipoCombustible`);


            // Lectura inicial (ESTO FALTABA)
            const lecturaInicial = clone.querySelector(".lectura-inicial");
            lecturaInicial.setAttribute("name", `lecturas[${index}].lecturaInicial`);
            lecturaInicial.value = pos.lecturaInicial || 0;

            // Lectura final
            const lecturaFinal = clone.querySelector(".lectura-final");
            lecturaFinal.setAttribute("name", `lecturas[${index}].lecturaFinal`);

            // Galones
            clone.querySelector(".gls").setAttribute("name", `lecturas[${index}].gls`);

            // Pesos
            clone.querySelector(".pesos").setAttribute("name", `lecturas[${index}].pesos`);


             contenedor.appendChild(clone);
         });



        } catch (err) {
            contenedor.innerHTML = `<div class="alert alert-danger">Error cargando posiciones.</div>`;
        }
    });
});
// ======================================================
// LIMPIAR CAMPOS .pesos ANTES DE ENVIAR FORMULARIO
// ======================================================
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");

    if (form) {
        form.addEventListener("submit", () => {
            document.querySelectorAll(".pesos").forEach(input => {
                input.value = input.value.replace(/\D/g, ""); // elimina todos los puntos y comas
            });
        });
    }
});

// AJUSTAR LA FECHA CUANDO SE SELECCIONA EL TURNO NOCHE
document.addEventListener("DOMContentLoaded", function () {

    const selectTurno = document.getElementById("turno");
    const inputFecha = document.getElementById("fecha");

    // Guardamos la fecha original que viene desde el backend
    const fechaOriginal = inputFecha.value;

    selectTurno.addEventListener("change", function () {

        const nombreTurno = selectTurno.value; // Ahora es el nombre

        if (nombreTurno === "Turno 3") {  // 👈 Cambia aquí el nombre exacto de tu turno
            const fecha = new Date(fechaOriginal);
            fecha.setDate(fecha.getDate() - 1);

            inputFecha.value = fecha.toISOString().split("T")[0];
        } else {
            inputFecha.value = fechaOriginal;
        }
    });
});



       // Evita que guarde la liquidacion al presionar el enter.

           document.getElementById("formLiquidacion").addEventListener("keydown", function (event) {
               if (event.key === "Enter" && event.target.tagName !== "TEXTAREA") {
                   event.preventDefault();
               }
           });

// BLOQUEO DE BOTON GARDAR.

 /*
  document.getElementById("btnGuardar").addEventListener("click", function () {
    this.disabled = true;
    this.innerHTML = '<i class="bi bi-hourglass-split"></i> Guardando...';
     });
     */

document.querySelector("form").addEventListener("submit", function (e) {
    if (!this.checkValidity()) {
        return; // si el form no es válido, no bloquear
    }
    const btn = document.getElementById("btnGuardar");
    btn.disabled = true;
    btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Guardando...';
});