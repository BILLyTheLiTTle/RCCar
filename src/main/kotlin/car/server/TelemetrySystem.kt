package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TelemetrySystem{

    /*
        All possible combinations and their meaning:
        wheel = 1 -> Front driver wheel speed
        wheel = 2 -> Front co-driver wheel speed
        wheel = 3 -> Back driver wheel speed
        wheel = 4 -> Back co-driver wheel speed

     */
    @GetMapping("/wheel_speed")
    fun getWheelSpeed(@RequestParam(value = "wheel", defaultValue = "0") wheel: Int): String
    {

        //TODO add function for the hardware
        //Synchronization maybe is useless here cuz I just read
        synchronized(this){
            Thread.sleep(1000)
            println("The $wheel is moving with speed")
        }

        return "The $wheel is moving with speed"
    }

    @GetMapping("/vehicle_speed")
    fun getVehicleSpeed(): String
    {

        //TODO add function for the hardware
        //Synchronization maybe is useless here cuz I just read
        synchronized(this){
            Thread.sleep(1000)
            println("The vehicle is moving with speed")
        }

        return "The vehicle is moving with speed"
    }

    /*
        All possible combinations and their meaning:
        wheel = 1 -> Front driver wheel speed
        wheel = 2 -> Front co-driver wheel speed
        wheel = 3 -> Back driver wheel speed
        wheel = 4 -> Back co-driver wheel speed

     */
    @GetMapping("/item_temperature")
    fun getItemTemperature(@RequestParam(value = "item", defaultValue = "0") item: Int): String
    {

        //TODO add function for the hardware
        //Synchronization maybe is useless here cuz I just read
        synchronized(this){
            Thread.sleep(1000)
            println("The $item has oC temperature")
        }

        return "The $item has oC temperature"
    }

    companion object {
        const val DIRECTION_FORWARD = 1
        const val DIRECTION_BACKWARD = -1
        const val DIRECTION_STILL = 0
    }

}