# Checklist de pruebas — FoodxEver (tercer avance)

Sigue este orden para evitar confundir errores de la base de datos con errores de las páginas.

## A. Comprobaciones antes de iniciar

- [ ] `java -version` muestra Java 25.
- [ ] PostgreSQL está iniciado.
- [ ] En pgAdmin existe una base de datos llamada `foodxever`.
- [ ] En PowerShell, desde la carpeta que contiene `mvnw.cmd`, configuré las tres variables locales de `SPRING_DATASOURCE_*` descritas en `README_TERCER_AVANCE.md`.

## B. Primer arranque

Ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

- [ ] La consola indica que Spring Boot inició correctamente sin errores de conexión a PostgreSQL.
- [ ] La primera ejecución crea las tablas a partir de las entidades JPA.
- [ ] Se cargan datos de demostración en una base nueva.
- [ ] `http://localhost:8080/` abre la página principal.

## C. Páginas a comprobar

- [ ] `/productos`: se listan los productos y sus imágenes.
- [ ] `/productos/nuevo`: puedo elegir un establecimiento y la lista de categorías cambia según el tipo de local.
- [ ] Creo, edito y elimino un producto de prueba.
- [ ] `/establecimientos`: puedo crear, editar y eliminar/desactivar un establecimiento de prueba.
- [ ] `/reservas`: puedo crear una reserva y se calcula el total desde el precio del producto en el servidor.
- [ ] En `/reservas/nueva`, cambiar el establecimiento modifica la lista de productos disponibles.
- [ ] `/consultas`: aparecen los dos listados DTO y el filtro por establecimiento afecta ambos.
- [ ] `/pagos`: puedo crear, editar y eliminar un pago vinculado a una reserva.
- [ ] `/resenas`: puedo crear, editar y eliminar una reseña vinculada a usuario y establecimiento.
- [ ] `/estadisticas` y `/estadisticas/reservas` abren sin error.
- [ ] `/registro` guarda una cuenta; después puedo iniciar sesión en `/login`.

Cuenta de demostración en una base recién creada: `demo@foodxever.com` / `123456`.

## D. Verificar persistencia y modelo relacional

- [ ] En pgAdmin veo las 11 tablas: `roles`, `usuarios`, `establecimientos`, `categorias`, `productos`, `reservas`, `metodos_pago`, `pagos`, `resenas`, `historial_reservas` y `horarios_recojo`.
- [ ] Creo o edito un producto, detengo y vuelvo a iniciar la aplicación y compruebo que el cambio siga en la base de datos.
- [ ] Compruebo que al crear/cambiar una reserva se registran movimientos en `historial_reservas`.
- [ ] Compruebo que la contraseña guardada de una cuenta no es texto plano (se almacena con hash BCrypt).

## E. Solo después de pasar las pruebas locales

1. Añade el servicio PostgreSQL al proyecto de Railway.
2. Configura las variables de referencia descritas en `README_TERCER_AVANCE.md` en el servicio web.
3. Revisa las variables antes de desplegar; no las subas a GitHub ni las incluyas en capturas públicas.
4. Haz un commit y push cuando la versión local funcione; revisa que Railway despliegue ese commit.

## Alcance actual

La interfaz mantiene CRUD para productos, establecimientos y reservas, registro/inicio de sesión persistentes para usuarios y ahora incluye pantallas CRUD para pagos y reseñas. Las tablas auxiliares de roles, categorías, métodos de pago, horarios e historial forman parte del modelo; roles es un catálogo controlado e historial funciona como bitácora de auditoría. Si el docente interpreta el requisito como CRUD manual de cada tabla sin excepción, conviene confirmar cómo evalúa tablas de catálogo y auditoría.
