package car.ecu.modules.dcm

import car.FRONT_REAR_WHEELS_DISTANCE
import car.FRONT_WHEELS_DISTANCE
import car.REAR_CHASSIS_WIDTH
import org.springframework.stereotype.Component

@Component("Differential Control Module Component")
class DcmComponent: Dcm {
    override var valueOuterFront = 0
    override var phi = 0.00
    override var theta = 0.00

    private val differentialStates = 5
    private val differentialSteps = differentialStates - 1

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
        get() = (valueOuterFront - valueInnerFront) / differentialSteps
    private val virSteps
        get() = (valueOuterRear - valueInnerRear) / differentialSteps
    //private val vorSteps = (valueOuterFront - valueOuterRear) / differentialSteps

    ////////////////////
    /* If the following values don't work well as PWM values
        then I have to create PWM in hand and store them in tables
     */
    override val frontOpenDiffValues
        get() = intArrayOf(valueInnerFront, valueOuterFront)
    override val rearOpenDiffValues
        get() = intArrayOf(valueInnerRear, valueOuterRear)

    override val frontMedi0DiffValues
        get() = intArrayOf(
            valueInnerFront + vifSteps,
            valueOuterFront
        )
    override val rearMedi0DiffValues
        get() = intArrayOf(
            valueInnerRear + virSteps,
            valueOuterRear
        )

    override val frontMedi1DiffValues
        get() = intArrayOf(
            valueInnerFront + 2 * vifSteps,
            valueOuterFront
        )
    override val rearMedi1DiffValues
        get() = intArrayOf(
            valueInnerRear + 2 * virSteps,
            valueOuterRear
        )

    override val frontMedi2DiffValues
        get() = intArrayOf(
            valueInnerFront + 3 * vifSteps,
            valueOuterFront
        )
    override val rearMedi2DiffValues
        get() = intArrayOf(
            valueInnerRear + 3 * virSteps,
            valueOuterRear
        )

    override val frontLockedDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    override val rearLockedDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    override val frontAutoDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    override val rearAutoDiffValues
        get() = intArrayOf(valueOuterFront, valueOuterFront)
    ////////////////////
}