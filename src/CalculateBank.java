
import java.util.Arrays;
import javax.swing.JOptionPane;
public class CalculateBank {
    /****
     首先申明数组，请求资源，尚需资源，可用资源，已得到分配的资源
     1.首先判断请求是否小于尚需资源
     2.判断请求资源是否小于现有资源
     3分配资源给进程，改变值，尚需资源减去请求资源，可用资源减去请求资源，已得到的资源加上请求资源
     4.安全性检测
     首先设置两个向量，work和finish，向量work的值等于可用资源的值
     判断当前进程的need是否小于work，是，就finish置为true
     改变work的值，继续下一个进程的判断，判断完成后生成一个安全序列，这样按照安全序列进行分配资源，每分配一个进程，就进行安全序列检测，这样每次分配的都是安全序列的第一个
     ，直到完成
     *
     *****/
    private static int p[];//进程数量
    private static int Source[];//系统各种资源的总量
    private static int Max[][]; // 最大需求矩阵
    private static int Allocation[][];// 每个进程现在所分配的各种资源类型的实例数量
    private static int Available[]; // 现有资源
    private static int Need[][]; // 每个进程尚需资源
    private static int Work[]; // 系统可供给进程的各类资源数量
    private static int _Work[][];//存放每一次分配资源前的Work，在没有改变前的资源
    private static boolean Finish[]; // 标志一个进程是否可以得到其所需要是资源
    private static int Request[]; // 进程对每个资源的实例数的请求数量
    private static int flag = 0;//标志变量

    private CalculateBank() {
        Source = new int[] { 10, 5, 7 };
        Max = new int[][] {
                { 7, 5, 3 },
                { 3, 2, 2 },
                { 9, 0, 2 },
                { 2, 2, 2 },
                { 4, 3, 3 }
        };
        Allocation = new int[][] {
                { 0, 1, 0 },
                { 2, 0, 0 },
                { 3, 0, 2 },
                { 2, 1, 1 },
                { 0, 0, 2 }
        };

        Available = new int[Source.length];
        Need = new int[Max.length][Source.length];
        for (int m = 0; m < Source.length; m++)
            Available[m] = Source[m] - Allocation[0][m] - Allocation[1][m] - Allocation[2][m] - Allocation[3][m] - Allocation[4][m];
        for (int i = 0; i < Max.length; i++)  //i表示进程数目
            for (int j = 0; j < 3; j++)//j表示资源数目
                Need[i][j] = Max[i][j] - Allocation[i][j];
        p = new int[Max.length];
        Work = new int[100];
        _Work = new int[100][100];
        Request = new int[100];
        Finish = new boolean[100];
        CalculateBank.Print(-1);
    }
    public static void Print(int p) {
        if (p == -1) {
            System.out.println("---------------------初始资源分配--------------------------");
            System.out.println("          Max        Allocation      Need        Available");
            System.out.println("       A   B   C     A   B   C     A   B   C     A   B   C");
            System.out.print("P[0]   ");
            for (int j = 0; j < 3; j++)
                System.out.print(Max[0][j] + "   ");
            System.out.print("  ");
            for (int j = 0; j < 3; j++)
                System.out.print(Allocation[0][j] + "   ");
            System.out.print("  ");
            for (int j = 0; j < 3; j++)
                System.out.print(Need[0][j] + "   ");
            System.out.print("  ");
            for (int j = 0; j < 3; j++)
                System.out.print(Available[j] + "   ");
            System.out.println();
            for (int i = 1; i < 5; i++) {
                System.out.print("P[" + i + "]   ");
                for (int j = 0; j < 3; j++)
                    System.out.print(Max[i][j] + "   ");
                System.out.print("  ");
                for (int j = 0; j < 3; j++)
                    System.out.print(Allocation[i][j] + "   ");
                System.out.print("  ");
                for (int j = 0; j < 3; j++)
                    System.out.print(Need[i][j] + "   ");
                System.out.println();
            }
        }else  {
            if (flag == 0) {
                System.out.println("---------------------请求资源后分配--------------------------");
                System.out.println("         Work          Need       Allocation  Work+Allocation  Finish");
                System.out.println("       A   B   C     A   B   C     A   B   C     A   B   C");
                flag = 1;
            }
            if (flag == 1) {
                System.out.print("P["+ p +"]   ");
                for (int j = 0; j < 3; j++)
                    System.out.print(_Work[p][j] + "   ");
                System.out.print("  ");
                for (int j = 0; j < 3; j++)
                    System.out.print(Need[p][j] + "   ");
                System.out.print("  ");
                for (int j = 0; j < 3; j++)
                    System.out.print(Allocation[p][j] + "   ");
                System.out.print("  ");
                for (int j = 0; j < 3; j++)
                    System.out.print((_Work[p][j]+Allocation[p][j]) + "   ");
                System.out.print("    "+Finish[p]);
                System.out.println();
            }
        }
    }

