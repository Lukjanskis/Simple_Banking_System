class ArrayOperations {
    public static void reverseElements(int[][] twoDimArray) {
        for (int i = 0; i < twoDimArray.length; i++) {
            for (int j = 0; j < twoDimArray[i].length / 2; j++) {
                int temp = twoDimArray[i][j];
                twoDimArray[i][j] = twoDimArray[i][twoDimArray[i].length - j - 1];
                twoDimArray[i][twoDimArray[i].length - j - 1] = temp;
            }
        }
        for (int i = 0; i < twoDimArray.length; i++){
            for (int j = 0; j < twoDimArray.length; j++){
                System.out.print(twoDimArray[i][j]);
            }
            System.out.println();
        }
    }
    public static void main(String[] args){
        int[][] arr = new int[4][4];
        arr[0][1] = 1;
        arr[0][3] = 2;
        reverseElements(arr);
    }
}