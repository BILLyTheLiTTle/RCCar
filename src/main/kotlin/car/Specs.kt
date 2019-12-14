package car

// --- Use SI values ---

const val FRONT_WHEELS_DISTANCE = 0.4 // "a" parameter at the differential formulas
const val FRONT_REAR_WHEELS_DISTANCE = 0.7 // "b" parameter at the differential formulas
const val REAR_CHASSIS_WIDTH = 0.3 // "c" parameter at the differential formulas

const val MAX_SERVO_HARDWARE_VALUE = 235 // actual max value is 250
const val MID_SERVO_HARDWARE_VALUE = 150
const val MIN_SERVO_HARDWARE_VALUE = 65 // actual min value is 50

const val THETA_ANGLE_00 = 0.0    // "θ" angle at the differential formulas
const val THETA_ANGLE_20 = 22.0
const val THETA_ANGLE_40 = 45.0
const val THETA_ANGLE_60 = 52.0
const val THETA_ANGLE_80 = 60.0
const val THETA_ANGLE_100 = 66.0
const val PHI_ANGLE_00 = 0.0      // "φ" angle at the differential formulas
const val PHI_ANGLE_20 = 19.9
const val PHI_ANGLE_40 = 37.9
const val PHI_ANGLE_60 = 41.7
const val PHI_ANGLE_80 = 49.2
const val PHI_ANGLE_100 = 47.8

// Speed limiter percentages
const val NO_SPEED_PERCENTAGE = 0.00
const val SLOW_SPEED_1_PERCENTAGE = 0.20
const val SLOW_SPEED_2_PERCENTAGE = 0.40
const val MEDIUM_SPEED_1_PERCENTAGE = 0.60
const val MEDIUM_SPEED_2_PERCENTAGE = 0.70
const val FAST_SPEED_1_PERCENTAGE = 0.80
const val FAST_SPEED_2_PERCENTAGE = 0.90
const val FULL_SPEED_PERCENTAGE = 1.00

// Collision Detection values
const val HIGH_SPEED_THRESHOLD_DISTANCE = 0.8
const val HIGH_SPEED_THRESHOLD_VALUE = 85
const val MEDIUM_SPEED_THRESHOLD_DISTANCE = 0.5
const val MEDIUM_SPEED_THRESHOLD_VALUE = 60
const val LOW_SPEED_THRESHOLD_DISTANCE = 0.3
const val LOW_SPEED_THRESHOLD_VALUE = 40
const val NO_SPEED_THRESHOLD_DISTANCE = 0.1