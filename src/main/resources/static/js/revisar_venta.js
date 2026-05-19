
// formatear primero para despues calcular
function getValue(id) {
    const input = document.getElementById(id);
    return parseMoney(input);
}

function parseMoney(input) {
    if (!input || !input.value) return 0;

    let value = input.value
        .replace(/\$/g, "")
        .replace(/\s/g, "");

    // Caso BD: 10000.00
    if (/^\d+(\.\d+)?$/.test(value)) {
        return Math.round(Number(value));
    }

    // Caso UI: 10.000 o 1.234.567
    return Number(value.replace(/\./g, "")) || 0;
}

function formatNumberWithDecimals(num) {
    return new Intl.NumberFormat("es-CO", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(num);
}

// calculo automatico
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

    document.getElementById('total').value = formatNumberWithDecimals(totalCalculado);
    document.getElementById('diferencia').value = formatNumberWithDecimals(diferencia);
    document.getElementById('descuadres').value = formatNumberWithDecimals(diferencia);
}

document.addEventListener("DOMContentLoaded", () => {
    const campos = [
        'venta_total', 'entregas', 'credito_satelite', 'credito_local', 'prepago_local',
        'pagos_electronicos', 'pagos_Qr', 'monedas', 'gastos', 'puntos_terpel',
        'devoluciones', 'cheques', 'regalos', 'prestamos', 'otros', 'efectivo_final'
    ];

    campos.forEach(id => {
        const campo = document.getElementById(id);
        if (campo) campo.addEventListener("input", calcularTotalYDescuadre);
    });

    calcularTotalYDescuadre();
});

// limpiar formato de miles para decimal y poder alamcenar sin errores
document.querySelector("form").addEventListener("submit", () => {

    ['total', 'diferencia', 'descuadres'].forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.value = toBackendNumber(input.value);
        }
    });

});
function toBackendNumber(value) {
    if (!value) return "0.00";

    // 20.000,00 → 20000.00
    return value
        .replace(/\./g, "")
        .replace(",", ".");
}

document.addEventListener("DOMContentLoaded", () => {

    const btnAprobar = document.getElementById("btnAprobar");
    if (!btnAprobar) return;

    btnAprobar.addEventListener("click", async () => {

        if (!confirm("¿Desea aprobar esta liquidación?")) return;

        const ventaId = document.querySelector('input[name="ventaId"]').value;

        const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        try {
            const response = await fetch("/liquidar/ventas/actualizarEstado", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    [csrfHeader]: csrfToken
                },
                body: new URLSearchParams({
                    ventaId: ventaId,
                    estado: "aprobado"
                })
            });

            if (!response.ok) throw new Error("Error HTTP");

            // ✅ Redirigir al reporte
            window.location.href = "/Reportes/reportesTurnos";

        } catch (error) {
            alert("❌ Error al aprobar la venta");
            console.error(error);
        }
    });
});


/*
ya lo coloque y no funciona


  document.addEventListener("DOMContentLoaded", () => {

      const moneyInputs = document.querySelectorAll(".money");

      moneyInputs.forEach(input => {

          // Formatear al cargar
          if (input.value) {
              input.value = formatCOP(input.value);
          }

          input.addEventListener("input", () => {
              const raw = input.value.replace(/\D/g, "");
              input.value = raw ? formatCOP(raw) : "";
          });

          // Antes de enviar el formulario → limpiar formato
          input.form?.addEventListener("submit", () => {
              input.value = input.value.replace(/\D/g, "");
          });
      });
  });

  function formatCOP(value) {
      return "$ " + new Intl.NumberFormat("es-CO").format(value);
  }
document.addEventListener("DOMContentLoaded", () => {

    const btnAprobar = document.getElementById("btnAprobar");
    if (!btnAprobar) return;

    btnAprobar.addEventListener("click", async () => {

        if (!confirm("¿Desea aprobar esta liquidación?")) return;

        const ventaId = document.querySelector('input[name="ventaId"]').value;

        const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        try {
            const response = await fetch("/liquidar/ventas/actualizarEstado", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    [csrfHeader]: csrfToken
                },
                body: new URLSearchParams({
                    ventaId: ventaId,
                    estado: "aprobado"
                })
            });

            if (!response.ok) throw new Error("Error HTTP");

            // ✅ Redirigir al reporte
            window.location.href = "/Reportes/reportesTurnos";

        } catch (error) {
            alert("❌ Error al aprobar la venta");
            console.error(error);
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {

    const moneyInputs = document.querySelectorAll(".money");

    function get(field) {
        return parseMoneyValue(
            document.querySelector(`[data-field="${field}"]`)
        );
    }

    function set(field, value) {
        const input = document.querySelector(`[data-field="${field}"]`);
        if (input) {
            input.value = "$ " + value.toLocaleString("es-CO");
        }
    }

    function recalcular() {

        const total =
              get("entregas")
            + get("credito_satelite")
            + get("credito_local")
            + get("prepago_local")
            + get("pagos_electronicos")
            + get("pagos_Qr")
            + get("monedas")
            + get("cheques")
            + get("otros")
            - get("gastos")
            - get("devoluciones")
            - get("regalos")
            - get("prestamos");

        const efectivoFinal = get("efectivo_final");
        const diferencia = efectivoFinal - total;

        set("total", total);
        set("diferencia", diferencia);
    }

    moneyInputs.forEach(i => {
        i.addEventListener("input", recalcular);
    });

    recalcular();
});*/

