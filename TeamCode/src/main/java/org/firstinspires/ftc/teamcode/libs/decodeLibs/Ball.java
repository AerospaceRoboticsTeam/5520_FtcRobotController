package org.firstinspires.ftc.teamcode.libs.decodeLibs;

/** Contains info about artifact stored in magazine. */
public class Ball {
	private final boolean isGreen;

	/** @param isGreen True if the ball is green, false if the ball is purple. */
	public Ball(boolean isGreen){
		this.isGreen = isGreen;
	}

	/** @return True if the ball is green, false if the ball is purple. */
	public boolean getIsGreen() { return isGreen; }
}
