import java.util.Stack;

public class test {
    public static void main(String[] args) {
        Stack<String> s1 = new Stack<String>();
s1.push("h");
s1.push("i");
Stack<String> s2 = new Stack<String>();
s2.push("t");
s2.push("h");
s2.push("e");
s2.push("r");
s2.push("e");
Stack<String> s3 = new Stack<String>();
String letter;
while( !s1.isEmpty() && !s2.isEmpty())
{
letter = s1.pop();
s3.push(letter);
letter = s2.pop();
s3.push(letter);
s1.push(letter);
}
System.out.println("Stack ==> " + s3);
    }
}
