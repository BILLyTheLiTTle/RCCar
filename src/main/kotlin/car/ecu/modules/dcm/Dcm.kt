package car.ecu.modules.dcm

/* DCM stands for Differential Control Module

 */

interface Dcm {
    var valueOuterFront: Int
    var phi: Double
    var theta: Double

    val frontOpenDiffValues: IntArray
    val rearOpenDiffValues: IntArray
    val frontMedi0DiffValues: IntArray
    val rearMedi0DiffValues: IntArray
    val frontMedi1DiffValues: IntArray
    val rearMedi1DiffValues: IntArray
    val frontMedi2DiffValues: IntArray
    val rearMedi2DiffValues: IntArray
    val frontLockedDiffValues: IntArray
    val rearLockedDiffValues: IntArray
    val frontAutoDiffValues: IntArray
    val rearAutoDiffValues: IntArray
}

const val DIFFERENTIAL_CONTROL_MODULE = "DCM"