interface IMaze
{
    /**
     * Width of grid in which the maze resides.
     * The leftmost coordinate is 0.
     * The rightmost is width() - 1.
     */
    int gridWidth();
    /**
     * Height of grid in which the maze resides.
     * The topmost coordinate is 0.
     * The bottommost is height() - 1.
     */
    int gridHeight();
    /**
     * Is a given x,y coordinate inside the maze?
     * A maze may be non-rectangular, or otherwise not use its entire extents.
     * Any coordinate outside the extents is automatically NOT in the maze.
     * @param x horizontal coordinate in the grid
     * @param y vertical coordinate in the grid
     * @return true if a coordinate is inside the maze
     */
    boolean isInMaze(int x, int y);
    /**
     * Does anything occupy the maze grid at the given coordinates
     * @param x horizontal coordinate in the grid
     * @param y vertical coordinate in the grid
     * @return true if the space is not occupied, and yet still occupiable
     */
    boolean isSpaceEmpty(int x, int y);
    /**
     * Are there any walls between two x,y coordinates?
     * Generally, the coordinates will be adjacent (non-diagonal).
     * The answer is undefined for non-adjacent coordinates.
     * While the intention is that walls are 2-sided, in theory a wall could
     * allow 1-way transit from "here" to "there"
     * @param xHere horizontal coordinate
     * @param yHere
     * @param xThere
     * @param yThere
     * @return
     */
    boolean isWallBetween(int xHere, int yHere, int xThere, int yThere);
    /**
     * Force the maze to re-draw itself (and its contents)
     */
    void repaint();
}