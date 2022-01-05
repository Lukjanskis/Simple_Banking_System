import java.util.Scanner;

     class Main {

        public static void main(String args[]){

            Scanner sc=new Scanner(System.in);
            int rows=sc.nextInt();
            int columns=sc.nextInt();

            int twoD[][]=new int[rows][columns];


            for(int i=0; i<rows;i++)
            {
                for(int j=0; j<columns;j++)
                {
                    twoD[i][j]=sc.nextInt();
                }
            }

            for (int k = 0; k < columns; k++){
                for (int l = 0; l < rows; l++){
                    System.out.print(twoD[rows - 1 - l][k] + " ");
                }
                System.out.println();
            }

        }

    }