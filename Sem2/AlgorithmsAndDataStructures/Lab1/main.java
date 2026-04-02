public class main {
    public static int[][] createBoard(int rows, int cols){
        return new int[rows][cols];
    }

    public static void printBoard(int[][] board){
        String white = "□";
        String black = "■";
        for(int i =0;i < board.length;i++){
            for(int j = 0; j< board[i].length;j++){
                if(board[i][j] == 0){
                    System.out.print(white);
                }
                else{
                    System.out.print(black);
                }
            }
            System.out.println();
        }
    }

    public static int[][] updateBoard(int[][] board){
        int[][] newBoard = createBoard(board.length, board[0].length);
        int[][] vectors ={{0,1}, {0,-1}, {1,0}, {1,1}, {1,-1}, {-1,0}, {-1,1}, {-1,-1}};

        for(int i =1;i < board.length;i++){
            for(int j = 1; j< board[i].length;j++){
                int counter = 0;
                for(int[] vector:vectors){
                    int y = vector[0];
                    int x = vector[1];

                    if(i+y >0 && j+x >0 && i+y<board.length && j+x<board[i].length){
                        if(board[i+y][j+x] > 0){
                            counter++;
                        }
                    }
                }
                if(board[i][j] == 1){
                    if (!(counter == 2 || counter == 3)){
                        newBoard[i][j] = 0;
                    }
                    else{
                        newBoard[i][j] = 1;
                    }
                }
                if(board[i][j] == 0){
                    if(counter == 3){
                        newBoard[i][j] = 1;
                    }
                }
            }
        }
        return newBoard;
    }
    public static void main(String[] args) {
        int[][] board = createBoard(5,15);
        board[1][8] = 1;
        board[2][8] = 1;
        board[3][8] = 1;
        board[2][6] = 1;
        board[3][7] = 1;
        printBoard(board);
        System.out.println();

        for(int k = 0; k<5;k++){
            int[][] newboard = board;
            newboard = updateBoard(newboard);
            printBoard(newboard);
            System.out.println("\n");
        }
    }
}
