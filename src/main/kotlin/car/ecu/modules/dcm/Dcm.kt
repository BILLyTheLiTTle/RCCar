package car.ecu.modules.dcm

/* DCM stands for Differential Control Module

 */

interface Dcm {
    var valueOuterFront: Int
    var phi: Double
    var theta: Double

}