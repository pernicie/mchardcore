# Hardcore Reset (Fabric 26.1.2)

Añade a la pantalla de muerte un botón "Respawnear (mundo nuevo)".
Junto a él siguen los botones de vanilla (Espectar y Menú principal).

## Conseguir el .jar (sin instalar nada)
1. Crea un repositorio en GitHub y sube TODO el contenido de esta carpeta
   (incluida la carpeta oculta `.github`). Con git:
       git init && git add . && git commit -m "mod"
       git branch -M main
       git remote add origin https://github.com/TU_USUARIO/hardcorereset.git
       git push -u origin main
2. Pestaña **Actions** -> abre la ejecución "build" -> abajo, **Artifacts** -> descarga.
3. Dentro del zip usa el .jar SIN "-sources" en el nombre.
4. Ponlo en la carpeta `mods` con Fabric Loader instalado para 26.1.2.

Requiere Java 25 (lo trae el launcher oficial).
