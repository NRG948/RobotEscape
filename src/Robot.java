import java.awt.*;

public class Robot extends MazeItem
{
    // The variables tracking the current position are inherited from MazeItem.
	// int xPosition, yPosition;
	// Two more variables track what direction it is facing (as a unit vector)
	int xFacing = 1, yFacing = 0;  // Default to pointing right
	// In our coordinate system, x and y are mapped like in
	// high school math, with the positive Y axis pointing up.
	// Not like computer graphics, where the positive Y is down.

    double speed = 0.3;  // in fractions of a second

    Color color;  // the color of this robot

    /**
     * Create a robot and place it in a maze
     * @param m the maze we've been placed in
     * @param x the starting x position
     * @param y the starting y position
     */
    public Robot(MazePanel m, int x, int y)
    {
        super(m);
        setPosition(x, y);
        color = randomColor();
    }

	public void EscapeMaze()
	{
		while (isInMaze())
		{
			turnRight();
			while (isAtWall(xFacing, yFacing))
			{
				turnLeft();
			}
			stepForward();
		}
	}

	public void setFacing(int x, int y)
	{
		xFacing = x;
		yFacing = y;
        update();
	}
	
	public void stepForward()
	{
		xPosition += xFacing;
		yPosition += yFacing;
        update();
	}
	
	public void turnRight()
	{
		int previousX = xFacing;
		xFacing = yFacing;
		yFacing = -previousX;
        update();
	}
	
	public void turnLeft()
	{
		int previousX = xFacing;
		xFacing = -yFacing;
        yFacing = previousX;
        update();
	}

    public void update()
    {
        repaint();  // update what we look like
        
        try
        {
            String t = Thread.currentThread().getName();
            if (!t.startsWith("AWT"))
            {
                Thread.sleep((int)(1000 * speed));  // slow down, so we animate
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    /**
     * Paint this item inside the specified rectangle
     * @param g the Graphics to paint on
     * @param r the rectangle boundaries of the current cell
     */
    public void paint(Graphics g, Rectangle r)
    {
        // A circle colored specifically to this robot
        g.setColor(color);
        g.fillOval(r.x + 4, r.y + 4, r.width - 8, r.height - 8);
        
        // With black triangle nose, pointing the way we're facing
        g.setColor(Color.black);
        int xMid = r.x + r.width / 2;
        int yMid = r.y + r.height / 2;
        int nose = 20;
        int wide = 10;
        if (xFacing != 0)
        {   // facing horizontally
            int[] xPoints = { xMid + xFacing * nose, xMid - xFacing * nose / 4, xMid - xFacing * nose / 4 };
            int[] yPoints = { yMid, yMid + wide, yMid - wide };
            g.fillPolygon(xPoints, yPoints, 3);
        }
        else
        {   // facing vertically
            int[] xPoints = { xMid, xMid + wide, xMid - wide };
            int[] yPoints = { yMid + yFacing * nose, yMid - yFacing * nose / 4, yMid - yFacing * nose / 4 };
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
}