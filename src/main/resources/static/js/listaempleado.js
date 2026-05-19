 document.addEventListener('DOMContentLoaded', function () {
                // Elementos del DOM
                const searchInput = document.getElementById('searchInput');
                const employeesTable = document.getElementById('employeesTable');
                const tableContainer = document.getElementById('tableContainer');
                const emptyState = document.getElementById('emptyState');
                const loadingState = document.getElementById('loadingState');
                const deleteModal = document.getElementById('deleteModal');
                const employeeName = document.getElementById('employeeName');
                const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

                // Función de búsqueda
                function filterEmployees() {
                    const searchTerm = searchInput.value.toLowerCase().trim();
                    const rows = employeesTable.querySelectorAll('tbody .employee-row');
                    let visibleRows = 0;

                    rows.forEach(row => {
                        const name = row.cells[1].textContent.toLowerCase();
                        const user = row.cells[2] ? row.cells[2].textContent.toLowerCase() : '';
                        const role = row.cells[3].textContent.toLowerCase();

                        const isVisible = name.includes(searchTerm) ||
                                user.includes(searchTerm) ||
                                role.includes(searchTerm);

                        if (isVisible) {
                            row.style.display = '';
                            visibleRows++;
                        } else {
                            row.style.display = 'none';
                        }
                    });

                    // Mostrar/ocultar estado vacío
                    if (visibleRows === 0 && searchTerm !== '') {
                        employeesTable.style.display = 'none';
                        emptyState.classList.remove('d-none');
                    } else {
                        employeesTable.style.display = '';
                        emptyState.classList.add('d-none');
                    }
                }

                // Event listener para búsqueda
                searchInput.addEventListener('input', filterEmployees);

                // Configurar modal de eliminación
                deleteModal.addEventListener('show.bs.modal', function (event) {
                    const button = event.relatedTarget;
                    const employeeId = button.getAttribute('data-employee-id');
                    const name = button.getAttribute('data-employee-name');

                    employeeName.textContent = name;
                    confirmDeleteBtn.href = `/empleado/eliminar/${employeeId}`;
                });

                // Animación de entrada para las filas
                const observerOptions = {
                    threshold: 0.1,
                    rootMargin: '0px 0px -50px 0px'
                };

                const observer = new IntersectionObserver(function (entries) {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.style.opacity = '0';
                            entry.target.style.transform = 'translateY(20px)';
                            entry.target.style.transition = 'all 0.5s ease';

                            setTimeout(() => {
                                entry.target.style.opacity = '1';
                                entry.target.style.transform = 'translateY(0)';
                            }, 100);

                            observer.unobserve(entry.target);
                        }
                    });
                }, observerOptions);

                // Observar todas las filas
                document.querySelectorAll('.employee-row').forEach(row => {
                    observer.observe(row);
                });

                // Efecto de hover mejorado para botones de acción
                document.querySelectorAll('.btn-action').forEach(btn => {
                    btn.addEventListener('mouseenter', function () {
                        this.style.transform = 'translateY(-2px) scale(1.1)';
                    });

                    btn.addEventListener('mouseleave', function () {
                        this.style.transform = 'translateY(0) scale(1)';
                    });
                });

                // Simulación de carga (opcional)
                setTimeout(() => {
                    loadingState.style.display = 'none';
                    tableContainer.style.display = 'block';
                }, 500);

                // Contador animado para stats
                function animateCounter(element, target) {
                    let current = 0;
                    const increment = target / 50;
                    const timer = setInterval(() => {
                        current += increment;
                        if (current >= target) {
                            current = target;
                            clearInterval(timer);
                        }
                        element.textContent = Math.floor(current);
                    }, 20);
                }

                // Animar contadores al cargar
                const statNumbers = document.querySelectorAll('.stat-number');
                statNumbers.forEach(stat => {
                    const target = parseInt(stat.textContent);
                    if (!isNaN(target)) {
                        animateCounter(stat, target);
                    }
                });
            });