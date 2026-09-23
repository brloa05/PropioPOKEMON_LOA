# ⚔️ POOBKemon

Simulador de batallas Pokémon por turnos escrito en **Java** puro con **Swing**, desarrollado como proyecto de **Programación Orientada a Objetos**. Implementa el motor de batalla completo (tipos, movimientos, objetos, IA de entrenadores) y una interfaz gráfica animada con selección de equipo, movimientos y modos de juego.

## Índice

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Modos de juego](#modos-de-juego)
- [Stack](#stack)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Cómo ejecutarlo](#cómo-ejecutarlo)
- [Autor](#autor)

## Características

- **Motor de batalla por turnos** con Pokémon de uno o dos tipos (`PokemonUnTipo`, `PokemonDosTipos`), sistema de vida, y tres categorías de movimientos: físicos, especiales y de estado (`Fisico`, `Especial`, `DeEstado`), cada uno con poder, precisión, PP y efectos secundarios configurables.
- **Objetos de combate**: pociones, superpociones, hiperpociones y revivir (`Potion`, `SuperPotion`, `HyperPotion`, `Revive`), con reglas de cantidad máxima por entrenador.
- **Entrenadores controlados por IA** con distintas estrategias de decisión: `AttackingTrainer` (ofensivo), `DefensiveTrainer` (defensivo), `ChangingTrainer` (rotación de equipo) y `ExpertTrainer` (estrategia avanzada), además del entrenador humano (`EntrenadorPlayer`).
- **Modo Supervivencia**, con selección aleatoria de equipos y movimientos balanceada por tipo.
- **Datos configurables por archivo de texto** (`resources/pokemones.txt`, `movimientos.txt`, `items.txt`), lo que permite añadir Pokémon, movimientos y objetos sin recompilar el motor.
- **Interfaz gráfica en Swing** con animaciones GIF de batalla, paneles de selección de Pokémon/movimientos y menús de modalidad.

## Arquitectura

El proyecto sigue una separación clásica **dominio / presentación**:

- `domain/` — lógica de juego pura: Pokémon, movimientos, objetos, entrenadores, modalidades de juego y reglas de negocio, independiente de la interfaz gráfica.
- `presentation/` — paneles Swing (`JPanel`) para cada pantalla del flujo: menú principal, selección de modalidad (PvP / PvM / MvM), selección de Pokémon y movimientos, y la pantalla de batalla con animaciones.

Los entrenadores se modelan con **herencia y polimorfismo** (`Entrenador` abstracto → estrategias concretas), y las modalidades de juego (`ModalidadJuego` abstracto → `ModalidadPvP`, `ModalidadPvM`, `ModalidadMvM`) encapsulan las reglas específicas de cada modo (quién elige Pokémon, quién elige movimientos, etc.).

## Modos de juego

| Modo | Descripción |
|---|---|
| **PvP** | Jugador contra jugador, ambos seleccionan su equipo y movimientos manualmente. |
| **PvM** | Jugador contra una máquina controlada por una de las estrategias de IA. |
| **MvM** | Dos entrenadores IA se enfrentan entre sí (modo espectador). |

## Stack

- Java (módulo `POOBKemon`, `requires java.desktop`)
- Swing / AWT para la interfaz gráfica
- Reflection (`java.lang.reflect`) para instanciar objetos dinámicamente desde configuración
- Datos de juego en archivos de texto plano (sin base de datos)

## Estructura del repositorio

```
.
├── src/
│   ├── domain/          # Motor de batalla: Pokémon, movimientos, ítems, entrenadores, modalidades
│   ├── presentation/     # Interfaz gráfica Swing (menús, selección, batalla)
│   └── module-info.java
└── resources/            # Sprites, GIFs de batalla, íconos de ítems y datos de juego (.txt)
```

## Cómo ejecutarlo

El proyecto está configurado como proyecto de **Eclipse** (`.project` / `.classpath`).

1. Importar la carpeta en Eclipse como *Existing Java Project*.
2. Ejecutar `presentation/PokemonGUI.java` (contiene el `main`).

También puede compilarse y ejecutarse manualmente:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin:resources presentation.PokemonGUI
```

> En Windows, reemplazar `:` por `;` en el classpath.

## Autor

Brayan Loaiza Leal
