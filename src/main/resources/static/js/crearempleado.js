 document.addEventListener('DOMContentLoaded', function () {
                // Elementos del DOM
                const form = document.getElementById('employeeForm');
                const saveBtn = document.getElementById('saveBtn');
                const passwordInput = document.getElementById('contrasena');
                const togglePassword = document.getElementById('togglePassword');
                const strengthFill = document.getElementById('strengthFill');
                const strengthText = document.getElementById('strengthText');
                const roleOptions = document.querySelectorAll('.role-option');

                // Toggle de contraseña
                togglePassword.addEventListener('click', function () {
                    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                    passwordInput.setAttribute('type', type);

                    const icon = this.querySelector('i');
                    icon.classList.toggle('bi-eye');
                    icon.classList.toggle('bi-eye-slash');
                });

                // Validación de fortaleza de contraseña
                passwordInput.addEventListener('input', function () {
                    const password = this.value;
                    let strength = 0;
                    let strengthClass = '';
                    let strengthLabel = '';

                    if (password.length >= 6)
                        strength++;
                    if (password.match(/[a-z]/))
                        strength++;
                    if (password.match(/[A-Z]/))
                        strength++;
                    if (password.match(/[0-9]/))
                        strength++;
                    if (password.match(/[^a-zA-Z0-9]/))
                        strength++;

                    switch (strength) {
                        case 0:
                        case 1:
                            strengthClass = 'strength-weak';
                            strengthLabel = 'Muy débil';
                            break;
                        case 2:
                            strengthClass = 'strength-fair';
                            strengthLabel = 'Débil';
                            break;
                        case 3:
                            strengthClass = 'strength-good';
                            strengthLabel = 'Buena';
                            break;
                        case 4:
                        case 5:
                            strengthClass = 'strength-strong';
                            strengthLabel = 'Fuerte';
                            break;
                    }

                    strengthFill.className = `strength-fill ${strengthClass}`;
                    strengthText.textContent = password.length > 0 ? `Fortaleza: ${strengthLabel}` : 'Ingresa una contraseña';
                });

                // Manejo de selección de roles
                roleOptions.forEach(option => {
                    option.addEventListener('click', function () {
                        // Remover selección anterior
                        roleOptions.forEach(opt => opt.classList.remove('selected'));

                        // Seleccionar actual
                        this.classList.add('selected');

                        // Marcar radio button
                        const radio = this.querySelector('input[type="radio"]');
                        radio.checked = true;
                    });
                });

                // Marcar rol seleccionado al cargar
                const selectedRole = document.querySelector('input[name="rol"]:checked');
                if (selectedRole) {
                    const roleOption = selectedRole.closest('.role-option');
                    if (roleOption) {
                        roleOption.classList.add('selected');
                    }
                }

                // Validación en tiempo real
                const inputs = form.querySelectorAll('input[required]');
                inputs.forEach(input => {
                    input.addEventListener('blur', validateField);
                    input.addEventListener('input', function () {
                        if (this.classList.contains('is-invalid')) {
                            validateField.call(this);
                        }
                    });
                });

                function validateField() {
                    const isValid = this.checkValidity();
                    this.classList.toggle('is-valid', isValid);
                    this.classList.toggle('is-invalid', !isValid);
                }

                // Validación del formulario
                form.addEventListener('submit', function (e) {
                    e.preventDefault();

                    // Validar todos los campos
                    let isFormValid = true;
                    inputs.forEach(input => {
                        validateField.call(input);
                        if (!input.checkValidity()) {
                            isFormValid = false;
                        }
                    });

                    // Validar que se haya seleccionado un rol
                    const roleSelected = document.querySelector('input[name="rol"]:checked');
                    if (!roleSelected) {
                        isFormValid = false;
                        // Mostrar mensaje de error para rol
                        alert('Por favor selecciona un rol para el empleado');
                        return;
                    }

                    if (isFormValid) {
                        // Mostrar estado de carga
                        saveBtn.classList.add('loading');
                        saveBtn.innerHTML = '<span>Guardando...</span>';

                        // Enviar formulario
                        setTimeout(() => {
                            this.submit();
                        }, 500);
                    } else {
                        // Scroll al primer campo inválido
                        const firstInvalid = form.querySelector('.is-invalid');
                        if (firstInvalid) {
                            firstInvalid.scrollIntoView({behavior: 'smooth', block: 'center'});
                            firstInvalid.focus();
                        }
                    }
                });

                // Prevenir zoom en iOS
                if (window.innerWidth < 768) {
                    inputs.forEach(input => {
                        input.addEventListener('focus', function () {
                            document.querySelector('meta[name=viewport]').setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0');
                        });

                        input.addEventListener('blur', function () {
                            document.querySelector('meta[name=viewport]').setAttribute('content', 'width=device-width, initial-scale=1.0');
                        });
                    });
                }

                // Animación de entrada
                setTimeout(() => {
                    document.querySelector('.form-card').style.opacity = '1';
                    document.querySelector('.form-card').style.transform = 'translateY(0)';
                }, 100);
            });