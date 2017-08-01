package cube;

import java.util.Observable;

import static java.lang.System.arraycopy;

/** Models an instance of the Cube puzzle: a cube with color on some sides
 *  sitting on a cell of a square grid, some of whose cells are colored.
 *  Any object may register to observe this model, using the (inherited)
 *  addObserver method.  The model notifies observers whenever it is modified.
 *  @author P. N. Hilfinger
 */
public class CubeModel extends Observable {
    int initSide=4;
    int initRow0=0;
    int initCol0=0;
    boolean[][] paintedGrid=new boolean[initSide][initSide];
    boolean[] paintedFace=new boolean[6];
    int initMove=0;

    /** A blank cube puzzle of size 4. */
    public CubeModel() {
        initialize(initSide,initRow0,initCol0,paintedGrid,paintedFace);
    }

    /** A copy of CUBE. */
    public CubeModel(CubeModel cube) {
        initialize(cube);
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c], and
     *  with face k painted iff FACEPAINTED[k] (see isPaintedFace).
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     *    * FACEPAINTED has length 6.
     */
    public void initialize(int side, int row0, int col0, boolean[][] painted,
                    boolean[] facePainted) {
        initSide=side;
        initRow0=row0;
        initCol0=col0;
        paintedGrid=painted;
        paintedFace=facePainted;

        setChanged();
        notifyObservers();
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c].
     *  The cube is initially blank.
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     */
    public void initialize(int side, int row0, int col0, boolean[][] painted) {
        initialize(side, row0, col0, painted, new boolean[6]);
    }

    /** Initialize puzzle to be a copy of CUBE. */
    public void initialize(CubeModel cube) {
        for(int i=0;i<6;i++){
            this.paintedFace[i]=cube.paintedFace[i];
        }
        this.initSide=cube.initSide;
        this.paintedGrid=cube.paintedGrid;
        this.initRow0=cube.initRow0;
        this.initCol0=cube.initCol0;
        this.initMove=cube.initMove;

        setChanged();
        notifyObservers();
    }

    /** Move the cube to (ROW, COL), if that position is on the board and
     *  vertically or horizontally adjacent to the current cube position.
     *  Transfers colors as specified by the rules.
     *  Throws IllegalArgumentException if preconditions are not met.
     */
    public void move(int row, int col) {
        //check boundary
        if(row>initSide-1||row<0||col>initSide-1||col<0){
            throw new IllegalArgumentException();
        }else if(col-initCol0==-1&&row==initRow0){
            paintedFace=new boolean[]{paintedFace[0],paintedFace[1],paintedFace[5],paintedFace[4],paintedFace[2],paintedFace[3]};
            initCol0=col;
        }else if(col-initCol0==1&&row==initRow0){
            paintedFace=new boolean[]{paintedFace[0],paintedFace[1],paintedFace[4],paintedFace[5],paintedFace[3],paintedFace[2]};
            initCol0=col;
        }else if(row-initRow0==1&&col==initCol0){
            paintedFace=new boolean[]{paintedFace[4],paintedFace[5],paintedFace[2],paintedFace[3],paintedFace[1],paintedFace[0]};
            initRow0=row;
        }else if(row-initRow0==-1&&col==initCol0){
            paintedFace=new boolean[]{paintedFace[5],paintedFace[4],paintedFace[2],paintedFace[3],paintedFace[0],paintedFace[1]};
            initRow0=row;
        }else{
            throw  new IllegalArgumentException();
        }

        if(paintedFace[4]!=paintedGrid[initRow0][initCol0]){
            paintedFace[4]=paintedGrid[initRow0][initCol0];//exchange the painted status of the face and the grid after they touch each other
            paintedGrid[initRow0][initCol0]=!paintedFace[4];
        }
        initMove++;

        setChanged();
        notifyObservers();
    }

    /** Return the number of squares on a side. */
    public int side() {
        return initSide;
    }

    /** Return true iff square ROW, COL is painted.
     *  Requires 0 <= ROW, COL < board size. */
    public boolean isPaintedSquare(int row, int col) {
        return paintedGrid[row][col];
    }

    /** Return current row of cube. */
    public int cubeRow() {
        return initRow0;
    }

    /** Return current column of cube. */
    public int cubeCol() {

        return initCol0;
    }

    /** Return the number of moves made on current puzzle. */
    public int moves() {

        return initMove;
    }

    /** Return true iff face #FACE, 0 <= FACE < 6, of the cube is painted.
     *  Faces are numbered as follows:
     *    0: Vertical in the direction of row 0 (nearest row to player).
     *    1: Vertical in the direction of last row.
     *    2: Vertical in the direction of column 0 (left column).
     *    3: Vertical in the direction of last column.
     *    4: Bottom face.
     *    5: Top face.
     */
    public boolean isPaintedFace(int face) {
        return paintedFace[face];
    }

    /** Return true iff all faces are painted. */
    public boolean allFacesPainted() {

        boolean result=true;
        for(int i=0;i<paintedFace.length;i++){
            if(!paintedFace[i]){
                result=false;
            }
        }
        return  result;
    }

    // ADDITIONAL FIELDS AND PRIVATE METHODS HERE, AS NEEDED.

}
