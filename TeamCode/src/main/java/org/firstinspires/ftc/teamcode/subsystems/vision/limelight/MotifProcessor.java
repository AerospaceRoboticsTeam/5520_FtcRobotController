package org.firstinspires.ftc.teamcode.subsystems.vision.limelight;

import org.firstinspires.ftc.teamcode.utils.Limelight_Behavior;
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessorJava;


public class MotifProcessor {

	 private LimelightProcessorJava limelightProcessor;

	 public MotifProcessor(LimelightProcessorJava limelightProcessor) {
	        this.limelightProcessor = limelightProcessor;
	 }
	public double getMotif(){
		double[] fiducials = limelightProcessor.getFiducials();
		if (fiducials.length > 0) {
			return fiducials[0]; // Return the ID of the first detected fiducial
		} else {
			return -1; // Indicate no fiducials detected
		}
	}

	public String getPatternString(){
		double motifID = getMotif();
		String pattern;
		if (motifID==21){
			return pattern = "GPP";
		} else if (motifID==22){
			return pattern = "PGP";
		} else if (motifID==23){
			return pattern = "PPG";
		} else {
			return pattern = "No valid motif detected";
		}
	}

}
