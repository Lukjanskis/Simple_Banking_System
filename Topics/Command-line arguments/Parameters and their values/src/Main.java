class Problem {
    public static void main(String[] args) {
        for (int i = 0; i < args.length;){
            System.out.print(args[i] + "=");
            i++;
            System.out.print(args[i]);
            i++;
            System.out.println();
        }
    }
}