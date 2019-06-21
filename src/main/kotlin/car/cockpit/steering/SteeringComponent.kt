package car.cockpit.steering

import car.cockpit.electrics.Electrics
import car.cockpit.engine.SUCCESS
import car.ecu.modules.dcm.Dcm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("Steering Component")
class SteeringComponent: Steering {

    @Autowired
    private lateinit var dcmComponent: Dcm

    @Autowired
    private lateinit var electricsComponent: Electrics

    private val innerFrontDegreesTheta = doubleArrayOf(22.0, 45.0, 52.0, 60.0, 66.0)
    private val outerFrontDegreesPhi = doubleArrayOf(19.9, 37.9, 41.7, 49.2, 47.8)

    override var direction = Turn.STRAIGHT
        protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/
    override var value = STEERING_VALUE_00
            protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/

    override fun turn(direction: Turn, value: Int): String {

        // Inform DCM of the ECU
        when (value) {
            STEERING_VALUE_20 -> {
                dcmComponent.phi = outerFrontDegreesPhi[0]
                dcmComponent.theta = innerFrontDegreesTheta[0]
            }
            STEERING_VALUE_40 -> {
                dcmComponent.phi = outerFrontDegreesPhi[1]
                dcmComponent.theta = innerFrontDegreesTheta[1]
            }
            STEERING_VALUE_60 -> {
                dcmComponent.phi = outerFrontDegreesPhi[2]
                dcmComponent.theta = innerFrontDegreesTheta[2]
            }
            STEERING_VALUE_80 -> {
                dcmComponent.phi = outerFrontDegreesPhi[3]
                dcmComponent.theta = innerFrontDegreesTheta[3]
            }
            STEERING_VALUE_100 -> {
                dcmComponent.phi = outerFrontDegreesPhi[4]
                dcmComponent.theta = innerFrontDegreesTheta[4]
            }
            else -> {
                dcmComponent.phi = 0.00
                dcmComponent.theta = 0.00
            }
        }

        this.direction = when (direction) {
            Turn.RIGHT ->
                // TODO prepare pins for turning right

                 Turn.RIGHT
            Turn.LEFT ->
                // TODO prepare pins for turning left

                Turn.LEFT
            else  -> {
                // TODO prepare pins for turning straight

                // turn off the turn lights
                if (this.direction != Turn.STRAIGHT) {
                    electricsComponent.rightTurnLightsState = false
                    electricsComponent.leftTurnLightsState = false
                }

                Turn.STRAIGHT
            }
        }

        //TODO set value to pins

        this.value = value
        return SUCCESS // or error message from pins
    }

    override fun reset() {
        direction = Turn.STRAIGHT
        value = STEERING_VALUE_00
    }
}