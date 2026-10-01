const currentYear = document.getElementById("currentYear");

if (currentYear) {
    currentYear.textContent = new Date().getFullYear();
}


// ==========================================
// CONTADOR DE IMPACTO
// ==========================================

const counters = document.querySelectorAll(".counter");

const startCounters = () => {

    counters.forEach(counter => {

        const target = Number(counter.dataset.target);

        let current = 0;

        const increment = Math.ceil(target / 60);

        const updateCounter = () => {

            current += increment;

            if (current >= target) {

                counter.textContent = target;

                return;
            }

            counter.textContent = current;

            requestAnimationFrame(updateCounter);
        };

        updateCounter();

    });

};



const impactSection =
    document.querySelector(".impact-section");

if (impactSection) {

    let countersStarted = false;

    const observer = new IntersectionObserver(
        entries => {

            entries.forEach(entry => {

                if (
                    entry.isIntersecting &&
                    !countersStarted
                ) {

                    startCounters();

                    countersStarted = true;

                }

            });

        },
        {
            threshold: 0.4
        }
    );

    observer.observe(impactSection);
}

// ==========================================
// PÁGINA EXPLORAR
// ==========================================

const productsContainer =
    document.getElementById("productsContainer");

const foodSearch =
    document.getElementById("foodSearch");

const searchButton =
    document.getElementById("searchButton");

const categoryButtons =
    document.querySelectorAll(".category-btn");

const resultsCount =
    document.getElementById("resultsCount");

const sortProducts =
    document.getElementById("sortProducts");

const noResults =
    document.getElementById("noResults");


let activeCategory = "all";


// ==========================================
// NORMALIZAR TEXTO
// ==========================================

function normalizeText(text) {

    return text
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "");

}


// ==========================================
// FILTRAR PRODUCTOS
// ==========================================

function filterProducts() {

    if (!productsContainer) {
        return;
    }


    const products =
        productsContainer.querySelectorAll(".product-item");


    const searchText =
        foodSearch
            ? normalizeText(foodSearch.value)
            : "";


    let visibleProducts = 0;


    products.forEach(product => {

        const productName =
            normalizeText(product.dataset.name);

        const productCategory =
            product.dataset.category;


        const matchesSearch =
            productName.includes(searchText);


        const matchesCategory =
            activeCategory === "all" ||
            productCategory === activeCategory;


        if (matchesSearch && matchesCategory) {

            product.classList.remove("d-none");

            visibleProducts++;

        } else {

            product.classList.add("d-none");

        }

    });


    updateResultsCounter(visibleProducts);


    if (noResults) {

        if (visibleProducts === 0) {

            noResults.classList.remove("d-none");

        } else {

            noResults.classList.add("d-none");

        }

    }

}


// ==========================================
// CONTADOR DE RESULTADOS
// ==========================================

function updateResultsCounter(number) {

    if (!resultsCount) {
        return;
    }


    if (number === 1) {

        resultsCount.textContent =
            "1 oferta disponible";

    } else {

        resultsCount.textContent =
            `${number} ofertas disponibles`;

    }

}


// ==========================================
// FILTRO POR CATEGORÍA
// ==========================================

categoryButtons.forEach(button => {

    button.addEventListener("click", () => {

        categoryButtons.forEach(btn => {

            btn.classList.remove("active");

        });


        button.classList.add("active");


        activeCategory =
            button.dataset.category;


        filterProducts();

    });

});


// ==========================================
// BUSCADOR
// ==========================================

if (foodSearch) {

    foodSearch.addEventListener(
        "input",
        filterProducts
    );

}


if (searchButton) {

    searchButton.addEventListener(
        "click",
        filterProducts
    );

}


// ==========================================
// ORDENAR POR PRECIO
// ==========================================

if (sortProducts && productsContainer) {

    sortProducts.addEventListener(
        "change",
        () => {

            const products =
                Array.from(
                    productsContainer.querySelectorAll(
                        ".product-item"
                    )
                );


            const sortType =
                sortProducts.value;


            if (sortType === "price-low") {

                products.sort(
                    (a, b) =>
                        Number(a.dataset.price) -
                        Number(b.dataset.price)
                );

            }


            else if (sortType === "price-high") {

                products.sort(
                    (a, b) =>
                        Number(b.dataset.price) -
                        Number(a.dataset.price)
                );

            }


            else {

                products.sort(
                    (a, b) => {

                        return (
                            Number(a.dataset.originalOrder) -
                            Number(b.dataset.originalOrder)
                        );

                    }
                );

            }


            products.forEach(product => {

                productsContainer.appendChild(product);

            });

        }
    );

}


// ==========================================
// GUARDAR ORDEN ORIGINAL
// ==========================================

if (productsContainer) {

    const products =
        productsContainer.querySelectorAll(
            ".product-item"
        );


    products.forEach(
        (product, index) => {

            product.dataset.originalOrder =
                index;

        }
    );

}


// ==========================================
// FAVORITOS
// ==========================================

const favoriteButtons =
    document.querySelectorAll(
        ".favorite-btn"
    );


favoriteButtons.forEach(button => {

    button.addEventListener(
        "click",
        () => {

            button.classList.toggle(
                "active"
            );


            const icon =
                button.querySelector("i");


            if (
                button.classList.contains("active")
            ) {

                icon.classList.remove(
                    "bi-heart"
                );

                icon.classList.add(
                    "bi-heart-fill"
                );

            } else {

                icon.classList.remove(
                    "bi-heart-fill"
                );

                icon.classList.add(
                    "bi-heart"
                );

            }

        }
    );

});


// ==========================================
// DATOS DE PRODUCTOS
// ==========================================

