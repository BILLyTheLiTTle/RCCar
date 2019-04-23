package car.cockpit.dashboard.lights.warning.temperatures

enum class ThermometerDevice(val id: String) {
    BATTERIES("batteries_temp"),
    H_BRIDGE_FRONT("h_bridge_front_temp"),
    H_BRIDGE_REAR("h_bridge_rear_temp"),
    MOTOR_FRONT_LEFT("motor_front_left_temp"),
    MOTOR_FRONT_RIGHT("motor_front_right_temp"),
    MOTOR_REAR_LEFT("motor_rear_left_temp"),
    MOTOR_REAR_RIGHT("motor_rear_right_temp"),
    RASPBERRY_PI("raspberry_pi_temp"),
    SHIFT_REGISTERS("shift_registers_temp")
}