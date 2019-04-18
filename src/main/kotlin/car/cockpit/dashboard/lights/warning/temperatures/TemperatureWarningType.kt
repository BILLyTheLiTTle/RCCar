package car.cockpit.dashboard.lights.warning.temperatures

import car.cockpit.engine.EMPTY_STRING

enum class TemperatureWarningType(val id: String) {
    NOTHING(EMPTY_STRING),
    UNCHANGED("unchanged"),
    NORMAL("normal"),
    MEDIUM("medium"),
    HIGH("high")
}