package car.controllers.advanced.ecu

import car.FRONT_REAR_WHEELS_DISTANCE
import car.FRONT_WHEELS_DISTANCE
import car.REAR_CHASSIS_WIDTH

object TcmImpl: Tcm {
    override var valueOuterFront = 0
    override var phi = 0.00
    override var theta = 0.00

    private const val DIFFERENTIAL_STEPS = 4

    private val valueInnerFront
        get() = ( valueOuterFront /
                ( ( 2 * Math.PI * ( ( FRONT_REAR_WHEELS_DISTANCE / Math.sin(Math.toRadians(phi) ) +
                        ( (FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) / 2) ) ) /
                        ( 2 * Math.PI * ( (FRONT_REAR_WHEELS_DISTANCE /Math.sin(Math.toRadians(theta) ) -
                                ((FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) /2) ) ) ) ) )
                ).toInt()
    private val valueInnerRear
        get() = ( valueOuterFront /
            ( ( 2 * Math.PI * ( (FRONT_REAR_WHEELS_DISTANCE/ Math.sin(Math.toRadians(phi) ) +
                    ( (FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) / 2) ) ) /
                    ( 2 * Math.PI * ( (FRONT_REAR_WHEELS_DISTANCE /Math.tan(Math.toRadians(theta) ) -
                            ((FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) /2) ) ) ) ) )
            ).toInt()
    private val valueOuterRear
        get() = ( valueOuterFront /
            ( ( 2 * Math.PI * ( (FRONT_REAR_WHEELS_DISTANCE/ Math.sin(Math.toRadians(phi) ) +
                    ( (FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) / 2) ) ) /
                    ( 2 * Math.PI * ( (FRONT_REAR_WHEELS_DISTANCE /Math.tan(Math.toRadians(phi) ) +
                            ((FRONT_WHEELS_DISTANCE - REAR_CHASSIS_WIDTH) /2) ) ) ) ) )
            ).toInt()

    private val vifSteps
        get() = (valueOuterFront - valueInnerFront) / DIFFERENTIAL_STEPS
    private val virSteps
        get() = (valueOuterRear - valueInnerRear) / DIFFERENTIAL_STEPS
    //private val vorSteps = (valueOuterFront - valueOuterRear) / DIFFERENTIAL_STEPS

    ////////////////////
    /* If the following values don't work well as PWM values
        then I have to create PWM in hand and store them in tables
     */
    val frontOpenDiffValues
        get() = intArrayOf(valueInnerFront, valueOuterFront)
    val rearOpenDiffValues
        get() = intArrayOf(valueInnerRear, valueOuterRear)
    val frontMedi0DiffValues
        get() = intArrayOf(valueInnerFront + vifSteps, valueOuterFront)
    val rearMedi0DiffValues
        get() = intArrayOf(valueInnerRear + virSteps, valueOuterRear)
    val frontMedi1DiffValues
        get() = intArrayOf(valueInnerFront + 2 * vifSteps, valueOuterFront)
    val rearMedi1DiffValues
        get() = intArrayOf(valueInnerRear + 2 * virSteps, valueOuterRear)
    val frontMedi2DiffValues
        get() = intArrayOf(valueInnerFront + 3 * vifSteps, valueOuterFront)
    val rearMedi2DiffValues
        get() = intArrayOf(valueInnerRear + 3 * virSteps, valueOuterRear)
    val frontLockedDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    val rearLockedDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    val frontAutoDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    val rearAutoDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    ////////////////////
}