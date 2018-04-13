package car.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TelemetrySystem{

    /*
        All possible combinations and their meaning:
        wheel = 0 -> do nothing
        wheel = 1 -> Front driver wheel speed
        wheel = 2 -> Front co-driver wheel speed
        wheel = 3 -> Back driver wheel speed
        wheel = 4 -> Back co-driver wheel speed

     */
    @GetMapping("/wheel_speed")
    fun getWheelSpeed(@RequestParam(value = "wheel", defaultValue = "$DO_NOTHING") wheel: Int): String
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
        item = 0 -> do nothing
        item = 1 -> Front driver wheel temperature
        item = 2 -> Front co-driver wheel temperature
        item = 3 -> Back driver wheel temperature
        item = 4 -> Back co-driver wheel temperature
        item = 5 -> Front H-bridge temperature
        item = 6 -> Back H-bridge temperature
        item = 7 -> Battery cables temperature
        item = 8 -> Raspberry Pi temperature

     */
    @GetMapping("/item_temperature")
    fun getItemTemperature(@RequestParam(value = "item", defaultValue = "$DO_NOTHING") item: Int): String
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
        const val DO_NOTHING = 0

        const val FRONT_DRIVER_WHEEL = 1
        const val FRONT_CO_DRIVER_WHEEL = 2
        const val BACK_DRIVER_WHEEL = 3
        const val BACK_CO_DRIVER_WHEEL = 4

        const val FRONT_H_BRIDGE = 5
        const val BACK_H_BRIDGE = 6
        const val BATTERY_CABLES = 7
        const val RASPBERRY_PI = 8
    }

}