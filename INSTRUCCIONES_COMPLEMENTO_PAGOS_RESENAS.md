# Complemento de FoodxEver: módulos de pagos y reseñas

Este ZIP contiene únicamente los archivos nuevos o modificados para añadir los CRUD de pagos y reseñas al proyecto FoodxEver del tercer avance.

## Cómo aplicarlo en Windows / VS Code

1. Conserva una copia de seguridad de tu proyecto actual.
2. Descomprime este ZIP dentro de la carpeta del proyecto que contiene `pom.xml`, `mvnw.cmd` y `src`. En la instalación descrita por el usuario, esa carpeta es `FoodxEver-Spring\foodxever`, no la carpeta exterior `FoodxEver-Spring`.
3. Permite combinar las carpetas y reemplazar los archivos existentes cuando Windows lo solicite. No reemplaza todo el proyecto; solo actualiza los archivos que aparecen en el ZIP.
4. Inicia FoodxEver desde la carpeta que contiene `mvnw.cmd`:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. Abre estas páginas:
   - `http://localhost:8080/pagos`
   - `http://localhost:8080/resenas`

## Qué se agregó

- CRUD de pagos, vinculados a una reserva y a un método de pago.
- El monto del pago se deriva del total de la reserva en el servidor.
- CRUD de reseñas, vinculadas a usuario y establecimiento, con reserva opcional.
- Validación de calificación entre una y cinco estrellas y protección contra dos reseñas asociadas a la misma reserva.
- Acceso a ambas páginas desde el menú “Más gestión”.

## Verificación

El código Java añadido se comprobó mediante compilación aislada contra stubs de las APIs usadas y las plantillas HTML se parsearon. No se pudo completar una compilación Maven real en el entorno de preparación porque no se pudo descargar Maven desde el repositorio; ejecuta `mvnw.cmd spring-boot:run` con el JDK 25 de tu equipo y revisa cualquier error de arranque antes de publicar cambios en GitHub o Railway.

No es necesario borrar ni recrear tu base de datos. Conserva las variables de conexión local y no subas contraseñas a GitHub.
