package car.raspi.hardware

import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.RaspiPin

class BcmPins {
    companion object {
        val BCM_02: Pin
            get() = RaspiPin.GPIO_08

        val BCM_03: Pin
            get() = RaspiPin.GPIO_09

        val BCM_04: Pin
            get() = RaspiPin.GPIO_07

        val BCM_05: Pin
            get() = RaspiPin.GPIO_21

        val BCM_06: Pin
            get() = RaspiPin.GPIO_22

        val BCM_07: Pin
            get() = RaspiPin.GPIO_11

        val BCM_08: Pin
            get() = RaspiPin.GPIO_10

        val BCM_09: Pin
            get() = RaspiPin.GPIO_13

        val BCM_10: Pin
            get() = RaspiPin.GPIO_12

        val BCM_11: Pin
            get() = RaspiPin.GPIO_14

        val BCM_12: Pin
            get() = RaspiPin.GPIO_26

        val BCM_13: Pin
            get() = RaspiPin.GPIO_23

        val BCM_14: Pin
            get() = RaspiPin.GPIO_15

        val BCM_15: Pin
            get() = RaspiPin.GPIO_16

        val BCM_16: Pin
            get() = RaspiPin.GPIO_27

        val BCM_17: Pin
            get() = RaspiPin.GPIO_00

        val BCM_18: Pin
            get() = RaspiPin.GPIO_01

        val BCM_19: Pin
            get() = RaspiPin.GPIO_24

        val BCM_20: Pin
            get() = RaspiPin.GPIO_28

        val BCM_21: Pin
            get() = RaspiPin.GPIO_29

        val BCM_22: Pin
            get() = RaspiPin.GPIO_03

        val BCM_23: Pin
            get() = RaspiPin.GPIO_04

        val BCM_24: Pin
            get() = RaspiPin.GPIO_05

        val BCM_25: Pin
            get() = RaspiPin.GPIO_06

        val BCM_26: Pin
            get() = RaspiPin.GPIO_25

        val BCM_27: Pin
            get() = RaspiPin.GPIO_02
    }
}

