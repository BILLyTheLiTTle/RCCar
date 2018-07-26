package car.controllers.advanced.ecu.sensors

interface UltrasonicDistanceMeter {
    val frontDistance: Double
    val rearDistance: Double
}