const foodProducts = {

    1: {

        id: 1,

        category: "PANADERÍA",

        name: "Pack sorpresa de panadería",

        store: "Panadería San Miguel",

        location: "San Miguel · 1.2 km",

        price: 8.90,

        originalPrice: 22.00,

        discount: 60,

        available: 4,

        pickup: "7:00 PM - 9:00 PM",

        image:
            "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=1000&q=80",

        description:
            "Rescata una selección de productos preparados durante el día por Panadería San Miguel a un precio reducido.",

        contents:
            "El pack puede incluir panes, croissants, bollería, piezas dulces u otros productos de panadería disponibles al finalizar el día."

    },


    2: {

        id: 2,

        category: "CAFETERÍA",

        name: "Pack de postres del día",

        store: "Café Central",

        location: "Pueblo Libre · 2.4 km",

        price: 12.00,

        originalPrice: 24.00,

        discount: 50,

        available: 2,

        pickup: "8:00 PM - 9:30 PM",

        image:
            "https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&w=1000&q=80",

        description:
            "Una selección de postres preparados durante el día que todavía se encuentran en buenas condiciones para su consumo.",

        contents:
            "Puede contener tortas, brownies, galletas, muffins u otros postres disponibles en Café Central."

    },


    3: {

        id: 3,

        category: "RESTAURANTE",

        name: "Pizza del día",

        store: "Pizza House",

        location: "Magdalena · 3.1 km",

        price: 16.90,

        originalPrice: 30.00,

        discount: 45,

        available: 5,

        pickup: "9:00 PM - 10:00 PM",

        image:
            "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&fit=crop&w=1000&q=80",

        description:
            "Pizza preparada durante el servicio del día que Pizza House ofrece mediante FoodxEver antes del cierre.",

        contents:
            "La variedad y los ingredientes disponibles pueden cambiar dependiendo de los excedentes del restaurante."

    },


    4: {

        id: 4,

        category: "MARKET",

        name: "Pack de frutas y verduras",

        store: "Mercado Verde",

        location: "Jesús María · 2.7 km",

        price: 10.50,

        originalPrice: 23.00,

        discount: 55,

        available: 7,

        pickup: "6:00 PM - 8:00 PM",

        image:
            "https://images.unsplash.com/photo-1610348725531-843dff563e2c?auto=format&fit=crop&w=1000&q=80",

        description:
            "Una selección de frutas y verduras que todavía pueden ser consumidas y que buscan evitar ser desperdiciadas.",

        contents:
            "La combinación depende de los productos disponibles durante el día y puede contener diferentes frutas y verduras."

    },


    5: {

        id: 5,

        category: "RESTAURANTE",

        name: "Menú ejecutivo",

        store: "Sabor Peruano",

        location: "Breña · 3.5 km",

        price: 13.90,

        originalPrice: 24.00,

        discount: 40,

        available: 1,

        pickup: "4:00 PM - 5:30 PM",

        image:
            "https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1000&q=80",

        description:
            "Menú preparado durante el servicio del restaurante y disponible por tiempo limitado para evitar su desperdicio.",

        contents:
            "El menú puede variar de acuerdo con los platos preparados y disponibles al finalizar el horario de almuerzo."

    },


    6: {

        id: 6,

        category: "CAFETERÍA",

        name: "Sándwich + café",

        store: "Good Morning Café",

        location: "San Isidro · 4.2 km",

        price: 7.90,

        originalPrice: 16.00,

        discount: 50,

        available: 3,

        pickup: "5:30 PM - 7:30 PM",

        image:
            "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=1000&q=80",

        description:
            "Una combinación de sándwich y bebida ofrecida por Good Morning Café al final de su horario de atención.",

        contents:
            "El tipo de sándwich y la bebida disponible pueden variar según los productos restantes del establecimiento."

    }

};



// ==========================================
// OBTENER PRODUCTO DESDE URL
// ==========================================

const productTitle =
    document.getElementById("productTitle");


let selectedProduct = null;


if (productTitle) {

    const urlParameters =
        new URLSearchParams(
            window.location.search
        );


    const productId =
        urlParameters.get("id") || "1";


    selectedProduct =
        foodProducts[productId] ||
        foodProducts[1];


    renderProduct(selectedProduct);

}



// ==========================================
// MOSTRAR INFORMACIÓN DEL PRODUCTO
// ==========================================

function renderProduct(product) {


    const productImage =
        document.getElementById("productImage");

    const productCategory =
        document.getElementById("productCategory");

    const productStore =
        document.getElementById("productStore");

    const productLocation =
        document.getElementById("productLocation");

    const productPrice =
        document.getElementById("productPrice");

    const productOriginalPrice =
        document.getElementById(
            "productOriginalPrice"
        );

    const productDiscount =
        document.getElementById(
            "productDiscount"
        );

    const productAvailability =
        document.getElementById(
            "productAvailability"
        );

    const productPickup =
        document.getElementById(
            "productPickup"
        );

    const productDescription =
        document.getElementById(
            "productDescription"
        );

    const productContents =
        document.getElementById(
            "productContents"
        );

    const storeNameCard =
        document.getElementById(
            "storeNameCard"
        );

    const storeLocationCard =
        document.getElementById(
            "storeLocationCard"
        );


    productImage.src =
        product.image;

    productImage.alt =
        product.name;


    productTitle.textContent =
        product.name;


    productCategory.textContent =
        product.category;


    productStore.textContent =
        product.store;


    productLocation.textContent =
        product.location;


    productPrice.textContent =
        `S/ ${product.price.toFixed(2)}`;


    productOriginalPrice.textContent =
        `S/ ${product.originalPrice.toFixed(2)}`;


    productDiscount.textContent =
        `-${product.discount}%`;


    productPickup.textContent =
        product.pickup;


    productDescription.textContent =
        product.description;


    productContents.textContent =
        product.contents;


    storeNameCard.textContent =
        product.store;


    storeLocationCard.textContent =
        product.location;




    if (product.available === 1) {

        productAvailability.textContent =
            "Última unidad";


        productAvailability.classList.remove(
            "available"
        );


        productAvailability.classList.add(
            "limited"
        );

    }

    else if (product.available <= 2) {

        productAvailability.textContent =
            `Solo ${product.available} disponibles`;


        productAvailability.classList.remove(
            "available"
        );


        productAvailability.classList.add(
            "limited"
        );

    }

    else {

        productAvailability.textContent =
            `${product.available} disponibles`;

    }



    createQuantityOptions(
        product.available
    );


    updateReservationTotal();

}



