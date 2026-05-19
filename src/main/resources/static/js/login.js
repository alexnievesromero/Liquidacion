
  <!-- SCRIPT PARA FECHA Y ANIMACIÓN -->


  document.addEventListener("DOMContentLoaded", () => {
        let now = new Date();
        document.getElementById("fecha").value = now.toISOString().slice(0, 10);

        const offset = now.getTimezoneOffset();
        const localTime = new Date(now.getTime() - offset * 60000).toISOString().slice(0, 16);
        document.getElementById("horaInicio").value = localTime;
    });

    const togglePassword = document.getElementById("togglePassword");
    const passwordField = document.getElementById("passwordField");
    const eyeIcon = document.getElementById("eyeIcon");

    togglePassword.addEventListener("click", () => {
        const isPassword = passwordField.type === "password";
        passwordField.type = isPassword ? "text" : "password";
        eyeIcon.classList.toggle("bi-eye");
        eyeIcon.classList.toggle("bi-eye-slash");
    });