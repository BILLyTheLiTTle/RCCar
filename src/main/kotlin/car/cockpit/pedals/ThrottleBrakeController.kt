package car.cockpit.pedals

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ThrottleBrakeController{

    @Autowired
    private lateinit var service: ThrottleBrakeService

    @GetMapping("/set_throttle_brake_system")
    fun setThrottleBrakeAction(@RequestParam(value = "id", defaultValue = "-1") id: Long,
                               @RequestParam(value = "action", defaultValue = Motion.DEFAULT)
                               action: String,
                               @RequestParam(value = "value", defaultValue =  "0") value: Int): String =
        service.setThrottleBrakeAction(id, action, value)

    @GetMapping("/get_parking_brake_state")
    fun getParkingBrakeState() = service.getParkingBrakeState()

    @GetMapping("/get_handbrake_state")
    fun getHandbrakeState() = service.getHandbrakeState()

    @GetMapping("/get_motion_state")
    fun getMotionState() = service.getMotionState()

}