// ==========================================
// CREAR OPCIONES DE CANTIDAD
// ==========================================

function createQuantityOptions(stock) {


    const quantitySelect =
        document.getElementById(
            "productQuantity"
        );


    if (!quantitySelect) {
        return;
    }


    quantitySelect.innerHTML = "";


    for (
        let quantity = 1;
        quantity <= stock;
        quantity++
    ) {


        const option =
            document.createElement("option");


        option.value =
            quantity;


        option.textContent =
            quantity === 1
                ? "1 unidad"
                : `${quantity} unidades`;


        quantitySelect.appendChild(option);

    }

}



// ==========================================
// ACTUALIZAR TOTAL
// ==========================================

function updateReservationTotal() {


    const quantitySelect =
        document.getElementById(
            "productQuantity"
        );


    const reservationTotal =
        document.getElementById(
            "reservationTotal"
        );


    if (
        !quantitySelect ||
        !reservationTotal ||
        !selectedProduct
    ) {
        return;
    }


    const quantity =
        Number(quantitySelect.value);


    const total =
        selectedProduct.price *
        quantity;


    reservationTotal.textContent =
        `S/ ${total.toFixed(2)}`;

}




const productQuantity =
    document.getElementById(
        "productQuantity"
    );


if (productQuantity) {

    productQuantity.addEventListener(
        "change",
        updateReservationTotal
    );

}



// ==========================================
// REALIZAR RESERVA
// ==========================================

const reserveButton =
    document.getElementById(
        "reserveButton"
    );


if (reserveButton) {

    reserveButton.addEventListener(
        "click",
        () => {


            if (!selectedProduct) {
                return;
            }


            const quantity =
                Number(
                    document.getElementById(
                        "productQuantity"
                    ).value
                );


            let reservations =
                JSON.parse(
                    localStorage.getItem(
                        "foodxeverReservations"
                    )
                ) || [];



            const existingReservation =
                reservations.find(
                    reservation =>
                        reservation.productId ===
                            selectedProduct.id &&
                        reservation.status !==
                            "Cancelada"
                );


            if (existingReservation) {

                alert(
                    "Ya tienes una reserva activa para esta oferta."
                );

                return;

            }



            const reservation = {

                reservationId:
                    Date.now(),

                productId:
                    selectedProduct.id,

                name:
                    selectedProduct.name,

                store:
                    selectedProduct.store,

                location:
                    selectedProduct.location,

                image:
                    selectedProduct.image,

                quantity:
                    quantity,

                unitPrice:
                    selectedProduct.price,

                total:
                    selectedProduct.price *
                    quantity,

                pickup:
                    selectedProduct.pickup,

                status:
                    "Pendiente",

                createdAt:
                    new Date().toISOString()

            };


            reservations.push(
                reservation
            );


            localStorage.setItem(
                "foodxeverReservations",
                JSON.stringify(
                    reservations
                )
            );




            const successMessage =
                document.getElementById(
                    "reservationSuccess"
                );


            successMessage.classList.remove(
                "d-none"
            );


            reserveButton.innerHTML = `
                <i class="bi bi-check-circle me-2"></i>
                Reserva realizada
            `;


            reserveButton.disabled =
                true;


            successMessage.scrollIntoView({
                behavior: "smooth",
                block: "center"
            });


        }
    );

}



// ==========================================
// FAVORITO EN DETALLE DE PRODUCTO
// ==========================================

const productFavorite =
    document.getElementById(
        "productFavorite"
    );


if (productFavorite) {

    productFavorite.addEventListener(
        "click",
        () => {


            productFavorite.classList.toggle(
                "active"
            );


            const icon =
                productFavorite.querySelector(
                    "i"
                );


            if (
                productFavorite.classList.contains(
                    "active"
                )
            ) {

                icon.classList.remove(
                    "bi-heart"
                );

                icon.classList.add(
                    "bi-heart-fill"
                );

            }

            else {

                icon.classList.remove(
                    "bi-heart-fill"
                );

                icon.classList.add(
                    "bi-heart"
                );

            }


        }
    );

}


// ==========================================
// PÁGINA MIS RESERVAS
// ==========================================

const reservationsContainer =
    document.getElementById(
        "reservationsContainer"
    );


const emptyReservations =
    document.getElementById(
        "emptyReservations"
    );


let reservationToCancel = null;



// ==========================================
// OBTENER RESERVAS
// ==========================================

function getReservations() {

    return JSON.parse(
        localStorage.getItem(
            "foodxeverReservations"
        )
    ) || [];

}



// ==========================================
// GUARDAR RESERVAS
// ==========================================

function saveReservations(reservations) {

    localStorage.setItem(
        "foodxeverReservations",
        JSON.stringify(reservations)
    );

}



// ==========================================
// MOSTRAR RESERVAS
// ==========================================