    public static boolean can(int Need[][], int i, int Request[]) {
        int j;
        for (j = 0; j < Source.length; j++) {
            if (Need[i][j] > Request[j])
                return false;
        }
        return true;
    }

    public static boolean Security_check() {
        boolean run = true;
        int count = 0,procedure = 0;
        System.arraycopy(Available, 0, Work, 0, Source.length);
        for (int m = 0; m < Max.length; m++)
            Finish[m] = false;

        while (run) {
            for (int i = 0; i < Max.length; i++) {
                count++; //从0开始记起
                if (!Finish[i] && can(Need, i, Work)) {
                    count--;//从i开始
                    Finish[i] = true;
                    p[procedure] = i;
                    procedure+=1;
                    System.arraycopy(Work, 0, _Work[i], 0, Source.length);
                    for (int j = 0; j < Source.length; j++) {
                        Work[j] = Work[j] + Allocation[i][j];
                    }
                }
            }

            if(count==Max.length) {
                run=false;
            }
            else {
                run = true;
                count=0;
            }
        }

        for (int i = 0; i < Max.length; i++)
            if (!Finish[i]) {
                System.out.println("找不到一个安全序列,系统处于不安全状态,分配失败");
                return false;
            }
        System.out.println("存在一个安全序列，系统处于安全状态，可为其分配资源。安全序列是"+Arrays.toString(p));
        return true;
    }



    public static void SourseRequest() {
        int  run = 1;
        while(run == 1) {
            String str=JOptionPane.showInputDialog("请输入需要申请资源的进程号");
            int id = Integer.parseInt(str);

            if(id < 0 || id >= Max.length){
                JOptionPane.showInputDialog("输入的进程号不存在！请重新输入。");
            }else{

                int count = 0;

                for(int i = 0; i < Source.length; i++) {
                    String str1=JOptionPane.showInputDialog("请输入此进程所请求的第"+"["+i+"]"+"类资源数");

                    Request[i]=Integer.parseInt(str1);

                }

                for(int i = 0; i < Source.length; i++) {
                    if(Request[i] > Need[id][i]) {
                        JOptionPane.showInputDialog("第"+(i+1)+"类资源请求量超出最大值！无效。");

                        count++;
                    }
                }

                if(count > 0)
                    run = 0;

                if(count == 0) {
                    run = 0;
                    for (int k = 0;k <Source.length;k++) {
                        Available[k] = Available[k] - Request[k];
                        Allocation[id][k] = Allocation[id][k] + Request[k];
                        Need[id][k] = Need[id][k] - Request[k];
                    }
                }
                String str1=JOptionPane.showInputDialog("是否继续，按1继续，按0退出");
                run=Integer.parseInt(str1);

            }

        }
    }

    public static void Distribution() {
        if(Security_check())
            for(int i =0 ; i < Max.length;i++)
                CalculateBank.Print(p[i]);
    }

    public static void show(){
        String a=JOptionPane.showInputDialog("请输入你要查看的资源 1：分配前2：分配后");
        int s1=Integer.parseInt(a);
        if(s1==1){
            Print(-1);
        }else if(s1==2){
            if(Security_check())
                for(int i =0 ; i < Max.length;i++)
                    CalculateBank.Print(p[i]);

        }

    }

    public static void back(){

        if(Security_check()){
            for(int d=0;d<Max.length;d++){
                for (int k = 0;k <Source.length;k++) {
                    Distribution();
                    Available[k] = Available[k] + Request[k];
                    Allocation[d][k] = Allocation[d][k] -Request[k];
                    Need[d][k] = Need[d][k] + Request[k];
                }
                CalculateBank.Print(p[d]);
            }
        }else{
            return;
        }
    }

    public static void exit(){
        System.exit(0);
    }

    public static void menu(){
        while(true){
            String str2=JOptionPane.showInputDialog("1.分配资源\n2.回收资源\n3.查看资源\n4.退出操作\n");
            int s=Integer.parseInt(str2);
            if(s==1){
                SourseRequest();
                Distribution();

            }else if(s==2){
                back();

            }else if(s==3){
                show();

            }else if(s==4){
                exit();

            }else{
                break;
            }

        }

    }

    public static void main(String[] args) {
        new CalculateBank();
        menu();
    }
}

