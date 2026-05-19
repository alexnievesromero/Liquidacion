document.addEventListener('DOMContentLoaded', function () {
                // Animaciones de scroll
                const observerOptions = {
                    threshold: 0.1,
                    rootMargin: '0px 0px -50px 0px'
                };

                const observer = new IntersectionObserver(function (entries) {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.classList.add('visible');
                        }
                    });
                }, observerOptions);

                // Observar elementos con animación
                document.querySelectorAll('.fade-in-up').forEach(el => {
                    observer.observe(el);
                });

                // Contador animado para estadísticas
                function animateCounter(element, target) {
                    let current = 0;
                    const increment = target / 100;
                    const timer = setInterval(() => {
                        current += increment;
                        if (current >= target) {
                            current = target;
                            clearInterval(timer);
                        }
                        element.textContent = Math.floor(current);
                    }, 20);
                }

                // Animar contadores cuando sean visibles
                const statsObserver = new IntersectionObserver(function (entries) {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            const target = parseInt(entry.target.getAttribute('data-target'));
                            animateCounter(entry.target, target);
                            statsObserver.unobserve(entry.target);
                        }
                    });
                }, {threshold: 0.5});

                document.querySelectorAll('.stat-number').forEach(stat => {
                    statsObserver.observe(stat);
                });

                // Smooth scrolling para enlaces internos
                document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                    anchor.addEventListener('click', function (e) {
                        e.preventDefault();
                        const target = document.querySelector(this.getAttribute('href'));
                        if (target) {
                            target.scrollIntoView({
                                behavior: 'smooth',
                                block: 'start'
                            });
                        }
                    });
                });

                // Efecto parallax sutil en hero
                window.addEventListener('scroll', function () {
                    const scrolled = window.pageYOffset;
                    const hero = document.querySelector('.hero-section');
                    if (hero && scrolled < hero.offsetHeight) {
                        hero.style.transform = `translateY(${scrolled * 0.5}px)`;
                    }
                });

                // Crear partículas flotantes adicionales
                function createFloatingParticle() {
                    const particle = document.createElement('div');
                    particle.className = 'shape';
                    particle.style.width = Math.random() * 60 + 40 + 'px';
                    particle.style.height = particle.style.width;
                    particle.style.left = Math.random() * 100 + '%';
                    particle.style.animationDuration = (Math.random() * 10 + 10) + 's';
                    particle.style.animationDelay = Math.random() * 5 + 's';

                    document.querySelector('.floating-shapes').appendChild(particle);

                    // Remover después de la animación
                    setTimeout(() => {
                        if (particle.parentNode) {
                            particle.parentNode.removeChild(particle);
                        }
                    }, 20000);
                }

                // Crear partículas periódicamente
                setInterval(createFloatingParticle, 3000);

                // Efecto de typing en el título (opcional)
                const title = document.querySelector('.hero-title');
                const originalText = title.textContent;
                title.textContent = '';

                let i = 0;
                function typeWriter() {
                    if (i < originalText.length) {
                        title.textContent += originalText.charAt(i);
                        i++;
                        setTimeout(typeWriter, 100);
                    }
                }

                // Iniciar efecto después de un delay
                setTimeout(typeWriter, 1000);
            });