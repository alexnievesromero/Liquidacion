 const modal = document.getElementById('detalleModal');
            let registroActual = null;
            let modoEdicion = false;

            // 📌 FORMATEADOR COP
            function fmtCOP(num) {
                return new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' }).format(Number(num) || 0);
            }

            // 📌 Conversión segura de "$10.000,00" → "10000.00"
            function normalizarValor(valor) {
                if (!valor) return "0";

                return valor
                    .toString()
                    .replace(/\./g, "")         // quitar separadores de miles
                    .replace(/[^0-9,-]/g, "")   // mantener números, coma y signo
                    .replace(",", ".")          // coma → punto
                    || "0";
            }




    // FILTRO EN TABLA
    function filtrarTabla() {
        const texto1 = document.getElementById("filtroFecha").value.toLowerCase();
        const texto2 = document.getElementById("filtroNumeroTurno").value.toLowerCase();

        document.querySelectorAll("tbody tr").forEach(fila => {
            const filaTexto = fila.textContent.toLowerCase();
            fila.style.display = filaTexto.includes(texto1) && filaTexto.includes(texto2) ? "" : "none";
        });
    }

    document.getElementById("filtroFecha").addEventListener("input", filtrarTabla);
    document.getElementById("filtroNumeroTurno").addEventListener("input", filtrarTabla);


    // EXPORTACIÓN PDF
    const formPdf = document.getElementById("formExportPdf");

    formPdf.addEventListener("submit", function (event) {
        event.preventDefault(); // Detener envío normal

        const fecha = document.getElementById("filtroFecha").value.trim();
        const turno = document.getElementById("filtroNumeroTurno").value.trim();

        if (!fecha && !turno) {
            alert("⚠ Debe seleccionar fecha o turno.");
            return;
        }

        // Crear URL dinámica solo con valores válidos
        let url = "/Reportes/Pdf?";
        if (fecha) url += "fecha=" + encodeURIComponent(fecha) + "&";
        if (turno) url += "numeroTurno=" + encodeURIComponent(turno);

        // Redirigir a la descarga del PDF
        window.location.href = url;
    });


    // EXPORTACIÓN EXCEL
    const formExcel = document.getElementById("formExportExcel");

    formExcel.addEventListener("submit", function (event) {
        event.preventDefault();

        const fecha = document.getElementById("filtroFecha").value.trim();
        const turno = document.getElementById("filtroNumeroTurno").value.trim();

        if (!fecha && !turno) {
            alert("⚠ Debe seleccionar fecha o turno.");
            return;
        }

        let url = "/Reportes/excel?";
        if (fecha) url += "fecha=" + encodeURIComponent(fecha) + "&";
        if (turno) url += "numeroTurno=" + encodeURIComponent(turno);

        window.location.href = url;
    });

    //Busqueda general
    document.addEventListener("DOMContentLoaded", () => {

        const buscador = document.getElementById("buscador");
        const filtroFecha = document.getElementById("filtroFecha");
        const filtroTurno = document.getElementById("filtroNumeroTurno");

        const tabla = document.querySelector("table");
        const filas = tabla.querySelectorAll("tbody tr");

        function aplicarFiltros() {
            const texto = buscador.value.toLowerCase().trim();
            const fecha = filtroFecha.value;
            const turno = filtroTurno.value;

            filas.forEach(fila => {
                const textoFila = fila.innerText.toLowerCase();

                const fechaFila = fila.children[6]?.innerText.trim(); // columna FECHA
                const turnoFila = fila.children[4]?.innerText.trim(); // columna N° TURNO

                let visible = true;

                // 🔍 búsqueda general
                if (texto && !textoFila.includes(texto)) {
                    visible = false;
                }

                // 📅 filtro fecha
                if (fecha && fechaFila !== fecha) {
                    visible = false;
                }

                // 🔢 filtro turno
                if (turno && turnoFila !== turno) {
                    visible = false;
                }

                fila.style.display = visible ? "" : "none";
            });
        }

        // Eventos
        buscador.addEventListener("input", aplicarFiltros);
        filtroFecha.addEventListener("change", aplicarFiltros);
        filtroTurno.addEventListener("input", aplicarFiltros);

        // -----------------------------
        // EXPORTACIÓN (mantiene filtros)
        // -----------------------------
        const formExcel = document.getElementById("formExportExcel");
        const formPdf = document.getElementById("formExportPdf");

        if (formExcel) {
            formExcel.addEventListener("submit", () => {
                document.getElementById("inputFechaExcel").value = filtroFecha.value;
                document.getElementById("inputTurnoExcel").value = filtroTurno.value;
                document.getElementById("inputBusquedaExcel").value = buscador.value;
            });
        }

        if (formPdf) {
            formPdf.addEventListener("submit", () => {
                document.getElementById("inputFechaPdf").value = filtroFecha.value;
                document.getElementById("inputTurnoPdf").value = filtroTurno.value;
            });
        }

    });
