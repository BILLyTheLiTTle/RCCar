package car.ecu.sensors.distance

interface UltrasonicDistanceMeter {
    val frontDistance: Double
    val rearDistance: Double
}