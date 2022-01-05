class Problem {
    public static void main(String[] args) {
        boolean isEnd = false;
       for (int i = 0; i < args.length; i++){
           if (args[i].equals("test")){
               System.out.println(i);
               isEnd = true;
               break;
           }
       }
       if (isEnd == false){
           System.out.println("-1");
       }
    }
}