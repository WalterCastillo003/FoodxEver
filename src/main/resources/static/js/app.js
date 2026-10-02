// ==========================================
// FOODXEVER
// JavaScript general
// ==========================================


// ==========================================
// AÑO DEL FOOTER
// ==========================================

const currentYear =
    document.getElementById(
        "currentYear"
    );


if (currentYear) {

    currentYear.textContent =
        new Date().getFullYear();

}



// ==========================================
// CONTADORES DE IMPACTO
// ==========================================

const counters =
    document.querySelectorAll(
        ".counter"
    );


function startCounters() {

    counters.forEach(
        counter => {

            const target =
                Number(
                    counter.dataset.target
                );


            let current = 0;


            const increment =
                Math.max(
                    1,
                    Math.ceil(
                        target / 60
                    )
                );


            function updateCounter() {

                current +=
                    increment;


                if (
                    current >= target
                ) {

                    counter.textContent =
                        target;

                    return;

                }


                counter.textContent =
                    current;


                requestAnimationFrame(
                    updateCounter
                );

            }


            updateCounter();

        }
    );

}



// ==========================================
// INICIAR CONTADORES CUANDO SEAN VISIBLES
// ==========================================

const impactSection =
    document.querySelector(
        ".impact-section"
    );


if (
    impactSection &&
    counters.length > 0
) {

    let countersStarted =
        false;


    const observer =
        new IntersectionObserver(
            entries => {

                entries.forEach(
                    entry => {

                        if (
                            entry.isIntersecting &&
                            !countersStarted
                        ) {

                            startCounters();

                            countersStarted =
                                true;

                        }

                    }
                );

            },
            {
                threshold: 0.3
            }
        );


    observer.observe(
        impactSection
    );

}