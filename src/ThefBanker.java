import java.util.Scanner;

/**
 * Created by Zer0xPoint on 2017/5/11.
 */
public class ThefBanker {
    int m;  //进程个数
    int n; //每个进程的资源个数
    int[][] max;  //最大需求矩阵
    int[][] allocation;  // 已分配的资源（已占有的资源）
    int[][] need;   // 需求的资源
    int[] available;   // 可利用的资源
    int[] p;   // 记录安全序列
    boolean[] finish; // 系统是否有足够的资源分配给进程？true 表示可以 false 表示不可以
    Scanner input = new Scanner(System.in);

    /************************** 初始化进程和各类资源数************************/
    public ThefBanker() {
        System.out.println("请输入系统中的【进程数】:");
        m = input.nextInt();
        System.out.println("请输入进程的【资源类型数】:");
        n = input.nextInt();
        max = new int[m][n];
        allocation = new int[m][n];
        need = new int[m][n];
        available = new int[n];
        finish = new boolean[m];

        System.out.println("请输入一个"+m+"行"+n+"列的各进程的最大需求量：");
        for (int i=0;i<max.length;i++) {  //依次输入进程的各个最大资源数
            System.out.println("请输入第p("+i+")进程的Max：");
            for(int j=0;j<max[i].length;j++){
                max[i][j] = input.nextInt();
            }
        }
        System.out.println("请输入一个"+m+"行"+n+"列的各资源数量：");
        for (int i=0;i<allocation.length;i++) {  //依次输入进程的各个占有资源数
            System.out.println("请输入第p("+i+")进程中的Alloction：");
            for (int j=0;j<allocation[i].length;j++) {
                allocation[i][j] = input.nextInt();
            }
        }
        for (int i=0;i<need.length;i++) {  //计算出各个进程需求的资源数
            for(int j=0;j<need[i].length;j++){
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        System.out.println("请输入可用资源数Avallable："); //输入进程的可用资源数
        for (int i=0;i<n;i++) {
            available[i] = input.nextInt();
        }
        System.out.println("初始化结果为下表：");
        print();
    }

    /******************显示资源分配情况********************/
    public void print() {
        System.out.println("-----------------------资源分配情况-----------------------------");
        System.out.println("\tMax\tAllocation\tNeed\tAvalable");
        // System.out.println("\tA B C\tA B C\t\tA B C\tA B C");
        for (int i=0;i<m;i++) {
            System.out.print("P("+i+"): ");
            System.out.print(" ");
            for (int j=0;j<n;j++) {
                if(max[i][j]>9){
                    System.out.print(" "+max[i][j]+" ");
                }
                System.out.print(max[i][j]+" ");
            }
            System.out.print("\t");
            for (int j=0;j<n;j++) {
                if(allocation[i][j]>9){
                    System.out.print(" "+allocation[i][j]+" ");
                }
                System.out.print(allocation[i][j]+" ");
            }
            System.out.print("\t\t ");
            for (int j=0;j<n;j++) {
                if(need[i][j]>9){
                    System.out.print(" "+need[i][j]+" ");
                }
                System.out.print(need[i][j]+" ");
            }
            System.out.print("\t");
            if (i==0) {
                for (int j=0;j<n;j++) {
                    if(available[j]>9){
                        System.out.print(" "+available[j]+" ");
                    }
                    System.out.print(available[j]+" ");
                }
            }
            System.out.println();
        }
        System.out.println("------------------------------------------");
    }

    /************************** 安全性序列检查（安全性）************************/
    public boolean Security_check() {
        int[] work = new int[n];
        for (int i=0;i<n;i++) {
            work[i] = available[i];// 把available的值赋给work
        }
        finish = new boolean[m];
        for (int i = 0; i < m; i++) {// 开始把进程全部置未分配状态 都为false；
            finish[i] = false;
        }

        int num = 0;// 对每个进程都要把所有资源都进行比较
        int num1 = 0;//记录进程号
        int count = 0;// 记录可以分配的序列
        int count1 = 0;// 记录所有序列是否分配
        p = new int[m];// 找到安全序列

        while (num1<m) {
            for (int i=0;i<m;i++) {
                if (finish[i] == false) {// 判断finish的状态，如果为true说明刚才已经找到，不需要重复。
                    for (int j=0;j<n;j++) {
                        if (need[i][j] <= work[j]) {// 比较一个进程的各种资源是否满足条件
                            num++;
                        }
                    }
                    if (num == n) {// 如果一个进程所有资源都满足条件need<work,则找到了一个进程满足
                        for (int k=0;k<n;k++) {
                            work[k] = work[k] + allocation[i][k];
                        }
                        finish[i] = true;// 找到一个进程满足
                        p[count++] = i;// 记录找到的是第几个进程
                    }
                }
                num = 0;// 必须把它清零，重新来找下个资源种类的每种是否都满足条件
            }
            num1++;
        }

        // 记录有多少个序列；
        for (int i=0;i<m;i++) {
            if (finish[i] == true) {
                count1++;// 检测是否所有的进程最后都是true,
            }
        }
        if (count1 == m) {// 如果序列里面总数等于总共有多少程序，就找到了安全的序列。并且输出。反之没有找到
            System.out.println("存在一个安全序列，安全序列为:");
            for (int i=0;i<m;i++) {
                if (i != m-1) {
                    System.out.print("P"+p[i]+"-->");
                } else {
                    System.out.println("P"+p[i]);
                }
            }
            System.out.println("----------------------------------------------------");
            return true;
        } else {
            System.out.println( "没有找到一个安全序列，系统处于不安全状态！");
            return false;
        }
    }
    /************************** 进程请求******************************/
    public void checkRequest() {
        int process = 0;// 记录输入的是第几个进程
        int count2 = 0;// 记录试分配过程中满足条件的个数
        boolean flag = true;// 主要防止输入的数字已经超出了本来process数量，则要求重新输入
        System.out.println("请输入要申请的第几个进程，注意进程p下标是从0开始的");
        while (flag) {
            process = input.nextInt();
            if (process > m) {
                flag = true;
                System.out.println("输入超出了本来进程的范围，请重新输入！");
            } else {
                flag = false;

            }
        }
        System.out.println("第"+process+"个进程提出请求");
        int[] request = new int[n];
        System.out.println("输入要请求的资源Request:");
        for (int i=0;i<n;i++) {
            request[i] = input.nextInt();
        }
        // 判断是否可以分配
        for (int i=0;i<n;i++) {
            if (request[i] <= need[process][i] && request[i] <= available[i]) {
                count2++;// 判断是否每个进程的所有资源都满足试分配的要求，并记录。
            }
        }

        if (count2==n) {// 如果资源都满足要求，则可以进程请求（试分配）
            for (int j=0;j<n;j++) {
                allocation[process][j] += request[j];  //注意数组下标是从0开始的
                need[process][j] -= request[j];
                available[j] -= request[j];
            }
            System.out.println("试分配如下-------->");
            print();// 打印试分配的结果
            System.out.println("进行安全性判断");
            flag = Security_check();// 判断是否为安全序列

            if (flag==false) {  //如果是分配后不能找到一个安全序列，则返回，不进行分配
                for (int j=0;j<n;j++) {
                    allocation[process][j] -= request[j];  //注意数组下标是从0开始的
                    need[process][j] += request[j];
                    available[j] += request[j];
                }


            }else{//资源回收
                for (int j=0;j<n;j++) {

                    available[j]+=allocation[process][j];
                }
                System.out.println("----------------进行资源回收后情况表------------------");
                print();
            }
        } else {
            System.out.println("不能进行试分配,也就找不到安全序列");

        }
    }
}