class Problem {
    public static void main(String[] args) {
        String operator = args[0];
        int num1 = Integer.parseInt(args[1]);
        int num2 = Integer.parseInt(args[2]);

        if ("+".equals(operator)) {
            System.out.println(num1 + num2);
        } else if ("-".equals(operator)) {
            System.out.println(num1 - num2);
        } else if ("*".equals(operator)) {
            System.out.println(num1 * num2);
        } else {
            System.out.println("Unknown operator");
        }


    }
}
