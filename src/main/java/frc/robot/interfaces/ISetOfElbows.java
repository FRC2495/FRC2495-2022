package frc.robot.interfaces;


public interface ISetOfElbows {

	public enum Position {
		CLOSED, // closed
		OPEN, // opend
		UNKNOWN;
	}	

	public void setPosition(Position pos);	
	
	public Position getPosition();
}