function renderReservations() {

    if (!reservationsContainer) {
        return;
    }


    const reservations =
        getReservations();


    reservationsContainer.innerHTML = "";



    if (reservations.length === 0) {

        emptyReservations.classList.remove(
            "d-none"
        );


        reservationsContainer.classList.add(
            "d-none"
        );


        updateReservationSummary(
            reservations
        );


        return;

    }


    emptyReservations.classList.add(
        "d-none"
    );


    reservationsContainer.classList.remove(
        "d-none"
    );




    reservations.sort(

        (a, b) =>
            new Date(b.createdAt) -
            new Date(a.createdAt)

    );



    reservations.forEach(reservation => {


        const column =
            document.createElement("div");


        column.className =
            "col-lg-6";


        const statusClass =
            getReservationStatusClass(
                reservation.status
            );


        const statusIcon =
            getReservationStatusIcon(
                reservation.status
            );


        const date =
            formatReservationDate(
                reservation.createdAt
            );


        column.innerHTML = `

            <div class="reservation-card h-100">

                <img
                    src="${reservation.image}"
                    alt="${reservation.name}"
                    class="reservation-image"
                >


                <div class="reservation-card-body">


                    <div
                        class="d-flex justify-content-between
                        align-items-start gap-3 mb-3"
                    >

                        <div>

                            <span
                                class="reservation-status ${statusClass}"
                            >

                                <i class="bi ${statusIcon}"></i>

                                ${reservation.status}

                            </span>

                        </div>


                        <small class="text-secondary">

                            ${date}

                        </small>

                    </div>



                    <h4 class="fw-bold">

                        ${reservation.name}

                    </h4>



                    <p class="text-secondary">

                        <i class="bi bi-shop me-1"></i>

                        ${reservation.store}

                    </p>



                    <hr>



                    <div class="reservation-detail">

                        <i class="bi bi-geo-alt"></i>

                        <span>

                            ${reservation.location}

                        </span>

                    </div>



                    <div class="reservation-detail">

                        <i class="bi bi-clock"></i>

                        <span>

                            Recojo:
                            ${reservation.pickup}

                        </span>

                    </div>



                    <div class="reservation-detail">

                        <i class="bi bi-bag"></i>

                        <span>

                            Cantidad:
                            ${reservation.quantity}

                            ${
                                reservation.quantity === 1
                                    ? "unidad"
                                    : "unidades"
                            }

                        </span>

                    </div>



                    <div class="reservation-total-box">

                        <span class="text-secondary">
                            Total
                        </span>


                        <span class="reservation-total-price">

                            S/
                            ${reservation.total.toFixed(2)}

                        </span>

                    </div>



                    <div class="mt-4">

                        ${createReservationButtons(
                            reservation
                        )}

                    </div>


                </div>

            </div>

        `;


        reservationsContainer.appendChild(
            column
        );

    });



    updateReservationSummary(
        reservations
    );

}



// ==========================================
// BOTONES SEGÚN ESTADO
// ==========================================

function createReservationButtons(
    reservation
) {


    if (
        reservation.status === "Cancelada"
    ) {

        return `

            <div class="d-flex gap-2">

                <a
                    href="producto.html?id=${reservation.productId}"
                    class="btn btn-outline-success flex-grow-1"
                >

                    <i class="bi bi-arrow-repeat me-1"></i>

                    Reservar nuevamente

                </a>

            </div>

        `;

    }


    if (
        reservation.status === "Completada"
    ) {

        return `

            <div class="alert alert-success mb-0">

                <i class="bi bi-check-circle me-1"></i>

                Esta reserva fue completada.

            </div>

        `;

    }


    return `

        <div class="d-flex gap-2">

            <a
                href="producto.html?id=${reservation.productId}"
                class="btn btn-outline-success flex-grow-1"
            >

                <i class="bi bi-eye me-1"></i>

                Ver oferta

            </a>


            <button
                class="btn btn-outline-danger cancel-reservation-btn"
                data-reservation-id="${reservation.reservationId}"
            >

                <i class="bi bi-x-circle"></i>

                Cancelar

            </button>

        </div>

    `;

}



// ==========================================
// CLASE DEL ESTADO
// ==========================================

function getReservationStatusClass(
    status
) {


    if (status === "Cancelada") {
        return "cancelled";
    }


    if (status === "Completada") {
        return "completed";
    }


    return "pending";

}



// ==========================================
// ICONO DEL ESTADO
// ==========================================

function getReservationStatusIcon(
    status
) {


    if (status === "Cancelada") {

        return "bi-x-circle-fill";

    }


    if (status === "Completada") {

        return "bi-check-circle-fill";

    }


    return "bi-clock-fill";

}



// ==========================================
// FORMATEAR FECHA
// ==========================================

function formatReservationDate(
    dateString
) {


    const date =
        new Date(dateString);


    return date.toLocaleDateString(
        "es-PE",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );

}



// ==========================================
// RESUMEN DE RESERVAS
// ==========================================

function updateReservationSummary(
    reservations
) {


    const activeCountElement =
        document.getElementById(
            "activeReservationsCount"
        );


    const cancelledCountElement =
        document.getElementById(
            "cancelledReservationsCount"
        );


    const reservedTotalElement =
        document.getElementById(
            "reservedTotal"
        );


    if (
        !activeCountElement ||
        !cancelledCountElement ||
        !reservedTotalElement
    ) {
        return;
    }



    const activeReservations =
        reservations.filter(

            reservation =>
                reservation.status ===
                "Pendiente"

        );



    const cancelledReservations =
        reservations.filter(

            reservation =>
                reservation.status ===
                "Cancelada"

        );



    const total =
        activeReservations.reduce(

            (sum, reservation) =>
                sum + reservation.total,

            0

        );



    activeCountElement.textContent =
        activeReservations.length;


    cancelledCountElement.textContent =
        cancelledReservations.length;


    reservedTotalElement.textContent =
        `S/ ${total.toFixed(2)}`;

}



// ==========================================
// DETECTAR BOTÓN CANCELAR
// ==========================================

if (reservationsContainer) {

    reservationsContainer.addEventListener(
        "click",
        event => {


            const cancelButton =
                event.target.closest(
                    ".cancel-reservation-btn"
                );


            if (!cancelButton) {
                return;
            }


            reservationToCancel =
                Number(
                    cancelButton.dataset
                        .reservationId
                );


            const modalElement =
                document.getElementById(
                    "cancelReservationModal"
                );


            const modal =
                new bootstrap.Modal(
                    modalElement
                );


            modal.show();

        }
    );

}



