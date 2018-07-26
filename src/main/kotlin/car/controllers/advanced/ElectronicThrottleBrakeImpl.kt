package car.controllers.advanced

import car.controllers.advanced.ecu.CdmImpl
import car.controllers.basic.SetupImpl
import car.controllers.basic.ThrottleBrake
import car.controllers.basic.ThrottleBrakeImpl
import car.server.SetupSystem

object ElectronicThrottleBrakeImpl: ThrottleBrake by ThrottleBrakeImpl{

    override fun throttle(direction: String, value: Int): String {
        // TODO send data to controller (android)

        return if (SetupImpl.handlingAssistanceState ==SetupSystem.ASSISTANCE_FULL) {
            ThrottleBrakeImpl.throttle(direction, CdmImpl.calculateThrottleValue(direction, value))
        }
        else {
            ThrottleBrakeImpl.throttle(direction, value)
        }
    }
}