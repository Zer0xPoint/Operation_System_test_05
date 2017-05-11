import java.util.Scanner;

/**
 * Created by Zer0xPoint on 2017/5/11.
 */
public class test {
    /************************** 主函数************************/
    public static void main(String[] args) {
        System.out.println("==================操作系统银行家算法===============");
        ThefBanker tb = new ThefBanker();
        boolean flag = true;
        while (flag) {
            System.out.println("1.死锁检查，检验是否安全（安全性算法）");
            System.out.println("2.避免死锁，检测是否能预分配（银行家算法）");
            System.out.println("3.退出！");
            System.out.println("----------------------------------------------------------");
            System.out.println("请您依次选择");
            Scanner input = new Scanner(System.in);
            int num = input.nextInt();
            switch (num) {
                case 1:
                    tb.Security_check();
                    flag = true;
                    break;
                case 2:
                    tb.checkRequest();//死锁检测

                    flag = true;
                    break;
                case 3:
                    System.out.println("谢谢使用,再见!!");
                    flag = false;
                    break;
            }
        }
    }
}