// ==========================================
// CONFIRMAR CANCELACIÓN
// ==========================================

const confirmCancelReservation =
    document.getElementById(
        "confirmCancelReservation"
    );


if (confirmCancelReservation) {

    confirmCancelReservation.addEventListener(
        "click",
        () => {


            if (!reservationToCancel) {
                return;
            }


            const reservations =
                getReservations();


            const reservation =
                reservations.find(

                    reservation =>
                        reservation.reservationId ===
                        reservationToCancel

                );


            if (!reservation) {
                return;
            }


            reservation.status =
                "Cancelada";


            saveReservations(
                reservations
            );


            reservationToCancel =
                null;




            const modalElement =
                document.getElementById(
                    "cancelReservationModal"
                );


            const modal =
                bootstrap.Modal.getInstance(
                    modalElement
                );


            modal.hide();




            renderReservations();

        }
    );

}



// ==========================================
// INICIAR PÁGINA RESERVAS
// ==========================================

if (reservationsContainer) {

    renderReservations();

}


// ==========================================
// FOODXEVER - MIGRACIÓN DE DATOS ANTIGUOS
// ==========================================

// Conserva datos creados con las claves antiguas.

if (
    !localStorage.getItem("foodxeverUsers") &&
    localStorage.getItem("foodRescueUsers")
) {
    localStorage.setItem(
        "foodxeverUsers",
        localStorage.getItem("foodRescueUsers")
    );
}

if (
    !localStorage.getItem("foodxeverCurrentUser") &&
    localStorage.getItem("foodRescueCurrentUser")
) {
    localStorage.setItem(
        "foodxeverCurrentUser",
        localStorage.getItem("foodRescueCurrentUser")
    );
}

if (
    !localStorage.getItem("foodxeverReservations") &&
    localStorage.getItem("foodRescueReservations")
) {
    localStorage.setItem(
        "foodxeverReservations",
        localStorage.getItem("foodRescueReservations")
    );
}


// ==========================================
// SISTEMA DE USUARIOS
// ==========================================

function getFoodxeverUsers() {

    return JSON.parse(
        localStorage.getItem(
            "foodxeverUsers"
        )
    ) || [];

}


function saveFoodxeverUsers(users) {

    localStorage.setItem(
        "foodxeverUsers",
        JSON.stringify(users)
    );

}



// ==========================================
// MENSAJES DE AUTENTICACIÓN
// ==========================================

function showAuthMessage(
    element,
    message,
    type
) {

    if (!element) {
        return;
    }


    element.textContent =
        message;


    element.className =
        `alert alert-${type}`;

}



// ==========================================
// REGISTRO
// ==========================================

const registerForm =
    document.getElementById(
        "registerForm"
    );


const registerRole =
    document.getElementById(
        "registerRole"
    );


const businessNameContainer =
    document.getElementById(
        "businessNameContainer"
    );


const businessNameInput =
    document.getElementById(
        "businessName"
    );




if (
    registerRole &&
    businessNameContainer &&
    businessNameInput
) {

    registerRole.addEventListener(
        "change",
        () => {

            if (
                registerRole.value ===
                "establecimiento"
            ) {

                businessNameContainer.classList.remove(
                    "d-none"
                );


                businessNameInput.required =
                    true;

            }

            else {

                businessNameContainer.classList.add(
                    "d-none"
                );


                businessNameInput.required =
                    false;


                businessNameInput.value =
                    "";

            }

        }
    );

}




if (registerForm) {

    registerForm.addEventListener(
        "submit",
        event => {


            event.preventDefault();


            const name =
                document
                    .getElementById(
                        "registerName"
                    )
                    .value
                    .trim();


            const email =
                document
                    .getElementById(
                        "registerEmail"
                    )
                    .value
                    .trim()
                    .toLowerCase();


            const password =
                document.getElementById(
                    "registerPassword"
                ).value;


            const confirmPassword =
                document.getElementById(
                    "confirmPassword"
                ).value;


            const role =
                registerRole.value;


            const businessName =
                businessNameInput
                    ? businessNameInput.value.trim()
                    : "";


            const messageElement =
                document.getElementById(
                    "registerMessage"
                );




            if (name.length < 3) {

                showAuthMessage(
                    messageElement,
                    "Ingresa un nombre válido.",
                    "danger"
                );

                return;

            }




            if (password.length < 6) {

                showAuthMessage(
                    messageElement,
                    "La contraseña debe tener al menos 6 caracteres.",
                    "danger"
                );

                return;

            }




            if (
                password !==
                confirmPassword
            ) {

                showAuthMessage(
                    messageElement,
                    "Las contraseñas no coinciden.",
                    "danger"
                );

                return;

            }




            if (
                role === "establecimiento" &&
                businessName.length < 3
            ) {

                showAuthMessage(
                    messageElement,
                    "Ingresa el nombre del establecimiento.",
                    "danger"
                );

                return;

            }



            const users =
                getFoodxeverUsers();




            const emailExists =
                users.some(
                    user =>
                        user.email ===
                        email
                );


            if (emailExists) {

                showAuthMessage(
                    messageElement,
                    "Ya existe una cuenta registrada con ese correo.",
                    "danger"
                );

                return;

            }




            const newUser = {

                userId:
                    Date.now(),

                name:
                    name,

                email:
                    email,

                password:
                    password,

                role:
                    role,

                businessName:
                    role === "establecimiento"
                        ? businessName
                        : null,

                createdAt:
                    new Date().toISOString()

            };



            users.push(
                newUser
            );


            saveFoodxeverUsers(
                users
            );



            showAuthMessage(
                messageElement,
                "Cuenta creada correctamente. Redirigiendo al inicio de sesión...",
                "success"
            );



            registerForm.reset();


            businessNameContainer
                ?.classList.add(
                    "d-none"
                );



            setTimeout(
                () => {

                    window.location.href =
                        "login.html?registered=1";

                },
                1000
            );


        }
    );

}


