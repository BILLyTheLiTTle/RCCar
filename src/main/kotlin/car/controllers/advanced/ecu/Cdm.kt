package car.controllers.advanced.ecu

/* CDM stands for Collision Detection Module

 */

interface Cdm {
    fun measureDistance()

    companion object {
        const val THRESHOLD_DISTANCE = 0.5 // counted in meters
    }

}