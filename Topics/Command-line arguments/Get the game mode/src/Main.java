class Problem {
    public static void main(String[] args) {
        boolean isEnd = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("mode") && i % 2 == 0) {
                System.out.println(args[i + 1]);
                isEnd = true;
                break;
            }
        }
        if (isEnd == false) {
            System.out.println("default");
        }
    }
}
