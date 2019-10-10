import java.awt.*;
import java.util.*;

/**
 * An item placed in a maze
 */
abstract class MazeItem
{
    public IMaze maze;

    /**
     * The current coordinates of the item in the maze grid
     */
    public int xPosition, yPosition;

    public MazeItem(IMaze m)
    {
        maze = m;
    }

    public void setPosition(int x, int y)
	{
		xPosition = x;
        yPosition = y;
        repaint();
    }
    
    Random rand = new Random();

    /**
     * Semi-random color, using the robot's starting location
     */
    public Color randomColor()
    {
        return Color.getHSBColor(rand.nextFloat(), 1, 0.9f);
    }

    /**
     * Paint this item in the specified rectangle
     * @param g the Graphics to paint on
     * @param r the rectangle boundaries of the current cell
     */
    public abstract void paint(Graphics g, Rectangle r);

    /**
     * Is this item currently inside the maze walls?
     */
    public boolean isInMaze()
    {
        return maze.isInMaze(xPosition, yPosition);
    }

    /**
     * Is there a wall adjacent to where we're positioned,
     * in the direciton we're facing (immediately in front of us)?
     * @param xFacing the delta-x we are facing
     * @param yFacing the delta-y we are facing
     * @return true if a wall is immedately in front of us
     */
    public boolean isAtWall(int xFacing, int yFacing)
    {
        return maze.isWallBetween(xPosition, yPosition, xPosition + xFacing, yPosition + yFacing);
    }

    public void repaint()
    {
        maze.repaint();
    }
}