import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class MazePanel extends JPanel implements IMaze, MouseListener, MouseMotionListener
{
    private static int padding = 5;
    private static int wallSize = 6;
    private static int cellSize = 50;
    private static int bevelSize = 4;

    boolean[][] horzWalls;
    boolean[][] vertWalls;

    LinkedList<MazeItem> items = new LinkedList<MazeItem>();

    public MazePanel(int maxWidth, int maxHeight)
    {
        horzWalls = new boolean[maxWidth][maxHeight + 1];
        vertWalls = new boolean[maxWidth + 1][maxHeight];

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int gridWidth()
    {
        return vertWalls[0].length;
    }

    public int gridHeight()
    {
        return horzWalls.length;
    }

    public boolean isInMaze(int x, int y)
    {
        if (x < 0 || x >= gridWidth() ||  y < 0 || y >= gridHeight())
            return false;  // Fully outside grid
        // If there are no walls between x,y and any 2 edges, then you're outside
        // That leaves a possibility that there is diagonal openness, but the robot doesn't see those
        int left = 0, right = 0, above = 0, below = 0;
        for (int x2 = x; x2 >= 0 && left == 0; x2--)
            left = isWallBetween(x2, y, x2-1, y) ? 1 : 0;
        for (int x2 = x; x2 < gridWidth() && right == 0; x2++)
            right = isWallBetween(x2, y, x2+1, y) ? 1 : 0;
        for (int y2 = y; y2 >= 0 && above == 0; y2--)
            above = isWallBetween(x, y2, x, y2-1) ? 1 : 0;
        for (int y2 = y; y2 < gridHeight() && below == 0; y2++)
            below = isWallBetween(x, y2, x, y2+1) ? 1 : 0;

        return (above + below + left + right) >= 3;
    }

    public boolean isSpaceEmpty(int x, int y)
    {
        if (x < 0 || x >= gridWidth() ||  y < 0 || y >= gridHeight())
            return false;  // Fully outside grid
        return itemAt(x, y) == null;
    }

    public boolean isWallBetween(int xHere, int yHere, int xThere, int yThere)
    {
        if (xThere < xHere && vertWalls[xHere][yHere])  // wall to left
            return true;
        if (xThere > xHere && vertWalls[xHere + 1][yHere])  // wall to right
            return true;
        if (yThere < yHere && horzWalls[xHere][yHere])  // wall above
            return true;
        if (yThere > yHere && horzWalls[xHere][yHere + 1])  // wall below
            return true;
        return false;
    }

    public MazeItem itemAt(int x, int y)
    {
        for (MazeItem item : items)
        {
            if (item.xPosition == x && item.yPosition == y)
                return item;
        }
        return null;
    }

    public void addItem(MazeItem item)
    {
        items.add(item);
        repaint();
    }

    /**
     * Given an x or y grid coordinate, return its pixel coordinate
     */
    private static int pix(int coord)
    {
        return padding + coord * (wallSize + cellSize);
    }

    /**
     * Given a client x or y pixel, return the nearest grid corner
     */
    private static int nearestCorner(int pix)
    {
        float f = pix - padding - wallSize / 2;
        return Math.round(f / (wallSize + cellSize));
    }

    /**
     * Given a client x or y pixel, return the nearest grid cell middle
     */
    private static int nearestCell(int pix)
    {
        float f = pix - padding - wallSize / 2 - cellSize / 2;
        return Math.round(f / (wallSize + cellSize));
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(pix(gridWidth()) + wallSize + padding,
            pix(gridHeight()) + wallSize + padding);
    }


    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        // Leave room for right/bottom-most walls, plus 1 pixel padding all around
        g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);

        int x, y;
        // Draw the cells
        for (x = 0; x < gridWidth(); x++)
        {
            for (y = 0; y < gridHeight(); y++)
            {
                drawCell(g, x, y);
            }
        }

        // Overlay the walls between the cell edges
        for (x = 0; x < gridWidth(); x++)
        {
            for (y = 0; y < gridHeight(); y++)
            {
                if (horzWalls[x][y])
                    drawWall(g, x, y, 1, 0);
                if (vertWalls[x][y])
                    drawWall(g, x, y, 0, 1);
            }
            if (horzWalls[x][y])  // bottom-most wall
                drawWall(g, x, y, 1, 0);
        }
        for (y = 0; y < gridHeight(); y++)  // right-most wall
        {
            if (vertWalls[x][y])
                drawWall(g, x, y, 0, 1);
        }

        // Draw any contained items
        for (MazeItem item : items)
        {
            drawItem(item, g);
        }
    }

    private void drawItem(MazeItem item, Graphics g)
    {
        Rectangle r = new Rectangle(pix(item.xPosition) + wallSize / 2, 
            pix(item.yPosition) + wallSize / 2,
            wallSize + cellSize,
            wallSize + cellSize);
        item.paint(g, r);
    }

    private static Color highlightColor = new Color(204, 204, 204);
    private static Color shadowColor = new Color(102, 102, 102);
    private static Color fillColor = new Color(153, 153, 153);
    private static Color wallColor = Color.red;

    private void drawCell(Graphics g, int x, int y)
    {
        x = pix(x) + wallSize / 2;
        y = pix(y) + wallSize / 2;
        int size = wallSize + cellSize;
        g.setColor(fillColor);
        g.fillRect(x + bevelSize, y + bevelSize, size - 2 * bevelSize, size - 2 * bevelSize);
        for (int i = 0; i < bevelSize; i++)
        {
            g.setColor(highlightColor);
            g.drawLine(x + i, y + size - i, x + i, y + i);
            g.drawLine(x + i, y + i, x + size - i, y + i);
            g.setColor(shadowColor);
            g.drawLine(x + i, y + size - i - 1, x + size - i - 1, y + size - i - 1);
            g.drawLine(x + size - i - 1, y + size - i - 1, x + size - i - 1, y + i);
        }
    }

    private void drawWall(Graphics g, int x, int y, int dx, int dy)
    {
        g.setColor(wallColor);
        g.fillRect(pix(x), pix(y), 
            wallSize + dx * (cellSize + wallSize), 
            wallSize + dy * (cellSize + wallSize));
    }

    public enum Mode
    {
        None,
        Draw,
        Erase,
        Place,
        Run
    }

    Mode mode = Mode.None;

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {   
    }

    public void mouseExited(MouseEvent e)
    {   
    }

    boolean mouseDown = false;
    int xDown, yDown;
    public void mousePressed(MouseEvent e)
    {
        mouseDown = true;
        if (mode == Mode.Draw)
        {
            xDown = nearestCorner(e.getX());
            yDown = nearestCorner(e.getY());
        }
        else if (mode == Mode.Erase)
        {
            xDown = nearestCell(e.getX());
            yDown = nearestCell(e.getY());
        }
        else
        {
            int x = nearestCell(e.getX());
            int y = nearestCell(e.getY());
            if (x < 0 || x >= gridWidth() || y < 0 || y >= gridHeight())
                return;

            Robot robot = (Robot)itemAt(x, y);

            if (mode == Mode.Place)
            {
                if (robot != null)
                {
                    // Rotate this robot in place
                    robot.turnRight();
                }
                else
                {
                    // Create a new robot here
                    robot = new Robot(this, x, y);
                    items.add(robot);
                }
            }
            else if (mode == Mode.Run)
            {
                if (robot == null)
                    return;
                new Thread(new RobotRunner(robot)).start();
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        mouseDown = false;
    }

    public void mouseMoved(MouseEvent e)
    {
        if (mouseDown)
            mouseDragged(e);
    }

    public void mouseDragged(MouseEvent e)
    {
        if (!mouseDown || mode == Mode.None)
            return;
        if (mode == Mode.Draw)
        {
            int xDrag = nearestCorner(e.getX());
            int yDrag = nearestCorner(e.getY());
            if (xDrag < 0 || xDrag > gridWidth() || yDrag < 0 || yDrag > gridHeight())
                return;  // Dragging outside the grid
            int dx = xDrag - xDown;
            int dy = yDrag - yDown;
            if (dx * dy != 0 || Math.abs(dx + dy) != 1)
                return;  // Only move 1 corner at a time
            if (dx == 1)
                horzWalls[xDown][yDown] = true;
            else if (dx == -1)
                horzWalls[xDown - 1][yDown] = true;
            else if (dy == 1)
                vertWalls[xDown][yDown] = true;
            else if (dy == -1)
                vertWalls[xDown][yDown - 1] = true;
            // Subsequent dragging is from here
            xDown = xDrag;
            yDown = yDrag;
            repaint();  // Force repaint
        }
        else if (mode == Mode.Erase)
        {
            int xDrag = nearestCell(e.getX());
            int yDrag = nearestCell(e.getY());
            int dx = xDrag - xDown;
            int dy = yDrag - yDown;
            if (dx * dy == 0 && Math.abs(dx + dy) == 1)
            {  // Only move 1 cell at a time
                if (xDown >= 0 && xDown < gridWidth() && yDown >= 0 && yDown < gridHeight())
                {  // Can't start out-of-bounds (can end there)
                    if (dx == 1)
                        vertWalls[xDown + 1][yDown] = false;
                    else if (dx == -1)
                        vertWalls[xDown][yDown] = false;
                    else if (dy == 1)
                        horzWalls[xDown][yDown + 1] = false;
                    else if (dy == -1)
                        horzWalls[xDown][yDown] = false;
                }
            }
            // Subsequent dragging is from here
            xDown = xDrag;
            yDown = yDrag;
            repaint();  // Force repaint
        }
    }

    class RobotRunner implements Runnable
    {
        Robot robot;
        public RobotRunner(Robot r)
        {
            robot = r;
        }
        public void run()
        {
            robot.EscapeMaze();
        }
    }

    /**
     * Required of JPanel, because it is Serializable
     */
    private static final long serialVersionUID = 1L;

}