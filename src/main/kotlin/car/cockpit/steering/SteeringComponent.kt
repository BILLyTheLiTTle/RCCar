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

    override var direction = Turn.STRAIGHT
        protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/
    override var value = SteeringValues.VALUE_00
            protected set
        /*private set -
            achieved by using val in the interface and use the interface instead of this class at the outside*/

    override fun turn(direction: Turn, value: SteeringValues): String {

        // Inform DCM of the ECU
        dcmComponent.phi = value.anglePhi
        dcmComponent.theta = value.angleTheta

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
        value = SteeringValues.VALUE_00
    }
}