// ==========================================
// LOGIN
// ==========================================

const loginForm =
    document.getElementById(
        "loginForm"
    );


if (loginForm) {

    const urlParameters =
        new URLSearchParams(
            window.location.search
        );


    const loginMessage =
        document.getElementById(
            "loginMessage"
        );




    if (
        urlParameters.get(
            "registered"
        ) === "1"
    ) {

        showAuthMessage(
            loginMessage,
            "Tu cuenta fue creada correctamente. Ahora puedes iniciar sesión.",
            "success"
        );

    }



    loginForm.addEventListener(
        "submit",
        event => {


            event.preventDefault();



            const email =
                document
                    .getElementById(
                        "loginEmail"
                    )
                    .value
                    .trim()
                    .toLowerCase();


            const password =
                document.getElementById(
                    "loginPassword"
                ).value;



            const users =
                getFoodxeverUsers();



            const user =
                users.find(

                    storedUser =>

                        storedUser.email ===
                            email &&

                        storedUser.password ===
                            password

                );




            if (!user) {

                showAuthMessage(
                    loginMessage,
                    "El correo o la contraseña son incorrectos.",
                    "danger"
                );

                return;

            }




            const currentUser = {

                userId:
                    user.userId,

                name:
                    user.name,

                email:
                    user.email,

                role:
                    user.role,

                businessName:
                    user.businessName

            };



            localStorage.setItem(
                "foodxeverCurrentUser",
                JSON.stringify(
                    currentUser
                )
            );



            showAuthMessage(
                loginMessage,
                `Bienvenido, ${user.name}.`,
                "success"
            );




            setTimeout(
                () => {


                    if (user.role === "establecimiento") {

                        window.location.href =
                            "panel-establecimiento.html";

                    } else {

                        window.location.href =
                            "explorar.html";

                    }


                },
                700
            );


        }
    );

}


// ==========================================
// MOSTRAR / OCULTAR CONTRASEÑA LOGIN
// ==========================================

const toggleLoginPassword =
    document.getElementById(
        "toggleLoginPassword"
    );


if (toggleLoginPassword) {

    toggleLoginPassword.addEventListener(
        "click",
        () => {


            const passwordInput =
                document.getElementById(
                    "loginPassword"
                );


            const icon =
                toggleLoginPassword.querySelector(
                    "i"
                );


            if (
                passwordInput.type ===
                "password"
            ) {

                passwordInput.type =
                    "text";


                icon.classList.remove(
                    "bi-eye"
                );


                icon.classList.add(
                    "bi-eye-slash"
                );

            }

            else {

                passwordInput.type =
                    "password";


                icon.classList.remove(
                    "bi-eye-slash"
                );


                icon.classList.add(
                    "bi-eye"
                );

            }


        }
    );

}


// ==========================================
// FOODXEVER - PANEL ESTABLECIMIENTO
// ==========================================

const businessOffersContainer =
    document.getElementById(
        "businessOffersContainer"
    );


const newOfferForm =
    document.getElementById(
        "newOfferForm"
    );


let dashboardCurrentUser = null;



// ==========================================
// OBTENER USUARIO ACTUAL
// ==========================================

function getFoodxEverCurrentUser() {

    return JSON.parse(
        localStorage.getItem(
            "foodxeverCurrentUser"
        )
    );

}



// ==========================================
// OBTENER OFERTAS
// ==========================================

function getFoodxEverOffers() {

    return JSON.parse(
        localStorage.getItem(
            "foodxeverOffers"
        )
    ) || [];

}



// ==========================================
// GUARDAR OFERTAS
// ==========================================

function saveFoodxEverOffers(offers) {

    localStorage.setItem(
        "foodxeverOffers",
        JSON.stringify(offers)
    );

}



// ==========================================
// INICIAR DASHBOARD
// ==========================================

function initializeBusinessDashboard() {

    if (!businessOffersContainer) {
        return;
    }


    dashboardCurrentUser =
        getFoodxEverCurrentUser();




    if (!dashboardCurrentUser) {

        window.location.href =
            "login.html";

        return;

    }




    if (
        dashboardCurrentUser.role !==
        "establecimiento"
    ) {

        window.location.href =
            "index.html";

        return;

    }



    const businessName =
        dashboardCurrentUser.businessName ||
        dashboardCurrentUser.name;



    document.getElementById(
        "dashboardBusinessName"
    ).textContent =
        businessName;


    document.getElementById(
        "dashboardBusinessNavbar"
    ).textContent =
        businessName;


    document.getElementById(
        "dashboardUserName"
    ).textContent =
        dashboardCurrentUser.name;



    renderBusinessOffers();

    renderBusinessReservations();

    updateBusinessDashboardStats();

}



// ==========================================
// PUBLICAR NUEVA OFERTA
// ==========================================

