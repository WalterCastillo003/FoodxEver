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

La interfaz incorpora CRUD para diez tablas: usuarios, roles, establecimientos, categorías, productos, reservas, métodos de pago, pagos, reseñas y horarios de recojo. `historial_reservas` se consulta en modo de solo lectura porque es una bitácora automática de auditoría. Las eliminaciones de registros vinculados se bloquean para preservar las claves foráneas y los datos históricos.

## F. Administración de las 10 tablas CRUD

Antes de probar los enlaces administrativos, configura `FOODXEVER_ADMIN_EMAIL` y `FOODXEVER_ADMIN_PASSWORD` con valores propios de al menos 10 caracteres para la contraseña, tanto en local como en el servicio web de Railway. El primer administrador se crea solo si el correo configurado todavía no existe.

- [ ] Inicio sesión con la cuenta `ADMIN` y aparece el menú Administración.
- [ ] `/administracion/usuarios`: crear, editar y eliminar un usuario de prueba sin relaciones. Verificar que no se muestra ni se almacena la contraseña en texto plano.
- [ ] `/administracion/roles`: crear, editar y eliminar un rol de prueba. Los tres roles del sistema están protegidos.
- [ ] `/administracion/categorias`: crear, editar y eliminar una categoría de prueba que no tenga productos asociados.
- [ ] `/administracion/metodos-pago`: crear, editar y eliminar un método de prueba que no tenga pagos asociados.
- [ ] `/administracion/horarios`: crear, editar y eliminar un horario de prueba. Verificar que la hora de fin sea posterior a la de inicio.
- [ ] `/administracion/historial`: consultar movimientos existentes y filtrar por reserva. No permite modificar la bitácora.
- [ ] Verificar que las restricciones impidan eliminar roles, categorías, métodos o usuarios que tengan registros relacionados.

Diez tablas tienen CRUD desde la interfaz: roles, usuarios, establecimientos, categorías, productos, reservas, métodos de pago, pagos, reseñas y horarios. `historial_reservas` es de solo lectura porque registra automáticamente los cambios de estado.
