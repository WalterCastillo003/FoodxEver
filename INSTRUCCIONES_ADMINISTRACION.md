# Complemento de administración — FoodxEver

Este ZIP contiene los archivos nuevos y modificados para añadir administración de usuarios, roles, categorías, métodos de pago, horarios de recojo y consulta del historial.

## Aplicación de los archivos

1. Haz una copia de seguridad de tu carpeta de proyecto actual.
2. Detén Spring Boot (`Ctrl + C`).
3. Extrae este ZIP directamente dentro de la carpeta raíz del proyecto, es decir, la carpeta donde están `pom.xml`, `mvnw.cmd` y `src`.
4. Permite combinar las carpetas `src` y reemplazar los archivos indicados. El ZIP no contiene `pom.xml`, imágenes, ni `target`.
5. No borres la base de datos ni el repositorio `.git`.

## Crear la primera cuenta administrativa local

En la misma terminal PowerShell, antes de iniciar la aplicación:

```powershell
$env:FOODXEVER_ADMIN_EMAIL = 'admin@foodxever.com'
$securePassword = Read-Host "Contraseña inicial del administrador (mínimo 10 caracteres)" -AsSecureString
$env:FOODXEVER_ADMIN_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

El comando solicita la contraseña sin mostrarla mientras la escribes. No envíes su valor por chat ni lo subas al repositorio. Las variables deben permanecer configuradas en esa misma terminal para que el inicializador pueda crear la cuenta. La cuenta se crea solo si el correo no existe todavía.

## Crear la primera cuenta administrativa en Railway

En el servicio web de FoodxEver (no en el servicio PostgreSQL), añade variables `FOODXEVER_ADMIN_EMAIL` y `FOODXEVER_ADMIN_PASSWORD` con una cuenta nueva y contraseña propia de al menos 10 caracteres. Aplica el despliegue. El inicializador crea el administrador en la base remota si no existe ese correo.

La contraseña no está escrita en el código fuente. Si el correo indicado ya existe en la base de datos con otro rol, el inicializador no lo convierte automáticamente en administrador: elige un correo distinto.

## Comprobaciones

1. Inicia sesión desde `/login` con la cuenta administrativa.
2. Comprueba que aparece **Administración** en el menú.
3. Abre `/administracion`.
4. Prueba las páginas Usuarios, Roles, Categorías, Métodos de pago y Horarios.
5. Abre Historial de reservas y confirma que se muestran movimientos existentes.

El historial es deliberadamente de solo lectura. Las relaciones existentes protegen registros referenciados: no se puede eliminar un usuario vinculado a reservas/reseñas/establecimientos, una categoría vinculada a productos, un método vinculado a pagos o un rol asignado a usuarios. Los roles `ADMIN`, `CONSUMIDOR` y `ESTABLECIMIENTO` son roles base protegidos.

## Rutas añadidas

- `/administracion`
- `/administracion/usuarios`
- `/administracion/roles`
- `/administracion/categorias`
- `/administracion/metodos-pago`
- `/administracion/horarios`
- `/administracion/historial`

Las rutas administrativas están protegidas por un interceptor que exige una sesión iniciada con rol `ADMIN`.
