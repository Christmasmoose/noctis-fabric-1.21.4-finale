# Noctis Masks — Fabric 1.21.4

**Drag-and-drop usage:** Fabric loads `.jar` files from `mods/`. This project is ready to build into a single `.jar`.
Compile once, then drop the generated JAR into both your server's and clients' `mods/` folders.

## Build
- Java 21 required
- Run: `./gradlew build`
- Output JAR: `build/libs/noctis-fabric-0.1.0.jar`

## Fog Ability
- Default key: `R`
- Server authoritative; 3-minute cooldown
- Grants 5s Invisibility + Resistance and plays a sound

## Notes
- Mixins included for damage tuning and Glass projectile reflection (40%).
- Placeholder 16x16 item textures are included; replace any file in `assets/noctis/textures/item/`.

## Zero-setup build (no Gradle install)
1. Create a new GitHub repo and upload this folder.
2. Go to **Actions** tab → run **Build Noctis (Fabric 1.21.4)**.
3. When it finishes, download the artifact `noctis-fabric-jar` → drop the `.jar` into your server and clients' `mods/` folders.