if (newOfferForm) {

    newOfferForm.addEventListener(
        "submit",
        event => {


            event.preventDefault();


            if (!dashboardCurrentUser) {
                return;
            }



            const name =
                document
                    .getElementById(
                        "offerName"
                    )
                    .value
                    .trim();


            const category =
                document.getElementById(
                    "offerCategory"
                ).value;


            const price =
                Number(
                    document.getElementById(
                        "offerPrice"
                    ).value
                );


            const originalPrice =
                Number(
                    document.getElementById(
                        "offerOriginalPrice"
                    ).value
                );


            const stock =
                Number(
                    document.getElementById(
                        "offerStock"
                    ).value
                );


            const pickup =
                document
                    .getElementById(
                        "offerPickup"
                    )
                    .value
                    .trim();


            const location =
                document
                    .getElementById(
                        "offerLocation"
                    )
                    .value
                    .trim();


            const image =
                document
                    .getElementById(
                        "offerImage"
                    )
                    .value
                    .trim();


            const description =
                document
                    .getElementById(
                        "offerDescription"
                    )
                    .value
                    .trim();


            const message =
                document.getElementById(
                    "offerFormMessage"
                );




            if (price >= originalPrice) {

                showAuthMessage(
                    message,
                    "El precio FoodxEver debe ser menor que el precio original.",
                    "danger"
                );

                return;

            }



            const discount =
                Math.round(
                    (
                        1 -
                        price /
                        originalPrice
                    ) *
                    100
                );



            const offers =
                getFoodxEverOffers();



            const newOffer = {

                offerId:
                    Date.now(),

                ownerId:
                    dashboardCurrentUser.userId,

                businessName:
                    dashboardCurrentUser.businessName,

                name:
                    name,

                category:
                    category,

                price:
                    price,

                originalPrice:
                    originalPrice,

                discount:
                    discount,

                stock:
                    stock,

                pickup:
                    pickup,

                location:
                    location,

                image:
                    image,

                description:
                    description,

                active:
                    true,

                createdAt:
                    new Date().toISOString()

            };



            offers.push(
                newOffer
            );


            saveFoodxEverOffers(
                offers
            );



            newOfferForm.reset();



            message.className =
                "alert alert-success";


            message.textContent =
                "Oferta publicada correctamente.";



            renderBusinessOffers();

            updateBusinessDashboardStats();



            setTimeout(
                () => {

                    const modalElement =
                        document.getElementById(
                            "newOfferModal"
                        );


                    const modal =
                        bootstrap.Modal.getInstance(
                            modalElement
                        );


                    modal.hide();


                    message.classList.add(
                        "d-none"
                    );

                },
                600
            );


        }
    );

}



// ==========================================
// MOSTRAR PUBLICACIONES
// ==========================================

function renderBusinessOffers() {

    if (!businessOffersContainer) {
        return;
    }


    const emptyState =
        document.getElementById(
            "emptyBusinessOffers"
        );


    const offers =
        getFoodxEverOffers()
            .filter(
                offer =>
                    offer.ownerId ===
                    dashboardCurrentUser.userId
            );


    businessOffersContainer.innerHTML =
        "";



    if (offers.length === 0) {

        businessOffersContainer.classList.add(
            "d-none"
        );


        emptyState.classList.remove(
            "d-none"
        );


        return;

    }



    businessOffersContainer.classList.remove(
        "d-none"
    );


    emptyState.classList.add(
        "d-none"
    );



    offers
        .sort(
            (a, b) =>
                new Date(b.createdAt) -
                new Date(a.createdAt)
        )
        .forEach(
            offer => {


                const column =
                    document.createElement(
                        "div"
                    );


                column.className =
                    "col-md-6 col-xl-4";


                const statusText =
                    offer.active
                        ? "Activa"
                        : "Pausada";


                const statusClass =
                    offer.active
                        ? "active"
                        : "inactive";



                column.innerHTML = `

                    <div
                        class="business-offer-card h-100"
                    >

                        <img
                            src="${offer.image}"
                            alt="${offer.name}"
                            class="business-offer-image"
                        >


                        <div
                            class="business-offer-body"
                        >

                            <div
                                class="d-flex
                                justify-content-between
                                align-items-center
                                mb-2"
                            >

                                <span
                                    class="category-label"
                                >
                                    ${offer.category}
                                </span>


                                <span
                                    class="offer-status ${statusClass}"
                                >
                                    ${statusText}
                                </span>

                            </div>


                            <h5 class="fw-bold">

                                ${offer.name}

                            </h5>


                            <p
                                class="text-secondary small"
                            >

                                <i
                                    class="bi bi-geo-alt"
                                ></i>

                                ${offer.location}

                            </p>


                            <div
                                class="d-flex
                                align-items-center
                                gap-2 mb-3"
                            >

                                <span class="food-price">

                                    S/
                                    ${offer.price.toFixed(2)}

                                </span>


                                <span
                                    class="text-secondary
                                    text-decoration-line-through"
                                >

                                    S/
                                    ${offer.originalPrice.toFixed(2)}

                                </span>

                            </div>


                            <div
                                class="d-flex
                                justify-content-between
                                mb-3 small"
                            >

                                <span>

                                    <i
                                        class="bi bi-box-seam me-1"
                                    ></i>

                                    Stock:
                                    ${offer.stock}

                                </span>


                                <span>

                                    -${offer.discount}%

                                </span>

                            </div>


                            <div class="d-flex gap-2">

                                <button
                                    class="btn
                                    btn-outline-success
                                    flex-grow-1
                                    toggle-offer-btn"
                                    data-offer-id="${offer.offerId}"
                                >

                                    ${
                                        offer.active
                                            ? "Pausar"
                                            : "Activar"
                                    }

                                </button>


                                <button
                                    class="btn
                                    btn-outline-danger
                                    delete-offer-btn"
                                    data-offer-id="${offer.offerId}"
                                >

                                    <i
                                        class="bi bi-trash"
                                    ></i>

                                </button>

                            </div>


                        </div>

                    </div>

                `;


                businessOffersContainer.appendChild(
                    column
                );

            }
        );

}



// ==========================================
// PAUSAR / ACTIVAR / ELIMINAR
// ==========================================

if (businessOffersContainer) {

    businessOffersContainer.addEventListener(
        "click",
        event => {


            const toggleButton =
                event.target.closest(
                    ".toggle-offer-btn"
                );


            const deleteButton =
                event.target.closest(
                    ".delete-offer-btn"
                );



            if (toggleButton) {

                toggleBusinessOffer(
                    Number(
                        toggleButton.dataset.offerId
                    )
                );

            }



            if (deleteButton) {

                deleteBusinessOffer(
                    Number(
                        deleteButton.dataset.offerId
                    )
                );

            }


        }
    );

}



// ==========================================
// ACTIVAR / PAUSAR
// ==========================================

