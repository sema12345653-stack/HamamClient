# OurMod — Minecraft 1.21.4

Стартовый Fabric-проект для нашего клиента с современным GUI.

## Что уже есть

- Minecraft 1.21.4 + Fabric
- Java 21
- открытие меню на Right Shift
- категории Combat / Movement / Render / Player / Display
- поиск по модулям
- карточки модулей и переключатели
- базовая структура для дальнейшего расширения

## Запуск

1. Установить JDK 21.
2. Открыть проект как Gradle-проект в IntelliJ IDEA.
3. Выполнить `./gradlew runClient` (Windows: `gradlew.bat runClient`).
4. В игре нажать Right Shift.

## Сборка

`./gradlew build`

Готовый jar появится в `build/libs/`.