function toggleBusinessOffer(
    offerId
) {

    const offers =
        getFoodxEverOffers();


    const offer =
        offers.find(
            offer =>
                offer.offerId === offerId
        );


    if (!offer) {
        return;
    }


    offer.active =
        !offer.active;


    saveFoodxEverOffers(
        offers
    );


    renderBusinessOffers();

    updateBusinessDashboardStats();

}



// ==========================================
// ELIMINAR OFERTA
// ==========================================

function deleteBusinessOffer(
    offerId
) {

    const confirmation =
        confirm(
            "¿Deseas eliminar esta oferta?"
        );


    if (!confirmation) {
        return;
    }


    let offers =
        getFoodxEverOffers();


    offers =
        offers.filter(
            offer =>
                offer.offerId !== offerId
        );


    saveFoodxEverOffers(
        offers
    );


    renderBusinessOffers();

    updateBusinessDashboardStats();

}

// ==========================================
// RESERVAS RECIBIDAS
// ==========================================

function renderBusinessReservations() {

    const body =
        document.getElementById(
            "businessReservationsBody"
        );


    if (!body) {
        return;
    }


    const reservations =
        JSON.parse(
            localStorage.getItem(
                "foodxeverReservations"
            )
        ) || [];


    const emptyState =
        document.getElementById(
            "emptyBusinessReservations"
        );


    const tableContainer =
        document.getElementById(
            "businessReservationsTableContainer"
        );



    const businessReservations =
        reservations.filter(
            reservation =>
                reservation.store ===
                dashboardCurrentUser.businessName
        );



    body.innerHTML =
        "";



    if (
        businessReservations.length === 0
    ) {

        tableContainer.classList.add(
            "d-none"
        );


        emptyState.classList.remove(
            "d-none"
        );


        return;

    }



    tableContainer.classList.remove(
        "d-none"
    );


    emptyState.classList.add(
        "d-none"
    );



    businessReservations.forEach(
        reservation => {


            const row =
                document.createElement(
                    "tr"
                );


            let statusBadge =
                "bg-warning text-dark";


            if (
                reservation.status ===
                "Completada"
            ) {

                statusBadge =
                    "bg-success";

            }


            if (
                reservation.status ===
                "Cancelada"
            ) {

                statusBadge =
                    "bg-danger";

            }



            let actionButton =
                "-";


            if (
                reservation.status ===
                "Pendiente"
            ) {

                actionButton = `

                    <button
                        class="btn
                        btn-success btn-sm
                        complete-reservation-btn"
                        data-reservation-id="${reservation.reservationId}"
                    >

                        <i
                            class="bi bi-check2"
                        ></i>

                        Confirmar entrega

                    </button>

                `;

            }



            row.innerHTML = `

                <td>

                    <strong>
                        ${reservation.name}
                    </strong>

                </td>


                <td>
                    ${reservation.quantity}
                </td>


                <td>

                    S/
                    ${reservation.total.toFixed(2)}

                </td>


                <td>
                    ${reservation.pickup}
                </td>


                <td>

                    <span
                        class="badge ${statusBadge}"
                    >
                        ${reservation.status}
                    </span>

                </td>


                <td>

                    ${actionButton}

                </td>

            `;


            body.appendChild(
                row
            );

        }
    );

}



// ==========================================
// CONFIRMAR ENTREGA
// ==========================================

const businessReservationsBody =
    document.getElementById(
        "businessReservationsBody"
    );


if (businessReservationsBody) {

    businessReservationsBody.addEventListener(
        "click",
        event => {


            const button =
                event.target.closest(
                    ".complete-reservation-btn"
                );


            if (!button) {
                return;
            }


            const reservationId =
                Number(
                    button.dataset
                        .reservationId
                );


            completeReservation(
                reservationId
            );


        }
    );

}



function completeReservation(
    reservationId
) {

    const reservations =
        JSON.parse(
            localStorage.getItem(
                "foodxeverReservations"
            )
        ) || [];


    const reservation =
        reservations.find(
            reservation =>
                reservation.reservationId ===
                reservationId
        );


    if (!reservation) {
        return;
    }


    reservation.status =
        "Completada";


    localStorage.setItem(
        "foodxeverReservations",
        JSON.stringify(
            reservations
        )
    );


    renderBusinessReservations();

    updateBusinessDashboardStats();

}


// ==========================================
// ESTADÍSTICAS ESTABLECIMIENTO
// ==========================================

function updateBusinessDashboardStats() {

    if (!dashboardCurrentUser) {
        return;
    }


    const offers =
        getFoodxEverOffers()
            .filter(
                offer =>
                    offer.ownerId ===
                    dashboardCurrentUser.userId
            );


    const reservations =
        JSON.parse(
            localStorage.getItem(
                "foodxeverReservations"
            )
        ) || [];


    const businessReservations =
        reservations.filter(
            reservation =>
                reservation.store ===
                dashboardCurrentUser.businessName
        );



    const activeOffers =
        offers.filter(
            offer => offer.active
        );


    const completedReservations =
        businessReservations.filter(
            reservation =>
                reservation.status ===
                "Completada"
        );



    document.getElementById(
        "businessOffersCount"
    ).textContent =
        offers.length;


    document.getElementById(
        "activeOffersCount"
    ).textContent =
        activeOffers.length;


    document.getElementById(
        "businessReservationsCount"
    ).textContent =
        businessReservations.length;


    document.getElementById(
        "completedOrdersCount"
    ).textContent =
        completedReservations.length;

}


// ==========================================
// CERRAR SESIÓN
// ==========================================

const logoutButton =
    document.getElementById(
        "logoutButton"
    );


if (logoutButton) {

    logoutButton.addEventListener(
        "click",
        () => {


            localStorage.removeItem(
                "foodxeverCurrentUser"
            );


            window.location.href =
                "login.html";

        }
    );

}


// ==========================================
// INICIAR PANEL
// ==========================================

if (businessOffersContainer) {

    initializeBusinessDashboard();

}